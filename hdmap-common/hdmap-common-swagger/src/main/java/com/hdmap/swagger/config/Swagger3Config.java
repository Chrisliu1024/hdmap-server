package com.hdmap.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 创建Swagger3配置
 */
@Slf4j
@Configuration
public class Swagger3Config {
    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("default")         // 分组名称
                .pathsToMatch("/**")  // 接口请求路径规则
                .build();
    }
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("接口API文档")
                        .description("文档说明")
                        .version("v1")
                        .contact(new Contact().name("hdmap").email("XXX@XXX.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );

    }
}
