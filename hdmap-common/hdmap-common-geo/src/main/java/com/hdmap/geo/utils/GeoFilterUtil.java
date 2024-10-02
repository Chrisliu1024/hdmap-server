package com.hdmap.geo.utils;

import org.apache.commons.lang3.StringUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/15 14:06
 * @description: 过滤器工具类
 */
public class GeoFilterUtil {
    private static final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private GeoFilterUtil() {
    }

    public static Filter getIntersectFilter(String geomFieldName, Geometry geom) {
        ReferencedEnvelope bbox = GeometryUtil.getReferencedEnvelope(geom);
        Filter filter1 = ff.bbox(ff.property(geomFieldName), bbox);
        Filter filter2 = ff.intersects(ff.property(geomFieldName), ff.literal(geom));
        return ff.and(filter1, filter2);
    }

    public static Filter getContainsFilter(String geomFieldName, Geometry geom) {
        ReferencedEnvelope bbox = GeometryUtil.getReferencedEnvelope(geom);
        Filter filter1 = ff.bbox(ff.property(geomFieldName), bbox);
        Filter filter2 = ff.within(ff.property(geomFieldName), ff.literal(geom));
        return ff.and(filter1, filter2);
    }

    public static Filter getBboxFilter(String geomFieldName, Geometry geom) {
        ReferencedEnvelope bbox = GeometryUtil.getReferencedEnvelope(geom);
        return ff.bbox(ff.property(geomFieldName), bbox);
    }

    public static Filter getEqualFilter(String fieldName, String value) {
        return ff.equals(ff.property(fieldName), ff.literal(value));
    }

    public static Filter getInFilter(String fieldName, String... values) throws CQLException {
        String joinValue = StringUtils.join(values, ",");
        return ECQL.toFilter(fieldName + " IN ('" + joinValue.replaceAll(",", "','") + "')");
    }

    public static Filter getOrFilter(String fieldName, String... values) throws CQLException {
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(fieldName).append(" = '").append(value).append("' or ");
        }
        String filterStr = sb.substring(0, sb.length() - 4);
        return ECQL.toFilter(filterStr);
    }

    public static Filter getOrFilter2(String value, String... fieldNames) throws CQLException {
        StringBuilder sb = new StringBuilder();
        for (String fieldName : fieldNames) {
            sb.append(fieldName).append(" = '").append(value).append("' or ");
        }
        String filterStr = sb.substring(0, sb.length() - 4);
        return ECQL.toFilter(filterStr);
    }

    public static Filter getLikeFilter(String fieldName, String... values) throws CQLException {
        // fieldName like %value1% or fieldName like %value2% 的形式组装字符
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            sb.append(fieldName).append(" like '%").append(value).append("%' or ");
        }
        String filterStr = sb.substring(0, sb.length() - 4);
        return ECQL.toFilter(filterStr);
    }

    public static Filter getOrFilter(Filter filter1, Filter filter2) {
        return ff.or(filter1, filter2);
    }

    public static Filter getAndFilter(Filter filter1, Filter filter2) {
        return ff.and(filter1, filter2);
    }
}
