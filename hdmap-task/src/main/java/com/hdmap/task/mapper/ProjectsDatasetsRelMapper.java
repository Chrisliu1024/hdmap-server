package com.hdmap.task.mapper;

import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_datasets_rel(项目-数据集关联表)】的数据库操作Mapper
* @createDate 2024-05-15 16:42:12
* @Entity generator.entity.ProjectsDatasetsRel
*/
public interface ProjectsDatasetsRelMapper extends BaseMapper<ProjectsDatasetsRel> {

    boolean deleteBatchByProjectIdAndDatasetIds(Serializable projectId, List<Serializable> datasetIds);
}




