package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.DatastoreInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 11:36
 * @description: 数据库详细信息（PipelineDetailInfo - DatastoreDetailInfo -> TableDetailInfo -> FieldInfo构成树状结构）
 */
@Data
public class DatastoreDetailInfo extends DatastoreInfo implements Serializable {
    /**
     * 表详细信息列表
     */
    private List<TableDetailInfo> tableDetailInfoList;
}
