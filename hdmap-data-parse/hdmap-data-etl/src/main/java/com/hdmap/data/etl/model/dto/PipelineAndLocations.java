package com.hdmap.data.etl.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/10 13:50
 * @description: pipeline和locations
 */
@Data
public class PipelineAndLocations {
    private PipelineDetailInfo info;
    private List<FieldRelLocation> fieldRelLocations;
}
