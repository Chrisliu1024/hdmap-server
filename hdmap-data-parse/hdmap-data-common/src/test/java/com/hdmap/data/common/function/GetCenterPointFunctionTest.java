package com.hdmap.data.common.function;

import com.googlecode.aviator.AviatorEvaluator;
import com.hdmap.data.common.CommonTest;
import lombok.extern.slf4j.Slf4j;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.hdmap.geo.utils.GeoTypeTransUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GetCenterPointFunction.class})
public class GetCenterPointFunctionTest extends CommonTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new GetCenterPointFunction());
    }
    @Test
    public void testPoint() throws ParseException {
        //生成SimpleFeature
        Geometry geometry = createPoint();
        SimpleFeature feature = createSimpleFeature(geometry);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getCenterPoint(feature)", env);
        assertEquals("POINT (116.403847 39.915526)", result.toString());
    }

    @Test
    public void testLineString() throws ParseException {
        //生成SimpleFeature
        Geometry geometry = createLineString();
        SimpleFeature feature = createSimpleFeature(geometry);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getCenterPoint(feature)", env);
        assertEquals("POINT (116.403847 39.915527)", result.toString());
    }

    @Test
    public void testPolygon() throws ParseException {
        //生成SimpleFeature
        Geometry geometry = createPolygon();
        SimpleFeature feature = createSimpleFeature(geometry);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getCenterPoint(feature)", env);
        assertEquals("POINT (116.40384800000001 39.915527000000004)", result.toString());
    }

}