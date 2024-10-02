package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdmap.task.model.dto.UserShortInfo;
import com.hdmap.task.model.entity.ProjectsUsersRel;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_users_rel(项目-用户关联表)】的数据库操作Mapper
* @createDate 2024-01-29 15:58:35
* @Entity com.hdmap.hdmap.task.entity.ProjectsUsersRel
*/
public interface ProjectsUsersRelMapper extends BaseMapper<ProjectsUsersRel> {

    List<UserShortInfo> selectUserShortInfosByProjectId(Serializable id);

    List<UserShortInfo> selectUserShortInfosByProjectIdAndRole(Serializable id, String role);

    boolean deleteBatchByProjectIdAndUserIds(Serializable projectId, List<Serializable> userIds);
}




