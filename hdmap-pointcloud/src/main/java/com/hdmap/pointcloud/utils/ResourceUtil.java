package com.hdmap.pointcloud.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.hdmap.core.utils.SnowFlakeIdUtil;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/16 14:21
 * @description: 系统资源工具类
 */

@Component
public class ResourceUtil {

    private static boolean deleteLocalFile;

    private final static String ZIP_FILE_SUFFIX = ".zip";

    private ResourceUtil() {
    }

    @Value("${pointcloud.deleteLocalFile:true}")
    public void setDeleteLocalFile(Boolean deleteLocalFile) {
        ResourceUtil.deleteLocalFile = deleteLocalFile;
    }

    /**
     * 获取jar包所在目录
     *
     * @return jar包所在目录
     */
    public static String getJarRootPath() {
        ApplicationHome h = new ApplicationHome(ResourceUtil.class);
        File jarF = h.getSource();
        if (jarF != null) {
            return jarF.getParentFile().toString();
        } else {
            // Use a default path when running in a non-packaged environment
            return System.getProperty("user.dir");
        }
    }

    /**
     * 将InputStream写入本地文件
     * @param localPath 写入本地目录
     * @param input	输入流
     * @throws IOException
     */
    public static void writeToLocal(String localPath, InputStream input) throws IOException {
        // check the existence of the root directory
        String rootPath = localPath.substring(0, localPath.lastIndexOf("/"));
        File rootDir = new File(rootPath);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream localFile = new FileOutputStream(localPath);
        while ((index = input.read(bytes)) != -1) {
            localFile.write(bytes, 0, index);
            localFile.flush();
        }
        localFile.close();
        input.close();
    }

    public static String storeInputStreamToLocal(InputStream inputStream, String fileName) {
        String localPath = getJarRootPath() + "/" + getUniqueTag() + "/" + fileName;
        try {
            writeToLocal(localPath, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localPath;
    }

    public static String zipDirectory(String directory) throws Exception {
        String zipPath = getJarRootPath() + "/" + getUniqueTag() + ZIP_FILE_SUFFIX;
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        ZipUtil.zip(directory, zipPath, true);
        return zipPath;
    }

    public static String zipFiles(String[] filePaths) {
        String zipPath = getJarRootPath() + "/" + getUniqueTag() + ZIP_FILE_SUFFIX;
        File zipFile = new File(zipPath);
        File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            files[i] = new File(filePaths[i]);
        }
        ZipUtil.zip(zipFile, true, files);
        return zipPath;
    }

    public static boolean deleteFile(String filePath) {
        return deleteLocalFile && FileUtil.del(filePath);
    }

    public static boolean checkCRSTransform(CoordinateReferenceSystem crs, int srid) {
        return crs == null || !crs.getIdentifiers().iterator().next().toString().contains(String.valueOf(srid));
    }

    public static String getWholeFileNameByPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static FileTypeEnum getFileTypeByPath(String path) {
        String fileName = getWholeFileNameByPath(path);
        // get file suffix
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return FileTypeEnum.getBySuffix(suffix);
    }

    public static Geometry getBboxByPath(String path) throws FactoryException {
        FileTypeEnum fileType = getFileTypeByPath(path);
        if (FileTypeEnum.LAS.equals(fileType)) {
            ReferencedEnvelope envelope = PipelineUtil.getBBoxByPath(path);
            // ReferencedEnvelope -> Geometry
            return JTS.toGeometry(envelope);
        } else if (FileTypeEnum.PCD.equals(fileType)) {
            // TODO
        } else if (FileTypeEnum.CSV.equals(fileType)) {
            // TODO
        } else if (FileTypeEnum.IBD.equals(fileType)) {
            // TODO
        }
        return null;
    }

    public static long getUniqueTag() {
        return SnowFlakeIdUtil.getId();
    }

    public static String getUniqueLocalPath() {
        return getJarRootPath() + "/" + getUniqueTag();
    }

    public static String getUniqueWholeName(String fileName) {
        // if exit / in file name, split the file name after /
        if (fileName.contains("/")) {
            String[] split = fileName.split("/");
            fileName = split[split.length - 1];
        }
        // split the suffix
        String[] split = fileName.split("\\.");
        String prefix = fileName.substring(0, fileName.length() - split[split.length - 1].length() - 1);
        String suffix = split[split.length - 1];
        return prefix + "_" + getUniqueTag() + "." + suffix;
    }

    public static String getLocalPathAndUniqueWholeName(String fileName) {
        return getJarRootPath() + "/" + getUniqueWholeName(fileName);
    }

    public static String getUniquePrefix(Serializable prefix) {
        return prefix + "_" + getUniqueTag();
    }

    public static String getUniqueLocalPathAndPrefixName(Serializable prefix) {
        return getJarRootPath() + "/" + getUniquePrefix(prefix);
    }
}
