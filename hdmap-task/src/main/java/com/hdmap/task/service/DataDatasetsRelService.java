package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.entity.DataDatasetsRel;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【data_datasets_rel(数据-数据集关联表)】的数据库操作Service
* @createDate 2024-01-29 18:19:38
*/
public interface DataDatasetsRelService extends IService<DataDatasetsRel> {
    IPage<DataDatasetsRel> getByDatasetId(IPage<DataDatasetsRel> page, Serializable datasetId);
    boolean removeByDataIds(List<Serializable> dataIds);
    boolean saveCustom(Long datasetId, Long dataId);
    boolean update(Long datasetId, Long dataId, Long newDatasetId);
}
