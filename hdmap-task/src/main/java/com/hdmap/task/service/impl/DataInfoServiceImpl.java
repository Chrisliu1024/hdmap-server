package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.service.DataDatasetsRelService;
import com.hdmap.task.service.DataInfoService;
import com.hdmap.task.service.DatasetInfoService;
import com.hdmap.task.service.TaskPoolInfoService;
import com.hdmap.core.utils.SpringReflectUtil;
import com.hdmap.geo.utils.GeometryUtil;
import com.hdmap.task.model.entity.DataInfo;
import com.hdmap.task.model.entity.DatasetInfo;
import com.hdmap.task.model.entity.TaskPoolInfo;
import com.hdmap.task.enums.DataSetTypeEnum;
import com.hdmap.task.mapper.DataInfoMapper;
import com.hdmap.minio.service.MinioSysFileService;
import com.hdmap.pointcloud.config.CommonConfig;
import com.hdmap.pointcloud.constant.enums.FileStatusEnum;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import com.hdmap.pointcloud.dto.GridClipResult;
import com.hdmap.pointcloud.entity.SourceFileInfo;
import com.hdmap.pointcloud.service.FileHandleService;
import com.hdmap.pointcloud.service.impl.CsvGridClipHandler;
import com.hdmap.pointcloud.service.impl.LasGridClipHandler;
import com.hdmap.pointcloud.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;

/**
* @author admin
* @description 针对表【data_info(数据信息表)】的数据库操作Service实现
* @createDate 2024-01-29 15:58:35
*/
@Slf4j
@Service
public class DataInfoServiceImpl extends ServiceImpl<DataInfoMapper, DataInfo>
    implements DataInfoService {

    @Value("${srid.guass_kruger:4547}")
    private Integer SRID_GUASS_KRUGER;
    @Value("${srid.wgs84:4326}")
    private Integer SRID_WGS84;
    @Value("${precision:7}")
    private Integer precision;

    @Resource
    private CommonConfig commonConfig;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private MinioSysFileService minioService;
    @Resource
    private DatasetInfoService datasetInfoService;
    @Resource
    private DataDatasetsRelService dataDatasetsRelService;
    @Resource
    private TaskPoolInfoService taskPoolInfoService;
    @Resource
    private FileHandleService fileHandleService;
    private LasGridClipHandler lasGridClipHandler;
    private CsvGridClipHandler csvGridClipHandler;

    @Autowired
    public DataInfoServiceImpl(LasGridClipHandler lasGridClipHandler, CsvGridClipHandler csvGridClipHandler) {
        this.lasGridClipHandler = lasGridClipHandler;
        this.csvGridClipHandler = csvGridClipHandler;
        // set next handler
        this.lasGridClipHandler.setNextHandler(this.csvGridClipHandler);
    }

    @Override
    public IPage<DataInfo> getByDataSetId(IPage<DataInfo> page, Serializable id) {
        return this.baseMapper.selectByDataSetId(page, id);
    }

    @Override
    public IPage<DataInfo>  getByProjectId(IPage<DataInfo> page, Serializable id) {
        return this.baseMapper.selectByProjectId(page, id);
    }

    @Override
    public IPage<DataInfo> getResultDataInfoByTaskId(IPage<DataInfo> page, Serializable id) {
        return this.baseMapper.selectResultDataInfoByTaskId(page, id);
    }

    @Override
    public DataInfo getByDataSetIdAndGeohashAndType(Serializable id, String geohash, Integer type) {
        return this.baseMapper.selectByDataSetIdAndGeohashAndType(id, geohash, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdsCustom(List<Serializable> idList) {
        // 删除数据信息，删除数据集与数据信息关联
        return this.removeByIds(idList) && this.dataDatasetsRelService.removeByDataIds(idList);
    }

    @Override
    public String downloadToLocalById(Serializable id) throws Exception {
        DataInfo dataInfo = this.baseMapper.selectById(id);
        if (dataInfo == null) {
            throw new Exception("文件不存在");
        }
        String fileName = dataInfo.getFileName();
        try (InputStream downloadFileStream = minioService.downloadFile(fileName)) {
            // 保存文件到本地
            return ResourceUtil.storeInputStreamToLocal(downloadFileStream, fileName);
        }
    }

    @Override
    public String downloadById(Serializable id, HttpServletResponse response) throws Exception {
        DataInfo dataInfo = this.baseMapper.selectById(id);
        if (dataInfo == null) {
            throw new Exception("文件不存在");
        }
        String fileName = dataInfo.getFileName();
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        downloadByFileName(fileName, response.getOutputStream());
        return fileName;
    }

    @Override
    public String downloadByTaskId(Serializable id, HttpServletResponse response) throws Exception {
        // 查询任务池中的数据ID
        TaskPoolInfo taskPoolInfo = taskPoolInfoService.getByTaskId(id);
        if (taskPoolInfo == null) {
            throw new Exception("任务不存在");
        }
        Long dataId = taskPoolInfo.getDataId();
        return downloadById(dataId, response);
    }

    @Override
    public boolean uploadAndHandle(Serializable datasetId, String localPath, String sourceLocation) throws Exception {
        // 获取数据集信息
        DatasetInfo dataInfo = this.datasetInfoService.getById(datasetId);
        if (dataInfo == null) {
            return false;
        }
        // 判断数据集类型
        Integer type = dataInfo.getType();
        // 处理4D标注数据
        if (DataSetTypeEnum.MARKING_4D.equalByCode(type)) {
            // 记录文件上传信息
            SourceFileInfo sourceInfo = fileHandleService.saveSourceFileInfo(Long.valueOf(datasetId.toString()), ResourceUtil.getWholeFileNameByPath(localPath), FileTypeEnum.LAS, sourceLocation);
           // 保存数据信息
            uploadAndSaveProxy(datasetId, localPath, null);
            // 更新文件上传状态
            fileHandleService.updateSourceFileStatus(sourceInfo, FileStatusEnum.CLIPPED.getCode());
        } else if (DataSetTypeEnum.DRAW_MAP.equalByCode(type)) { // 处理绘制地图数据
            List<GridClipResult> results = fileHandleService.uploadAndClipFile(Long.valueOf(datasetId.toString()), localPath, sourceLocation, precision);
            if (results == null || results.isEmpty()) {
                return false;
            }
            for (GridClipResult result : results) {
                lockAndMerge(Long.valueOf(datasetId.toString()), result.getGridIdentifier(), result.getFilePath());
                // 删除本地文件
                ResourceUtil.deleteFile(result.getFilePath());
            }
        }
        return true;
    }

    private void uploadAndSaveProxy(Serializable datasetId, String localPath, String geohash) throws Exception {
        // 获取代理对象
        DataInfoService proxy = (DataInfoService) SpringReflectUtil.getBean(DataInfoService.class);
        // 上传并保存数据
        proxy.uploadAndSave(datasetId, localPath, geohash);
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadAndSave(Serializable datasetId, String localPath, String geohash) throws Exception {
        // 上传文件
        String cloudLocation = commonConfig.getZipUploadFile() ? minioService.uploadAndZipFile(localPath)
                : minioService.uploadFile(localPath);
        FileTypeEnum fileTypeEnum = ResourceUtil.getFileTypeByPath(localPath);
        // 保存数据信息
        DataInfo info = new DataInfo();
        info.setName(ResourceUtil.getWholeFileNameByPath(localPath));
        info.setType(fileTypeEnum.getCode());
        info.setFileName(ResourceUtil.getWholeFileNameByPath(cloudLocation));
        info.setLocation(cloudLocation);
        // 当文件类型为LAS时，获取文件的bbox
        if (FileTypeEnum.LAS.equals(fileTypeEnum)) {
            Geometry geom = ResourceUtil.getBboxByPath(localPath);
            // 如果geometry不是WGS84坐标系，则转换为WGS84坐标系
            if (geom.getSRID() != SRID_WGS84) {
                geom = GeometryUtil.transform(geom, SRID_GUASS_KRUGER, SRID_WGS84);
            }
            info.setGeom(geom);
            if (geohash != null) {
                info.setGeohash(geohash);
            }
        }
        this.save(info);
        // 保存数据集与数据信息关联
        dataDatasetsRelService.saveCustom(Long.valueOf(datasetId.toString()), info.getId());
    }

    private void lockAndMerge(Long identifier, String gridIdentifier, String inputPath) throws Exception {
        // get redis lock
        String lockKey = identifier + ":" + gridIdentifier;
        boolean lock = redissonClient.getLock(lockKey).tryLock();
        if (lock) {
            // retrieve db by taskId and geoHash
            DataInfo info = getByDataSetIdAndGeohashAndType(identifier, gridIdentifier, FileTypeEnum.LAS.getCode());
            if (info == null) {
                uploadAndSaveProxy(identifier, inputPath, gridIdentifier);
            } else {
                // merge path
                String originCloudPath = info.getLocation();
                String mergeCloudPath = merge(originCloudPath, inputPath, gridIdentifier);
                // update db
                info.setLocation(mergeCloudPath);
                this.updateById(info);
            }
            // release lock
            redissonClient.getLock(lockKey).unlock();
        } else {
            // print log
            log.info("lock failed, retry after 3s. identifier: {}, grid: {}", identifier, gridIdentifier);
            // retry after 3s if lock failed
            Thread.sleep(3000);
            lockAndMerge(identifier, gridIdentifier, inputPath);
        }
    }

    private String merge(String originCloudPath, String inputPath, String identifier) throws Exception {
        // get file type by path
        FileTypeEnum fileType = ResourceUtil.getFileTypeByPath(originCloudPath);
        String mergeCloudPath = lasGridClipHandler.handleMerge(fileType, identifier, originCloudPath, inputPath);
        if (mergeCloudPath == null) {
            throw new Exception("不支持的文件类型");
        }
        return mergeCloudPath;
    }

    private void downloadByFileName(String fileName, ServletOutputStream outputStream) throws Exception {
        try (InputStream downloadFileStream = minioService.downloadFile(fileName)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = downloadFileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        }
    }

}




