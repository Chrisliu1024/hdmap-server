package com.hdmap.pointcloud.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/23 14:36
 * @description: 公共配置
 */
@Configuration
@EnableAsync
@Getter
public class CommonConfig {
    @Value("${pointcloud.deleteLocalFile:true}")
    private Boolean deleteLocalFile;
    @Value("${pointcloud.geohash.precision:7}")
    private Integer geohashPrecision;
    @Value("${pointcloud.zipUploadFile:true}")
    private Boolean zipUploadFile;
}
