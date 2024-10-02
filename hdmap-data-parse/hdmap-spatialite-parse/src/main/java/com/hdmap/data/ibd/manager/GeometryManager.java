package com.hdmap.data.ibd.manager;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 15:50
 * @description: 几何通用方法类
 */
@Slf4j
@Component
@RefreshScope
public class GeometryManager {
    @Value("${default.geom.srid.plane:32649}")
    private String defaultPlaneSrid;
    @Value("${default.extend.length:1}")
    private double defaultExtendLength;

    protected final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

    public Object getAttriMaxValueFromArray(List<SimpleFeature> features, String attributeName) {
        if (features == null || features.isEmpty()) {
            return null;
        }
        Object maxVal = null;
        for (SimpleFeature feature : features) {
            Object val = feature.getAttribute(attributeName);
            if (val == null) {
                continue;
            }
            if (maxVal == null) {
                maxVal = val;
            } else {
                if (Double.parseDouble(val.toString()) > Double.parseDouble(maxVal.toString())) {
                    maxVal = val;
                }
            }
        }
        return maxVal;
    }

    public Object getAttriMinValueFromArray(List<SimpleFeature> features, String attributeName) {
        if (features == null || features.isEmpty()) {
            return null;
        }
        Object minVal = null;
        for (SimpleFeature feature : features) {
            Object val = feature.getAttribute(attributeName);
            if (val == null) {
                continue;
            }
            if (minVal == null) {
                minVal = val;
            } else {
                if (Double.parseDouble(val.toString()) < Double.parseDouble(minVal.toString())) {
                    minVal = val;
                }
            }
        }
        return minVal;
    }

    /**
     * 获取前驱lane/road
     */
    public List<SimpleFeature> getPreFeature(SimpleFeature feature, SimpleFeatureCollection lineCollection, String lineSlNodeIdName, String lineElNodeIdName) throws CQLException {
        return getEqualFeatures(feature, lineCollection, lineSlNodeIdName, lineElNodeIdName);
    }

    /**
     * 获取后继lane/road
     */
    public List<SimpleFeature> getSucFeature(SimpleFeature feature, SimpleFeatureCollection lineCollection, String lineSlNodeIdName, String lineElNodeIdName) throws CQLException {
        return getEqualFeatures(feature, lineCollection, lineElNodeIdName, lineSlNodeIdName);
    }

    public List<SimpleFeature> getEqualFeatures(SimpleFeature feature, SimpleFeatureCollection relCollection, String attriName, String relAttriName) throws CQLException {
        // 获取road_id
        Object attriValueObj = feature.getAttribute(attriName);
        if (attriValueObj == null) {
            return new ArrayList<>();
        }
        String attriValue = attriValueObj.toString();
        // 查询
        return getEqualFeatures(attriValue, relCollection, relAttriName);
    }

    public List<SimpleFeature> getEqualFeatures(String attriValue, SimpleFeatureCollection relCollection, String relAttriName) throws CQLException {
        // 查询
        SimpleFeatureCollection subRelCollection = getEqualFilterCollection(relCollection, relAttriName, attriValue);
        return Arrays.asList(subRelCollection.toArray(new SimpleFeature[0]));
    }

    public SimpleFeatureCollection getEqualFilterCollection(DataStore dataStore, String tableName, String attriName, String attriVal) throws CQLException, IOException {
        Filter filter = getEqualFilter(attriName, attriVal);
        return dataStore.getFeatureSource(tableName).getFeatures(filter);
    }

    public SimpleFeatureCollection getEqualFilterCollection(SimpleFeatureCollection collection, String attriName, String attriVal) throws CQLException {
        Filter filter = getEqualFilter(attriName, attriVal);
        return collection.subCollection(filter);
    }

    public SimpleFeature[] getLikeFilterArray(SimpleFeatureCollection collection, String attriName, String... attriVals) throws CQLException {
        return getLikeFilterCollection(collection, attriName, attriVals).toArray(new SimpleFeature[0]);
    }

    public SimpleFeatureCollection getLikeFilterCollection(SimpleFeatureCollection collection, String attriName, String... attriVals) throws CQLException {
        // field1 like %value1% or field2 like %value2% 的形式组装字符
        StringBuilder sb = new StringBuilder();
        for (String fieldValue : attriVals) {
            sb.append(attriName).append(" like '%").append(fieldValue).append("%' or ");
        }
        String filterStr = sb.substring(0, sb.length() - 4);
        Filter filter = ECQL.toFilter(filterStr);
        return collection.subCollection(filter);
    }

    public SimpleFeatureCollection getIntersectFilterCollection(SimpleFeatureCollection collection, SimpleFeature feature, double bufferDistance) {
        CoordinateReferenceSystem crs = collection.getSchema().getCoordinateReferenceSystem();
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        if (bufferDistance > 0) {
            double transDis = transDistance(bufferDistance, geom);
            geom = geom.buffer(transDis);
        }
        // 获取相交的几何
        return getIntersectFilterCollection(collection, geom, crs);
    }

    private SimpleFeatureCollection getIntersectFilterCollection(SimpleFeatureCollection collection, Geometry geom, CoordinateReferenceSystem crs) {
        SimpleFeatureType sft = collection.getSchema();
        // 重新设置坐标系srid为32649
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.init(sft);
        builder.setSRS("EPSG:" + defaultPlaneSrid);
        SimpleFeatureType newSft = builder.buildFeatureType();
        // 重新设置坐标系
        SimpleFeatureCollection newCollection = new ListFeatureCollection(newSft, collection.toArray(new SimpleFeature[0]));

        String geomName = newCollection.getSchema().getGeometryDescriptor().getType().getName().toString();
        ReferencedEnvelope bbox = new ReferencedEnvelope(geom.getEnvelopeInternal(), crs);
        Filter filter1 = ff.bbox(ff.property(geomName), bbox);
        Filter filter2 = ff.intersects(ff.property(geomName), ff.literal(geom));
        Filter filter = ff.and(filter1, filter2);
        // 获取相交的几何
        return newCollection.subCollection(filter);
    }

    private Filter getEqualFilter(String attriName, String attriVal) throws CQLException {
        String cql = String.format("%s = '%s'", attriName, attriVal);
        return ECQL.toFilter(cql);
    }

    /**
     * 转换距离，如果为4326，米转换为经纬度
     */
    public double transDistance(double distance, Geometry geom) {
        int srid = geom.getSRID();
        if (srid == 4326) {
            return ciloMetter(distance);
        }
        return distance;
    }

    /**
     * 米转换为经纬度
     */
    public double ciloMetter(double meter){
        return meter / (Math.PI * 6371004) * 180;
    }

    /**
     * 坐标去重，按照距离阈值
     */
    public Coordinate[] getDistinctCoords(Coordinate[] coords, double distanceThreshold) {
        // 相临两两比较距离，小于阈值的删除
        List<Coordinate> coordList = new ArrayList<>();
        for (int i = 0; i < coords.length; i++) {
            Coordinate rightCoord = coords[i];
            if (coordList.isEmpty()) {
                coordList.add(rightCoord);
                continue;
            }
            Coordinate leftCoord = coordList.get(coordList.size() - 1);
            double distance = leftCoord.distance(rightCoord);
            if (distance < distanceThreshold) {
                continue;
            }
            coordList.add(rightCoord);
        }
        return coordList.toArray(new Coordinate[0]);
    }

    public boolean checkLeft1(SimpleFeature feature1, SimpleFeature feature2) {
        try {
            return checkLeftComplex(feature1, feature2);
        } catch (Exception e) {
            return checkLeftSimple(feature1, feature2);
        }
    }

    /**
     * 计算feature2相对于feature1的左右
     */
    public boolean checkLeftSimple(SimpleFeature feature1, SimpleFeature feature2) {
        LineString line1 = (LineString) feature1.getDefaultGeometry();
        LineString line2 = (LineString) feature2.getDefaultGeometry();
        // 计算line2相对于line1的左右
        //Coordinate line1StartPoint = line1.getStartPoint().getCoordinate();
        //Coordinate line1EndPoint = line1.getEndPoint().getCoordinate();
        Coordinate line2StartPoint = line2.getStartPoint().getCoordinate();
        Coordinate line2EndPoint = line2.getEndPoint().getCoordinate();
        // 根据line2的起点和终点对line1进行截取
        LocationIndexedLine lil = new LocationIndexedLine(line1);
        LinearLocation start = lil.indexOf(line2StartPoint);
        LinearLocation end = lil.indexOf(line2EndPoint);
        LineString line1Clip = (LineString)lil.extractLine(start, end);
        // line1的质心点
        Coordinate line1MidPoint = line1.getCentroid().getCoordinate();
        // line2的质心点
        Coordinate line2MidPoint = line2.getCentroid().getCoordinate();
        // 质心计算时出现特例，交叉时，车道汇入时，line2起点在line1右侧，但是质心在line1左侧
        // 车道汇出时，line2终点在line1右侧，但是质心在line1左侧
        // 特例，目前仅考虑车道汇入时 TODO
        Coordinate line2CompPoint = line2.getStartPoint().getCoordinate();
        // 计算角度，顺时针返回正，逆时针返回负
        double isClockwise = Angle.angleBetweenOriented(line2CompPoint, line1Clip.getStartPoint().getCoordinate(), line1Clip.getEndPoint().getCoordinate());
        // 顺时针，line2在line1的右侧；逆时针，line2在line1的左侧
        return isClockwise < 0;
    }

    /**
     * 计算feature2相对于feature1的左右
     */
    public boolean checkLeftComplex(SimpleFeature feature1, SimpleFeature feature2) {
        LineString line1 = (LineString) feature1.getDefaultGeometry();
        LineString line2 = (LineString) feature2.getDefaultGeometry();
        // 计算line2到line1的距离
        double distance = line1.distance(line2);
        // 倒数第二个点和倒数第一个点的向量
        Coordinate[] coordinates = line1.getCoordinates();
        // 去除距离过近的点
        coordinates = getDistinctCoords(coordinates, 0.1);
        Coordinate leftPoint = coordinates[coordinates.length - 2];
        Coordinate rightPoint = coordinates[coordinates.length - 1];
        // 单位向量
        Coordinate vector = getNormalVector(leftPoint, rightPoint);
        // 最后点沿向量方向延伸XX米
        double extendLength = distance + defaultExtendLength;
        Coordinate lastPointExtend = new Coordinate(rightPoint.x + vector.x * extendLength,
                rightPoint.y + vector.y * extendLength);
        // line1和lastPointExtend构建新线
        Coordinate[] newCoords = new Coordinate[line1.getCoordinates().length + 1];
        for (int i = 0; i < line1.getCoordinates().length; i++) {
            newCoords[i] = line1.getCoordinates()[i];
        }
        newCoords[newCoords.length - 1] = lastPointExtend;
        LineString line1Extend = gf.createLineString(newCoords);
        // line1的起点向前延伸，应对大弯曲，起点和终点和缓冲区交汇到一起
        Coordinate vector1 = getNormalVector(coordinates[1], coordinates[0]);
        Coordinate firstPointExtend = new Coordinate(coordinates[0].x + vector1.x * extendLength,
                coordinates[0].y + vector1.y * extendLength);
        // line1的终点向前延伸，应对大弯曲
        double extendLength1 = extendLength * 1.2;
        Coordinate lastPointExtend1 = new Coordinate(rightPoint.x + vector.x * extendLength1,
                rightPoint.y + vector.y * extendLength1);
        // line1Extend和firstPointExtend,lastPointExtend1构建新线
        Coordinate[] newCoords1 = new Coordinate[line1Extend.getCoordinates().length + 2];
        newCoords1[0] = firstPointExtend;
        for (int i = 0; i < line1Extend.getCoordinates().length; i++) {
            newCoords1[i + 1] = line1Extend.getCoordinates()[i];
        }
        newCoords1[newCoords1.length - 1] = lastPointExtend1;
        LineString line1Extend1 = gf.createLineString(newCoords1);
        // 构建缓冲区
        Geometry buffer = line1Extend.buffer(extendLength, 8, BufferParameters.CAP_FLAT);
        // 构建小缓冲区（0.1米）
        Geometry bufferSmall = line1Extend1.buffer(0.1, 8, BufferParameters.CAP_ROUND);
        // 计算小缓冲区和缓冲区的差集
        Geometry diff = buffer.difference(bufferSmall);
        // MultiPolygon -> Polygon
        List<Geometry> diffGeomList = new ArrayList<>();
        if (diff instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) diff;
            int numGeometries = multiPolygon.getNumGeometries();
            for (int i = 0; i < numGeometries; i++) {
                Geometry geometryN = multiPolygon.getGeometryN(i);
                if (geometryN instanceof Polygon) {
                    diffGeomList.add(geometryN);
                }
            }
        }
        // 过滤掉面积小于1的面
        diffGeomList = diffGeomList.stream().filter(geometry -> geometry.getArea() > 1).collect(Collectors.toList());
        // 增加提示，实际用不到
        if (diffGeomList.size() != 2) {
            log.warn("左右面判断有误");
        }
        // 计算左右面的质心
        Geometry leftPolygon = null;
        Coordinate centroid0 = diffGeomList.get(0).getCentroid().getCoordinate();
        Coordinate centroid1 = diffGeomList.get(1).getCentroid().getCoordinate();
        // 计算左右
        if (checkLeftSimple(line1.getStartPoint().getCoordinate(), centroid0, centroid1)) {
            leftPolygon = diffGeomList.get(0);
        } else {
            leftPolygon = diffGeomList.get(1);
        }
        // 判断line2在左面还是右面
        return line2.intersects(leftPolygon);
    }

    private boolean checkLeftSimple(Coordinate A, Coordinate B, Coordinate C) {
        // 计算角度，顺时针返回正，逆时针返回负
        double isClockwise = Angle.angleBetweenOriented(B, A, C);
        // 顺时针(正)，右侧；逆时针(负)，左侧
        return isClockwise < 0;
    }

    private Coordinate getNormalVector(Coordinate leftPoint, Coordinate rightPoint) {
        Coordinate vector = new Coordinate(rightPoint.x - leftPoint.x, rightPoint.y - leftPoint.y);
        // 向量的单位向量
        double vectorLength = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
        return new Coordinate(vector.x / vectorLength, vector.y / vectorLength);
    }

    // SimpleFeature -> JSON
    public String feature2Json(SimpleFeature feature) throws IOException {
        GeometryJSON geometryJson = new GeometryJSON(16);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        return featureJson.toString(feature);
    }

}
