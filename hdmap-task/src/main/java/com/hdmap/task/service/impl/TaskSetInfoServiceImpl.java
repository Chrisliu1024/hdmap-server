package com.hdmap.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.geo.model.Grid;
import com.hdmap.geo.utils.GeoHashUtil;
import com.hdmap.geo.utils.GeometryUtil;
import com.hdmap.task.enums.TaskSetStatusEnum;
import com.hdmap.task.enums.TaskSetTypeEnum;
import com.hdmap.task.enums.UserRoleEnum;
import com.hdmap.task.mapper.TaskSetInfoMapper;
import com.hdmap.task.model.dto.*;
import com.hdmap.task.model.entity.ProjectInfo;
import com.hdmap.task.model.entity.ProjectsTaskSetsRel;
import com.hdmap.task.model.entity.TaskSetInfo;
import com.hdmap.task.service.ProjectInfoService;
import com.hdmap.task.service.ProjectsTaskSetsRelService;
import com.hdmap.task.service.ProjectsUsersRelService;
import com.hdmap.task.service.TaskSetInfoService;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author admin
* @description 针对表【task_set_info(任务集信息表)】的数据库操作Service实现
* @createDate 2024-01-29 15:58:35
*/
@Service
public class TaskSetInfoServiceImpl extends ServiceImpl<TaskSetInfoMapper, TaskSetInfo>
    implements TaskSetInfoService {
    @Value("${hdmap.task.geojson.geometry:geometry}")
    private String jsonGeomName;
    @Value("${hdmap.task.geojson.decision:16}")
    private int decision;

    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private ProjectsUsersRelService projectsUsersRelService;
    @Resource
    private ProjectsTaskSetsRelService projectsTaskSetsRelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(TaskSetRequest request) {
        // 保存任务集信息
        TaskSetInfo taskSetInfo = new TaskSetInfo();
        taskSetInfo.setName(request.getName());
        taskSetInfo.setType(request.getType());
        taskSetInfo.setStatus(TaskSetStatusEnum.PROCESSING.getCode());
        taskSetInfo.setCreateBy(request.getCreateBy());
        save(taskSetInfo);
        // 保存项目-任务集关联信息
        saveProjectTaskSetRel(request.getProjectId(), taskSetInfo.getId());
        return true;
    }

    public IPage<TaskSetInfo> selectListByProjectId(IPage<TaskSetInfo> page, Serializable id) {
        return this.baseMapper.selectListByProjectId(page, id);
    }

    private boolean saveProjectTaskSetRel(Long projectId, Long taskSetId) {
        ProjectsTaskSetsRel rel = new ProjectsTaskSetsRel();
        rel.setProjectId(projectId);
        rel.setTaskSetId(taskSetId);
        return projectsTaskSetsRelService.save(rel);
    }

    @Override
    public IPage<TaskSetInfo> getByPage(IPage<TaskSetInfo> page, TaskSetInfo info) {
        return page(page, new QueryWrapper<>(info));
    }

    @Override
    public IPage<TaskSetInfo> getByProjectIdAndCreateBy(IPage<TaskSetInfo> page, Serializable projectId, Serializable createBy) {
        if (projectId != null && createBy != null) {
            return this.baseMapper.selectByProjectIdAndCreateBy(page, projectId, createBy);
        } else if (projectId != null) {
            return getByProjectId(page, projectId);
        } else if (createBy != null) {
            return getByCreateBy(page, createBy);
        } else {
            return page(page);
        }
    }

    @Override
    public IPage<TaskSetInfo> getByProjectId(IPage<TaskSetInfo> page, Serializable id) {
        return this.baseMapper.selectByProjectId(page, id);
    }

    @Override
    public IPage<TaskSetInfo> getByCreateBy(IPage<TaskSetInfo> page, Serializable id) {
        TaskSetInfo taskSetInfo = new TaskSetInfo();
        taskSetInfo.setCreateBy(Long.valueOf(id.toString()));
        return page(page, new QueryWrapper<>(taskSetInfo));
    }

    @Override
    public TaskSetInfo getByTaskId(Serializable id) {
        return this.baseMapper.selectByTaskId(id);
    }

    @Override
    public TaskDivideResult getDivideTasksById(Serializable id, List<String> geohashs) throws IOException, FactoryException {
        // 获取任务集信息
        TaskSetInfo taskSetInfo = getById(id);
        // 获取项目信息
        ProjectInfo projectInfo = projectInfoService.getByTaskSetId(id);
        // 根据任务集类型确定角色
        TaskSetTypeEnum taskSetTypeEnum  = TaskSetTypeEnum.getByCode(taskSetInfo.getType());
        UserRoleEnum role;
        if (TaskSetTypeEnum.CHECK.equals(taskSetTypeEnum)) {
            role = UserRoleEnum.CHECKER;
        } else if (TaskSetTypeEnum.DATA_COLLECTION.equals(taskSetTypeEnum)) {
            role = UserRoleEnum.COLLECTOR;
        } else {
            role = UserRoleEnum.OPERATOR;
        }
        // 获取项目下的用户ID列表
        List<UserShortInfo> userIdList = projectsUsersRelService.getUserInfosByProjectIdAndRole(projectInfo.getId(), role.getDesc());
        // 划分网格
        List<List<Grid>> gridList = gridDivide(geohashs, userIdList.size());
        // 将网格分配给用户
        List<TaskDivide> taskDivideList = new ArrayList<>(gridList.size());
        for (int i = 0; i < gridList.size(); i++) {
            TaskDivide taskDivide = new TaskDivide();
            taskDivide.setRecipientId(userIdList.get(i).getId());
            taskDivide.setGeohashs(gridList.get(i).stream().map(Grid::getIdentifier).collect(Collectors.toList()));
            taskDivide.setGeometry(geohashInfos2Json(gridList.get(i)));
            taskDivideList.add(taskDivide);
        }
        // 封装结果
        TaskDivideResult taskDivideResult = new TaskDivideResult();
        taskDivideResult.setTaskSetId(Long.valueOf(id.toString()));
        taskDivideResult.setTaskSetName(taskSetInfo.getName());
        taskDivideResult.setType(TaskSetTypeEnum.getByCode(taskSetInfo.getType()));
        // 封装任务分配结果
        taskDivideResult.setTasks(taskDivideList);
        return taskDivideResult;
    }

    public List<List<Grid>> gridDivide(List<String> geohashs, int clusterNum) throws IOException {
        List<List<Grid>> result = new ArrayList<>();
        // get geohash bbox geometry of the bounds
        List<Grid> geohashes = GeoHashUtil.getGeoHashByStr(geohashs);
        // if the num of intersectGeoHashInfos less than clusterNum
        if (geohashs.size() <= clusterNum) {
            result.add(geohashes);
            return result;
        }
        // interCollection -> GridCluster
        List<GridCluster> gridClusters = new ArrayList<>();
        for (Grid grid : geohashes) {
            Polygon polygon = (Polygon) grid.getGeometry();
            double[] point = new double[]{polygon.getCentroid().getX(), polygon.getCentroid().getY()};
            gridClusters.add(new GridCluster(point, grid));
        }
        FuzzyKMeansClusterer fuzzyKMeansClusterer = new FuzzyKMeansClusterer(clusterNum, 10);
        List<Cluster<GridCluster>> clusterList = fuzzyKMeansClusterer.cluster(gridClusters);
        //int count = 0;
        for (Cluster<GridCluster> cluster : clusterList) {
            List<GridCluster> gridClusters1 = cluster.getPoints();
            List<Grid> gridList = gridClusters1.stream().map(GridCluster::getGeoHashInfo).collect(Collectors.toList());
            result.add(gridList);
        }
        return result;
    }

    private String geohashInfos2Json(List<Grid> geohashes) throws IOException, FactoryException {
        List<SimpleFeature> features = new ArrayList<>();
        for (Grid info : geohashes) {
            features.add(createFeature(info));
        }
        return features2GeoJson(features);
    }

    private SimpleFeature createFeature(Grid info) throws FactoryException {
        // create feature
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Feature");
        builder.setCRS(CRS.decode("EPSG:" + GeometryUtil.SRID_WGS84));
        builder.add("geohash", Short.class);
        builder.add("geometry", Polygon.class);
        SimpleFeatureType TYPE = builder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        featureBuilder.add(info.getIdentifier());
        featureBuilder.add(info.getGeometry());
        return featureBuilder.buildFeature(null);
    }

    private String features2GeoJson(List<SimpleFeature> list) throws IOException {
        if (list == null || list.isEmpty()) {
            log.warn("features is empty");
            return null;
        }
        SimpleFeatureCollection collection = new ListFeatureCollection(list.get(0).getFeatureType(), list);
        StringWriter writer = new StringWriter();
        GeometryJSON g = new GeometryJSON(decision);
        FeatureJSON featureJson = new FeatureJSON(g);
        featureJson.writeFeatureCollection(collection, writer);
        return writer.toString();
    }

    private void features2GeoJson(SimpleFeatureCollection collection, String path) throws IOException {
        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(decision));// 避免精度丢失
        featureJSON.writeFeatureCollection(collection, path);
    }

}




