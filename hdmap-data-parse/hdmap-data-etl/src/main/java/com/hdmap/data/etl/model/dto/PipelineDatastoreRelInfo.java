package com.hdmap.data.etl.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.Pipeline;
import lombok.Data;
import org.apache.ibatis.type.BooleanTypeHandler;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/9 15:01
 * @description: TODO
 */

@Data
public class PipelineDatastoreRelInfo implements Serializable {
    /**
     * 处理管线pipeline
     */
    private Pipeline pipeline;
    /**
     * 源数据存储列表
     */
    private List<DatastoreInfo> datastoreList;
    /**
     * 是否为源数据存储（左）
     */
    @TableField(value = "\"left\"", typeHandler = BooleanTypeHandler.class)
    private Boolean left;
}
