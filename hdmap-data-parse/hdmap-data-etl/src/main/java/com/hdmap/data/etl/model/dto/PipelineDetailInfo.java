package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.Pipeline;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/9 18:07
 * @description: 管道详情信息（PipelineDetailInfo - DatastoreDetailInfo -> TableDetailInfo -> FieldInfo构成树状结构）
 */

@Data
public class PipelineDetailInfo extends Pipeline implements Serializable {

    /**
     * 数据存储详细信息列表（左）
     */
    private List<DatastoreDetailInfo> leftDatastoreDetailInfoList;
    /**
     * 数据存储详细信息列表（右）
     */
    private List<DatastoreDetailInfo> rightDatastoreDetailInfoList;
}
