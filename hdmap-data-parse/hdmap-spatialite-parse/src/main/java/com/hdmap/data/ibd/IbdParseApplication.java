package com.hdmap.data.ibd;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IBD解析服务
 *
 */
//@EnableDubbo
@EnableKnife4j
@MapperScan(basePackages = {"com.hdmap.data.ibd.mapper"})
@SpringBootApplication(scanBasePackages = {"com.hdmap"})
public class IbdParseApplication {
    public static void main(String[] args) {
        SpringApplication.run(IbdParseApplication.class, args);
    }
}