package com.hdmap.geo.utils;

import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.opengis.feature.simple.SimpleFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/1 15:50
 * @description: 左右关系工具类
 */

@Slf4j
public class GeoLeftUtil {

    private final static GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

    private GeoLeftUtil() {
    }

    /**
     * feature2相对于feature1的左右
     */
    public static boolean checkLeft(SimpleFeature rightFeature, SimpleFeature leftFeature) {
        Geometry rightGeometry = (Geometry) rightFeature.getDefaultGeometry();
        Geometry leftGeometry = (Geometry) leftFeature.getDefaultGeometry();
        if (rightGeometry instanceof LineString && leftGeometry instanceof LineString) {
            return checkLeft((LineString) rightGeometry, (LineString) leftGeometry);
        } else {
            throw new IllegalArgumentException("参数类型错误：feature的几何类型不是LineString");
        }
    }

    /**
     * leftLine相对于rightLine左右
     */
    public static boolean checkLeft(LineString rightLine, LineString leftLine) {
        try {
            return checkLeftComplex(rightLine, leftLine);
        } catch (Exception e) {
            return checkLeftSimple(rightLine, leftLine);
        }
    }

    /**
     * 计算leftLine相对于rightLine的左右
     * 核心思想 根据leftLine，在rightLine上截取一段线，计算leftLine的质心点相对于rightLine的左右
     * 优点：简单，适用于大多数场景
     * 缺点：特例场景下，计算有误，比如line1和line2都存在大幅度弯曲时，质心偏离本体较远
     */
    public static boolean checkLeftSimple(LineString rightLine, LineString leftLine) {
        // 计算line2相对于line1的左右
        //Coordinate line1StartPoint = line1.getStartPoint().getCoordinate();
        //Coordinate line1EndPoint = line1.getEndPoint().getCoordinate();
        Coordinate line2StartPoint = leftLine.getStartPoint().getCoordinate();
        Coordinate line2EndPoint = leftLine.getEndPoint().getCoordinate();
        // 根据line2的起点和终点对line1进行截取
        LocationIndexedLine lil = new LocationIndexedLine(rightLine);
        LinearLocation start = lil.indexOf(line2StartPoint);
        LinearLocation end = lil.indexOf(line2EndPoint);
        LineString line1Clip = (LineString)lil.extractLine(start, end);
        // line1的质心点
        Coordinate line1MidPoint = rightLine.getCentroid().getCoordinate();
        // line2的质心点
        Coordinate line2MidPoint = leftLine.getCentroid().getCoordinate();
        // 质心计算时出现特例，大幅度弯曲时，车line2的质心在line1右侧，但是实际在line1左侧
        Coordinate line2CompPoint = leftLine.getStartPoint().getCoordinate();
        // 计算角度，顺时针返回正，逆时针返回负
        double isClockwise = Angle.angleBetweenOriented(line2CompPoint, line1Clip.getStartPoint().getCoordinate(), line1Clip.getEndPoint().getCoordinate());
        // 顺时针，line2在line1的右侧；逆时针，line2在line1的左侧
        return isClockwise < 0;
    }

    /**
     * 计算leftLine相对于rightLine的左右
     */
    public static boolean checkLeftComplex(LineString rightLine, LineString leftLine) {
        // 计算leftLine到rightLine的最大距离
        Coordinate[] leftCoordinates = leftLine.getCoordinates();
        List<Point> leftPoints = new ArrayList<>();
        for (Coordinate coordinate : leftCoordinates) {
            leftPoints.add(gf.createPoint(coordinate));
        }
        double maxDistance = 0;
        for (Point point : leftPoints) {
            double distance = rightLine.distance(point);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }
        // 计算line1的缓冲区
        BufferParameters bufferParameters = new BufferParameters();
        bufferParameters.setSingleSided(true);
        BufferOp bufOp = new BufferOp(rightLine, bufferParameters);
        // 左侧缓冲区
        Geometry bufferLeft = bufOp.getResultGeometry(maxDistance * 1.1); //左

        // 判断leftLine的点是否全部左侧缓冲区相交
       for (Point point : leftPoints) {
            if (!bufferLeft.intersects(point)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkLeftSimple(Coordinate A, Coordinate B, Coordinate C) {
        // 计算角度，顺时针返回正，逆时针返回负
        double isClockwise = Angle.angleBetweenOriented(B, A, C);
        // 顺时针(正)，右侧；逆时针(负)，左侧
        return isClockwise < 0;
    }

}
