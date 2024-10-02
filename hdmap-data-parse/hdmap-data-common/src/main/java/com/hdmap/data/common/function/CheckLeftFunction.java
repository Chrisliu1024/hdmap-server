package com.hdmap.data.common.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.hdmap.geo.utils.GeoLeftUtil;
import lombok.SneakyThrows;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 15:17
 * @description: 检查左侧函数
 */
public class CheckLeftFunction extends AbstractFunction {
    @SneakyThrows
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        SimpleFeature feature1 = (SimpleFeature)FunctionUtils.getJavaObject(arg1, env);
        SimpleFeature feature2 = (SimpleFeature)FunctionUtils.getJavaObject(arg2, env);
        boolean checkLeft = GeoLeftUtil.checkLeft(feature1, feature2);
        return AviatorRuntimeJavaType.valueOf(checkLeft);
    }

    @Override
    public String getName() {
        return "checkLeft";
    }
}
