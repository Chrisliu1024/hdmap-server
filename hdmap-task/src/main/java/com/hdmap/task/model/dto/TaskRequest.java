package com.hdmap.task.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/22 15:17
 * @description: 任务请求
 */
@Data
@Schema(description = "任务请求")
public class TaskRequest {
    /**
     * 项目ID
     */
    @Schema(description = "项目ID", required = true)
    private Long projectId;
    /**
     * 人员ID
     */
    @Schema(description = "人员ID", required = true)
    private Long userId;
    /**
     * 人员角色
     */
    @Schema(description = "人员角色(map_dataCollector 采集员，map_producer 作业员，map_checkuser 质检员)", required = true)
    private String role;
}
