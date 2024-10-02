package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.ProjectInfo;
import com.hdmap.task.model.entity.ProjectsUsersRel;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【project_info(项目表)】的数据库操作Mapper
* @createDate 2024-01-29 15:58:35
* @Entity com.hdmap.hdmap.task.entity.ProjectInfo
*/
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {

    ProjectInfo selectOneByTaskSetId(Serializable id);

    IPage<ProjectInfo> selectPageByProjectsUsersRel(IPage<ProjectInfo> page, ProjectsUsersRel projectsUsersRel);
}




