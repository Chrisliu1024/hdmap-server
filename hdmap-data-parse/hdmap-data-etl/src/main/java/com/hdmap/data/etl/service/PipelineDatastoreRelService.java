package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.PipelineDatastoreRelInfo;
import com.hdmap.data.etl.model.dto.PipelineDatastoreRelResult;
import com.hdmap.data.etl.model.entity.PipelineDatastoreRel;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【pipeline_datastore_rel(处理pipeline和数据存储的关联)】的数据库操作Service
* @createDate 2024-07-05 16:38:03
*/
public interface PipelineDatastoreRelService extends IService<PipelineDatastoreRel> {

    PipelineDatastoreRelResult save(PipelineDatastoreRelInfo info);

}
