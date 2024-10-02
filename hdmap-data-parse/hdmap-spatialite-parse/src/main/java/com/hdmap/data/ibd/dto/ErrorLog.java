package com.hdmap.data.ibd.dto;

import com.hdmap.data.ibd.Enum.ErrorLogEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 17:29
 * @description: 错误日志
 */
@Data
@AllArgsConstructor
public class ErrorLog {
    /**
     * feature ID
     */
    String featureId;
    /**
     * 错误信息
     */
    ErrorLogEnum logEnum;
    /**
     * 源表名
     */
    String sourceTableName;
    /**
     * 目标表名
     */
    String targetTableName;
    /**
     * 源表字段名
     */
    String sourceColName;
    /**
     * 目标表字段名
     */
    String targetColName;
}
