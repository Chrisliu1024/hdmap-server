package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.task.model.entity.DataDatasetsRel;
import com.hdmap.task.event.DataDatasetsRelEvent;
import com.hdmap.task.mapper.DataDatasetsRelMapper;
import com.hdmap.task.service.DataDatasetsRelService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【data_datasets_rel(数据-数据集关联表)】的数据库操作Service实现
* @createDate 2024-01-29 18:19:38
*/
@Service
public class DataDatasetsRelServiceImpl extends ServiceImpl<DataDatasetsRelMapper, DataDatasetsRel>
    implements DataDatasetsRelService, ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public IPage<DataDatasetsRel> getByDatasetId(IPage<DataDatasetsRel> page, Serializable datasetId) {
        return this.baseMapper.selectByDatasetId(page, datasetId);
    }

    @Override
    public boolean removeByDataIds(List<Serializable> dataIds) {
        return this.baseMapper.deleteByDataIds(dataIds);
    }

    @Override
    public boolean saveCustom(Long datasetId, Long dataId) {
        DataDatasetsRel entity = new DataDatasetsRel();
        entity.setDatasetId(datasetId);
        entity.setDataId(dataId);
        // 发布事件
        eventPublisher.publishEvent(new DataDatasetsRelEvent(this, entity));
        return this.save(entity);
    }

    @Override
    public boolean update(Long datasetId, Long dataId, Long newDatasetId) {
        return this.baseMapper.updateDatasetIdByDatasetIdAndDataId(datasetId, dataId, newDatasetId);
    }

}




