package com.hdmap.task.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdmap.task.model.entity.DataDatasetsRel;
import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.hdmap.task.model.entity.TaskPoolInfo;
import com.hdmap.task.service.DataDatasetsRelService;
import com.hdmap.task.service.ProjectsDatasetsRelService;
import com.hdmap.task.service.TaskPoolInfoService;
import com.hdmap.core.utils.SnowFlakeIdUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/23 14:56
 * @description: 任务池更新监听器
 */

@Component
public class TaskPoolUpdateListener {

    @Resource
    private TaskPoolInfoService taskPoolInfoService;
    @Resource
    private DataDatasetsRelService dataDatasetsRelService;
    @Resource
    private ProjectsDatasetsRelService projectsDatasetsRelService;

    @EventListener
    public void handleProjectsDatasetsRelEvent(ProjectsDatasetsRelEvent event) {
        List<ProjectsDatasetsRel> rels = event.getEntitys();
        for (ProjectsDatasetsRel rel : rels) {
            TaskPoolInfo taskPoolInfo = new TaskPoolInfo();
            taskPoolInfo.setProjectId(rel.getProjectId());
            taskPoolInfo.setDatasetId(rel.getDatasetId());
            taskPoolInfo.setPriority(rel.getPriority());
            // 查询数据集下的数据信息
            DataDatasetsRel dataDatasetsRel = new DataDatasetsRel();
            dataDatasetsRel.setDatasetId(rel.getDatasetId());
            List<DataDatasetsRel> dataDatasetsRels = dataDatasetsRelService.list(new QueryWrapper<>(dataDatasetsRel));
            for (DataDatasetsRel dataDatasetsRel1 : dataDatasetsRels) {
                taskPoolInfo.setId(SnowFlakeIdUtil.getId());
                taskPoolInfo.setDataId(dataDatasetsRel1.getDataId());
                taskPoolInfoService.save(taskPoolInfo);
            }
        }
    }

    @EventListener
    public void handleDataDatasetsRelEvent(DataDatasetsRelEvent event) {
        DataDatasetsRel dataDatasetsRel = event.getEntity();
        Long datasetId = dataDatasetsRel.getDatasetId();
        // 获取数据集关联的项目
        ProjectsDatasetsRel projectsDatasetsRel = new ProjectsDatasetsRel();
        projectsDatasetsRel.setDatasetId(datasetId);
        List<ProjectsDatasetsRel> projectsDatasetsRels = projectsDatasetsRelService.list(new QueryWrapper<>(projectsDatasetsRel));
        if (projectsDatasetsRels.isEmpty()) {
            return;
        }
        for (ProjectsDatasetsRel rel : projectsDatasetsRels) {
            TaskPoolInfo taskPoolInfo = new TaskPoolInfo();
            taskPoolInfo.setProjectId(rel.getProjectId());
            taskPoolInfo.setDatasetId(rel.getDatasetId());
            taskPoolInfo.setPriority(rel.getPriority());
            taskPoolInfo.setId(SnowFlakeIdUtil.getId());
            taskPoolInfo.setDataId(dataDatasetsRel.getDataId());
            taskPoolInfoService.save(taskPoolInfo);
        }
    }
}
