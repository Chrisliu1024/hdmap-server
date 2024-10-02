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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {GetAttributeFunction.class})
public class GetAttributeFunctionTest extends CommonTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new GetAttributeFunction());
    }

    @Test
    public void test() throws ParseException {
        //生成SimpleFeature
        SimpleFeature feature = createSimpleFeature();
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("feature", feature);
        Object result = AviatorEvaluator.execute("getAttribute(feature, 'name')", env);
        assertEquals("zhangsan", result);
    }

}