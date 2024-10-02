package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.model.dto.UserShortInfo;
import com.hdmap.task.model.entity.ProjectsUsersRel;
import com.hdmap.task.mapper.ProjectsUsersRelMapper;
import com.hdmap.task.service.ProjectsUsersRelService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_users_rel(项目-用户关联表)】的数据库操作Service实现
* @createDate 2024-01-29 15:58:35
*/
@Service
public class ProjectsUsersRelServiceImpl extends ServiceImpl<ProjectsUsersRelMapper, ProjectsUsersRel>
    implements ProjectsUsersRelService {

    @Override
    public List<UserShortInfo> getUserInfosByProjectId(Serializable id) {
        return this.baseMapper.selectUserShortInfosByProjectId(id);
    }

    @Override
    public List<UserShortInfo> getUserInfosByProjectIdAndRole(Serializable id, String role) {
        return this.baseMapper.selectUserShortInfosByProjectIdAndRole(id, role);
    }

    public boolean removeBatchByProjectIdAndUserIds(Serializable projectId, List<Serializable> userIds) {
        return this.baseMapper.deleteBatchByProjectIdAndUserIds(projectId, userIds);
    }

}




