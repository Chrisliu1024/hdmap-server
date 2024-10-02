package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdmap.task.model.entity.TaskInfo;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【task_info(任务信息表)】的数据库操作Mapper
* @createDate 2024-02-04 11:33:54
* @Entity com.hdmap.hdmap.task.entity.TaskInfo
*/
public interface TaskInfoMapper extends BaseMapper<TaskInfo> {

    IPage<TaskInfo> selectByProjectIdAndInfo(IPage<TaskInfo> page, Serializable id, TaskInfo info);

    IPage<TaskInfo> selectByTaskSetId(IPage<TaskInfo> page, Serializable id);

    IPage<TaskInfo> selectByTaskSetIdAndCheckerId(IPage<TaskInfo> page, Serializable taskSetId, Serializable checkerId);

}




