package com.hdmap.data.common.function;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@SpringBootTest(classes = {GetRandomIdFunction.class})
public class GetRandomIdFunctionTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new GetRandomIdFunction());
    }

    @Test
    public void test() {
        int count = 10;
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("count", count);
        Object result = AviatorEvaluator.execute("getRandomId(count)", env);
        assertEquals(count, result.toString().length());
    }

}