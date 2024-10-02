package com.hdmap.geo.model;

import com.hdmap.geo.utils.GeometryUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/15 15:14
 * @description: 网格信息（geohash）
 */
@Data
@AllArgsConstructor
public class Grid {
    private String identifier;
    private short classification;
    private Geometry geometry;

    public SimpleFeature toFeature() throws FactoryException {
        // create feature
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Feature");
        builder.setCRS(CRS.decode("EPSG:" + GeometryUtil.SRID_GAUSSIAN_KRUGER));
        builder.add("classification", Short.class);
        builder.add("geom", Polygon.class);
        SimpleFeatureType type = builder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        featureBuilder.add(classification);
        featureBuilder.add(geometry);
        return featureBuilder.buildFeature(null);
    }

}
