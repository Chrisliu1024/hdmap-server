package com.hdmap.task.event;

import com.hdmap.task.model.entity.TaskPoolInfo;
import com.hdmap.task.service.TaskPoolInfoService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/23 10:19
 * @description: 全局任务状态变更监听器
 */
@Component
public class TaskInfoStatusChangeListener implements TaskInfoChangeListener {
    @Resource
    private TaskPoolInfoService taskPoolInfoService;

    @Override
    public void changed(TaskInfoChangeEvent event) {
        if (!(event instanceof TaskInfoStatusChangeEvent)) {
            return;
        }
        TaskInfoStatusChangeEvent statusChangeEvent = (TaskInfoStatusChangeEvent) event;
        // 获取任务池
        TaskPoolInfo taskPoolInfo = taskPoolInfoService.getByTaskId(event.getEntity().getId());
        // 更新任务池状态
        taskPoolInfo.setStatus(statusChangeEvent.getNewStatus());
        taskPoolInfoService.updateById(taskPoolInfo);
    }
}