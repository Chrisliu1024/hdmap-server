package com.hdmap.core.global;

import com.hdmap.core.model.BaseResponse;
import com.hdmap.core.enums.ErrorEnum;
import com.hdmap.core.exception.DefinitionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author admin
 * @version 1.0
 * @date 2023/11/2 11:42
 * @description: 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 处理自定义异常
     *
     */
    @ExceptionHandler(value = DefinitionException.class)
    @ResponseBody
    public BaseResponse bizExceptionHandler(DefinitionException e) {
        return BaseResponse.defineError(e);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponse exceptionHandler(Exception e) {
        return BaseResponse.otherError(e, ErrorEnum.EXCEPTION);
    }
}
