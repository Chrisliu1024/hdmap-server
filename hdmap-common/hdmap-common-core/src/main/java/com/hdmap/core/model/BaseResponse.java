package com.hdmap.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hdmap.core.enums.ErrorEnum;
import com.hdmap.core.exception.DefinitionException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: admin
 * @Date: 2023/2/6 10:48
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponse<T> implements Serializable {

    @Schema(name = "0成功，非0失败", requiredMode = Schema.RequiredMode.REQUIRED)
    protected int code = 0;

    @Schema(name = "提示信息", requiredMode = Schema.RequiredMode.REQUIRED)
    protected String message = "成功";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected T data;

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> BaseResponse of(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setData(data);
        return response;
    }

    /**
     * 只打印code、message，日志级别在info(含)以上使用
     *
     */
    public String toShortString() {
        final StringBuilder sb = new StringBuilder("BaseResponse(");
        sb.append("code=").append(code);
        sb.append(", message=").append(message);
        sb.append(')');
        return sb.toString();
    }

    //自定义异常返回的结果
    public static BaseResponse defineError(DefinitionException de){
        BaseResponse res = new BaseResponse();
        res.setCode(de.getErrorCode());
        res.setMessage(de.getErrorMsg());
        res.setData(null);
        return res;
    }
    //其他异常处理方法返回的结果
    public static BaseResponse otherError(Exception e, ErrorEnum errorEnum){
        BaseResponse res = new BaseResponse();
        res.setMessage(e.getMessage());
        res.setCode(errorEnum.getCode());
        res.setData(null);
        return res;
    }
}
