package com.hdmap.pointcloud.constant.enums;

import lombok.Getter;

@Getter
public enum FileTypeEnum {
    LAS(0, "las"),
    PCD(1, "pcd"),
    CSV(2, "csv"),
    IBD(3, "ibd"),
    GEOJSON(4, "geojson"),
    ZIP(5, "zip"),
    UNSUPPORT(6, "unsupport"),
    ;

    private final int code;
    private final String name;

    FileTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getSuffix() {
        return "." + name;
    }

    public static FileTypeEnum getBySuffix(String suffix) {
        for (FileTypeEnum value : values()) {
            if (value.name.equals(suffix)) {
                return value;
            }
        }
        return UNSUPPORT;
    }

}
