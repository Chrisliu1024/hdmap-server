package com.hdmap.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.dto.UserShortInfo;
import com.hdmap.task.model.entity.ProjectsUsersRel;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_users_rel(项目-用户关联表)】的数据库操作Service
* @createDate 2024-01-29 15:58:35
*/
public interface ProjectsUsersRelService extends IService<ProjectsUsersRel> {

    List<UserShortInfo> getUserInfosByProjectId(Serializable id);

    List<UserShortInfo> getUserInfosByProjectIdAndRole(Serializable id, String role);
    boolean removeBatchByProjectIdAndUserIds(Serializable projectId, List<Serializable> userIds);
}
