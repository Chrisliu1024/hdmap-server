package com.hdmap.minio.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.hdmap.minio.config.MinioConfig;
import com.hdmap.minio.service.MinioSysFileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 *
 * @author admin
 * @version 1.0
 * @date 2023/7/18 18:31
 * @description: Minio 文件存储
 */
@Service
public class MinioSysFileServiceImpl implements MinioSysFileService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * Minio文件上传接口
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @return 云端文件存储url
     */
    @Override
    public String uploadFile(InputStream inputStream, String fileName) throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucket())
                .object(fileName)
                .stream(inputStream, inputStream.available(), -1)
                .contentType("application/octet-stream")
                .build();
        client.putObject(args);
        inputStream.close();
        return minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + fileName;
    }

    /**
     * Minio文件上传接口
     * @param localPath 本地文件路径
     * @return 云端文件存储url
     */
    @Override
    public String uploadFile(String localPath) throws Exception {
        File file = new File(localPath);
        InputStream input = Files.newInputStream(file.toPath());
        String fileName = file.getName();
        return uploadFile(input, fileName);
    }

    /**
     * Minio文件上传接口（压缩后上传）
     * @param localPath 本地文件路径
     * @return 云端文件存储url
     */
    public String uploadAndZipFile(String localPath) throws Exception {
        // get the directory of local path
        String fileName = getWholeFileNameByPath(localPath);
        String localDirectory = getDirectoryByPath(localPath);
        // zip file
        String zipFileName = getNameByWholeFileName(fileName) + ".zip";
        String zipFilePath = localDirectory + "/" + zipFileName;
        File zipFile = ZipUtil.zip(localPath, zipFilePath);
        InputStream input = Files.newInputStream(zipFile.toPath());
        String cloudPath = uploadFile(input, zipFileName);
        // delete zip file
        FileUtil.del(zipFilePath);
        return cloudPath;
    }

    /**
     * Minio文件下载接口
     * @param fileName 云端文件名
     * @return 文件输入流
     */
    @Override
    public InputStream downloadFile(String fileName) throws Exception {
        return client.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucket()).object(fileName).build());
    }

    /**
     * Minio文件下载接口
     * @param localDirectory 本地文件夹路径
     * @param fileName 云端文件名
     * @return 本地文件路径
     */
    @Override
    public String downloadFile(String localDirectory, String fileName) throws Exception {
        InputStream is = client.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucket()).object(fileName).build());
        // write to local
        String localPath = localDirectory + "/" + fileName;
        writeToLocal(localPath, is);
        return localPath;
    }

    /**
     * Minio文件下载接口（解压后下载）
     * @param localDirectory 本地文件夹路径
     * @param fileName 云端文件名
     * @return 解压后文件路径
     */
    @Override
    public String downloadAndUnzipFile(String localDirectory, String fileName) throws Exception {
        // download file
        String localPath = downloadFile(localDirectory, fileName);
        // unzip
        String unzipDirectory = localDirectory + "/unzip";
        File unzipFile = ZipUtil.unzip(localPath, unzipDirectory);
        // delete zip file
        FileUtil.del(localPath);
        // return las file path
        return unzipDirectory + "/" + getNameByWholeFileName(fileName) + ".las";
    }

    /**
     * Minio文件删除接口
     * @param fileName 云端文件名
     */
    @Override
    public void deleteFile(String fileName) throws Exception {
        client.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucket()).object(fileName).build());
    }


    private void writeToLocal(String localPath, InputStream is)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream localFile = new FileOutputStream(localPath);
        while ((index = is.read(bytes)) != -1) {
            localFile.write(bytes, 0, index);
            localFile.flush();
        }
        localFile.close();
        is.close();
    }

    private String getWholeFileNameByPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String getDirectoryByPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    private String getNameByWholeFileName(String wholeFileName) {
        return wholeFileName.substring(0, wholeFileName.lastIndexOf("."));
    }
}
