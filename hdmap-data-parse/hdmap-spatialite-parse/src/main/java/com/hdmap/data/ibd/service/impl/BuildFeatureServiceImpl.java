package com.hdmap.data.ibd.service.impl;

import com.hdmap.data.ibd.manager.GeometryManager;
import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 11:13
 * @description: 表解析实现类
 */
@Slf4j
public class BuildFeatureServiceImpl extends AbstractTableMappingTemplate {

    public BuildFeatureServiceImpl(BaseMappingComponent baseTemplate, GeometryManager geometryManager) {
        super(baseTemplate, geometryManager);
    }

    @Override
    public List<SimpleFeature> buildFeatures(Map<String, Object> colValMap, SimpleFeatureType schema, SimpleFeature feature) throws FactoryException, TransformException, java.text.ParseException {
        List<SimpleFeature> featureList = new ArrayList<>();
        SimpleFeature targetFeature = generateSimpleFeature(colValMap, schema);
        featureList.add(targetFeature);
        return featureList;
    }
}
