package com.hdmap.data.ibd.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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
@Component
@RefreshScope
public class PostgisManager {

    @Value("${password.dbtype:postgis}")
    private String dbtype;
    @Value("${dbconfig.host:10.10.30.43}")
    private String host;
    @Value("${dbconfig.port:5432}")
    private String port;
//    @Value("${db.postgis.schema:public}")
//    private String schema;
    @Value("${dbconfig.username:postgres}")
    private String username;
    @Value("${dbconfig.password:hdmap}")
    private String password;

    public final Map<String, DataStore> dbNameDataStoreMap = new ConcurrentHashMap<>();

    public String getDataStoreName(String userId, String prefix) {
        if (StringUtils.isBlank(userId)) {
            return prefix;
        }
        return String.format("%s_%s", prefix, userId);
    }

    public DataStore getDataStore(String dbName, String schema) {
        String keyName = getKeyName(dbName, schema);
        // 双重检查锁获取dataStore
        DataStore dataStore = dbNameDataStoreMap.get(keyName);
        if (dataStore == null) {
            synchronized (dbNameDataStoreMap) {
                dataStore = dbNameDataStoreMap.get(keyName);
                if (dataStore == null) {
                    dataStore = connDbAndCache(dbtype, host, port, dbName, schema, username, password);
                }
            }
        }
        return dataStore;
    }

    public void removeDataStore(String dbName, String schema) {
        String keyName = getKeyName(dbName, schema);
        // 双重检查锁获取dataStore
        DataStore dataStore = dbNameDataStoreMap.remove(keyName);
        if (dataStore != null) {
            dataStore.dispose();
        }
    }

    public SimpleFeatureCollection getCollection(String dbName, String schema, String tableName) {
        SimpleFeatureCollection fcollection = null;
        try {
            DataStore dataStore = getDataStore(dbName, schema);
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
    public SimpleFeatureCollection getCollection(String dbName, String schema, String tableName, String filterSql) {
        SimpleFeatureCollection fcollection = null;
        try {
            DataStore dataStore = getDataStore(dbName, schema);
            //根据表名获取source
            SimpleFeatureSource fSource = dataStore.getFeatureSource(tableName);
            fcollection = fSource.getFeatures(ECQL.toFilter(filterSql));
        } catch (IOException | CQLException e) {
            log.error("获取表{}的数据失败\n", tableName, e);
        }
        return fcollection;
    }

    private DataStore connDbAndCache(String dbtype, String host, String port,
                                     String dbName, String schema, String userName, String password) {
        DataStore dataStore = connDb(dbtype, host, port, dbName, schema, userName, password);
        if (dataStore != null) {
            String keyName = getKeyName(dbName, schema);
            dbNameDataStoreMap.put(keyName, dataStore);
        }
        return dataStore;
    }

    private synchronized DataStore connDb(String dbtype, String host, String port,
                             String database, String schema, String userName, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", dbtype); //数据库类型，如postgis
        params.put("host", host);   //ip地址
        params.put("port", port);  //端口号
        params.put("database", database);   //需要连接的数据库
        params.put("schema", schema); //schema
        params.put("user", userName);   //用户名
        params.put("passwd", password); //密码
        params.put("max connections", 64); //设置最大连接数
        params.put("min connections", 32); //设置最小连接数
        params.put("fetch size", 5000); //设置获取数据的批次大小
        //params.put("Expose primary keys", true); //设置是否将主键暴露出来
        params.put("validate connections", true); //设置是否验证连接
        params.put("Test while idle", true); //设置是否在空闲时测试连接

        try {
            //获取存储空间
            DataStore ds = DataStoreFinder.getDataStore(params);
            if (ds != null) {
                log.info("连接到位于：{}的空间数据库{}成功", host, database);
                return ds;
            } else {
                log.error("连接到位于：{}的空间数据库{}失败。请检查相关参数", host, database);
            }
        } catch (IOException e) {
            log.error("连接到位于：{}的空间数据库{}失败。请检查相关参数\n", host, database, e);
        }
        return null;
    }

    private String getKeyName(String dbName, String schema) {
        return String.format("%s_%s", dbName, schema);
    }

    // 清空表
    public void truncate(String dbName, String schema, String tableName) throws SQLException {
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
    public void saveToDb(SimpleFeatureCollection collection, DataStore dataStore, String tableName) throws InterruptedException, IOException {
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
    public void saveToDbBatch(SimpleFeatureCollection collection, DataStore dataStore, String tableName) throws InterruptedException {
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

    public boolean isDataStoreWritable(DataStore dataStore, String tableName) {
        try {
            FeatureSource featureSource = dataStore.getFeatureSource(tableName);
            return featureSource instanceof FeatureStore;
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
        return false;
    }

    private String getDbSchemaConnUrl(String host, String port, String dbName, String schema) {
        return String.format("jdbc:postgresql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&currentSchema=%s&stringtype=unspecified",
                host, port, dbName, schema);
    }

}
