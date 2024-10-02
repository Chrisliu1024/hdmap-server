package com.hdmap.task.mapper;

import com.hdmap.task.model.entity.TaskPoolInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【task_pool_info(任务池表，逻辑表，用于关联项目与数据，并记录数据的分配状态)】的数据库操作Mapper
* @createDate 2024-05-15 16:42:12
* @Entity generator.entity.TaskPoolInfo
*/
public interface TaskPoolInfoMapper extends BaseMapper<TaskPoolInfo> {

    TaskPoolInfo getOneTaskByProjectIdAndStatus(Serializable id, Serializable status, int limit);

    boolean deleteBatchByProjectIdAndDatasetIdsAndStatus(Serializable projectId, List<Serializable> datasetIds, Integer status);
}




