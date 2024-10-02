package com.hdmap.pointcloud.service;

import com.alibaba.fastjson.JSONObject;
import io.pdal.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes =PointCloudApplicationTest.class)
public class PointCloudApplicationTest {

    @Before
    public void contextLoads() {

    }

    @Test
    public void pdalClipTest() {
        // pipeline definition
        String json = "{\n" +
                "    \"pipeline\": [\n" +
                "        {\n" +
                "            \"filename\": \"/Users/admin/Downloads/276672099.las\",\n" +
                "            \"type\": \"readers.las\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"filters.overlay\",\n" +
                "            \"dimension\": \"Classification\",\n" +
                "            \"datasource\": \"/Users/admin/project/hdmap_server/hdmap-pointcloud/wkn7whm_1713184030943.geojson\",\n" +
                "            \"layer\": \"wkn7whm_1713184030943\",\n" +
                "            \"column\": \"ID\",\n" +
                "            \"threads\": 4\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"filters.range\",\n" +
                "            \"limits\": \"Classification['wkn7whm':'wkn7whm']\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"filename\": \"/Users/admin/project/hdmap_server/hdmap-pointcloud/wkn7whm_1713184030943.las\",\n" +
                "            \"type\": \"writers.las\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Pipeline pipeline = new Pipeline(json, LogLevel.Debug5()); // initialize and make it really noisy

        pipeline.execute(); // execute the pipeline

//        String metadata = pipeline.getMetadata(); // retrieve metadata
//
//        PointViewIterator pvs = pipeline.getPointViews(); // iterator over PointViews
//        PointView pv = pvs.next(); // let's take the first PointView
//
//        // load all points into JVM memory
//        // PointCloud provides operations on PDAL points that
//        // are loaded in this case into JVM memory as a single Array[Byte]
//        PointCloud pointCloud = pv.getPointCloud();
//        double x = pointCloud.getDouble(0, DimType.X()); // get a point with PointId = 0 and only a single dimensions
//
//        // in some cases it is not neccesary to load everything into JVM memory
//        // so it is possible to get only required points directly from the PointView
//        double y = pv.getDouble(0, DimType.Y());
//
//        // it is also possible to get access to the triangular mesh generated via PDAL
//        TriangularMesh mesh = pv.getTriangularMesh();
//        // the output is an Array of Triangles
//        // Each Triangle contains PointIds from the PDAL point table
//        Triangle[] triangles = mesh.asArray();
//
//        pv.close();
//        pvs.close();
        pipeline.close();
    }

    @Test
    public void pdalBBoxTest() {
        // pipeline definition
        String json = "[\"/Users/admin/Downloads/276672099.las\"]";
        //String json = "[\"/Users/admin/project/hdmap_server/hdmap-pointcloud/wkn7whz_1713232543350.las\"]";

        Pipeline pipeline = new Pipeline(json, LogLevel.Debug5()); // initialize and make it really noisy

        pipeline.execute(); // execute the pipeline

        String metadata = pipeline.getMetadata(); // retrieve metadata
        JSONObject jsonObject = JSONObject.parseObject(metadata);
        PointViewIterator pvs = pipeline.getPointViews(); // iterator over PointViews
        PointView pv = pvs.next(); // let's take the first PointView

        // load all points into JVM memory
        // PointCloud provides operations on PDAL points that
        // are loaded in this case into JVM memory as a single Array[Byte]
        PointCloud pointCloud = pv.getPointCloud();
        double x = pointCloud.getDouble(0, DimType.X()); // get a point with PointId = 0 and only a single dimensions

        // in some cases it is not neccesary to load everything into JVM memory
        // so it is possible to get only required points directly from the PointView
        double y = pv.getDouble(0, DimType.Y());

        // it is also possible to get access to the triangular mesh generated via PDAL
        TriangularMesh mesh = pv.getTriangularMesh();
        // the output is an Array of Triangles
        // Each Triangle contains PointIds from the PDAL point table
        Triangle[] triangles = mesh.asArray();

        pv.close();
        pvs.close();
        pipeline.close();
    }

}