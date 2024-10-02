package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.service.ProjectInfoService;
import com.hdmap.task.service.ProjectsDatasetsRelService;
import com.hdmap.task.service.ProjectsTaskSetsRelService;
import com.hdmap.task.service.ProjectsUsersRelService;
import com.hdmap.task.model.entity.ProjectInfo;
import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.hdmap.task.model.entity.ProjectsTaskSetsRel;
import com.hdmap.task.model.entity.ProjectsUsersRel;
import com.hdmap.task.mapper.ProjectInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;

/**
* @author admin
* @description 针对表【project_info(项目表)】的数据库操作Service实现
* @createDate 2024-01-29 15:58:35
*/
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo>
    implements ProjectInfoService {

    @Resource
    private ProjectsUsersRelService projectsUsersRelService;
    @Resource
    private ProjectsDatasetsRelService projectsDatasetsRelService;
    @Resource
    private ProjectsTaskSetsRelService projectsTaskSetsRelService;

    @Override
    public IPage<ProjectInfo> getByPage(IPage<ProjectInfo> page, ProjectInfo info) {
        return page(page, new QueryWrapper<>(info));
    }

    @Override
    public IPage<ProjectInfo> getByPage(IPage<ProjectInfo> page, ProjectsUsersRel projectsUsersRel) {
        return this.baseMapper.selectPageByProjectsUsersRel(page, projectsUsersRel);
    }

    @Override
    public ProjectInfo getByTaskSetId(Serializable id) {
        return this.baseMapper.selectOneByTaskSetId(id);
    }

    @Override
    public boolean removeByIdCustom(Serializable id) {
        Long longId = Long.valueOf(id.toString());
        // 删除项目时，检查项目-用户关联表、项目-数据集、项目-任务集中的数据是否存在，若存在，则提示
        ProjectsUsersRel projectsUsersRel = new ProjectsUsersRel();
        projectsUsersRel.setProjectId(longId);
        if (projectsUsersRelService.count(new QueryWrapper<>(projectsUsersRel)) > 0) {
            throw new RuntimeException("项目-用户关联表中存在数据");
        }
        ProjectsDatasetsRel projectsDatasetsRel = new ProjectsDatasetsRel();
        projectsDatasetsRel.setProjectId(longId);
        if (projectsDatasetsRelService.count(new QueryWrapper<>(projectsDatasetsRel)) > 0) {
            throw new RuntimeException("项目-数据关联表中存在数据");
        }
        ProjectsTaskSetsRel projectsTaskSetsRel = new ProjectsTaskSetsRel();
        projectsTaskSetsRel.setProjectId(longId);
        if (projectsTaskSetsRelService.count(new QueryWrapper<>(projectsTaskSetsRel)) > 0) {
            throw new RuntimeException("项目-任务集关联表中存在数据");
        }
        return removeById(id);

    }

    private boolean saveProjectUserRel(Long projectId, Long userId) {
        ProjectsUsersRel rel = new ProjectsUsersRel();
        rel.setProjectId(projectId);
        rel.setUserId(userId);
        return projectsUsersRelService.save(rel);
    }
}




