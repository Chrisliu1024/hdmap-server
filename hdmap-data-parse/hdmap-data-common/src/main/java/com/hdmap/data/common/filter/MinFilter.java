package com.hdmap.data.common.filter;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.opengis.feature.simple.SimpleFeature;

import java.util.*;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 17:19
 * @description: 获取最小值
 */
public class MinFilter extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        List<SimpleFeature> featureList = (List) FunctionUtils.getJavaObject(arg1, env);
        String attributeName = FunctionUtils.getStringValue(arg2, env);
        if (featureList == null) {
            throw new IllegalArgumentException("featureList is null");
        }
        if (featureList.isEmpty()) {
            return AviatorRuntimeJavaType.valueOf(new ArrayList<>());
        }
        Object maxValue = featureList.stream().map(feature -> feature.getAttribute(attributeName)).filter(Objects::nonNull)
                .min(Comparator.comparing(o -> Double.parseDouble(o.toString()))).orElse(null);
        return new AviatorDouble(Double.parseDouble(maxValue.toString()));
    }

    @Override
    public String getName() {
        return "minFilter";
    }
}
