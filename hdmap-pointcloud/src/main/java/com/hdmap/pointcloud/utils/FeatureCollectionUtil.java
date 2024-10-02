package com.hdmap.pointcloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.csv.CSVDataStore;
import org.geotools.data.csv.CSVDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/23 14:08
 * @description: 要素集合工具类
 */
@Slf4j
public class FeatureCollectionUtil {
    private FeatureCollectionUtil() {
    }

    // SimpleFeatureCollection -> GeoJSON（文件）
    public static void features2GeoJsonFile(SimpleFeature feature, String path) throws IOException {
        List<SimpleFeature> features = new ArrayList<>();
        features.add(feature);
        features2GeoJsonFile(features, path);
    }

    public static void features2GeoJsonFile(List<SimpleFeature> list, String path) throws IOException {
        if (list == null || list.isEmpty()) {
            log.warn("features is empty");
            return;
        }
        SimpleFeatureCollection collection = new ListFeatureCollection(list.get(0).getFeatureType(), list);
        features2GeoJsonFile(collection, path);
    }

    public static void features2GeoJsonFile(FeatureCollection collection, String path) throws IOException {
        FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(8));// 避免精度丢失
        featureJSON.writeFeatureCollection(collection, path);
    }

    public static void features2Csv(List<SimpleFeature> list, String path) throws IOException {
        if (list == null || list.isEmpty()) {
            log.warn("features is empty");
            return;
        }
        SimpleFeatureCollection collection = new ListFeatureCollection(list.get(0).getFeatureType(), list);
        features2Csv(collection, path);
    }

    public static void features2Csv1(SimpleFeatureCollection collection, String path) throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("file", path);
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params.put(CSVDataStoreFactory.LATFIELDP.key, "latitude");
        params.put(CSVDataStoreFactory.LnGFIELDP.key, "longitude");

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore datastore = factory.createNewDataStore(params);
        SimpleFeatureType featureType = collection.getSchema();

        datastore.createSchema(featureType);
        SimpleFeatureSource source = ((CSVDataStore)datastore).getFeatureSource();
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;
            outStore.addFeatures(collection);
        }
    }

    public static void features2Csv(SimpleFeatureCollection collection, String path) throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("file", path);
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.ATTRIBUTES_ONLY_STRATEGY);

        CSVDataStoreFactory factory = new CSVDataStoreFactory();
        DataStore datastore = factory.createNewDataStore(params);

        // Get the original feature type
        SimpleFeatureType originalFeatureType = collection.getSchema();

        // Create a new feature type builder
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        // Set the name and CRS (if any) of the new feature type
        builder.setName(originalFeatureType.getName());
        builder.setCRS(originalFeatureType.getCoordinateReferenceSystem());

        // Add the rest of the attributes, skipping the geometry attribute
        for (AttributeDescriptor descriptor : originalFeatureType.getAttributeDescriptors()) {
            if (!"location".equals(descriptor.getLocalName())) {
                builder.add(descriptor);
            } else {
                builder.add("latitude", Double.class);
                builder.add("longitude", Double.class);
            }
        }

        // Build the new feature type
        SimpleFeatureType newFeatureType = builder.buildFeatureType();

        // Create the new schema in the datastore
        datastore.createSchema(newFeatureType);

        // Get the feature source
        SimpleFeatureSource source = ((CSVDataStore)datastore).getFeatureSource();

        // If the source is a feature store, add the features
        if (source instanceof SimpleFeatureStore) {
            SimpleFeatureStore outStore = (SimpleFeatureStore) source;

            // Create a new collection with the new feature type
            List<SimpleFeature> featureList = new ArrayList<>();
            SimpleFeatureIterator iterator = collection.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(newFeatureType);
                featureBuilder.addAll(feature.getAttributes());
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry != null) {
                    Point point = geometry.getCentroid();
                    featureBuilder.set("latitude", point.getY());
                    featureBuilder.set("longitude", point.getX());
                }
                featureList.add(featureBuilder.buildFeature(feature.getID()));
            }
            iterator.close();
            SimpleFeatureCollection newCollection = new ListFeatureCollection(newFeatureType, featureList);

            // Add the features to the store
            outStore.addFeatures(newCollection);
        }
    }
}
