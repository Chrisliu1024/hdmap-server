package com.hdmap.data.ibd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hdmap.data.ibd.dto.TableMapping;
import com.hdmap.data.ibd.entity.IbdTableMapping;
import com.hdmap.data.ibd.manager.PostgisManager;
import com.hdmap.data.ibd.manager.SpatialiteManager;
import com.hdmap.data.ibd.service.IbdParseService;
import com.hdmap.data.ibd.service.IbdTableMappingService;
import com.hdmap.data.ibd.service.TableParseService;
import com.hdmap.data.ibd.dto.ErrorLog;
import com.hdmap.data.ibd.dto.IbdUpload;
import com.hdmap.minio.service.MinioSysFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.io.ParseException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 11:50
 * @description: ibd文件解析服务
 */
@Slf4j
@Service
@RefreshScope
public class IbdParseServiceImpl implements IbdParseService {
    @Value("${minio.save.switch:false}")
    private Boolean minioSwitch;
    @Resource
    private SpatialiteManager spatialiteManager;
    @Resource
    private PostgisManager postgisManager;
    @Resource
    private IbdTableMappingService ibdTableMappingService;
    @Resource
    private TableParseService tableParseService;
    @Resource
    private MinioSysFileService minioService;
    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean parse(IbdUpload params) throws Exception {
        // 接收文件，并临时存储
        MultipartFile file = params.getFile();
        String fileName = String.format("%s_%s_%s_%s", params.getUserId(), params.getProject(), file.getOriginalFilename(), System.currentTimeMillis());
        // 存储在本地jar同级目录下
        String filePath = getJarPath();
        File dest = new File(filePath + "/" + fileName);
        // 流写入
        InputStream ins = file.getInputStream();
        inputStreamToFile(ins, dest);
        ins.close();
        // 临时文件地址
        String destFilePath = dest.getAbsolutePath();
        try {
            if (minioSwitch) {
                // 上传至minio（optional）
                minioService.uploadFile(file.getInputStream(), fileName);
            }
            // 连接spatialite数据库
            DataStore sourceDs = spatialiteManager.getDataStore(destFilePath);
            // 连接PostGIS数据库
            String targetDsName = postgisManager.getDataStoreName(params.getUserId(), params.getPrefix());
            DataStore targetDs = postgisManager.getDataStore(targetDsName, params.getProject());
            // 获取表中的全部表映射关系
            List<IbdTableMapping> ibdTableMappings = ibdTableMappingService.list();
            // 根据is_useable过滤
            ibdTableMappings.removeIf(ibdTableMapping -> !ibdTableMapping.getIsUseable());
            // 根据version过滤 V1.0.0 -> V1.1.1 -> V1.2.1
            if (StringUtils.isNotEmpty(params.getVersion())) {
                ibdTableMappings.removeIf(ibdTableMapping -> !params.getVersion().equals(ibdTableMapping.getVersion()));
            }
            // 根据prority_level进行排序
            ibdTableMappings.sort(Comparator.comparing(ibdTableMapping -> Integer.parseInt(ibdTableMapping.getProrityLevel().toString())));
            // 根据prority_level进行聚类成多个Map
            Map<Object, List<IbdTableMapping>> classfiedMap = new HashMap<>();
            for (IbdTableMapping ibdTableMapping : ibdTableMappings) {
                Object prorityLevel = ibdTableMapping.getProrityLevel();
                if (classfiedMap.containsKey(prorityLevel)) {
                    classfiedMap.get(prorityLevel).add(ibdTableMapping);
                } else {
                    List<IbdTableMapping> ibdTableMappingList = new ArrayList<>();
                    ibdTableMappingList.add(ibdTableMapping);
                    classfiedMap.put(prorityLevel, ibdTableMappingList);
                }
            }
            // 逐个表解析入库
            String schema = params.getProject();
            List<ErrorLog> errorLogs = parseTables(params.getUserId(), classfiedMap, sourceDs, targetDs, targetDsName, schema, destFilePath);
            // 保存/输出错误日志 TODO
        } finally {
            // 移除spatialite数据库连接
            spatialiteManager.removeDataStore(destFilePath);
            // 删除临时文件
            Files.deleteIfExists(dest.toPath());
        }
        return true;
    }

    private List<ErrorLog> parseTables(String userId, Map<Object, List<IbdTableMapping>> classfiedMap, DataStore sourceDs, DataStore targetDs, String targetDsName, String schema, String destFilePath) throws IOException, CQLException, FactoryException, TransformException, ParseException, java.text.ParseException, ExecutionException, InterruptedException, SQLException {
        // get redis lock
        boolean lock = redissonClient.getLock(targetDsName + ":" + schema + ":" + userId).tryLock();
        List<ErrorLog> errorLogs = new ArrayList<>();
        if (lock) {
            try{
                // 清空表
                List<String> tableNames = classfiedMap.values().stream().flatMap(Collection::stream).map(IbdTableMapping::getTargetTableName).distinct().collect(Collectors.toList());
                for (String tableName : tableNames) {
                    postgisManager.truncate(targetDsName, schema, tableName);
                }
                for (Map.Entry<Object, List<IbdTableMapping>> entry : classfiedMap.entrySet()) {
                    for (IbdTableMapping ibdTableMapping : entry.getValue()) {
                        log.info("userID: {}, project:{}, table: {}", userId, schema, ibdTableMapping.getSourceTableName());
                        Object tableMappingJsonObj = ibdTableMapping.getTableMappingJson();
                        // 将json字符串转换为TableMapping对象
                        TableMapping tableMapping = JSONObject.parseObject(tableMappingJsonObj.toString(), TableMapping.class);
                        SimpleFeatureCollection sourceCollection = Strings.isBlank(tableMapping.getFilter())
                                ? spatialiteManager.getCollection(destFilePath, tableMapping.getSourceTableName())
                                : spatialiteManager.getCollection(destFilePath, tableMapping.getSourceTableName(), tableMapping.getFilter());

                        List<ErrorLog> errLogs = tableParseService.parse(tableMapping, sourceCollection, sourceDs, targetDs);
                        errorLogs.addAll(errLogs);
                    }
                }
            } finally {
                // release lock
                redissonClient.getLock(targetDsName + ":" + schema + ":" + userId).unlock();
            }
        } else {
            log.warn("获取锁失败，等待10s后重试");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error("等待获取锁失败", e);
            }
            parseTables(userId, classfiedMap, sourceDs, targetDs, targetDsName, schema, destFilePath);
        }
        return errorLogs;
    }

    /**
     * 流写入文件
     *
     * @param inputStream 文件输入流
     * @param file        输出文件
     */
    private void inputStreamToFile(InputStream inputStream, File file) {
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取jar包所在目录
     *
     * @return jar包所在目录
     */
    private String getJarPath() {
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        return jarF.getParentFile().toString();
    }
}
