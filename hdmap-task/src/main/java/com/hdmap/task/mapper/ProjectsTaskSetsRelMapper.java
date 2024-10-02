package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdmap.task.model.entity.ProjectsTaskSetsRel;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_task_sets_rel(项目-任务集关联表)】的数据库操作Mapper
* @createDate 2024-01-31 17:51:00
* @Entity com.hdmap.hdmap.task.entity.ProjectsTaskSetsRel
*/
public interface ProjectsTaskSetsRelMapper extends BaseMapper<ProjectsTaskSetsRel> {

    boolean deleteBatchByProjectIdAndTaskSetIds(Serializable projectId, List<Serializable> taskSetIds);
}




