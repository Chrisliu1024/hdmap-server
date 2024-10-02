package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.mapper.PipelineFieldsRelRelMapper;
import com.hdmap.data.etl.model.dto.FieldsRelInfo;
import com.hdmap.data.etl.model.dto.FieldsRelResult;
import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelInfo;
import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelResult;
import com.hdmap.data.etl.model.entity.*;
import com.hdmap.data.etl.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【pipeline_fields_rel_rel(pipeline关联field的映射的关联)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class PipelineFieldsRelRelServiceImpl extends ServiceImpl<PipelineFieldsRelRelMapper, PipelineFieldsRelRel>
    implements PipelineFieldsRelRelService{

    @Resource
    private PipelineService pipelineService;
    @Resource
    private DatastoreInfoService datastoreInfoService;
    @Resource
    private PipelineDatastoreRelService pipelineDatastoreRelService;
    @Resource
    private FieldsRelService fieldsRelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PipelineFieldsRelRelResult save(PipelineFieldsRelRelInfo info) {
        Pipeline pipeline = info.getPipeline();
        FieldsRelInfo fieldsRelInfo = info.getFieldsRelInfo();
        DatastoreInfo leftDatastoreInfo = fieldsRelInfo.getLeftDatastoreInfo();
        DatastoreInfo rightDatastoreInfo = fieldsRelInfo.getRightDatastoreInfo();
        saveInfoAndRel(pipeline, leftDatastoreInfo, true);
        saveInfoAndRel(pipeline, rightDatastoreInfo, false);
        FieldsRelResult fieldsRelResult = fieldsRelService.save(fieldsRelInfo);
        savePipelineFieldsRelRel(pipeline.getId(), fieldsRelResult.getFieldsRelId());
        // result
        PipelineFieldsRelRelResult result = new PipelineFieldsRelRelResult();
        result.setPipelineId(pipeline.getId());
        result.setFieldsRelResult(fieldsRelResult);
        return result;
    }

    private void saveInfoAndRel(Pipeline pipeline, DatastoreInfo datastoreInfo, Boolean left) {
        boolean pipelineDatastoreRelExist = true;
        if (pipeline != null && pipeline.getId() == null) {
            pipelineDatastoreRelExist = false;
            pipelineService.save(pipeline);
        }
        if (datastoreInfo != null && datastoreInfo.getId() == null) {
            pipelineDatastoreRelExist = false;
            datastoreInfoService.save(datastoreInfo);
        }
        if (!pipelineDatastoreRelExist) {
            PipelineDatastoreRel rel = new PipelineDatastoreRel();
            rel.setPipelineId(pipeline.getId());
            rel.setDatastoreId(datastoreInfo.getId());
            rel.setLeft(left);
            pipelineDatastoreRelService.save(rel);
        }

    }

    private void savePipelineFieldsRelRel(Long pipelineId, Long fieldsRelId) {
        PipelineFieldsRelRel rel = new PipelineFieldsRelRel();
        rel.setPipelineId(pipelineId);
        rel.setFieldsRelId(fieldsRelId);
        save(rel);
    }
}




