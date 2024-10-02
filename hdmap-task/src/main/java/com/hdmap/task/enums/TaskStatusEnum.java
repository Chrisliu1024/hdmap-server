package com.hdmap.task.enums;

public enum TaskStatusEnum {

    //状态：0：未分配， 1：已分配，2：作业中，3：作业完成/待质检，4：质检中，5: 作业错误/待修正，6：返工/待修正，7：作业错误已修正/待质检，8：已完成
    UNASSIGNED(0, "未分配"),
    ASSIGNED(1, "已分配"),
    OPERATING(2, "作业中"),
    TO_BE_QUALITY_CHECKED(3, "作业完成/待质检"),
    CHECKING(4, "质检中"),
    OPERATE_ERROR(5, "作业错误/待修正"),
    REWORK(6, "返工/待修正"),
    OPERATE_ERROR_CORRECTED(7, "作业错误已修正/待质检"),
    COMPLETED(8, "已完成"),;

    private Integer code;
    private String desc;

    TaskStatusEnum(Integer code, String desc) {
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
