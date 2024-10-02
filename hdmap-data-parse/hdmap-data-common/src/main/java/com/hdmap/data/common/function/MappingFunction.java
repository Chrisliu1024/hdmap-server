package com.hdmap.data.common.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 14:39
 * @description: 映射函数
 */
public class MappingFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Map<String, String> mappingObj =  (Map<String, String>)FunctionUtils.getJavaObject(arg1, env);
        String key = FunctionUtils.getStringValue(arg2, env);
        return new AviatorString(mappingObj.get(key));
    }

    @Override
    public String getName() {
        return "mapping";
    }
}
