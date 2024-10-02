package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.DatastoreDetailInfo;
import com.hdmap.data.etl.model.dto.TableDetailInfo;

import java.util.List;
import java.util.Map;

public interface GeoDatastoreService {

    /**
     * 从数据库读取用户对应的数据源和数据源的表和表的属性列表
     */
    DatastoreDetailInfo getDatastoreAndTableAndAttributeInfos(Map<String, Object> params);

    /**
     * 从数据库读取用户对应的表和表的属性列表
     */
    List<TableDetailInfo> getTableAndAttributeInfos(Map<String, Object> params);

    /**
     * 获取用于对应的数据库的表名
     */
    List<String> getTableNames(Map<String, Object> params);

}
