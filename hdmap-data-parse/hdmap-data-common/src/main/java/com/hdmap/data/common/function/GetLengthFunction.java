package com.hdmap.data.common.function;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.hdmap.geo.utils.GeometryUtil;
import lombok.SneakyThrows;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Map;

/**
 * @author admin
 * @version 1.0
 * @date 2024/6/28 15:17
 * @description: 获取长度
 */
public class GetLengthFunction extends AbstractFunction {
    @SneakyThrows
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        SimpleFeature feature = (SimpleFeature)FunctionUtils.getJavaObject(arg1, env);
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        if (geometry.getSRID() == 0) {
            throw new IllegalArgumentException("Geometry SRID is not set.");
        }
        //如果是经纬度坐标系，需要转换成真实距离
        if (geometry.getSRID() != 3857) {
            geometry = GeometryUtil.transform(geometry, 3857);
        }
        return AviatorRuntimeJavaType.valueOf(geometry.getLength());
    }

    @Override
    public String getName() {
        return "getLength";
    }
}
