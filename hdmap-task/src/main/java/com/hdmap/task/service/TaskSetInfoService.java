package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.dto.TaskDivideResult;
import com.hdmap.task.model.dto.TaskSetRequest;
import com.hdmap.task.model.entity.TaskSetInfo;
import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【task_set_info(任务集信息表)】的数据库操作Service
* @createDate 2024-01-29 15:58:35
*/
public interface TaskSetInfoService extends IService<TaskSetInfo> {

    IPage<TaskSetInfo> getByPage(IPage<TaskSetInfo> page, TaskSetInfo info);

    IPage<TaskSetInfo> getByProjectIdAndCreateBy(IPage<TaskSetInfo> page, Serializable projectId, Serializable createBy);

    IPage<TaskSetInfo> getByProjectId(IPage<TaskSetInfo> page, Serializable id);

    IPage<TaskSetInfo> getByCreateBy(IPage<TaskSetInfo> page, Serializable id);

    TaskSetInfo getByTaskId(Serializable id);

    TaskDivideResult getDivideTasksById(Serializable id, List<String> geohashs) throws IOException, FactoryException;

    boolean save(TaskSetRequest request);

    IPage<TaskSetInfo> selectListByProjectId(IPage<TaskSetInfo> page, Serializable id);
}
