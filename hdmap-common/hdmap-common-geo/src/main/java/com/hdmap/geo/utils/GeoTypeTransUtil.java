package com.hdmap.geo.utils;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.io.*;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/21 14:26
 * @description: 地理几何类型转换工具类
 */
public class GeoTypeTransUtil {

    public static int DIMENSION = 3;
    public static int GEOMETRY_PRECISION = 16;
    public static final GeometryJSON g = new GeometryJSON(GEOMETRY_PRECISION); // 默认精度为4
    public static final FeatureJSON f = new FeatureJSON(g);

    private GeoTypeTransUtil() {
    }

    public static void setDimension(int dimension) {
        GeoTypeTransUtil.DIMENSION = dimension;
    }

    public static void setGeometryPrecision(int precision) {
        GeoTypeTransUtil.GEOMETRY_PRECISION = precision;
    }

    // geometry -> wkt (String)
    public static String geom2Wkt(Geometry geom) {
        WKTWriter writer = new WKTWriter(DIMENSION);
        return writer.write(geom);
    }

    // wkt (String) -> geometry
    public static Geometry wkt2Geom(String wkt) throws ParseException {
        WKTReader reader = new WKTReader();
        return reader.read(wkt);
    }

    // geometry -> wkb (byte[])
    public static byte[] geom2Wkb(Geometry geom) {
        WKBWriter writer = new WKBWriter(DIMENSION, ByteOrderValues.LITTLE_ENDIAN, true);
        return writer.write(geom);
    }

    // wkb (String) -> geometry
    public static Geometry wkb2Geom(String wkb) throws ParseException {
        WKBReader reader = new WKBReader( );
        return reader.read(WKBReader.hexToBytes(wkb));
    }

    // List<Geometry> -> GeoJSON
    public static String geoms2Json(List<Geometry> geoms) throws IOException {
        if (geoms.isEmpty()) {
            return "";
        }
        StringWriter writer = new StringWriter();
        GeometryCollection collection = new GeometryCollection(geoms.toArray(new Geometry[0]), geoms.get(0).getFactory());
        g.writeGeometryCollection(collection, writer);
        return writer.toString();
    }

    // geometry -> GeoJSON
    public static String geom2Json(Geometry geom) throws IOException {
        StringWriter writer = new StringWriter();
        g.write(geom, writer);
        return writer.toString();
    }

    // SimpleFeatureCollection -> GeoJSON
    public static String collection2Json(SimpleFeatureCollection collection) throws IOException {
        StringWriter writer = new StringWriter();
        f.writeFeatureCollection(collection, writer);
        return writer.toString();
    }

    // SimpleFeature -> GeoJSON
    public static String feature2Json(SimpleFeature feature) throws IOException {
        return f.toString(feature);
    }

    // GeoJSON -> SimpleFeature
    public static SimpleFeature json2Feature (String json) throws IOException {
        return f.readFeature(json);
    }

    // SimpleFeatureCollection -> GeoJSON（文件）
    public static void featureCollection2GeoJsonFile(SimpleFeatureCollection featureCollection, String path) throws IOException {
        f.writeFeatureCollection(featureCollection, path);
    }

}
