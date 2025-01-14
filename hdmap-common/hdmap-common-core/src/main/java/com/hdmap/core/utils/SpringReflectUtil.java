package com.hdmap.core.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;

/**
 * @author admin
 * @version 1.0
 * @description: 反射调用spring bean中的方法
 * @date 2023/7/12 11:48
 */
@Component
public class SpringReflectUtil implements ApplicationContextAware {
    /**
     * Spring容器 spring应用上下文对象
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringReflectUtil.applicationContext = applicationContext;
    }

    /**
     * 对象名称获取spring bean对象
     * @param name
     * @return
     * @throws BeansException
     */
    public static Object getBean(Class name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据 服务名称 ，方法名 反射调用  spring bean 中的 方法
     * @param serviceName 服务名
     * @param methodName 方法名
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static Object springInvokeMethod(String serviceName, String methodName, Object[] params) throws Exception {
        Class clazz = Class.forName(serviceName);
        Object service = getBean(clazz);
        Class<? extends Object>[] paramClass = null;
        if (params != null) {
            int paramsLength = params.length;
            paramClass = new Class[paramsLength];
            for (int i = 0; i < paramsLength; i++) {
                paramClass[i] = params[i].getClass();
            }
        }
        // 找到方法
        Method method = ReflectionUtils.findMethod(service.getClass(), methodName, paramClass);
        // 执行方法
        return ReflectionUtils.invokeMethod(method, service, params);
    }

}
