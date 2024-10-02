package com.hdmap.data.common.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 15:17
 * @description: 获取中心点
 */
public class GetCenterPointFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        SimpleFeature feature = (SimpleFeature)FunctionUtils.getJavaObject(arg1, env);
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        return AviatorRuntimeJavaType.valueOf(geometry.getCentroid());
    }

    @Override
    public String getName() {
        return "getCenterPoint";
    }
}
