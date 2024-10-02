package com.hdmap.data.etl.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 17:59
 * @description: 处理管线pipeline和数据存储关联结果
 */
@Data
public class PipelineDatastoreRelResult implements Serializable {
    /**
     * 处理管线ID
     */
    private Long pipelineId;
    /**
     * 数据存储ID列表
     */
    private List<Long> datastoreIdList;

}
