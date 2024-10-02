package com.hdmap.pointcloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/17 17:52
 * @description: AspectJ配置类
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectJConfiguration {
}
