package com.hdmap.pointcloud.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/17 17:54
 * @description: 定义切面
 */
@Slf4j
@Aspect
@Component
public class CallTimeAspect {
    @Pointcut("@annotation(com.hdmap.pointcloud.annotation.CallTime)")
    public void callTimePointcut() {}

    @Around("callTimePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String functionName = joinPoint.getSignature().getName();
        log.info("{}.{}() call time: {}ms", className, functionName, end - start);
        return proceed;
    }
}
