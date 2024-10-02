package com.hdmap.data.ibd.manager;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/1 11:17
 * @description: Spatialite数据库中读取数据
 */
@Slf4j
@Component
@RefreshScope
public class SpatialiteManager {

    @Value("${db.spatialite.cacheSize: 1000}")
    private int maximumSize;
    public final Map<String, DataStore> pathDataStoreMap = new LinkedHashMap<String, DataStore>() {
        private static final long serialVersionUID = 1L;
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, DataStore> eldest) {
            return size() > maximumSize;
        }
    };

    public DataStore getDataStore(String filePath) {
        // 计算文件md5码
        //String md5 = Md5Util.getFileMD5(filePath);
        // 双重检查锁获取dataStore
        DataStore dataStore = pathDataStoreMap.get(filePath);
        if (dataStore == null) {
            synchronized (pathDataStoreMap) {
                dataStore = pathDataStoreMap.get(filePath);
                if (dataStore == null) {
                    dataStore = connDbAndCache(filePath);
                }
            }
        }
        return dataStore;
    }

    public void removeDataStore(String filePath) throws IOException {
        // 计算文件md5码
        //String md5 = Md5Util.getFileMD5(filePath);
        DataStore dataStore = pathDataStoreMap.remove(filePath);
        if (dataStore != null) {
            // 关闭
            dataStore.dispose();
        }
    }

    public SimpleFeatureCollection getCollection(String filePath, String tableName) throws IOException {
        DataStore dataStore = getDataStore(filePath);
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(tableName);
        return featureSource.getFeatures();
    }

    /**
     * 按条件查询
     */
    public SimpleFeatureCollection getCollection(String filePath, String tableName, String filterSql) throws CQLException, IOException {
        DataStore dataStore = getDataStore(filePath);
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(tableName);
        return featureSource.getFeatures(ECQL.toFilter(filterSql));
    }

    private DataStore connDbAndCache(String filePath) {
        // 连接数据库
        DataStore dataStore = connDb(filePath);
        if (dataStore != null) {
            pathDataStoreMap.put(filePath, dataStore);
        }
        return dataStore;
    }

    private synchronized DataStore connDb(String filePath) {
        // 设置连接参数
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "spatialite");
        params.put("database", filePath);
        params.put("read-only", true);
        params.put("fetch size", 5000); //设置获取数据的批次大小
        params.put("Expose primary keys", true);  // 暴露主键

        try {
            //获取存储空间
            DataStore ds = DataStoreFinder.getDataStore(params);
            if (ds != null) {
                log.info("连接到位于：{}的空间数据库成功", filePath);
                return ds;
            } else {
                log.error("连接到位于：{}的空间数据库失败。请检查相关参数", filePath);
            }
        } catch (IOException e) {
            log.error("连接到位于：{}的空间数据库失败。请检查相关参数\n", filePath, e);
        }
        return null;
    }
}
