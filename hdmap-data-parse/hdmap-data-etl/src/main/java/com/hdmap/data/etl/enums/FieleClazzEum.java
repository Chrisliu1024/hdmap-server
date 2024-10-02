package com.hdmap.data.etl.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;
import java.util.Date;

@Getter
public enum FieleClazzEum implements IEnum<Short> {
    // 所属类：0-Byte，1-Short，2-Integer，3-Long，4-Float，5-Double，6-String，7-Boolean，8-Date，9-Timestamp，10-Geometry，11-Point，12-LineString，13-Polygon，14-MultiPoint，15-MultiLineString，16-MultiPolygon，17-GeometryCollection
    BYTE((short) 0, Byte.class, "Byte"),
    SHORT((short) 1, Short.class, "Short"),
    INTEGER((short) 2, Integer.class, "Integer"),
    LONG((short) 3, Long.class, "Long"),
    FLOAT((short) 4, Float.class, "Float"),
    DOUBLE((short) 5, Double.class, "Double"),
    STRING((short) 6, String.class, "String"),
    BOOLEAN((short) 7, Boolean.class, "Boolean"),
    DATE((short) 8, Date.class, "Date"),
    TIMESTAMP((short) 9, java.sql.Timestamp.class, "Timestamp"),
    GEOMETRY((short) 10, org.locationtech.jts.geom.Geometry.class, "Geometry"),
    POINT((short) 11, org.locationtech.jts.geom.Point.class, "Point"),
    LINESTRING((short) 12, org.locationtech.jts.geom.LineString.class, "LineString"),
    POLYGON((short) 13, org.locationtech.jts.geom.Polygon.class, "Polygon"),
    MULTIPOINT((short) 14, org.locationtech.jts.geom.MultiPoint.class, "MultiPoint"),
    MULTILINESTRING((short) 15, org.locationtech.jts.geom.MultiLineString.class, "MultiLineString"),
    MULTIPOLYGON((short) 16, org.locationtech.jts.geom.MultiPolygon.class, "MultiPolygon"),
    GEOMETRYCOLLECTION((short) 17, org.locationtech.jts.geom.GeometryCollection.class, "GeometryCollection"),
    ;


    private short code;
    private Class<?> clazz;
    private String desc;

    FieleClazzEum(short code, Class<?> clazz, String desc) {
        this.code = code;
        this.clazz = clazz;
        this.desc = desc;
    }

    @Override
    public Short getValue() {
        return this.code;
    }

    public static FieleClazzEum getByClazz(Class<?> clazz) {
        for (FieleClazzEum e : FieleClazzEum.values()) {
            if (e.getClazz().equals(clazz)) {
                return e;
            }
        }
        return null;
    }
}
