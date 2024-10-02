package com.hdmap.pointcloud.constant.enums;

import lombok.Getter;

@Getter
public enum FileStatusEnum {
    RECEIVING(0, "receiving"),
    RECEIVED(1, "received"),
    CLIPPING(2, "clipping"),
    CLIPPED(3, "clipped"),
    ;

    private final int code;
    private final String name;

    FileStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
