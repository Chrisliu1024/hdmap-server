package com.hdmap.data.etl.model.dto;

import com.hdmap.data.etl.model.entity.DatastoreInfo;
import lombok.Data;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 15:38
 * @description: 数据存储信息
 */
@Data
public class DatastoreAndLeftRel extends DatastoreAndLeft {

    /**
     * 关联的数据存储ID
     */
    private Long datastoreId;

}
