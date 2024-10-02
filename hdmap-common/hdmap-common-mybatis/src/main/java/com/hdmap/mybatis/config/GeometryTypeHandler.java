package com.hdmap.mybatis.config;

import com.hdmap.geo.utils.GeometryUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ByteOrderValues;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author admin
 * @version 1.0
 * @date 2023/10/26 19:38
 * @description: 自定义Geometry处理器
 */
@MappedTypes({Geometry.class})
@MappedJdbcTypes({JdbcType.OTHER})
public class GeometryTypeHandler extends BaseTypeHandler<Geometry> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Geometry parameter, JdbcType jdbcType) throws SQLException {
        // Geometry的维度
        int dimension = GeometryUtil.getDimension(parameter);
        // Geometry转WKB
        byte[] geometryBytes = new WKBWriter(dimension, ByteOrderValues.LITTLE_ENDIAN, true).write(parameter);
        ps.setBytes(i, geometryBytes);
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, String columnName){
        try{
            String wkb = rs.getString(columnName);
            return wkb2Geometry(wkb);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, int columnName){
        try{
            String wkb = rs.getString(columnName);
            return wkb2Geometry(wkb);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Geometry getNullableResult(CallableStatement callableStatement, int columnIndex) {
        return null;
    }
    /**
     * 流 转 geometry
     * */
    private Geometry wkb2Geometry(String wkb) throws Exception {
        if (wkb == null) {
            return null;
        }
        WKBReader reader = new WKBReader();
        Geometry geometry = reader.read(WKBReader.hexToBytes(wkb));
        //转换成4326
        if (geometry.getSRID() != GeometryUtil.SRID_WGS84) {
            geometry = GeometryUtil.transform(geometry, geometry.getSRID(), GeometryUtil.SRID_WGS84);
        }
        return geometry;
    }
}
