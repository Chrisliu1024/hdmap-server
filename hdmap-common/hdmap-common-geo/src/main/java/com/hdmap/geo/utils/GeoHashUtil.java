package com.hdmap.geo.utils;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.BoundingBoxGeoHashIterator;
import ch.hsr.geohash.util.TwoGeoHashBoundingBox;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hdmap.geo.model.Grid;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.util.List;
import java.util.Set;

public class GeoHashUtil {

    public static List<Grid> getGeoHashByStr(List<String> geoHashStrList) {
        List<Grid> list = Lists.newArrayList();
        ch.hsr.geohash.GeoHash geoHash;
        short geoHashNum = 1;
        for (String geoHashStr : geoHashStrList) {
            geoHash = ch.hsr.geohash.GeoHash.fromGeohashString(geoHashStr);
            Geometry geometry = getGeoHashGeom(geoHash);
            Grid gridInfo = new Grid(geoHashStr, geoHashNum, geometry);
            list.add(gridInfo);
            geoHashNum++;
        }
        return list;
    }

    public static List<Grid> getGeoHashByBBox(ReferencedEnvelope bbox, int precision) {
        // 判断bbox的坐标系是否为经度在前
        if (bbox.getCoordinateReferenceSystem().getCoordinateSystem().getAxis(0).getName().getCode().equals("Geodetic longitude")) {
            return getGeoHashByBBox(bbox.getMinY(), bbox.getMinX(), bbox.getMaxY(), bbox.getMaxX(), precision);
        } else {
            return getGeoHashByBBox(bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), precision);
        }
    }
    /**
     * @param minLat 矩形内纬度的最小值
     * @param minLng 矩形内经度的最小值
     * @param maxLat 矩形内纬度的最大值
     * @param maxLng 矩形内经度的最大值
     * @param precision geoHash的精度
     * @Description: 获取指定经纬度范围内的geoHash编码列表
     */

    public static List<Grid> getGeoHashByBBox(double minLat, double minLng, double maxLat, double maxLng, int precision) {
        //盒型geoHash迭代器,获取矩形内的全部geoHash
        BoundingBoxGeoHashIterator iterator = getBoundingBoxGeoHashIterator(minLat, minLng, maxLat, maxLng, precision);
        List<Grid> list = Lists.newArrayList();
        ch.hsr.geohash.GeoHash geoHash;
        //循环遍历
        short geoHashNum = 1;
        while (iterator.hasNext()) {
            geoHash = iterator.next();
            //获取geoHash编码
            String geoHashStr = geoHash.toBase32();
            //构建geoHash的矩形
            Geometry geometry = getGeoHashGeom(geoHash);
            Grid gridInfo = new Grid(geoHashStr, geoHashNum, geometry);
            list.add(gridInfo);
            // 记录geohash序号，用于点云划分
            geoHashNum++;
        }
        return list;

    }
    /**
     * @param minLat 矩形内纬度的最小值
     * @param minLng 矩形内经度的最小值
     * @param maxLat 矩形内纬度的最大值
     * @param maxLng 矩形内经度的最大值
     * @param precision geoHash的精度
     * @Description: 获取指定经纬度范围内的geoHash编码列表
     */

    public static Set<String> getGeoHashStrByBBox(double minLat, double minLng, double maxLat, double maxLng, int precision) {
        //盒型geoHash迭代器,获取矩形内的全部geoHash
        BoundingBoxGeoHashIterator iterator = getBoundingBoxGeoHashIterator(minLat, minLng, maxLat, maxLng, precision);
        Set<String> set = Sets.newHashSet();
        ch.hsr.geohash.GeoHash geoHash;
        //循环遍历
        while (iterator.hasNext()) {
            geoHash = iterator.next();
            //获取geoHash编码
            set.add(geoHash.toBase32());
        }
        return set;

    }

    /**
     * @param minLat 矩形内纬度的最小值
     * @param minLng 矩形内经度的最小值
     * @param maxLat 矩形内纬度的最大值
     * @param maxLng 矩形内经度的最大值
     * @param precision geoHash的精度
     * @Description: 获取指定经纬度范围内的geoHash的矩形列表
     */

    public static List<Geometry> getGeoHashGeomByBBox(double minLat, double minLng, double maxLat, double maxLng, int precision) {
        //盒型geoHash迭代器,获取矩形内的全部geoHash
        BoundingBoxGeoHashIterator iterator = getBoundingBoxGeoHashIterator(minLat, minLng, maxLat, maxLng, precision);
        List<Geometry> list = Lists.newArrayList();
        ch.hsr.geohash.GeoHash geoHash;
        //循环遍历
        while (iterator.hasNext()) {
            geoHash = iterator.next();
            //构建geoHash的矩形
            Geometry geometry = getGeoHashGeom(geoHash);
            list.add(geometry);
        }
        return list;
    }

    /**
     * @param geoHashStr 指定的geoHash的字符形式，如：wqj
     * @Description: 获取指定geoHash的矩形
     */

    public static Geometry getGeoHashGeom(String geoHashStr) {
        ch.hsr.geohash.GeoHash geoHash = ch.hsr.geohash.GeoHash.fromGeohashString(geoHashStr);
        return getGeoHashGeom(geoHash);
    }

    /**
     * @param geoHash 指定的geoHash
     * @Description: 获取指定geoHash的矩形
     */

    public static Geometry getGeoHashGeom(ch.hsr.geohash.GeoHash geoHash) {
        //获取geoHash的矩形
        BoundingBox boundingBox = geoHash.getBoundingBox();
        //获取geoHash的矩形
        WGS84Point northWestCorner = boundingBox.getNorthWestCorner();
        WGS84Point northEastCorner = boundingBox.getNorthEastCorner();
        WGS84Point southEastCorner = boundingBox.getSouthEastCorner();
        WGS84Point southWestCorner = boundingBox.getSouthWestCorner();
        //构建geoHash的矩形
        return createPolygonByWGS84Points(northWestCorner, northEastCorner, southEastCorner, southWestCorner);
    }

    public static BoundingBoxGeoHashIterator getBoundingBoxGeoHashIterator(double minLat, double minLng, double maxLat, double maxLng, int precision) {
        //通过矩形的左下角 (西南角) 构建一个精度为precision的geoHash值
        ch.hsr.geohash.GeoHash southWestCorner = ch.hsr.geohash.GeoHash.withCharacterPrecision(minLat, minLng, precision);
        //通过矩形的右上角 (东北角) 构建一个精度为precision的geoHash值
        ch.hsr.geohash.GeoHash northEastCorner = ch.hsr.geohash.GeoHash.withCharacterPrecision(maxLat, maxLng, precision);
        //使用两个geoHash构建一个外接盒型
        TwoGeoHashBoundingBox twoGeoHashBoundingBox = new TwoGeoHashBoundingBox(southWestCorner, northEastCorner);
        //盒型geoHash迭代器,获取矩形内的全部geoHash
        return new BoundingBoxGeoHashIterator(twoGeoHashBoundingBox);
    }

    public static Geometry createPolygonByWGS84Points(WGS84Point northWestCorner, WGS84Point northEastCorner, WGS84Point southEastCorner, WGS84Point southWestCorner) {
        //构建geoHash的矩形
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(northWestCorner.getLongitude(), northWestCorner.getLatitude());
        coordinates[1] = new Coordinate(northEastCorner.getLongitude(), northEastCorner.getLatitude());
        coordinates[2] = new Coordinate(southEastCorner.getLongitude(), southEastCorner.getLatitude());
        coordinates[3] = new Coordinate(southWestCorner.getLongitude(), southWestCorner.getLatitude());
        coordinates[4] = new Coordinate(northWestCorner.getLongitude(), northWestCorner.getLatitude());
        return GeometryUtil.createPolygon(coordinates, 4326);
    }
}
