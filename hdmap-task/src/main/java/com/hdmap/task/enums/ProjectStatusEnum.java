package com.hdmap.task.enums;

public enum ProjectStatusEnum {
    END(0, "结束"),
    PROCESSING(1, "进行中"),
    EXCEPTION(2, "异常");

    private Integer code;
    private String desc;

    ProjectStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProjectStatusEnum getByCode(Integer code) {
        for (ProjectStatusEnum projectStatusEnum : ProjectStatusEnum.values()) {
            if (projectStatusEnum.getCode().equals(code)) {
                return projectStatusEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
