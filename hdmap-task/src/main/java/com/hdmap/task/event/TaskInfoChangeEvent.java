package com.hdmap.task.event;

import com.hdmap.task.model.entity.TaskInfo;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/23 10:13
 * @description: 任务信息变更事件
 */
public abstract class TaskInfoChangeEvent {
    private TaskInfo entity;

    public TaskInfoChangeEvent(TaskInfo source) {
        this.entity = source;
    }

    public TaskInfo getEntity() {
        return entity;
    }

}
