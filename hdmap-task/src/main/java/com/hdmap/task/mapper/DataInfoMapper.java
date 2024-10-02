package com.hdmap.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.DataInfo;

import java.io.Serializable;

/**
* @author admin
* @description 针对表【data_info(数据信息表)】的数据库操作Mapper
* @createDate 2024-01-29 15:58:35
* @Entity com.hdmap.hdmap.task.entity.DataInfo
*/
public interface DataInfoMapper extends BaseMapper<DataInfo> {

    IPage<DataInfo> selectByDataSetId(IPage<DataInfo> page, Serializable id);

    IPage<DataInfo> selectByProjectId(IPage<DataInfo> page, Serializable id);

    DataInfo selectByDataSetIdAndGeohashAndType(Serializable id, String geohash, Integer type);

    IPage<DataInfo> selectResultDataInfoByTaskId(IPage<DataInfo> page, Serializable id);
}




