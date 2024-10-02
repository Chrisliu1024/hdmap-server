package com.hdmap.data.etl.service.impl;

import com.hdmap.core.utils.ConvertUtil;
import com.hdmap.data.etl.enums.DatastoreTypeEnum;
import com.hdmap.data.etl.enums.FieldTypeEnum;
import com.hdmap.data.etl.enums.FieleClazzEum;
import com.hdmap.data.etl.enums.TableTypeEnum;
import com.hdmap.data.etl.model.dto.DatastoreDetailInfo;
import com.hdmap.data.etl.model.dto.TableDetailInfo;
import com.hdmap.data.etl.model.entity.FieldInfo;
import com.hdmap.data.etl.service.GeoDatastoreService;
import com.hdmap.geo.manager.PostgisManager;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.util.InternationalString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @description: 空间数据库服务实现类
 * @date 2023/7/3 18:13
 */
@Slf4j
@Service
public class GeoDatastoreServiceImpl implements GeoDatastoreService {
    @Value("${db.name.prefix:ruqi_map_user}")
    private String prefix;

    @Override
    public DatastoreDetailInfo getDatastoreAndTableAndAttributeInfos(Map<String, Object> params) {
        DataStore ds = getDataStore(params);
        DatastoreDetailInfo datastoreDetailInfo = new DatastoreDetailInfo();
        datastoreDetailInfo.setName(String.format("%s[%s]",getDataStoreName(ds), getDatastoreFlag(params)));
        datastoreDetailInfo.setType(getDatastoreType(ds));
        datastoreDetailInfo.setConnectionParams(ConvertUtil.mapToJson(params));
        // 获取表列表及属性列表的详细信息
        List<TableDetailInfo> list = new ArrayList<>();
        List<String> tableNames = getTableList(ds);
        for (String tableName : tableNames) {
            List<FieldInfo> fieldInfos = getFields(ds, tableName);
            TableDetailInfo tableDetailInfo = new TableDetailInfo();
            tableDetailInfo.setName(tableName);
            tableDetailInfo.setType(TableTypeEnum.ENTITY);
            tableDetailInfo.setFields(fieldInfos);
            list.add(tableDetailInfo);
        }
        datastoreDetailInfo.setTableDetailInfoList(list);
        return datastoreDetailInfo;
    }

    @Override
    public List<TableDetailInfo> getTableAndAttributeInfos(Map<String, Object> params) {
        List<TableDetailInfo> list = new ArrayList<>();
        DataStore ds = getDataStore(params);
        List<String> tableNames = getTableList(ds);
        for (String tableName : tableNames) {
            List<FieldInfo> fieldInfos = getFields(ds, tableName);
            TableDetailInfo tableDetailInfo = new TableDetailInfo();
            tableDetailInfo.setName(tableName);
            tableDetailInfo.setType(TableTypeEnum.ENTITY);
            tableDetailInfo.setFields(fieldInfos);
            list.add(tableDetailInfo);
        }
        return list;
    }

    @Override
    public List<String> getTableNames(Map<String, Object> params) {
        DataStore ds = getDataStore(params);
        return getTableList(ds);
    }

    /**
     * 从数据库读取表列表
     */
    private List<String> getTableList(DataStore ds) {
        String[] tableNames = null;
        try {
            tableNames = ds.getTypeNames();
        } catch (IOException e) {
            log.error("获取表列表失败\n", e);
        }
        // 转换成List
        return Arrays.asList(tableNames);
    }

    public DataStore getDataStore(Map<String, Object> params) {
        return PostgisManager.getDataStore(params);
    }

    /**
     * 从数据库读取表的属性列表
     */
    private List<FieldInfo> getFields(DataStore ds, String tableName) {
        List<FieldInfo> list = new ArrayList<>();
        SimpleFeatureSource fSource = null;
        try {
            fSource = ds.getFeatureSource(tableName);
            SimpleFeatureType schema = fSource.getSchema();
            // 遍历获取属性列表
            for (AttributeDescriptor attr : schema.getAttributeDescriptors()) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setName(attr.getLocalName());
                fieldInfo.setType(FieldTypeEnum.ENTITY);
                fieldInfo.setClazz(FieleClazzEum.getByClazz(attr.getType().getBinding()));
                InternationalString desc = attr.getType().getDescription();
                fieldInfo.setDescription(desc != null ? desc.toString() : null); // todo: get description from db
                list.add(fieldInfo);
            }

        } catch (IOException e) {
            log.error("获取表{}的结构失败\n", tableName, e);
        }
        return list;
    }


    private String getDatastoreFlag(Map<String, Object> params) {
        String[] checkKey = {"database", "file", "url"};
        for (String key : checkKey) {
            if (params.get(key) != null) {
                return params.get(key) + "";
            }
        }
        return "";
    }

    private DatastoreTypeEnum getDatastoreType(DataStore ds) {
        String desc = ds.getInfo().getDescription();
        for (DatastoreTypeEnum type : DatastoreTypeEnum.values()) {
            if (desc.toLowerCase().contains(type.getName())) {
                return type;
            }
        }
        return DatastoreTypeEnum.UNKNOW;
    }

    private String getDataStoreName(DataStore ds) {
        String[] names = ds.getInfo().getDescription().split(" ");
        return names[names.length - 1];
    }

}
