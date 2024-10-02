package com.hdmap.core.enums;

/**
 * @Author: admin
 * @Date: 2023/2/6 11:18
 */
public enum ErrorEnum {
    /**
     * 失败
     */
    DEFAULT_ERROR(-1, "失败"),

    /**
     * 源数据存在问题
     */
    SOURCE_DATA_ERROR(400, "源数据存在问题"),

    /**
     * 抱歉，当前系统繁忙，请稍后重试
     */
    EXCEPTION(500, "抱歉，当前系统繁忙，请稍后重试"),

    /**
     * 参数校验不通过
     */
    PARAM_VALID_NO_PASS(40401, "参数校验不通过"),

    /**
     * 手机号格式错误
     */
    PHONE_ERROR(40402, "手机号格式错误"),

    /**
     * 用户账号已存在
     */
    EXIST_USER_NAME(40403, "用户账号已存在"),

    /**
     * 用户手机号已存在
     */
    EXIST_PHONE(40404, "用户手机号已存在"),

    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(40405, "用户不存在"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(40406, "密码错误"),

    /**
     * 参数错误
     */
    INVALID_PARAM(40407, "参数错误");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
