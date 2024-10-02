package com.hdmap.task.model.dto;

import com.hdmap.task.model.entity.TaskInfo;
import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/2/4 10:45
 * @description: 任务批量请求
 */
@Data
public class TaskBatchRequest {
    /**
     * 任务集ID
     */
    private Long taskSetId;
    /**
     * 任务列表
     */
    private List<TaskInfo> tasks;
}
