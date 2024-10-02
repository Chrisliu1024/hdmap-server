package com.hdmap.pointcloud.service.impl;

import com.hdmap.geo.utils.FeatureCollectionUtil;
import com.hdmap.pointcloud.config.CommonConfig;
import com.hdmap.pointcloud.dto.GridClipResult;
import com.hdmap.geo.model.Grid;
import com.hdmap.minio.service.MinioSysFileService;
import com.hdmap.pointcloud.constant.enums.FileTypeEnum;
import com.hdmap.pointcloud.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.csv.CSVDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/23 13:31
 * @description: gps轨迹处理
 */
@Slf4j
@Service
public class CsvGridClipHandler extends GridClipHandler {
    @Resource
    private CommonConfig commonConfig;
    @Resource
    private MinioSysFileService minioService;

    @Override
    public List<GridClipResult> handleClip(FileTypeEnum fileType, Long identifier, String filePath, int precision) throws Exception {
        if (FileTypeEnum.CSV == fileType) {
            return clip(identifier, filePath, precision);
        } else if (nextHandler != null) {
            nextHandler.handleClip(fileType, identifier, filePath, precision);
        }
        return null;
    }

    @Override
    public String handleMerge(FileTypeEnum fileType, String identifier, String originCloudPath, String inputPath) throws Exception {
        if (FileTypeEnum.CSV == fileType) {
            return merge(identifier, originCloudPath, inputPath);
        } else if (nextHandler != null) {
            nextHandler.handleMerge(fileType, identifier, originCloudPath, inputPath);
        }
        return null;
    }

    public ReferencedEnvelope getBounds(String filePath) throws IOException {
        SimpleFeatureCollection collection = getCSVFeatureCollection(filePath);
        if (collection.size() == 0) {
            return null;
        }
        // get bbox
        return collection.getBounds();
    }

    public GridClipResult clipByPolygon(Long identifier, Grid grid, String filePath) throws Exception {
        SimpleFeatureCollection collection = getCSVFeatureCollection(filePath);
        SimpleFeatureCollection clipCollection = FeatureCollectionUtil.getIntersectCollection(collection, grid.getGeometry());
        if (clipCollection.size() == 0) {
            return null;
        }
        log.debug("clip success, grid: {}, size: {}", grid.getIdentifier(), clipCollection.size());
        // save to csv file
        String csvPath = ResourceUtil.getUniqueLocalPathAndPrefixName(grid.getIdentifier()) + FileTypeEnum.CSV.getSuffix();
        com.hdmap.pointcloud.utils.FeatureCollectionUtil.features2Csv(clipCollection, csvPath);
        return new GridClipResult(identifier, grid.getIdentifier(), csvPath);
    }

    @Override
    public String merge(String identifier, String originCloudPath, String inputPath) throws Exception {
        // get whole name of cloud file
        String originFileName = ResourceUtil.getWholeFileNameByPath(originCloudPath);
        // download to local and unzip
        String downloadPath = commonConfig.getZipUploadFile() ? minioService.downloadAndUnzipFile(ResourceUtil.getJarRootPath(), originFileName)
                : minioService.downloadFile(ResourceUtil.getJarRootPath(), originFileName);
        // merge
        FeatureSource<SimpleFeatureType, SimpleFeature> cloudSource = getCSVSource(downloadPath);
        FeatureSource<SimpleFeatureType, SimpleFeature> localSource = getCSVSource(inputPath);
        FeatureCollection<SimpleFeatureType, SimpleFeature> cloudCollection = cloudSource.getFeatures();
        FeatureCollection<SimpleFeatureType, SimpleFeature> localCollection = localSource.getFeatures();
        SimpleFeature[] cloudFeatures = cloudCollection.toArray(new SimpleFeature[0]);
        SimpleFeature[] localFeatures = localCollection.toArray(new SimpleFeature[0]);
        List<SimpleFeature> features = new ArrayList<>();
        Collections.addAll(features, cloudFeatures);
        Collections.addAll(features, localFeatures);
        // save to csv file
        String mergedPath = ResourceUtil.getUniqueLocalPathAndPrefixName(identifier) + FileTypeEnum.CSV.getSuffix();
        com.hdmap.pointcloud.utils.FeatureCollectionUtil.features2Csv(features, mergedPath);
        // upload
        String mergeCloudPath = commonConfig.getZipUploadFile() ? minioService.uploadAndZipFile(mergedPath)
                : minioService.uploadFile(mergedPath);
        // delete local file
        ResourceUtil.deleteFile(downloadPath);
        ResourceUtil.deleteFile(mergedPath);
        // delete origin cloud file
        minioService.deleteFile(originFileName);
        return mergeCloudPath;
    }

    private SimpleFeatureCollection getCSVFeatureCollection(String localPath) throws IOException {
        SimpleFeatureSource source = getCSVSource(localPath);
        Filter filter = Filter.INCLUDE;
        return source.getFeatures(filter);
    }

    private SimpleFeatureSource getCSVSource(String localPath) throws IOException {
        // 解析gps轨迹文件
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", localPath);
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params.put(CSVDataStoreFactory.LATFIELDP.key, "latitude");
        params.put(CSVDataStoreFactory.LnGFIELDP.key, "longitude");
        DataStore store = DataStoreFinder.getDataStore(params);

        String typeName = store.getTypeNames()[0];

       return store.getFeatureSource(typeName);
    }

}
