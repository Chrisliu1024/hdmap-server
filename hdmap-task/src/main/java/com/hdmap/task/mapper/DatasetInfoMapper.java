package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.DatasetInfo;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【dataset_info(数据集信息表)】的数据库操作Mapper
* @createDate 2024-01-29 15:58:35
* @Entity com.hdmap.hdmap.task.entity.DatasetInfo
*/
public interface DatasetInfoMapper extends BaseMapper<DatasetInfo> {

    IPage<DatasetInfo> getListByProjectId(IPage<DatasetInfo> page, Serializable id);

    IPage<DatasetInfo> page(IPage<DatasetInfo> page, DatasetInfo info);
}




