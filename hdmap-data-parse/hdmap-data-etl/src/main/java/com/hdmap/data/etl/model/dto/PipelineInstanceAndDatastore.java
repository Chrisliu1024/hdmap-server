package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.PipelineInstance;
import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 15:38
 * @description: 数据存储信息
 */
@Data
public class PipelineInstanceAndDatastore extends PipelineInstance {

    /**
     * 数据存储信息
     */
    List<DatastoreAndLeftRel> datastoreAndLeftRelList;

}
