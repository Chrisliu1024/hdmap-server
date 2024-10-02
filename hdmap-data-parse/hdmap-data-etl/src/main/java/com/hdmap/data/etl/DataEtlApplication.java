package com.hdmap.data.etl;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author admin
 * @version 1.0
 * @date 2024/7/9 09:50
 * @description: 数据ETL服务
 */
//@EnableDubbo
@EnableKnife4j
@MapperScan(basePackages = {"com.hdmap.data.etl.mapper"})
@SpringBootApplication(scanBasePackages = {"com.hdmap"})
public class DataEtlApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataEtlApplication.class, args);
    }
}
