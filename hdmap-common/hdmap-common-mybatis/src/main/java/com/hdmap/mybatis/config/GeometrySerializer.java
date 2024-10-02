package com.hdmap.mybatis.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hdmap.geo.utils.GeometryUtil;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;

import java.io.IOException;

/**
 * @author admin
 * @version 1.0
 * @date 2024/1/3 14:52
 * @description: 自定义Geometry序列化器
 */
public class GeometrySerializer<T extends Geometry> extends JsonSerializer<T> {

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        int dimension = GeometryUtil.getDimension(value);
        WKTWriter writer = new WKTWriter(dimension);
        gen.writeString(writer.write(value));
    }
}
