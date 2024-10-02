package com.hdmap.task.model.dto;

import com.hdmap.task.enums.TaskSetTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/1/31 13:50
 * @description: 基于网格划分的任务拆分结果
 */
@Data
public class TaskDivideResult {
    /**
     * 任务集ID
     */
    private Long taskSetId;
    /**
     * 任务集名称
     */
    private String taskSetName;
    /**
     * 类型
     */
    private TaskSetTypeEnum type;
    /**
     * 网格划分结果
     */
    List<TaskDivide> tasks;
}

