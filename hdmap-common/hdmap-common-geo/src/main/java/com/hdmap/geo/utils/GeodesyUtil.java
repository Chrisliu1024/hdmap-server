package com.hdmap.geo.utils;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @author admin
 * @version 1.0
 * @description: Geodesy转换工具类
 * @date 2023/7/13 11:23
 */
public class GeodesyUtil {

    // 地球平均半径，单位m
    private static final double EARTH_MEAN_RADIUS_METER = 6371008.7714;

    private GeodesyUtil() {
    }

    /**
     * 米转换为经纬度下弧度
     */
    public static double meterToRadian(double meter){
        return meter / (Math.PI * EARTH_MEAN_RADIUS_METER) * 180;
    }

    /**
     * 经纬度转换为米
     */
    public static double degreeToMeter(double degree) {
        return degree * (Math.PI * EARTH_MEAN_RADIUS_METER) / 180;
    }


}
