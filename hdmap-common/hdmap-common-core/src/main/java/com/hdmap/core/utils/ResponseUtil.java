package com.hdmap.core.utils;

import com.hdmap.core.model.BaseResponse;
import com.hdmap.core.enums.ErrorEnum;

/**
 * @Author: admin
 * @Date: 2023/2/6 11:11
 */
public class ResponseUtil {
    private ResponseUtil() {
    }

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.of(data);
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    /**
     * 失败
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(T data) {
        return fail(ErrorEnum.DEFAULT_ERROR, data);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(int code, String message, T data) {
        BaseResponse baseResponse = new BaseResponse<>();
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        baseResponse.setData(data);
        return baseResponse;
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(ErrorEnum errorCode, T data) {
        return fail(errorCode.getCode(), errorCode.getMessage(), data);
    }

    /**
     * 失败
     *
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail() {
        return fail(ErrorEnum.DEFAULT_ERROR, null);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(int code, String message) {
        return fail(code, message, null);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(ErrorEnum errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
