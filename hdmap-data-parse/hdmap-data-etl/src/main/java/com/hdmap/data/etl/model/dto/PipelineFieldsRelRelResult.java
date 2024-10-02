package com.hdmap.data.etl.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 17:59
 * @description: 字段关联结果
 */
@Data
public class PipelineFieldsRelRelResult implements Serializable {
    /**
     * 处理管线pipeline ID
     */
    private Long pipelineId;
    /**
     * 处理管线pipeline名称
     */
    private FieldsRelResult fieldsRelResult;
}
