package com.hdmap.data.common.function;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@Slf4j
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MappingFunction.class})
public class MappingFunctionTest {

    @Before
    public void contextLoads() {
        //注册函数
        AviatorEvaluator.addFunction(new MappingFunction());
    }

    @Test
    public void test() {
        //生成Map<String, String>对象
        Map<String, String> map = new java.util.HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        //执行表达式
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("map", map);
        env.put("key", "key1");
        Object result = AviatorEvaluator.execute("mapping(map, key)", env);
        assertEquals("value1", result);
    }
}