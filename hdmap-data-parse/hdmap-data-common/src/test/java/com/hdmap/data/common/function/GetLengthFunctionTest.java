package com.hdmap.data.common.function;

import com.googlecode.aviator.AviatorEvaluator;
import com.hdmap.data.common.CommonTest;
import com.hdmap.geo.utils.GeoTypeTransUtil;
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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GetLengthFunction.class})
public class GetLengthFunctionTest extends CommonTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new GetLengthFunction());
    }
    @Test
    public void testPoint() throws ParseException {
        //生成SimpleFeature
        Geometry geometry = createPoint();
        SimpleFeature feature = createSimpleFeature(geometry);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getLength(feature)", env);
        assertEquals(0.0, result);
    }

    @Test
    public void testLineString() throws ParseException {
        //生成SimpleFeature
        Geometry geometry = createLineString();
        SimpleFeature feature = createSimpleFeature(geometry);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getLength(feature)", env);
        assertEquals(0.2902757590636611, result);
    }

    @Test
    public void testPolygon() throws ParseException {
        //生成SimpleFeature
        Geometry geometry = createPolygon();
        SimpleFeature feature = createSimpleFeature(geometry);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getLength(feature)", env);
        assertEquals(1.0258294772356749, result);
    }

}