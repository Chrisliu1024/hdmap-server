package com.hdmap.task.service;

import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【projects_datasets_rel(项目-数据集关联表)】的数据库操作Service
* @createDate 2024-05-15 16:46:32
*/
public interface ProjectsDatasetsRelService extends IService<ProjectsDatasetsRel> {

    boolean removeBatchByProjectIdAndDatasetIds(Serializable projectId, List<Serializable> datasetIds, boolean isDeleteRel);

    boolean saveBatchAndInsertTaskPool(List<ProjectsDatasetsRel> rels);

}
