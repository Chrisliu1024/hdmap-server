package com.hdmap.data.common.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.apache.commons.lang3.RandomStringUtils;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/1 10:18
 * @description: 获取随机ID函数
 */
public class GetRandomIdFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        int count = (int) FunctionUtils.getJavaObject(arg1, env);
        String randomId = RandomStringUtils.randomNumeric(count);
        return new AviatorString(randomId);
    }

    @Override
    public String getName() {
        return "getRandomId";
    }

}
