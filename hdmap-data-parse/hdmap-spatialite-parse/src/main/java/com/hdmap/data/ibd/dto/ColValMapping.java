package com.hdmap.data.ibd.dto;


import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @description: 字段值映射
 * @author admin
 * @date 2023/11/1 11:08
 * @version 1.0
 */
@Data
public class ColValMapping {
    /**
     * 源表字段名
     */
    String sourceColName;
    /**
     * 目标表字段名
     */
    String targetColName;
    /**
     * 映射函数名称
     */
    String mappingFunction;
    /**
     * 关联表是否来自于目标数据，true：是，false：来自于源数据库
     */
    Boolean isTargetDs = false;
    /**
     * 字段值映射
     */
    Map<String, String> valuesMapping;
    /**
     * 关联表名
     */
    String relTableName;
    /**
     * 关联表的关联字段名
     */
    List<String> relColName;
    /**
     * 关联表的目标字段名
     */
    List<String> relTargetColName;
    /**
     * 设置固定值时的输入值
     */
    String defaultValue;
}
