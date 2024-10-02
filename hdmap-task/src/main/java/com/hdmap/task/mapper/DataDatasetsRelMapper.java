package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.DataDatasetsRel;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【data_datasets_rel(数据-数据集关联表)】的数据库操作Mapper
* @createDate 2024-01-29 18:19:38
* @Entity com.hdmap.hdmap.task.entity.DataDatasetsRel
*/
public interface DataDatasetsRelMapper extends BaseMapper<DataDatasetsRel> {

    boolean deleteByDataIds(List<Serializable> dataIds);

    IPage<DataDatasetsRel> selectByDatasetId(IPage<DataDatasetsRel> page, Serializable datasetId);

    boolean updateDatasetIdByDatasetIdAndDataId(Long datasetId, Long dataId, Long newDatasetId);
}




