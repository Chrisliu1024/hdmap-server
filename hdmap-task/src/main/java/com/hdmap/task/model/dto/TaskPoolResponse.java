package com.hdmap.task.model.dto;

import com.hdmap.task.model.entity.TaskInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务池表，逻辑表，用于关联项目与数据，并记录数据的分配状态
 */
@Data
public class TaskPoolResponse implements Serializable {
    /**
     * 任务池ID
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 数据状态：0 未下发，1 已下发
     */
    private Integer status;

    /**
     * 数据集ID
     */
    private Long datasetId;

    /**
     * 优先级：0 高，1 中，2 低
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 备注
     */
    private String memo;

    /**
     * 任务
     */
    private TaskInfo taskInfo;
}