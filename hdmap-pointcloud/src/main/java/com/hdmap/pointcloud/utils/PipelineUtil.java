package com.hdmap.pointcloud.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.hdmap.pointcloud.annotation.CallTime;
import com.hdmap.geo.utils.GeometryUtil;
import io.pdal.LogLevel;
import io.pdal.Pipeline;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import scala.Enumeration;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/15 16:01
 * @description: PDAL工具类
 */
@Slf4j
public class PipelineUtil {

    private static int SRID_DEFAULT = 4547;
    private static final String DIMENSION = "PointSourceId";
    private static final int THREAD_NUM = 4;
    private static final Enumeration.Value LOG_LEVEL = LogLevel.Info();
    private PipelineUtil() {
    }

    public static void setDefaultSrid(int srid) {
        SRID_DEFAULT = srid;
    }

    public static String getMetadataJson(String path) {
        return String.format("[\"%s\"]", path);
    }

    public static String getOverlayAndRangeSettingJson(String inputPath, String outputPath, String geoJsonPath, String filterFieldName, short filterValue) {
        // get file name from path
        String jsonFileName = geoJsonPath.substring(geoJsonPath.lastIndexOf("/") + 1, geoJsonPath.lastIndexOf("."));
        // pipeline definition
        return String.format("[\n" +
                "    \"%s\",\n" +
                "    {\n" +
                "      \"type\":\"filters.overlay\",\n" +
                "      \"dimension\":\"%s\",\n" +
                "      \"datasource\":\"%s\",\n" +
                "      \"layer\":\"%s\",\n" +
                "      \"column\":\"%s\",\n" +
                "      \"threads\": %s\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\":\"filters.range\",\n" +
                "      \"limits\":\"PointSourceId[%s:%s]\"\n" +
                "    },\n" +
                "    \"%s\"\n" +
                "]", inputPath, DIMENSION, geoJsonPath, jsonFileName, filterFieldName, THREAD_NUM, filterValue, filterValue, outputPath);
    }

    public static String getOverlaySettingJson(String inputPath, String outputPath, String geoJsonPath, String filterFieldName) {
        // get file name from path
        String jsonFileName = geoJsonPath.substring(geoJsonPath.lastIndexOf("/") + 1, geoJsonPath.lastIndexOf("."));
        // pipeline definition
        return String.format("[\n" +
                "    \"%s\",\n" +
                "    {\n" +
                "      \"type\":\"filters.overlay\",\n" +
                "      \"dimension\":\"%s\",\n" +
                "      \"datasource\":\"%s\",\n" +
                "      \"layer\":\"%s\",\n" +
                "      \"column\":\"%s\",\n" +
                "      \"threads\": %s\n" +
                "    },\n" +
                "    \"%s\"\n" +
                "]", inputPath, DIMENSION, geoJsonPath, jsonFileName, filterFieldName, THREAD_NUM, outputPath);
    }

    public static String getRangeSettingJson(String inputPath, String outputPath, short filterValue) {
        // pipeline definition
        return String.format("[\n" +
                "    \"%s\",\n" +
                "    {\n" +
                "      \"type\":\"filters.range\",\n" +
                "      \"limits\":\"PointSourceId[%s:%s]\"\n" +
                "    },\n" +
                "    \"%s\"\n" +
                "]", inputPath,  filterValue, filterValue, outputPath);
    }

    public static String getMergeSettingJson(List<String> inputPaths, String outputPath) {
        // pipeline definition
        StringBuilder sb = new StringBuilder("[\n");
        for (String inputPath : inputPaths) {
            sb.append(String.format("    \"%s\",\n", inputPath));
        }
        sb.append("    {\n" +
                "      \"type\":\"filters.merge\"\n" +
                "    },\n");
        sb.append(String.format("    \"%s\"\n", outputPath));
        sb.append("]");
        return sb.toString();
    }
    public static ReferencedEnvelope getBBoxByMetadata(String metadata) throws FactoryException {
        // String -> JSONObject
        JSONObject jsonObject = JSONObject.parseObject(metadata);
        // JSONObject -> Geometry
        JSONObject info = jsonObject.getJSONObject("metadata").getJSONObject("readers.las");
        BigDecimal minX = info.getBigDecimal("minx");
        BigDecimal minY = info.getBigDecimal("miny");
        BigDecimal maxX = info.getBigDecimal("maxx");
        BigDecimal maxY = info.getBigDecimal("maxy");
        // get the crs
        String crsStr = info.getString("comp_spatialreference");
        // comp_spatialreference = "PROJCS[\"WGS 84 / UTM zone 33N\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",15],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",500000],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH],AUTHORITY[\"EPSG\",\"32633\"]]";
        // generate the crs
        CoordinateReferenceSystem crs = CRS.decode("EPSG:" + SRID_DEFAULT, true);
        if (!crsStr.isEmpty()) {
            crs = GeometryUtil.getCRS(crsStr);
        }
        // Polygon
        return GeometryUtil.createBBox(minX.doubleValue(), minY.doubleValue(), maxX.doubleValue(), maxY.doubleValue(), crs);
    }

    public static ReferencedEnvelope getBBoxByPath(String path) throws FactoryException {
        // pipeline definition
        String json = getMetadataJson(path);
        // initialize and make it really noisy
        try (Pipeline pipeline = new Pipeline(json, LOG_LEVEL)) {
            // execute the pipeline
            pipeline.execute();
            // retrieve metadata
            String metadata = pipeline.getMetadata();
            return getBBoxByMetadata(metadata);
        }
    }

    public static Integer getPointCountByMetadata(String metadata) {
        // String -> JSONObject
        JSONObject jsonObject = JSONObject.parseObject(metadata);
        // JSONObject -> Geometry
        JSONObject info = jsonObject.getJSONObject("metadata").getJSONObject("readers.las");
        return info.getInteger("count");
    }

    public static Integer getPointCountByPath(String path) {
        // pipeline definition
        String json = getMetadataJson(path);
        // initialize and make it really noisy
        try (Pipeline pipeline = new Pipeline(json, LOG_LEVEL)) {
            // execute the pipeline
            pipeline.execute();
            // retrieve metadata
            String metadata = pipeline.getMetadata();
            return getPointCountByMetadata(metadata);
        }
    }

    @CallTime
    public static Boolean overlayAndRange(String inputPath, String outputPath, String jsonPath, String filterFieldName, short filterValue) {
        // pipeline definition
        String json = getOverlayAndRangeSettingJson(inputPath, outputPath, jsonPath, filterFieldName, filterValue);
        // initialize and make it really noisy
        try (Pipeline pipeline = new Pipeline(json, LOG_LEVEL)) {
            // execute the pipeline
            pipeline.execute();
        }
        // check the output file whether contains points
        if (getPointCountByPath(outputPath) == 0) {
            // delete the empty file
            FileUtil.del(outputPath);
            // print log
            log.warn("The output las file is empty, contains 0 point. jsonPath: {}", jsonPath);
            return false;
        }
        return true;
    }

    @CallTime
    public static Boolean overlay(String inputPath, String outputPath, String geoJsonPath, String filterFieldName) {
        // pipeline definition
        String json = getOverlaySettingJson(inputPath, outputPath, geoJsonPath, filterFieldName);
        // initialize and make it really noisy
        try (Pipeline pipeline = new Pipeline(json, LOG_LEVEL)) {
            // execute the pipeline
            pipeline.execute();
        }
        return true;
    }

    @CallTime
    public static Boolean range(String inputPath, String outputPath, short filterValue) {
        // pipeline definition
        String json = getRangeSettingJson(inputPath, outputPath, filterValue);
        // initialize and make it really noisy
        try (Pipeline pipeline = new Pipeline(json, LOG_LEVEL)) {
            // execute the pipeline
            pipeline.execute();
        }
        // check the output file whether contains points
        if (getPointCountByPath(outputPath) == 0) {
            // delete the empty file
            FileUtil.del(outputPath);
            // print log
            log.warn("The output las file is empty, contains 0 point. classifyValue: {}", filterValue);
            return false;
        }
        return true;
    }

    @CallTime
    public static String merge(List<String> inputPaths, String outputPath) {
        // check the exist of base path
        String bashDirectory = outputPath.substring(0, outputPath.lastIndexOf("/"));
        FileUtil.mkdir(bashDirectory);
        // pipeline definition
        String json = getMergeSettingJson(inputPaths, outputPath);
        // initialize and make it really noisy
        try (Pipeline pipeline = new Pipeline(json, LOG_LEVEL)) {
            // execute the pipeline
            pipeline.execute();
        }
        return outputPath;
    }

}
