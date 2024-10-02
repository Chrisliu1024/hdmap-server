package com.hdmap.task.enums;

public enum UserRoleEnum {
    // 0 采集员，1 作业员，2 质检员
    COLLECTOR(0, "map_dataCollector"),
    OPERATOR(1, "map_producer"),
    CHECKER(2, "map_checkuser"),
    ;

    private Integer code;
    private String desc;

    UserRoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
