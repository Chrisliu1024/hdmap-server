package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.entity.ProjectInfo;
import com.hdmap.task.model.entity.ProjectsUsersRel;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【project_info(项目表)】的数据库操作Service
* @createDate 2024-01-29 15:58:35
*/
public interface ProjectInfoService extends IService<ProjectInfo> {

    IPage<ProjectInfo> getByPage(IPage<ProjectInfo> page, ProjectInfo info);

    IPage<ProjectInfo> getByPage(IPage<ProjectInfo> page, ProjectsUsersRel projectsUsersRel);

    ProjectInfo getByTaskSetId(Serializable id);

    boolean removeByIdCustom(Serializable id);
}
