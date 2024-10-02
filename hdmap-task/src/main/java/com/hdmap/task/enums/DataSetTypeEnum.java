package com.hdmap.task.enums;

import lombok.Getter;

@Getter
public enum DataSetTypeEnum {
    COMMON(0, "通用数据集"),
    DRAW_MAP(1, "制图数据集"),
    MARKING_4D(2, "4D标注数据集"),
    ;

    private Integer code;
    private String desc;

    DataSetTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean equalByCode(Integer code) {
        return this.code.equals(code);
    }

    public static DataSetTypeEnum getByCode(Integer code) {
        for (DataSetTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

}
