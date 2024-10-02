package com.hdmap.task.enums;

import lombok.Getter;

@Getter
public enum DataTypeEnum {
    LAS(0, "las"),
    PCD(1, "pcd"),
    CSV(2, "csv"),
    IBD(3, "ibd"),
    ;

    private Integer code;

    private String desc;

    DataTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
