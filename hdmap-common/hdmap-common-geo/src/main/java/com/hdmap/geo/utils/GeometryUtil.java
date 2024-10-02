package com.hdmap.geo.utils;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class GeometryUtil {

    public static final int SRID_DEFAULT = 4326;
    public static final int SRID_WGS84 = 4326;
    public static final int SRID_GAUSSIAN_KRUGER = 4547;
    public static final String EPSG = "EPSG:";
    public static PrecisionModel precisionModel = new PrecisionModel();
    public static GeometryFactory gf = new GeometryFactory(precisionModel, SRID_WGS84);
    public static CoordinateReferenceSystem crs;

    private GeometryUtil() {
    }

    public static Geometry transform(Geometry geometry, int toSrid) throws FactoryException, TransformException {
        int fromSrid = geometry.getSRID();
        CoordinateReferenceSystem source = CRS.decode(EPSG + fromSrid, true);
        CoordinateReferenceSystem target = CRS.decode(EPSG + toSrid, true);
        // 建立转换
        MathTransform transform = CRS.findMathTransform(source, target, true);
        Geometry geom = JTS.transform(geometry, transform);
        geom.setSRID(toSrid);
        return geom;
    }

    public static Geometry transform(Geometry geometry, int fromSrid, int toSrid) throws FactoryException, TransformException {
        CoordinateReferenceSystem source = CRS.decode(EPSG + fromSrid, true);
        CoordinateReferenceSystem target = CRS.decode(EPSG + toSrid, true);
        // 建立转换
        MathTransform transform = CRS.findMathTransform(source, target, true);
        Geometry geom = JTS.transform(geometry, transform);
        geom.setSRID(toSrid);
        return geom;
    }

    public static ReferencedEnvelope transform(ReferencedEnvelope bbox, int fromSrid, int toSrid) throws FactoryException, TransformException {
        CoordinateReferenceSystem source = CRS.decode(EPSG + fromSrid, true);
        CoordinateReferenceSystem target = CRS.decode(EPSG + toSrid, true);
        // new ReferencedEnvelope, setting crs
        ReferencedEnvelope newBbox = new ReferencedEnvelope(bbox.getMinX(), bbox.getMaxX(), bbox.getMinY(), bbox.getMaxY(), source);
        return  newBbox.transform(target, true);
    }

    public static ReferencedEnvelope transform(ReferencedEnvelope bbox, int toSrid) throws FactoryException, TransformException {
        // get the srid of the bbox
        Set<ReferenceIdentifier> identifiers = bbox.getCoordinateReferenceSystem().getIdentifiers();
        if (identifiers.size() != 1) {
            throw new IllegalArgumentException("bbox crs is not valid");
        }
        int fromSrid = Integer.parseInt(identifiers.iterator().next().getCode());
        CoordinateReferenceSystem source = CRS.decode(EPSG + fromSrid, true);
        CoordinateReferenceSystem target = CRS.decode(EPSG + toSrid, true);
        // new ReferencedEnvelope, setting crs
        ReferencedEnvelope newBbox = new ReferencedEnvelope(bbox.getMinX(), bbox.getMaxX(), bbox.getMinY(), bbox.getMaxY(), source);
        return  newBbox.transform(target, true);
    }

    public static CoordinateReferenceSystem getCRS(String crsStr) throws FactoryException {
        // example:
        // "PROJCS[\"NAD_1983_Oregon_Statewide_Lambert_Feet_Intl\",GEOGCS[\"GCS_North_American_1983\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS_1980\",6378137,298.257222101]],PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]]],PROJECTION[\"Lambert_Conformal_Conic_2SP\"],PARAMETER[\"latitude_of_origin\",41.75],PARAMETER[\"central_meridian\",-120.5],PARAMETER[\"standard_parallel_1\",43],PARAMETER[\"standard_parallel_2\",45.5],PARAMETER[\"false_easting\",400000],PARAMETER[\"false_northing\",0],UNIT[\"foot\",0.3048,AUTHORITY[\"EPSG\",\"9002\"]],AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH]]",
        return CRS.parseWKT(crsStr);
    }

    public static int getDimension(Geometry g) {
        // 有些情况下，即使是三维的，z值也可能是0
        CoordinateSequence seq = findSampleSequence(g);
        return seq.getDimension();
    }

    private static CoordinateSequence findSampleSequence(Geometry g) {
        if (g == null || g.isEmpty()) {
            return null;
        }
        if (g instanceof GeometryCollection) {
            GeometryCollection col = (GeometryCollection) g;
            return findSampleSequence(col.getGeometryN(0));
        }
        if (g instanceof Point) {
            Point point = (Point) g;
            return point.getCoordinateSequence();
        }
        if (g instanceof LineString) {
            LineString line = (LineString) g;
            return line.getCoordinateSequence();
        }
        if (g instanceof Polygon) {
            Polygon poly = (Polygon) g;
            return findSampleSequence(poly.getExteriorRing());
        }
        return null;
    }

    public static String toText(Geometry g) {
        int dim = getDimension(g);
        WKTWriter writer = new WKTWriter(dim);
        return writer.write(g);
    }

    public static Geometry to3DGeometry(Geometry g) {
        if (g == null || g.isEmpty() || g.getCoordinates().length == 0) {
            return g;
        }
        // check whether the z value of geometry is null
        if (!Double.isNaN(g.getCoordinate().getZ())) {
            return g;
        }
        // if z value is null, set 0 to z value
        Coordinate[] coordinates = g.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            coordinate.setZ(0);
        }
        return g;
    }

    public static ReferencedEnvelope getReferencedEnvelope(Geometry geom) {
        ReferencedEnvelope bbox = new ReferencedEnvelope();
        bbox.init(geom.getEnvelopeInternal());
        return bbox;
    }

    public static String getGeometryFieldName(SimpleFeatureCollection collection) {
        return getGeometryFieldName(collection.getSchema());
    }

    public static String getGeometryFieldName(SimpleFeature feature) {
        return getGeometryFieldName(feature.getFeatureType());
    }

    public static String getGeometryFieldName(SimpleFeatureType schema) {
        return schema.getGeometryDescriptor().getLocalName();
    }

    public static ReferencedEnvelope createBBox(double minX, double minY, double maxX, double maxY, CoordinateReferenceSystem crs) {
        return new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
    }

    public static ReferencedEnvelope createBBox(double minX, double minY, double maxX, double maxY) throws FactoryException {
        CoordinateReferenceSystem crs = CRS.decode(EPSG + SRID_DEFAULT, true);
        return new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
    }

    public static Polygon createBBoxPolygon(double minX, double minY, double maxX, double maxY, CoordinateReferenceSystem crs) {
        Coordinate[] coordinates = new Coordinate[5];
        coordinates[0] = new Coordinate(minX, minY);
        coordinates[1] = new Coordinate(maxX, minY);
        coordinates[2] = new Coordinate(maxX, maxY);
        coordinates[3] = new Coordinate(minX, maxY);
        coordinates[4] = new Coordinate(minX, minY);
        Polygon polygon = gf.createPolygon(coordinates);
        polygon.setSRID(getSrid(crs));
        return polygon;
    }

    public static Polygon createPolygon(List<Coordinate> coordinates, int srid) {
        return createPolygon(coordinates.toArray(new Coordinate[0]), srid);
    }

    public static Polygon createPolygon(Coordinate[] coordinates, int srid) {
        if (coordinates.length < 3) {
            throw new IllegalArgumentException("coordinates length must be equal or greater than 3");
        }
        Polygon polygon = gf.createPolygon(coordinates);
        polygon.setSRID(srid);
        return polygon;
    }

    public static int getSrid(CoordinateReferenceSystem crs) {
        Iterator<ReferenceIdentifier> iterator = crs.getIdentifiers().iterator();
        if (iterator.hasNext()) {
            return Integer.parseInt(iterator.next().getCode());
        }
        return 0;
    }

    public static List<Coordinate> getNearestNCoordinates(Geometry geom, List<Coordinate> targets, int n) {
        Coordinate centroid = geom.getCentroid().getCoordinate();
        return targets.stream()
                .sorted((c1, c2) -> Double.compare(c1.distance(centroid), c2.distance(centroid)))
                .limit(n)
                .collect(Collectors.toList());
    }

}
