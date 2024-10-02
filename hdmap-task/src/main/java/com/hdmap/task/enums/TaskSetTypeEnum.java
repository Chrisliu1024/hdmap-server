package com.hdmap.task.enums;

import lombok.Getter;

@Getter
public enum TaskSetTypeEnum {
    DRAW_MAP(0, "制图任务集"),
    DATA_COLLECTION(1, "数据采集任务集"),
    MARKER_4D(2, "4D标注任务集"),
    DATA_HANDLE(3, "数据处理任务集"),
    MAP_RESULT_UNION(4, "成果合图任务集"),
    CHECK(5, "质检任务集"),
    ;

    private Integer code;

    private String desc;

    TaskSetTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TaskSetTypeEnum getByCode(Integer code) {
        for (TaskSetTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
