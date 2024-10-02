package com.hdmap.data.etl.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/8 14:12
 * @description: 数据源类型枚举
 */
@Getter
public enum DatastoreTypeEnum implements IEnum<Short> {
    // -1-unknow，0-postgis，1-csv，2-geojson，3-shapefile，4-mysql，5-geopackage，6-mongodb，7-kml，8-wfs
    UNKNOW((short) -1, "unknow"),
    POSTGIS((short) 0, "postgis"),
    CSV((short) 1, "csv"),
    GEOJSON((short) 2, "geojson"),
    SHAPEFILE((short) 3, "shapefile"),
    MYSQL((short) 4, "mysql"),
    GEOPACKAGE((short) 5, "geopackage"),
    MONGODB((short) 6, "mongodb"),
    KML((short) 7, "kml"),
    WFS((short) 8, "wfs"),
    ;

    private short code;
    private String name;

    DatastoreTypeEnum(short code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Short getValue() {
        return this.code;
    }

    public static DatastoreTypeEnum getByName(String name) {
        for (DatastoreTypeEnum value : DatastoreTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return UNKNOW;
    }

}
