package com.hdmap.data.common;

import com.hdmap.geo.utils.GeoTypeTransUtil;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/3 18:02
 * @description: TODO
 */
public class CommonTest {

    protected SimpleFeature createSimpleFeature() throws ParseException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SimpleFeatureType");
        builder.add("name", String.class);
        builder.add("age", Integer.class);
        builder.add("geometry", Geometry.class);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(builder.buildFeatureType());
        featureBuilder.set("name", "zhangsan");
        featureBuilder.set("age", 18);
        featureBuilder.set("geometry", createPoint());
        return featureBuilder.buildFeature("1");
    }
    protected SimpleFeature createSimpleFeature(Geometry geometry) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SimpleFeatureType");
        builder.add("name", String.class);
        builder.add("age", Integer.class);
        builder.add("geometry", Geometry.class);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(builder.buildFeatureType());
        featureBuilder.set("name", "zhangsan");
        featureBuilder.set("age", 18);
        featureBuilder.set("geometry", geometry);
        return featureBuilder.buildFeature("1");
    }

    protected SimpleFeature createSimpleFeature(int number) throws ParseException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SimpleFeatureType");
        builder.add("name", String.class);
        builder.add("age", Integer.class);
        builder.add("geometry", Geometry.class);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(builder.buildFeatureType());
        featureBuilder.set("name", "zhangsan");
        featureBuilder.set("age", number);
        featureBuilder.set("geometry", createPoint());
        return featureBuilder.buildFeature("1");
    }

    protected SimpleFeature createSimpleFeature(String str) throws ParseException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("SimpleFeatureType");
        builder.add("name", String.class);
        builder.add("age", Integer.class);
        builder.add("geometry", Geometry.class);
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(builder.buildFeatureType());
        featureBuilder.set("name", str);
        featureBuilder.set("age", 18);
        featureBuilder.set("geometry", createPoint());
        return featureBuilder.buildFeature("1");
    }

    protected Geometry createPoint() throws ParseException {
        return createGeometry("POINT(116.403847 39.915526)", 4326);
    }

    protected Geometry createLineString() throws ParseException {
        return createGeometry("LINESTRING(116.403847 39.915526,116.403847 39.915528)", 4326);
    }

    protected Geometry createPolygon() throws ParseException {
        return createGeometry("POLYGON((116.403847 39.915526,116.403847 39.915528,116.403849 39.915528,116.403849 39.915526,116.403847 39.915526))", 4326);
    }

    protected Geometry createGeometry(String wkt, int srid) throws ParseException {
        Geometry geometry = GeoTypeTransUtil.wkt2Geom(wkt);
        geometry.setSRID(srid);
        return geometry;
    }

}
