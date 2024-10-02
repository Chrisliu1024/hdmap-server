package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.mapper.DatasetInfoMapper;
import com.hdmap.task.model.entity.DataDatasetsRel;
import com.hdmap.task.model.entity.DatasetInfo;
import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.hdmap.task.model.entity.TaskPoolInfo;
import com.hdmap.task.service.DataDatasetsRelService;
import com.hdmap.task.service.DatasetInfoService;
import com.hdmap.task.service.ProjectsDatasetsRelService;
import com.hdmap.task.service.TaskPoolInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* @author admin
* @description 针对表【dataset_info(数据集信息表)】的数据库操作Service实现
* @createDate 2024-01-29 15:58:35
*/
@Service
public class DatasetInfoServiceImpl extends ServiceImpl<DatasetInfoMapper, DatasetInfo>
    implements DatasetInfoService {

    @Resource
    private DataDatasetsRelService dataDatasetsRelService;
    @Resource
    private ProjectsDatasetsRelService projectsDatasetsRelService;
    @Resource
    private TaskPoolInfoService taskPoolInfoService;

    @Override
    public IPage<DatasetInfo> getByPage(IPage<DatasetInfo> page, DatasetInfo info) {
        return this.baseMapper.page(page, info);
    }

    @Override
    public IPage<DatasetInfo> selectListByProjectId(IPage<DatasetInfo> page, Serializable id) {
        return this.baseMapper.getListByProjectId(page, id);
    }

    @Override
    public boolean removeByIdsCustom(List<Serializable> ids) {
        for (Serializable id : ids) {
            ProjectsDatasetsRel projectsDatasetsRel = new ProjectsDatasetsRel();
            projectsDatasetsRel.setDatasetId(Long.valueOf(id.toString()));
            if (projectsDatasetsRelService.count(new QueryWrapper<>(projectsDatasetsRel)) > 0) {
                throw new RuntimeException("数据集已关联项目，请先移除关联后删除");
            }
            DataDatasetsRel dataDatasetsRel = new DataDatasetsRel();
            dataDatasetsRel.setDatasetId(Long.valueOf(id.toString()));
            if (dataDatasetsRelService.count(new QueryWrapper<>(dataDatasetsRel)) > 0) {
                throw new RuntimeException("数据集下存在数据关联，请先移除关联后删除");
            }
        }
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatasetInfo recycleUnallocatedDataByDatasetIdAndProjectId(Serializable datasetId, Serializable projectId) {
        // 获取任务池中未分配的数据
        List<TaskPoolInfo> taskPoolInfos = taskPoolInfoService.getTaskByProjectIdAndDatasetIdAndStatus(projectId, datasetId, 0);
        // 删除任务池中未分配的数据
        List<Serializable> datasetIds = new ArrayList<>();
        datasetIds.add(datasetId);
        boolean remove = taskPoolInfoService.deleteBatchByProjectIdAndDatasetIdsAndStatus(projectId, datasetIds, 0);
        // 生成新的数据集信息
        DatasetInfo datasetInfo = this.getById(datasetId);
        DatasetInfo newDatasetInfo = new DatasetInfo();
        newDatasetInfo.setName(datasetInfo.getName() + "_recycle");
        newDatasetInfo.setType(datasetInfo.getType());
        newDatasetInfo.setCreateBy(datasetInfo.getCreateBy());
        this.save(newDatasetInfo);
        // 修改数据-数据集关联关系
        for (TaskPoolInfo taskPoolInfo : taskPoolInfos) {
            Long dataId = taskPoolInfo.getDataId();
            dataDatasetsRelService.update(Long.valueOf(datasetId.toString()), dataId, newDatasetInfo.getId());
        }
        return newDatasetInfo;
    }

}




