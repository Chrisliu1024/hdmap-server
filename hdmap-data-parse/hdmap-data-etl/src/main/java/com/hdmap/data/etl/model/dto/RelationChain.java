package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.FieldInfo;
import com.hdmap.data.etl.model.entity.Pipeline;
import com.hdmap.data.etl.model.entity.TableInfo;
import lombok.Data;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 10:59
 * @description: 关联链（pipeline-datastore-table-field）
 */
@Data
public class RelationChain {
    private Pipeline pipeline;
    private DatastoreInfo datastore;
    private TableInfo table;
    private FieldInfo field;
}
