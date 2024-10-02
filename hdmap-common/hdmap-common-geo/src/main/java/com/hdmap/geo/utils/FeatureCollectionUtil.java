package com.hdmap.geo.utils;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import java.util.Collection;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/15 14:09
 * @description: FeatureCollection工具类
 */
public class FeatureCollectionUtil {
    private FeatureCollectionUtil() {
    }

    public static SimpleFeatureCollection getIntersectCollection(SimpleFeatureCollection collection, SimpleFeature feature) {
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        // 获取相交的几何
        return getIntersectCollection(collection, geom);
    }

    public static SimpleFeatureCollection getIntersectCollection(SimpleFeatureCollection collection, Geometry geom) {
        String geomFieldName = GeometryUtil.getGeometryFieldName(collection);
        Filter filter = GeoFilterUtil.getIntersectFilter(geomFieldName, geom);
        // 获取相交的几何
        return collection.subCollection(filter);
    }

    public static SimpleFeatureCollection getContainsCollection(SimpleFeatureCollection collection, SimpleFeature feature) {
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        return getContainsCollection(collection, geom);
    }

    public static SimpleFeatureCollection getContainsCollection(SimpleFeatureCollection collection, Geometry geom) {
        String geomFieldName = GeometryUtil.getGeometryFieldName(collection);
        Filter filter = GeoFilterUtil.getContainsFilter(geomFieldName, geom);
        // 获取包含的几何
        return collection.subCollection(filter);
    }

    public SimpleFeatureCollection getBboxCollection(SimpleFeatureCollection collection, SimpleFeature feature) {
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        // 获取bbox下的几何
        return getBboxCollection(collection, geom);
    }

    public SimpleFeatureCollection getBboxCollection(SimpleFeatureCollection collection, Geometry geom) {
        String geomFieldName = GeometryUtil.getGeometryFieldName(collection);
        Filter filter = GeoFilterUtil.getBboxFilter(geomFieldName, geom);
        // 获取bbox下的几何
        return collection.subCollection(filter);
    }

    public static SimpleFeatureCollection getEqualCollection(SimpleFeatureCollection collection, String fieldName, String value) {
        Filter filter = GeoFilterUtil.getEqualFilter(fieldName, value);
        // 获取包含的几何
        return collection.subCollection(filter);
    }

    public SimpleFeatureCollection getInCollection(SimpleFeatureCollection collection, String fieldName, Collection<String> values) throws CQLException {
        // Collection -> Array
        String[] valuesArr = values.toArray(new String[0]);
        return getInCollection(collection, fieldName, valuesArr);
    }

    public SimpleFeatureCollection getInCollection(SimpleFeatureCollection collection, String fieldName, String... values) throws CQLException {
        Filter filter = GeoFilterUtil.getInFilter(fieldName, values);
        return collection.subCollection(filter);
    }

    public SimpleFeatureCollection getOrCollection(SimpleFeatureCollection collection, String fieldName, Collection<String> values) throws CQLException {
        // Collection -> Array
        String[] valuesArr = values.toArray(new String[0]);
        return getOrCollection(collection, fieldName, valuesArr);
    }
    public SimpleFeatureCollection getOrCollection(SimpleFeatureCollection collection, String fieldName, String... values) throws CQLException {
        Filter filter = GeoFilterUtil.getOrFilter(fieldName, values);
        return collection.subCollection(filter);
    }

    public SimpleFeatureCollection getOrCollection2(SimpleFeatureCollection collection, String value, Collection<String> fieldNames) throws CQLException {
        // Collection -> Array
        String[] fieldNamesArr = fieldNames.toArray(new String[0]);
        return getOrCollection2(collection, value, fieldNamesArr);
    }
    public SimpleFeatureCollection getOrCollection2(SimpleFeatureCollection collection, String value, String... fieldNames) throws CQLException {
        Filter filter = GeoFilterUtil.getOrFilter2(value, fieldNames);
        return collection.subCollection(filter);
    }

    public SimpleFeatureCollection getLikeCollection(SimpleFeatureCollection collection, String fieldName, Collection<String> values) throws CQLException {
        // Collection -> Array
        String[] valuesArr = values.toArray(new String[0]);
        if (collection.getSchema().getType(fieldName).getBinding() == String.class) {
            return getLikeCollection(collection, fieldName, valuesArr);
        } else {
            // 非String类型使用Like时会报错，此时使用In
            return getInCollection(collection, fieldName, valuesArr);
        }
    }

    public SimpleFeatureCollection getLikeCollection(SimpleFeatureCollection collection, String fieldName, String... values) throws CQLException {
        // field1 like %value1% or field2 like %value2% 的形式组装字符
        Filter filter = GeoFilterUtil.getLikeFilter(fieldName, values);
        return collection.subCollection(filter);
    }


}
