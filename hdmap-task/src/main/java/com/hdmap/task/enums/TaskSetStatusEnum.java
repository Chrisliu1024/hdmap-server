package com.hdmap.task.enums;

import lombok.Getter;

@Getter
public enum TaskSetStatusEnum {
    END(0, "已结束"),
    PROCESSING(1, "进行中"),
    EXCEPTION(2, "异常"),
    ;

    private Integer code;
    private String desc;

    TaskSetStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
