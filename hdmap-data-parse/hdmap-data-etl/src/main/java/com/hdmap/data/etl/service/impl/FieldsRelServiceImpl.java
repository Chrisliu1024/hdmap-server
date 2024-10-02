package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.dto.FieldsRelInfo;
import com.hdmap.data.etl.model.dto.FieldsRelResult;
import com.hdmap.data.etl.model.entity.*;
import com.hdmap.data.etl.service.*;
import com.hdmap.data.etl.mapper.FieldsRelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【fields_rel(字段关联)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class FieldsRelServiceImpl extends ServiceImpl<FieldsRelMapper, FieldsRel>
    implements FieldsRelService{

    @Resource
    private DatastoreInfoService datastoreInfoService;
    @Resource
    private TableInfoService tableInfoService;
    @Resource
    private FieldInfoService fieldInfoService;
    @Resource
    private DatastoreTableRelService datastoreTableRelService;
    @Resource
    private TableFieldRelService tableFieldRelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FieldsRelResult save(FieldsRelInfo info) {
        DatastoreInfo leftDatastoreInfo = info.getLeftDatastoreInfo();
        TableInfo leftTableInfo = info.getLeftTableInfo();
        FieldInfo leftFieldInfo = info.getLeftFieldInfo();
        DatastoreInfo rightDatastoreInfo = info.getRightDatastoreInfo();
        TableInfo rightTableInfo = info.getRightTableInfo();
        FieldInfo rightFieldInfo = info.getRightFieldInfo();
        saveInfoAndRel(leftDatastoreInfo, leftTableInfo, leftFieldInfo);
        saveInfoAndRel(rightDatastoreInfo, rightTableInfo, rightFieldInfo);
        Long fieldRelId = saveFieldRel(leftFieldInfo.getId(), rightFieldInfo.getId(), info.getTransformerInstanceId(), info.getSequence());
        // result
        FieldsRelResult result = new FieldsRelResult();
        result.setLeftDataStoreId(leftDatastoreInfo.getId());
        result.setRightDataStoreId(rightDatastoreInfo.getId());
        result.setLeftTableId(leftTableInfo.getId());
        result.setRightTableId(rightTableInfo.getId());
        result.setLeftFieldId(leftFieldInfo.getId());
        result.setRightFieldId(rightFieldInfo.getId());
        result.setFieldsRelId(fieldRelId);
        return result;
    }

    private void saveInfoAndRel(DatastoreInfo datastoreInfo, TableInfo tableInfo, FieldInfo fieldInfo) {
        boolean datastoreTableRelExist = true;
        boolean tableFieldRelExist = true;
        if (datastoreInfo != null && datastoreInfo.getId() == null) {
            datastoreTableRelExist = false;
            datastoreInfoService.save(datastoreInfo);
        }
        if (tableInfo != null && tableInfo.getId() == null) {
            datastoreTableRelExist = false;
            tableFieldRelExist = false;
            tableInfoService.save(tableInfo);
        }
        if (fieldInfo != null && fieldInfo.getId() == null) {
            tableFieldRelExist = false;
            fieldInfoService.save(fieldInfo);
        }
        if (!datastoreTableRelExist) {
            DatastoreTableRel rel = new DatastoreTableRel();
            rel.setDatastoreId(datastoreInfo.getId());
            rel.setTableId(tableInfo.getId());
            datastoreTableRelService.save(rel);
        }
        if (!tableFieldRelExist) {
            TableFieldRel rel = new TableFieldRel();
            rel.setTableId(tableInfo.getId());
            rel.setFieldId(fieldInfo.getId());
            tableFieldRelService.save(rel);
        }

    }

    private Long saveFieldRel(Long leftFieldId, Long rightFieldId, Long transformerInstanceId, Short sequence) {
        FieldsRel fieldsRel = new FieldsRel();
        fieldsRel.setLeftFieldId(leftFieldId);
        fieldsRel.setRightFieldId(rightFieldId);
        fieldsRel.setTransformerInstanceId(transformerInstanceId);
        fieldsRel.setSequence(sequence);
        save(fieldsRel);
        return fieldsRel.getId();
    }

}




