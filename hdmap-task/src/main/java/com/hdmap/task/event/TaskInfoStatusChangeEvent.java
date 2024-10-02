package com.hdmap.task.event;

import com.hdmap.task.model.entity.TaskInfo;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/23 10:13
 * @description: 任务信息变更事件
 */
public class TaskInfoStatusChangeEvent extends TaskInfoChangeEvent {
    private Integer oldStatus;
    private Integer newStatus;

    public TaskInfoStatusChangeEvent(TaskInfo source, Integer oldStatus, Integer newStatus) {
        super(source);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public int getOldStatus() {
        return oldStatus;
    }

    public int getNewStatus() {
        return newStatus;
    }
}
