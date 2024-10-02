package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdmap.task.model.entity.TaskSetInfo;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【task_set_info(任务集信息表)】的数据库操作Mapper
* @createDate 2024-01-29 15:58:35
* @Entity com.hdmap.hdmap.task.entity.TaskSetInfo
*/
public interface TaskSetInfoMapper extends BaseMapper<TaskSetInfo> {
    IPage<TaskSetInfo> selectByProjectIdAndCreateBy(IPage<TaskSetInfo> page, Serializable projectId, Serializable createBy);
    IPage<TaskSetInfo> selectByProjectId(IPage<TaskSetInfo> page, Serializable id);

    TaskSetInfo selectByTaskId(Serializable id);

    IPage<TaskSetInfo> selectListByProjectId(IPage<TaskSetInfo> page, Serializable id);
}




