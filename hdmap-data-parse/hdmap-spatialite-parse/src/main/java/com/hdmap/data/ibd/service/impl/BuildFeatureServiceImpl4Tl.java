package com.hdmap.data.ibd.service.impl;

import com.hdmap.data.ibd.manager.GeometryManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
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
 * @date 2023/11/3 15:50
 * @description: traffic_light表解析服务
 */
@Slf4j
public class BuildFeatureServiceImpl4Tl extends AbstractTableMappingTemplate {

    private String rowNumberColName;

    private String colNumberColName;

    public BuildFeatureServiceImpl4Tl(BaseMappingComponent baseTemplate, GeometryManager geometryManager, String rowNumberColName, String colNumberColName) {
        super(baseTemplate, geometryManager);
        this.rowNumberColName = rowNumberColName;
        this.colNumberColName = colNumberColName;
    }

    @Override
    public List<SimpleFeature> buildFeatures(Map<String, Object> colValMap, SimpleFeatureType schema, SimpleFeature feature) throws FactoryException, TransformException, java.text.ParseException {
        List<SimpleFeature> featureList = new ArrayList<>();
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        // 生成一个10位的随机数
        long random = Long.parseLong(RandomStringUtils.randomNumeric(10));
        // 红绿灯子灯的行数
        int rowNum = (int) feature.getAttribute(rowNumberColName);
        // 红绿灯子灯的列数
        int colNum = (int) feature.getAttribute(colNumberColName);
        // 根据行列数进行复制
        for (int i = 1; i < rowNum + 1; i++) {
            for (int j = 1; j < colNum + 1; j++) {
                // 设置ID
                colValMap.put("ss_id", random * 10 + i * j);
                // 设置geom
                colValMap.put("geom", getSubCenterPoint(geom, rowNum, colNum, i, j));
                // 生成SimpleFeature
                SimpleFeature targetFeature = generateSimpleFeature(colValMap, schema);
                featureList.add(targetFeature);
            }
        }
        return featureList;
    }

    // 计算子灯中心点位置，geom都为矩形
    private Geometry getSubCenterPoint(Geometry geom, int rowNum, int colNum, int i, int j) throws FactoryException, TransformException {
        Coordinate[] coords = geom.getCoordinates();
        Coordinate leftUpCoord = coords[0];
        Coordinate rightUpCoord = coords[1];
        Coordinate leftDownCoord = coords[3];
        double deltaX0 = (rightUpCoord.x - leftUpCoord.x);
        double deltaY0 = (rightUpCoord.y - leftUpCoord.y);
        double deltaZ0 = (rightUpCoord.z - leftUpCoord.z);
        double deltaX1 = (leftDownCoord.x - leftUpCoord.x);
        double deltaY1 = (leftDownCoord.y - leftUpCoord.y);
        double deltaZ1 = (leftDownCoord.z - leftUpCoord.z);

        double x = leftUpCoord.x + ((double) i / rowNum) * deltaX1 / 2 + (double) j / colNum * deltaX0 / 2;
        double y = leftUpCoord.y + (double) i / rowNum * deltaY1 / 2 + (double) j / colNum * deltaY0 / 2;
        double z = leftUpCoord.z + (double) i / rowNum * deltaZ1 / 2 + (double) j / colNum * deltaZ0 / 2;

        return geom.getFactory().createPoint(new Coordinate(x, y, z));
    }
}
