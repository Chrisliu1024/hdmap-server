package com.hdmap.pointcloud;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@EnableKnife4j
@MapperScan(basePackages = {"com.hdmap.pointcloud.mapper"})
@SpringBootApplication(scanBasePackages = {"com.hdmap"})
public class PointCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(PointCloudApplication.class, args);
    }
}