package com.hdmap.data.etl.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum PipelineTypeEnum implements IEnum<Short> {
    //类型：0-实体字段，1-逻辑字段
    ENTITY((short) 0, "实体字段"),
    LOGIC((short) 1, "逻辑字段"),
    ;

    private short code;
    private String desc;

    PipelineTypeEnum(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Short getValue() {
        return this.code;
    }

}
