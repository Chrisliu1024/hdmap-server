package com.hdmap.data.etl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hdmap.data.etl.model.dto.*;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.FieldInfo;
import com.hdmap.data.etl.model.entity.Pipeline;
import com.hdmap.data.etl.model.entity.TableInfo;
import com.hdmap.data.etl.service.CreateRelService;
import com.hdmap.data.etl.service.PipelineFieldsRelRelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 09:56
 * @description: 创建pipeline-datastore-table-field关系服务实现类
 */

@Service
@RefreshScope
public class CreateRelServiceImpl implements CreateRelService {

    @Value("${field.rel.split:-}")
    private String fieldRelSplit;

    @Resource
    private PipelineFieldsRelRelService pipelineFieldsRelRelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PipelineDetailInfo createRel(PipelineDetailInfo info, List<FieldRelLocation> fieldRelLocations) {
        Pipeline pipeline = BeanUtil.copyProperties(info, Pipeline.class);
        for (FieldRelLocation fieldRelLocation : fieldRelLocations) {
            String rightFieldLocation = fieldRelLocation.getRightFieldLocation();
            List<String> leftFieldLocations = fieldRelLocation.getLeftFieldLocations();
            Long transformerInstanceId = fieldRelLocation.getTransformerInstanceId();
            short sequence = 0;
            for (String leftFieldLocation : leftFieldLocations) {
                RelationChain leftRelationChain = createRelationChain(info, leftFieldLocation, true);
                RelationChain rightRelationChain = createRelationChain(info, rightFieldLocation, false);
                FieldsRelInfo fieldsRelInfo = getFieldsRelInfo(leftRelationChain, rightRelationChain, transformerInstanceId, sequence++);
                PipelineFieldsRelRelInfo pipelineFieldsRelRelInfo = new PipelineFieldsRelRelInfo();
                pipelineFieldsRelRelInfo.setPipeline(pipeline);
                pipelineFieldsRelRelInfo.setFieldsRelInfo(fieldsRelInfo);
                PipelineFieldsRelRelResult idResult = pipelineFieldsRelRelService.save(pipelineFieldsRelRelInfo);
                // Complete the id
                completeId(info, idResult, leftFieldLocation, true);
                completeId(info, idResult, rightFieldLocation, false);
            }
        }
        return info;
    }

    private FieldsRelInfo getFieldsRelInfo(RelationChain leftRelationChain, RelationChain rightRelationChain, Long transformerInstanceId, Short sequence) {
        FieldsRelInfo fieldsRelInfo = new FieldsRelInfo();
        fieldsRelInfo.setLeftDatastoreInfo(leftRelationChain.getDatastore());
        fieldsRelInfo.setLeftTableInfo(leftRelationChain.getTable());
        fieldsRelInfo.setLeftFieldInfo(leftRelationChain.getField());
        fieldsRelInfo.setRightDatastoreInfo(rightRelationChain.getDatastore());
        fieldsRelInfo.setRightTableInfo(rightRelationChain.getTable());
        fieldsRelInfo.setRightFieldInfo(rightRelationChain.getField());
        fieldsRelInfo.setTransformerInstanceId(transformerInstanceId);
        fieldsRelInfo.setSequence(sequence);
        return fieldsRelInfo;
    }

    private RelationChain createRelationChain(PipelineDetailInfo info, String fieldRelLocation, Boolean left) {
        RelationDetailChain relationDetailChain = getRelationDetailChain(info, fieldRelLocation, left);
        RelationChain relationChain = new RelationChain();
        relationChain.setPipeline(relationDetailChain.getPipeline());
        DatastoreInfo datastoreInfo = BeanUtil.copyProperties(relationDetailChain.getDatastoreDetail(), DatastoreInfo.class);
        relationChain.setDatastore(datastoreInfo);
        TableInfo tableInfo = BeanUtil.copyProperties(relationDetailChain.getTableDetail(), TableInfo.class);
        relationChain.setTable(tableInfo);
        relationChain.setField(relationDetailChain.getField());
        return relationChain;
    }

    private void completeId(PipelineDetailInfo info, PipelineFieldsRelRelResult idResult, String fieldRelLocation, Boolean left) {
        // Get the relation detail chain
        RelationDetailChain relationDetailChain = getRelationDetailChain(info, fieldRelLocation, left);
        // Set the id
        info.setId(idResult.getPipelineId());
        FieldsRelResult fieldsRelResult = idResult.getFieldsRelResult();
        relationDetailChain.getDatastoreDetail().setId(left? fieldsRelResult.getLeftDataStoreId(): fieldsRelResult.getRightDataStoreId());
        relationDetailChain.getTableDetail().setId(left? fieldsRelResult.getLeftTableId(): fieldsRelResult.getRightTableId());
        relationDetailChain.getField().setId(left? fieldsRelResult.getLeftFieldId() : fieldsRelResult.getRightFieldId());
    }


    private RelationDetailChain getRelationDetailChain(PipelineDetailInfo info, String fieldRelLocation, Boolean left) {
        int[] fieldRelLocations = getFieldRelLocations(fieldRelLocation);
        RelationDetailChain relationDetailChain = new RelationDetailChain();
        // 默认根节点为管道，起始为0
        Pipeline pipeline = BeanUtil.copyProperties(info, Pipeline.class);
        if (pipeline == null) {
            throw new IllegalArgumentException("Pipeline cannot be null");
        }
        relationDetailChain.setPipeline(pipeline);
        List<DatastoreDetailInfo> datastoreDetailInfoList = left ? info.getLeftDatastoreDetailInfoList() : info.getRightDatastoreDetailInfoList();
        DatastoreDetailInfo datastoreDetailInfo = datastoreDetailInfoList.get(fieldRelLocations[1]);
        if (datastoreDetailInfo == null) {
            throw new IllegalArgumentException("DatastoreInfo cannot be null");
        }
        relationDetailChain.setDatastoreDetail(datastoreDetailInfo);
        TableDetailInfo tableDetailInfo = datastoreDetailInfo.getTableDetailInfoList().get(fieldRelLocations[2]);
        if (tableDetailInfo == null) {
            throw new IllegalArgumentException("TableInfo cannot be null");
        }
        relationDetailChain.setTableDetail(tableDetailInfo);
        FieldInfo fieldInfo = tableDetailInfo.getFields().get(fieldRelLocations[3]);
        if (fieldInfo == null) {
            throw new IllegalArgumentException("FieldInfo cannot be null");
        }
        relationDetailChain.setField(fieldInfo);
        return relationDetailChain;
    }

    private int[] getFieldRelLocations(String fieldRelLocation) {
        int[] fieldRelLocations = Arrays.stream(fieldRelLocation.split(fieldRelSplit)).map(String::trim).mapToInt(Integer::parseInt).toArray();
        if (fieldRelLocations.length != 4) {
            throw new IllegalArgumentException("FieldRelLocation must be 3");
        }
        return fieldRelLocations;
    }
}
