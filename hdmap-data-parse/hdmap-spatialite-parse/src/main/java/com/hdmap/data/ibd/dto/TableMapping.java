package com.hdmap.data.ibd.dto;

import lombok.Data;

import java.util.List;

/**
 * @description: 表映射关系，包含字段映射关系，字段值映射关系，过滤条件等信息
 * @author admin
 * @date 2023/11/1 11:08
 * @version 1.0
 */
@Data
public class TableMapping {
    /**
     * 源表名
     */
    String sourceTableName;
    /**
     * 目标表名
     */
    String targetTableName;
    /**
     * 源表主键ID名
     */
    String sourceIdColName;
    /**
     * 目标表主键ID名
     */
    String targetIdColName;
    /**
     * 过滤条件
     */
    String filter;
    /**
     * 字段映射关系
     */
    List<ColValMapping> colValMappings;

}
