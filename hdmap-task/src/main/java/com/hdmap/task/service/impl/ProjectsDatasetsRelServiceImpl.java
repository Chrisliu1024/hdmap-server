package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.hdmap.task.enums.TaskStatusEnum;
import com.hdmap.task.event.ProjectsDatasetsRelEvent;
import com.hdmap.task.mapper.ProjectsDatasetsRelMapper;
import com.hdmap.task.service.DataDatasetsRelService;
import com.hdmap.task.service.ProjectsDatasetsRelService;
import com.hdmap.task.service.TaskPoolInfoService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_datasets_rel(项目-数据集关联表)】的数据库操作Service实现
* @createDate 2024-05-15 16:46:32
*/
@Service
public class ProjectsDatasetsRelServiceImpl extends ServiceImpl<ProjectsDatasetsRelMapper, ProjectsDatasetsRel>
    implements ProjectsDatasetsRelService, ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @Resource
    private TaskPoolInfoService taskPoolInfoService;
    @Resource
    private DataDatasetsRelService dataDatasetsRelService;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBatchByProjectIdAndDatasetIds(Serializable projectId, List<Serializable> datasetIds, boolean isDeleteRel) {
        boolean removedProjectRel = this.baseMapper.deleteBatchByProjectIdAndDatasetIds(projectId, datasetIds);
        if (!isDeleteRel) {
            return removedProjectRel;
        }
        boolean removedTaskPoolRel = false;
        if (removedProjectRel) {
            // 删除关联的任务池信息
            removedTaskPoolRel = taskPoolInfoService.deleteBatchByProjectIdAndDatasetIdsAndStatus(projectId, datasetIds, TaskStatusEnum.UNASSIGNED.getCode());
        }
        return removedProjectRel && removedTaskPoolRel;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchAndInsertTaskPool(List<ProjectsDatasetsRel> rels) {
        this.saveBatch(rels);
        // 发布事件
        eventPublisher.publishEvent(new ProjectsDatasetsRelEvent(this, rels));
        return true;
    }

}




