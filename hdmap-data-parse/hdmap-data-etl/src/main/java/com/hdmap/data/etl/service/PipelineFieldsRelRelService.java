package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelInfo;
import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelResult;
import com.hdmap.data.etl.model.entity.PipelineFieldsRelRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author admin
* @description 针对表【pipeline_fields_rel_rel(pipeline关联field的映射的关联)】的数据库操作Service
* @createDate 2024-07-05 16:38:03
*/
public interface PipelineFieldsRelRelService extends IService<PipelineFieldsRelRel> {

    PipelineFieldsRelRelResult save(PipelineFieldsRelRelInfo info);

}
