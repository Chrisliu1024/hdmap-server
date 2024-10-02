package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.DatastoreAndLeft;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【datastore_info(数据存储信息)】的数据库操作Service
* @createDate 2024-07-05 16:38:03
*/
public interface DatastoreInfoService extends IService<DatastoreInfo> {

    List<DatastoreAndLeft> getByPipelineId(Serializable id);

}
