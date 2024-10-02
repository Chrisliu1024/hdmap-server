package com.hdmap.data.etl.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 10:06
 * @description: 字段关联逻辑（多（左）对一（右））
 */
@Data
public class FieldRelLocation implements Serializable {
    /**
     * 左侧字段位置 pipeline(root) -> left-datastore -> left-table -> left-field
     * example: ["0-0-0-0", "0-0-0-1"]
     */
    private List<String> leftFieldLocations;
    /**
     * 右侧字段位置 pipeline(root) -> right-datastore -> right-table -> right-field
     * example: "0-0-0-0"
     */
    private String rightFieldLocation;
    /**
     * 转换器实例ID
     */
    private Long transformerInstanceId;

}
