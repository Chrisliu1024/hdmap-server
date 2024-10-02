package com.hdmap.data.etl.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.mybatis.config.JsonTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.BooleanTypeHandler;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 15:38
 * @description: 数据存储信息
 */
@Data
public class DatastoreAndLeft extends DatastoreInfo {

    /**
     * 是否源数据存储（左）
     */
    @TableField(value = "\"left\"", typeHandler = BooleanTypeHandler.class)
    private Boolean left;

}
