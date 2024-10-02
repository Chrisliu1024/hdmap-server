package com.hdmap.core.utils;

import java.util.Map;
import com.google.gson.Gson;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 14:20
 * @description: 类型转换工具类
 */
public class ConvertUtil {

    private ConvertUtil() {
    }

    /**
     * Map转换为Json
     */
    public static String mapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    /**
     * Json转换为Map
     */
    public static Map<String, Object> jsonToMap(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Map.class);
    }

}
