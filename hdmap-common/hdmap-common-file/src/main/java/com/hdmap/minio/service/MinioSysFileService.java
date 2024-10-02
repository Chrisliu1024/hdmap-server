package com.hdmap.minio.service;

import java.io.InputStream;

public interface MinioSysFileService {

    String uploadFile(InputStream inputStream, String fileName) throws Exception;

    String uploadFile(String path) throws Exception;

    String uploadAndZipFile(String localPath) throws Exception;

    InputStream downloadFile(String fileName) throws Exception;

    String downloadFile(String localDirectory, String fileName) throws Exception;

    String downloadAndUnzipFile(String localDirectory, String fileName) throws Exception;

    void deleteFile(String fileName) throws Exception;

}
