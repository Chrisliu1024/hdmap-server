package com.hdmap.geo.manager;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @version 1.0
 * @description: PostGIS数据库中读取数据
 * @date 2023/7/11 10:48
 */
@Slf4j
public class PostgisManager {

    private PostgisManager() {
    }

    public static final Map<String, DataStore> dbNameDataStoreMap = new ConcurrentHashMap<>();

    public static DataStore getDataStore(Map<String, Object> params) {
        String keyName = getKeyName(params);
        // 双重检查锁获取dataStore
        DataStore dataStore = dbNameDataStoreMap.get(keyName);
        if (dataStore == null) {
            synchronized (dbNameDataStoreMap) {
                dataStore = dbNameDataStoreMap.get(keyName);
                if (dataStore == null) {
                    dataStore = connDbAndCache(params);
                }
            }
        }
        return dataStore;
    }

    public static void removeDataStore(Map<String, Object> params) {
        String keyName = getKeyName(params);
        // 双重检查锁获取dataStore
        DataStore dataStore = dbNameDataStoreMap.remove(keyName);
        if (dataStore != null) {
            dataStore.dispose();
        }
    }

    public static SimpleFeatureCollection getCollection(Map<String, Object> params, String tableName) {
        SimpleFeatureCollection fcollection = null;
        try {
            DataStore dataStore = getDataStore(params);
            //根据表名获取source
            SimpleFeatureSource fSource = dataStore.getFeatureSource(tableName);
            fcollection = fSource.getFeatures();
        } catch (IOException e) {
            log.error("获取表{}的数据失败\n", tableName, e);
        }
        return fcollection;
    }

    /**
     * 条件查询
     */
    public static SimpleFeatureCollection getCollection(Map<String, Object> params, String tableName, String filter) {
        SimpleFeatureCollection fcollection = null;
        try {
            DataStore dataStore = getDataStore(params);
            //根据表名获取source
            SimpleFeatureSource fSource = dataStore.getFeatureSource(tableName);
            fcollection = fSource.getFeatures(ECQL.toFilter(filter));
        } catch (IOException | CQLException e) {
            log.error("获取表{}的数据失败\n", tableName, e);
        }
        return fcollection;
    }

    private static DataStore connDbAndCache(Map<String, Object> params) {
        DataStore dataStore = connDb(params);
        if (dataStore != null) {
            String keyName = getKeyName(params);
            dbNameDataStoreMap.put(keyName, dataStore);
        }
        return dataStore;
    }

    // 清空表
    public static void truncate(Map<String, Object> params, String tableName) throws SQLException {
        String host = (String) params.get("host");
        String port = (String) params.get("port");
        String dbName = (String) params.get("database");
        String schema = (String) params.get("schema");
        String username = (String) params.get("user");
        String password = (String) params.get("passwd");
        String newUrl = getDbSchemaConnUrl(host, port, dbName, schema);
        try(// 链接数据库
            Connection conn = DriverManager.getConnection(newUrl, username, password);
            // 用于执行静态SQL语句并返回其产生的结果的对象
            Statement smt = conn.createStatement()) {
            String sql = String.format("TRUNCATE TABLE %s.%s", schema, tableName);
            smt.execute(sql);
        }
    }

    // 逐条入库
    public static void saveToDb(SimpleFeatureCollection collection, DataStore dataStore, String tableName) throws InterruptedException, IOException {
        // 判断dataStore是否支持写入
        boolean isWritable = isDataStoreWritable(dataStore, tableName);
        if (!isWritable) {
            // 等待一段时间后重试
            String description = dataStore.getInfo().getDescription();
            log.warn("dataStore{}不支持写入，等待0.2s后重试", description);
            Thread.sleep(200);
            saveToDb(collection, dataStore, tableName);
        }
        try (FeatureIterator<SimpleFeature> iterator = collection.features();
             FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = dataStore.getFeatureWriterAppend(tableName, Transaction.AUTO_COMMIT)) {
            List<AttributeType> attributeTypes = featureWriter.getFeatureType().getTypes();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                SimpleFeature simpleFeature = featureWriter.next();
                for (AttributeType attributeType : attributeTypes) {
                    String attributeName = attributeType.getName().toString();
                    simpleFeature.setAttribute(attributeName, feature.getAttribute(attributeName));
                }
                featureWriter.write();
            }
        } catch (IOException e) {
            log.error("获取/写入{}的FeatureWriter失败", tableName, e);
            throw e;
        }
    }

    // 批量入库
    public static void saveToDbBatch(SimpleFeatureCollection collection, DataStore dataStore, String tableName) throws InterruptedException {
        // 判断dataStore是否支持写入
        boolean isWritable = isDataStoreWritable(dataStore, tableName);
        if (!isWritable) {
            // 等待一段时间后重试
            String description = dataStore.getInfo().getDescription();
            log.warn("dataStore{}不支持写入，等待0.2s后重试", description);
            Thread.sleep(200);
            saveToDbBatch(collection, dataStore, tableName);
        }
        Transaction t = new DefaultTransaction("transactoin-saveToDbBatch");
        SimpleFeatureStore featureStore = null;
        try {
            featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(tableName);
            featureStore.setTransaction(t);
            List<SimpleFeature> list = new ArrayList<>(collection.size());
            SimpleFeatureType schema = featureStore.getSchema();
            List<AttributeType> attributeTypes = schema.getTypes();
            try(FeatureIterator<SimpleFeature> iterator = collection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    // 遍历SimpleFeatureType创建新SimpleFeature
                    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
                    for (AttributeType attributeType : attributeTypes) {
                        String attributeName = attributeType.getName().toString();
                        featureBuilder.set(attributeName, feature.getAttribute(attributeName));
                    }
                    SimpleFeature newFeature = featureBuilder.buildFeature(null);
                    list.add(newFeature);
                }
            }
            SimpleFeatureCollection newCollection = new ListFeatureCollection(schema, list);
            featureStore.addFeatures(newCollection);
            // commit transaction
            t.commit();
            t.close();
        } catch (IOException e) {
            log.error("获取/写入{}的SimpleFeatureStore失败", tableName, e);
        }
    }

    public static boolean isDataStoreWritable(DataStore dataStore, String tableName) {
        try {
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(tableName);
            return featureSource instanceof FeatureStore;
        } catch (IOException e) {
            // Handle exception
            log.error("获取{}的FeatureSource失败", tableName, e);
        }
        return false;
    }

    private static String getDbSchemaConnUrl(String host, String port, String dbName, String schema) {
        return String.format("jdbc:postgresql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&currentSchema=%s&stringtype=unspecified",
                host, port, dbName, schema);
    }

    private static synchronized DataStore connDb(Map<String, Object> params) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("dbtype", dbtype); //数据库类型，如postgis
//        params.put("host", host);   //ip地址
//        params.put("port", port);  //端口号
//        params.put("database", database);   //需要连接的数据库
//        params.put("schema", schema); //schema
//        params.put("user", userName);   //用户名
//        params.put("passwd", password); //密码
//        params.put("max connections", 64); //设置最大连接数
//        params.put("min connections", 32); //设置最小连接数
//        params.put("fetch size", 5000); //设置获取数据的批次大小
//        //params.put("Expose primary keys", true); //设置是否将主键暴露出来
//        params.put("validate connections", true); //设置是否验证连接
//        params.put("Test while idle", true); //设置是否在空闲时测试连接

        try {
            //获取存储空间
            DataStore ds = DataStoreFinder.getDataStore(params);
            if (ds != null) {
                log.info("连接到位于：{}的空间数据库{}成功", params.get("host"), params.get("database"));
                return ds;
            } else {
                log.error("连接到位于：{}的空间数据库{}失败。请检查相关参数", params.get("host"), params.get("database"));
            }
        } catch (IOException e) {
            log.error("连接到位于：{}的空间数据库{}失败。请检查相关参数\n", params.get("host"), params.get("database"), e);
        }
        return null;
    }

    private static String getKeyName(Map<String, Object> params) {
        String dbName = (String) params.get("database");
        String schema = (String) params.get("schema");
        return String.format("%s_%s", dbName, schema);
    }

}
