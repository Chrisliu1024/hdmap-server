package com.hdmap.task.model.dto;

import lombok.Data;

/**
 * @author admin
 * @version 1.0
 * @date 2024/1/31 18:40
 * @description: 任务集请求参数
 */
@Data
public class TaskSetRequest {
    /**
     * 任务集名称
     */
    private String name;
    /**
     * 任务集类型
     */
    private Integer type;
    /**
     * 任务集创建人ID
     */
    private Long createBy;
    /**
     * 任务集关联的项目ID
     */
    private Long projectId;

}
