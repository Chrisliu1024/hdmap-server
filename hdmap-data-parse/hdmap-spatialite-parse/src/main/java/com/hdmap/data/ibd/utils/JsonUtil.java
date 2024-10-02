package com.hdmap.data.ibd.utils;

public class JsonUtil {

    private JsonUtil() {
    }

    public static String wrapperJson(String originStr) {
        // 剔除结尾的逗号或分号
        if (originStr.endsWith(",") || originStr.endsWith(";")) {
            originStr = originStr.substring(0, originStr.length() - 1);
        }
        // 头尾加括号
        String json = String.format("{%s}", originStr);
        // 添加分号
        return json.replace(":", "\":\"").replace(";", "\",\"").replace("{", "{\"").replace("}", "\"}");
    }
}
