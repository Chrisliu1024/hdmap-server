package com.hdmap.data.common.filter;

import com.googlecode.aviator.AviatorEvaluator;
import com.hdmap.data.common.CommonTest;
import com.hdmap.data.common.function.GetCenterPointFunction;
import com.hdmap.data.common.function.GetLengthFunction;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MaxFilter.class})
public class MaxFilterTest extends CommonTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new MaxFilter());
    }
    @Test
    public void test() throws ParseException {
        //生成SimpleFeature
        SimpleFeature feature1 = createSimpleFeature(18);
        SimpleFeature feature2 = createSimpleFeature(20);
        SimpleFeature feature3 = createSimpleFeature(22);
        List<SimpleFeature> featureList = new ArrayList<>();
        featureList.add(feature1);
        featureList.add(feature2);
        featureList.add(feature3);
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("features", featureList);
        Object result = AviatorEvaluator.execute("maxFilter(features, 'age')", env);
        assertEquals(22.0, result);
    }

}