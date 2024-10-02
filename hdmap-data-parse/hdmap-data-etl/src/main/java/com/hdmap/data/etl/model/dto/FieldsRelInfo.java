package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.FieldInfo;
import com.hdmap.data.etl.model.entity.TableInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 17:51
 * @description: 字段关联信息
 */
@Data
public class FieldsRelInfo implements Serializable {
    /**
     * 源数据存储（左）
     */
    private DatastoreInfo leftDatastoreInfo;
    /**
     * 目标数据存储（右）
     */
    private DatastoreInfo rightDatastoreInfo;
    /**
     * 源表（左）
     */
    private TableInfo leftTableInfo;
    /**
     * 目标表（右）
     */
    private TableInfo rightTableInfo;
    /**
     * 源字段（左）
     */
    private FieldInfo leftFieldInfo;
    /**
     * 目标字段（右）
     */
    private FieldInfo rightFieldInfo;
    /**
     * 转换器实例ID
     */
    private Long transformerInstanceId;
    /**
     * 多对一映射时的序列号
     */
    private Short sequence;

}

