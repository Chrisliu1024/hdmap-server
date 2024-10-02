package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.PipelineInstanceAndDatastore;
import com.hdmap.data.etl.model.entity.PipelineInstanceDatastoreInstanceRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author admin
* @description 针对表【pipeline_instance_datastore_instance_rel(处理pipeline instance和数据存储的关联)】的数据库操作Service
* @createDate 2024-07-10 10:45:44
*/
public interface PipelineInstanceDatastoreInstanceRelService extends IService<PipelineInstanceDatastoreInstanceRel> {

    PipelineInstanceAndDatastore save(PipelineInstanceAndDatastore info);
}
