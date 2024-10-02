package com.hdmap.data.common.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 14:26
 * @description: 获取属性函数
 */
public class GetAttributeFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        SimpleFeature feature = (SimpleFeature)FunctionUtils.getJavaObject(arg1, env);
        String attributeName = FunctionUtils.getStringValue(arg2, env);
        Object attributeObj = feature.getAttribute(attributeName);
        return attributeObj == null ? new AviatorString(null) : new AviatorString(attributeObj.toString());
    }

    @Override
    public String getName() {
        return "getAttribute";
    }
}
