package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.mapper.TablesRelMapper;
import com.hdmap.data.etl.model.dto.TablesRelInfo;
import com.hdmap.data.etl.model.dto.TablesRelResult;
import com.hdmap.data.etl.model.entity.*;
import com.hdmap.data.etl.service.FieldInfoService;
import com.hdmap.data.etl.service.TableFieldRelService;
import com.hdmap.data.etl.service.TableInfoService;
import com.hdmap.data.etl.service.TablesRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【tables_rel(表关联)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class TablesRelServiceImpl extends ServiceImpl<TablesRelMapper, TablesRel>
    implements TablesRelService{

    @Resource
    private TableInfoService tableInfoService;
    @Resource
    private FieldInfoService fieldInfoService;
    @Resource
    private TableFieldRelService tableFieldRelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TablesRelResult save(TablesRelInfo info) {
        TableInfo leftTableInfo = info.getLeftTableInfo();
        TableInfo rightTableInfo = info.getRightTableInfo();
        FieldInfo leftFieldInfo = info.getLeftFieldInfo();
        FieldInfo rightFieldInfo = info.getRightFieldInfo();
        saveInfoAndRel(leftTableInfo, leftFieldInfo);
        saveInfoAndRel(rightTableInfo, rightFieldInfo);
        saveTableRel(leftTableInfo.getId(), rightTableInfo.getId(), leftFieldInfo.getId(), rightFieldInfo.getId());
        // result
        TablesRelResult result = new TablesRelResult();
        result.setLeftTableId(leftTableInfo.getId());
        result.setRightTableId(rightTableInfo.getId());
        result.setLeftFieldId(leftFieldInfo.getId());
        result.setRightTableId(rightFieldInfo.getId());
        return result;
    }

    private void saveInfoAndRel(TableInfo tableInfo, FieldInfo fieldInfo) {
        boolean tableFieldRelExist = true;
        if (tableInfo != null && tableInfo.getId() == null) {
            tableFieldRelExist = false;
            tableInfoService.save(tableInfo);
        }
        if (fieldInfo != null && fieldInfo.getId() == null) {
            tableFieldRelExist = false;
            fieldInfoService.save(fieldInfo);
        }
        if (!tableFieldRelExist) {
            TableFieldRel rel = new TableFieldRel();
            rel.setTableId(tableInfo.getId());
            rel.setFieldId(fieldInfo.getId());
            tableFieldRelService.save(rel);
        }

    }

    private void saveTableRel(Long leftTableId, Long rightTableId, Long leftFieldId, Long rightFieldId) {
        TablesRel rel = new TablesRel();
        rel.setLeftTableId(leftTableId);
        rel.setRightTableId(rightTableId);
        rel.setLeftFieldId(leftFieldId);
        rel.setRightFieldId(rightFieldId);
        save(rel);
    }

}




