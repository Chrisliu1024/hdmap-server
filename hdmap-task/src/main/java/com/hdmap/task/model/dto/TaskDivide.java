package com.hdmap.task.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/1/31 13:56
 * @description: 单个拆分任务
 */
@Data
public class TaskDivide {
    /**
     * 领取人ID
     */
    private Long recipientId;
    /**
     * 任务包含的geohash，逗号分隔，与geometry一一对应
     */
    private List<String> geohashs;
    /**
     * 任务包含的网格geojson
     */
    private String geometry;
}
