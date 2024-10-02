package com.hdmap.data.ibd.Enum;

import lombok.Getter;

@Getter
public enum ErrorLogEnum {
    NO_MAPPING_VALUE(-1, "无映射值"),

    VALUE_IS_NOT_ONLY(1, "值不唯一"),
    ;
    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorLogEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
