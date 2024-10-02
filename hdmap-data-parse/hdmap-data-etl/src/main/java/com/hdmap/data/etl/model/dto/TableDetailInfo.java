package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.FieldInfo;
import com.hdmap.data.etl.model.entity.TableInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/5 13:48
 * @description: 表结构详细信息（PipelineDetailInfo - DatastoreDetailInfo -> TableDetailInfo -> FieldInfo构成树状结构）
 */
@Data
public class TableDetailInfo extends TableInfo implements Serializable {
    /**
     * 属性列表
     */
    private List<FieldInfo> fields;
}
