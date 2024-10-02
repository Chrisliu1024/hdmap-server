package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.Pipeline;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 17:51
 * @description: 处理管线pipeline关联字段关联关系
 */
@Data
public class PipelineFieldsRelRelInfo implements Serializable {
    /**
     * 处理管线pipeline
     */
    private Pipeline pipeline;
    /**
     * 字段关联关系
     */
    private FieldsRelInfo fieldsRelInfo;

}

