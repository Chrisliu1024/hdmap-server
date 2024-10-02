package com.hdmap.pointcloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.pointcloud.dto.GridClipResult;
import com.hdmap.pointcloud.entity.SourceFileInfo;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/18 16:58
 * @description: 文件下载
 */
public interface FileHandleService {

    List<GridClipResult> uploadAndClipFile(Long identifier, String localPath, String sourceLocation, Integer precision) throws Exception;

    SourceFileInfo saveSourceFileInfo(Long identifier, String fileName, FileTypeEnum fileType, String sourceLocation);

    boolean updateSourceFileStatus(SourceFileInfo fileInfo, Integer status);

    List<SourceFileInfo> getSourceFileInfo(IPage<SourceFileInfo> page, Serializable identifier);

    String storeFileToLocal(MultipartFile file);

    String storeFileToCloud(MultipartFile file);

    Boolean deleteFile(String filePath);

    void zipFiles(List<String> filePaths, ServletOutputStream outputStream) throws Exception;

}
