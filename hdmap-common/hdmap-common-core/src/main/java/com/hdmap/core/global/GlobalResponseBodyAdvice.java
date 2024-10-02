package com.hdmap.core.global;

import com.hdmap.core.utils.ResponseUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @author admin
 * @version 1.0
 * @date 2024/6/20 17:05
 * @description: 全局返回体处理
 */

@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    //判断是否要执行beforeBodyWrite方法，true为执行，false不执行
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 针对swagger的接口不进行处理
        if (returnType.getDeclaringClass().getName().toLowerCase().matches(".*springdoc.*")) {
            return false;
        }
        return true;
    }

    //对response处理的执行方法
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 这里面参数很多，一般使用如下几个：
        // body 返回的内容 request 请求 response 响应
        return body != null ? ResponseUtil.success(body) : ResponseUtil.fail();
    }

}
