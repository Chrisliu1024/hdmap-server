package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.DatastoreDetailInfo;
import com.hdmap.data.etl.model.dto.TableDetailInfo;
import com.hdmap.data.etl.service.impl.GeoDatastoreServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.csv.CSVDataStoreFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GeoDatastoreService.class, GeoDatastoreServiceImpl.class})
public class GeoDatastoreServiceTest {

    private Map<String, Object> params;

    @Autowired
    private GeoDatastoreServiceImpl geoDatastoreService;

    @Before
    public void setUp() throws Exception {
        params = getPostgisParams();
//        params = getCSVParams();
    }

    @org.junit.Test
    public void getDatastoreAndTableAndAttributeInfos() {
        DatastoreDetailInfo datastoreAndTableAndAttributeInfos = geoDatastoreService.getDatastoreAndTableAndAttributeInfos(params);
        log.info("datastoreAndTableAndAttributeInfos:{}", datastoreAndTableAndAttributeInfos);
    }

    @org.junit.Test
    public void getTableAndAttributeInfos() {
        List<TableDetailInfo> tableDetailInfoList = geoDatastoreService.getTableAndAttributeInfos(params);
        log.info("tableStructList:{}", tableDetailInfoList);
    }

    @org.junit.Test
    public void getTableNames() {
        List<String> tableNames = geoDatastoreService.getTableNames(params);
        log.info("tableNames:{}", tableNames);
    }

    private Map<String, Object> getPostgisParams() {
        Map<String, Object>  params = new HashMap<>();
        params.put("dbtype", "postgis"); //数据库类型，如postgis
        params.put("host", "10.70.28.43");   //ip地址
        params.put("port", "5432");  //端口号
        params.put("database", "ruqi_map_user_109");   //需要连接的数据库
        params.put("schema", "xlab"); //schema
        params.put("user", "postgres");   //用户名
        params.put("passwd", "hdmap"); //密码
        params.put("max connections", 64); //设置最大连接数
        params.put("min connections", 32); //设置最小连接数
        params.put("fetch size", 5000); //设置获取数据的批次大小
        //params.put("Expose primary keys", true); //设置是否将主键暴露出来
        params.put("validate connections", true); //设置是否验证连接
        params.put("Test while idle", true); //设置是否在空闲时测试连接
        return params;
    }

    private Map<String, Object> getCSVParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("file", getClass().getClassLoader().getResource("locations.csv").getFile());
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.SPECIFC_STRATEGY);
        params.put(CSVDataStoreFactory.LATFIELDP.key, "position.y");
        params.put(CSVDataStoreFactory.LnGFIELDP.key, "position.x");
        return params;
    }

}