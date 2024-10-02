package com.hdmap.data.etl.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 17:59
 * @description: 表关联结果
 */
@Data
public class TablesRelResult implements Serializable {
    /**
     * 源数据表ID（左）
     */
    private Long leftTableId;
    /**
     * 源数据表ID（右）
     */
    private Long rightTableId;
    /**
     * 源数据字段ID（左）
     */
    private Long leftFieldId;
    /**
     * 源数据字段ID（右）
     */
    private Long rightFieldId;

}
