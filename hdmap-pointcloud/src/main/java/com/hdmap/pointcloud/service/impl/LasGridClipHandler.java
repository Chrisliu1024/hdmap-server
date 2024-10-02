package com.hdmap.pointcloud.service.impl;

import com.hdmap.pointcloud.config.CommonConfig;
import com.hdmap.pointcloud.dto.GridClipResult;
import com.hdmap.geo.model.Grid;
import com.hdmap.minio.service.MinioSysFileService;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import com.hdmap.pointcloud.utils.FeatureCollectionUtil;
import com.hdmap.pointcloud.utils.PipelineUtil;
import com.hdmap.pointcloud.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/15 15:56
 * @description: 点云裁剪
 */
@Slf4j
@Service
public class LasGridClipHandler extends GridClipHandler {

    @Value("${pointcloud.filter.field:classification}")
    private String filterFieldName;

    @Resource
    private CommonConfig commonConfig;
    @Resource
    private MinioSysFileService minioService;

    @Override
    public List<GridClipResult> handleClip(FileTypeEnum fileType, Long identifier, String filePath, int precision) throws Exception {
        if (FileTypeEnum.LAS == fileType) {
            return clip(identifier, filePath, precision);
        } else if (nextHandler != null) {
            nextHandler.handleClip(fileType, identifier, filePath, precision);
        }
        return null;
    }

    @Override
    public String handleMerge(FileTypeEnum fileType, String identifier, String originCloudPath, String inputPath) throws Exception {
        if (FileTypeEnum.LAS == fileType) {
            return merge(identifier, originCloudPath, inputPath);
        } else if (nextHandler != null) {
            nextHandler.handleMerge(fileType, identifier, originCloudPath, inputPath);
        }
        return null;
    }

    @Override
    public ReferencedEnvelope getBounds(String filePath) throws FactoryException {
        return PipelineUtil.getBBoxByPath(filePath);
    }

    @Override
    public GridClipResult clipByPolygon(Long identifier, Grid geohash, String filePath) throws Exception {
        // save polygon to geojson
        SimpleFeature feature = geohash.toFeature();
        // 存储在本地jar同级目录下
        String jsonPath = ResourceUtil.getUniqueLocalPathAndPrefixName(geohash.getIdentifier()) + FileTypeEnum.GEOJSON.getSuffix();
        String outputPath = ResourceUtil.getUniqueLocalPathAndPrefixName(geohash.getIdentifier()) + FileTypeEnum.LAS.getSuffix();
        FeatureCollectionUtil.features2GeoJsonFile(feature, jsonPath);
        // clip point cloud
        Boolean lasExist = PipelineUtil.overlayAndRange(filePath, outputPath, jsonPath, filterFieldName, geohash.getClassification());
        // delete unused local file
        ResourceUtil.deleteFile(jsonPath);
        if (!lasExist) {
            return null;
        }
        // print log
        log.debug("clip success, identifier:{}, grid: {}, intputPath: {}, outputPath: {}, jsonPath: {}",
                identifier, geohash.getIdentifier(), filePath, outputPath, jsonPath);
        return new GridClipResult(identifier, geohash.getIdentifier(), outputPath);
    }

    @Override
    public String merge(String originCloudPath, String inputPath, String identifier) throws Exception {
        // get whole name of cloud file
        String originFileName = ResourceUtil.getWholeFileNameByPath(originCloudPath);
        // download to local and unzip
        String downloadPath = commonConfig.getZipUploadFile() ? minioService.downloadAndUnzipFile(ResourceUtil.getJarRootPath(), originFileName)
                : minioService.downloadFile(ResourceUtil.getJarRootPath(), originFileName);
        // merge
        List<String> inputPaths = new ArrayList<>();
        inputPaths.add(downloadPath);
        inputPaths.add(inputPath);
        String mergedPath = PipelineUtil.merge(inputPaths, ResourceUtil.getUniqueLocalPathAndPrefixName(identifier) + FileTypeEnum.LAS.getSuffix());
        // upload and zip
        String mergeCloudPath =  commonConfig.getZipUploadFile() ? minioService.uploadAndZipFile(mergedPath)
                : minioService.uploadFile(mergedPath);
        // delete unused local file
        ResourceUtil.deleteFile(downloadPath);
        ResourceUtil.deleteFile(mergedPath);
        // delete origin cloud file
        minioService.deleteFile(originFileName);
        return mergeCloudPath;
    }

}
