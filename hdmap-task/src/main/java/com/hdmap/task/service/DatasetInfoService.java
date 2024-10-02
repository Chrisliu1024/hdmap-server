package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.entity.DatasetInfo;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【dataset_info(数据集信息表)】的数据库操作Service
* @createDate 2024-01-29 15:58:35
*/
public interface DatasetInfoService extends IService<DatasetInfo> {
    IPage<DatasetInfo> getByPage(IPage<DatasetInfo> page, DatasetInfo info);

    IPage<DatasetInfo> selectListByProjectId(IPage<DatasetInfo> page, Serializable id);

    boolean removeByIdsCustom(List<Serializable> ids);

    DatasetInfo recycleUnallocatedDataByDatasetIdAndProjectId(Serializable datasetId, Serializable projectId);

}
