package com.hdmap.pointcloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/30 13:52
 * @description: 网格划分结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GridClipResult {
    Long identifier;
    String gridIdentifier;
    String filePath;
}
