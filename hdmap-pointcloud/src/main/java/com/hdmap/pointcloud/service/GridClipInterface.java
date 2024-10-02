package com.hdmap.pointcloud.service;

import com.hdmap.pointcloud.dto.GridClipResult;

import java.util.List;

public interface GridClipInterface {
    List<GridClipResult> clip(Long identifier, String inputPath, int precision) throws Exception;

    String merge(String originCloudPath, String inputPath, String identifier) throws Exception;

}
