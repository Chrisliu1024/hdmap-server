package com.hdmap.data.common.filter;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.*;
import org.opengis.feature.simple.SimpleFeature;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 17:57
 * @description: 去重过滤器
 */
public class DistinctFilter extends AbstractFunction {
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
        List<Object> list = featureList.stream().map(feature -> feature.getAttribute(attributeName)).filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
        return AviatorRuntimeJavaType.valueOf(list);
    }

    @Override
    public String getName() {
        return "distinctFilter";
    }
}
