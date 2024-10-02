package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.FieldRelLocation;
import com.hdmap.data.etl.model.dto.PipelineDetailInfo;

import java.util.List;

public interface CreateRelService {

    PipelineDetailInfo createRel(PipelineDetailInfo info, List<FieldRelLocation> fieldRelLocations);
}
