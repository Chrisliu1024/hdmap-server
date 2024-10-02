package com.hdmap.data.ibd.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 11:27
 * @description: md5工具类
 */
public class Md5Util {

    private Md5Util() {
    }

    public static String getFileMD5(String filePath) throws IOException {
        // 计算文件md5码
        InputStream is = Files.newInputStream(Paths.get(filePath));
        return DigestUtils.md5Hex(is);

    }
}
