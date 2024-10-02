package com.hdmap.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.entity.ProjectsTaskSetsRel;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_task_sets_rel(项目-任务集关联表)】的数据库操作Service
* @createDate 2024-01-31 17:51:00
*/
public interface ProjectsTaskSetsRelService extends IService<ProjectsTaskSetsRel> {

    boolean removeBatchByProjectIdAndTaskSetIds(Serializable projectId, List<Serializable> taskSetIds);

}
