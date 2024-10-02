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
@SpringBootTest(classes = {CheckLeftFunction.class})
public class CheckLeftFunctionTest extends CommonTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new CheckLeftFunction());
    }
    @Test
    public void test() throws ParseException {
        //生成left SimpleFeature
        Geometry left = createGeometry("LINESTRING (0 0, 1 1)", 4326);
        SimpleFeature leftFeature = createSimpleFeature(left);
        //生成right SimpleFeature
        Geometry right = createGeometry("LINESTRING (1 0, 1 1)", 4326);
        SimpleFeature rightFeature = createSimpleFeature(right);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("leftFeature", leftFeature);
        env.put("rightFeature", rightFeature);
        Object result = AviatorEvaluator.execute("checkLeft(rightFeature, leftFeature)", env);
        assertEquals(true, result);
        Object result2 = AviatorEvaluator.execute("checkLeft(leftFeature, rightFeature)", env);
        assertEquals(false, result2);
    }

}