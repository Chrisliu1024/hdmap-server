package com.hdmap.mybatis.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.base.Strings;
import com.hdmap.geo.utils.GeometryUtil;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;

/**
 * @author admin
 * @version 1.0
 * @date 2024/1/3 15:20
 * @description: 自定义Geometry反序列化器
 */
public class GeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        try {
            String text = jp.getValueAsString();
            if(Strings.isNullOrEmpty(text)) {
                return null;
            }
            // 读取EWKT/WKT字符串
            String srid = null;
            if(text.startsWith("SRID=")) {
                srid = text.substring(text.indexOf('=') + 1, text.indexOf(';'));
                text = text.substring(text.indexOf(';') + 1);
            }
            int defaultSrid = GeometryUtil.SRID_WGS84;
            int sridInt = srid == null ? defaultSrid : Integer.parseInt(srid);
            WKTReader reader = new WKTReader(new GeometryFactory(new PrecisionModel(), sridInt));
            T geom =  (T) reader.read(text);
            return sridInt != defaultSrid ? (T) GeometryUtil.transform(geom, sridInt, defaultSrid) : geom;
        } catch (ParseException e) {
            throw new IOException(e);
        } catch (FactoryException | TransformException e) {
            throw new RuntimeException(e);
        }
    }

}
