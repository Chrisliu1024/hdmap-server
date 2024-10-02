package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.FieldsRelInfo;
import com.hdmap.data.etl.model.dto.FieldsRelResult;
import com.hdmap.data.etl.model.entity.FieldsRel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【fields_rel(字段关联)】的数据库操作Service
* @createDate 2024-07-05 16:38:03
*/
public interface FieldsRelService extends IService<FieldsRel> {

    FieldsRelResult save(FieldsRelInfo info);
}
