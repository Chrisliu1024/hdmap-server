package com.hdmap.data.etl.mapper;

import com.hdmap.data.etl.model.dto.DatastoreAndLeft;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【datastore_info(数据存储信息)】的数据库操作Mapper
* @createDate 2024-07-05 16:38:03
* @Entity com.hdmap.data.etl.entity.DatastoreInfo
*/
public interface DatastoreInfoMapper extends BaseMapper<DatastoreInfo> {

    List<DatastoreAndLeft> getByPipelineId(Serializable id);
}




