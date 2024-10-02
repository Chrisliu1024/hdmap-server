package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.model.entity.ProjectsTaskSetsRel;
import com.hdmap.task.mapper.ProjectsTaskSetsRelMapper;
import com.hdmap.task.service.ProjectsTaskSetsRelService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_task_sets_rel(项目-任务集关联表)】的数据库操作Service实现
* @createDate 2024-01-31 17:51:00
*/
@Service
public class ProjectsTaskSetsRelServiceImpl extends ServiceImpl<ProjectsTaskSetsRelMapper, ProjectsTaskSetsRel>
    implements ProjectsTaskSetsRelService {

    @Override
    public boolean removeBatchByProjectIdAndTaskSetIds(Serializable projectId, List<Serializable> taskSetIds) {
        return this.baseMapper.deleteBatchByProjectIdAndTaskSetIds(projectId, taskSetIds);
    }
}




