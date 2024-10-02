package com.hdmap.pointcloud.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.pointcloud.config.CommonConfig;
import com.hdmap.pointcloud.dto.GridClipResult;
import com.hdmap.pointcloud.entity.SourceFileInfo;
import com.hdmap.pointcloud.service.FileHandleService;
import com.hdmap.pointcloud.service.SourceFileInfoService;
import com.hdmap.minio.service.MinioSysFileService;
import com.hdmap.pointcloud.constant.enums.FileStatusEnum;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import com.hdmap.pointcloud.service.*;
import com.hdmap.pointcloud.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/18 17:01
 * @description: 文件
 */
@Slf4j
@Service
public class FileHandleServiceImpl implements FileHandleService {
    @Resource
    private CommonConfig commonConfig;
    @Resource
    private MinioSysFileService minioService;
    @Resource
    private SourceFileInfoService sourceFileInfoService;
    private LasGridClipHandler lasGridClipHandler;
    private CsvGridClipHandler csvGridClipHandler;

    public FileHandleServiceImpl(LasGridClipHandler lasGridClipHandler, CsvGridClipHandler csvGridClipHandler) {
        this.lasGridClipHandler = lasGridClipHandler;
        this.csvGridClipHandler = csvGridClipHandler;
        // set next handler
        this.lasGridClipHandler.setNextHandler(this.csvGridClipHandler);
    }

    /**
     * 上传las文件，按geohash切分
     */
    @Override
    public List<GridClipResult> uploadAndClipFile(Long identifier, String localPath, String sourceLocation, Integer precision) throws Exception {
        if (precision == null) {
            precision = commonConfig.getGeohashPrecision();
        }
        // get file name
        String fileName = new File(localPath).getName();
        // get file type
        FileTypeEnum fileType = getFileType(fileName);
        // save source file info
        SourceFileInfo fileInfo = saveSourceFileInfo(identifier, fileName, fileType, sourceLocation);
        // update source file info - clipping
        //uptadeSourceFileStatus(fileInfo, FileStatusEnum.CLIPPING.getCode());
        // clip
        List<GridClipResult> results = lasGridClipHandler.handleClip(fileType, identifier, localPath, precision);
        if (results == null || results.isEmpty()) {
            // handle other file types
            throw new Exception("不支持的文件类型");
        }
        // update source file info - clipped
        updateSourceFileStatus(fileInfo, FileStatusEnum.CLIPPED.getCode());
        // delete local file
        ResourceUtil.deleteFile(localPath);
        return results;
    }

    @Override
    public SourceFileInfo saveSourceFileInfo(Long identifier, String fileName, FileTypeEnum fileType, String sourceLocation) {
        // save source file info
        SourceFileInfo fileInfo = new SourceFileInfo();
        fileInfo.setIdentifier(identifier);
        fileInfo.setName(fileName);
        fileInfo.setType(fileType.getCode());
        fileInfo.setStatus(FileStatusEnum.RECEIVED.getCode());
        fileInfo.setLocation(sourceLocation);
        sourceFileInfoService.save(fileInfo);
        return fileInfo;
    }

    @Override
    public boolean updateSourceFileStatus(SourceFileInfo fileInfo, Integer status) {
        fileInfo.setStatus(status);
        return sourceFileInfoService.updateById(fileInfo);
    }

    @Override
    public List<SourceFileInfo> getSourceFileInfo(IPage<SourceFileInfo> page, Serializable identifier) {
        if (identifier == null) {
            return sourceFileInfoService.list(page);
        }
        SourceFileInfo fileInfo = new SourceFileInfo();
        fileInfo.setIdentifier(Long.valueOf(identifier.toString()));
        return sourceFileInfoService.list(page, new QueryWrapper<>(fileInfo));
    }

    @Override
    public String storeFileToLocal(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // store file to local
        String localPath = ResourceUtil.getLocalPathAndUniqueWholeName(fileName);
        try {
            ResourceUtil.writeToLocal(localPath, file.getInputStream());
        } catch (IOException e) {
            log.error("store file to local failed", e);
            throw new RuntimeException("store file to local failed");
        }
        return localPath;
    }

    @Override
    public String storeFileToCloud(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // get unique file name
        String uniqueName = ResourceUtil.getUniqueWholeName(fileName);
        try {
            return minioService.uploadFile(file.getInputStream(), uniqueName);
        } catch (Exception e) {
            log.error("store file to cloud failed", e);
            throw new RuntimeException("store file to cloud failed");
        }
    }

    @Override
    public Boolean deleteFile(String filePath) {
        return commonConfig.getDeleteLocalFile() && FileUtil.del(filePath);
    }

    @Override
    public void zipFiles(List<String> filePaths, ServletOutputStream outputStream) throws Exception {
        String zipPath = ResourceUtil.zipFiles(filePaths.toArray(new String[0]));
        // 将zip文件响应给前端
        InputStream is = Files.newInputStream(Paths.get(zipPath));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        is.close();
        // 删除zip文件
        ResourceUtil.deleteFile(zipPath);
    }

    private FileTypeEnum getFileType(String fileName) {
        String[] split = fileName.split("\\.");
        String suffix = split[split.length - 1];
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.getName().equals(suffix)) {
                return fileTypeEnum;
            }
        }
        return FileTypeEnum.LAS;
    }

}
