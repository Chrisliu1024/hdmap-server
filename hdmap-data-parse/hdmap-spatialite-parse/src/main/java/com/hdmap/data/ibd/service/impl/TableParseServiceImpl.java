package com.hdmap.data.ibd.service.impl;

import com.google.common.collect.Lists;
import com.hdmap.data.ibd.dto.TableMapping;
import com.hdmap.data.ibd.manager.GeometryManager;
import com.hdmap.data.ibd.manager.PostgisManager;
import com.hdmap.data.ibd.service.TableParseService;
import com.hdmap.data.ibd.dto.ErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 11:13
 * @description: 表解析实现类
 */
@Slf4j
@Service
@RefreshScope
public class TableParseServiceImpl implements TableParseService {
    @Value("${default.table.name.trafficLight:traffic_light}")
    private String trafficLightTableName;
    @Value("${default.thread.split.num:250}")
    private int dataSplitNum;
    @Value("${default.column.name.rowNumber:ROW_NUMBER}")
    private String rowNumberColName;
    @Value("${default.column.name.rowNumber:COLUMN_NUMBER}")
    private String colNumberColName;
    @Resource
    private PostgisManager postgisManager;
    @Resource
    private BaseMappingComponent baseComponent;
    @Resource
    private GeometryManager geometryManager;

    @Override
    public List<ErrorLog> parse(TableMapping tableMapping, SimpleFeatureCollection sourceCollection, DataStore sourceDs, DataStore targetDs) throws IOException, InterruptedException {
        List<ErrorLog> errorLogs = Lists.newArrayList();
        String sourceTableName = tableMapping.getSourceTableName();
        String targetTableName = tableMapping.getTargetTableName();
        SimpleFeatureType sft = targetDs.getSchema(targetTableName);
        List<SimpleFeature> featureList = new ArrayList<>(sourceCollection.size());

        SimpleFeature[] features = sourceCollection.toArray(new SimpleFeature[0]);
        List<Future> futures = new ArrayList<>();
        // 拆分为多份，每份dataSplitNum条数据
        int size = features.length;
        int count = size / dataSplitNum;
        int remainder = size % dataSplitNum;
        if (remainder > 0) {
            count++;
        }
        for (int i = 0; i < count; i++) {
            int start = i * dataSplitNum;
            int end = (i + 1) * dataSplitNum;
            if (end > size) {
                end = size;
            }
            SimpleFeature[] features1 = new SimpleFeature[end - start];
            System.arraycopy(features, start, features1, 0, end - start);

            List<SimpleFeature> featureList1;
            if (trafficLightTableName.equals(tableMapping.getTargetTableName())) {
                BuildFeatureServiceImpl4Tl buildFeatureService4Tl = new BuildFeatureServiceImpl4Tl(baseComponent, geometryManager, rowNumberColName, colNumberColName);
                featureList1 = buildFeatureService4Tl.parseMappingAndBuildFeatures(features1, tableMapping, sourceDs, targetDs, sft, errorLogs);
            } else {
                BuildFeatureServiceImpl buildFeatureService = new BuildFeatureServiceImpl(baseComponent, geometryManager);
                featureList1 = buildFeatureService.parseMappingAndBuildFeatures(features1, tableMapping, sourceDs, targetDs, sft, errorLogs);
            }
            featureList.addAll(featureList1);
        }
        SimpleFeatureCollection targetCollection = new ListFeatureCollection(sft, featureList);
        // 入库
        postgisManager.saveToDbBatch(targetCollection, targetDs, targetTableName);
        return errorLogs;
    }

}
