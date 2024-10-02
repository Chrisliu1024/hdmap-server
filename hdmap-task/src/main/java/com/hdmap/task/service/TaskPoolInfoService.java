package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.dto.TaskPoolResponse;
import com.hdmap.task.model.entity.TaskPoolInfo;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【task_pool_info(任务池表，逻辑表，用于关联项目与数据，并记录数据的分配状态)】的数据库操作Service
* @createDate 2024-05-15 16:46:32
*/
public interface TaskPoolInfoService extends IService<TaskPoolInfo> {

    IPage<TaskPoolInfo> getShortByPage(IPage<TaskPoolInfo> page, TaskPoolInfo info);
    IPage<TaskPoolResponse> getDetailByPage(IPage<TaskPoolInfo> page, TaskPoolInfo info);
    TaskPoolInfo getByTaskId(Serializable id);
    TaskPoolInfo getOneTaskByProjectIdAndStatus(Serializable id, Serializable status);
    TaskPoolInfo getOneTaskByProjectIdAndStatus(Serializable id, Serializable status, int limit);
    boolean deleteBatchByProjectIdAndDatasetIdsAndStatus(Serializable projectId, List<Serializable> datasetIds, Integer status);
    List<TaskPoolInfo> getTaskByProjectIdAndDatasetIdAndStatus(Serializable projectId, Serializable datasetId, Serializable status);
}
