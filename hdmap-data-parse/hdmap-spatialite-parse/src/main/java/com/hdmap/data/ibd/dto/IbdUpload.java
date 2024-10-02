package com.hdmap.data.ibd.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author admin
 * @version 1.0
 * @description: IbD上传的请求参数
 * @date 2023/7/10 17:27
 */
@Data
@AllArgsConstructor
public class IbdUpload {
    /**
     * 文件
     */
    @Schema(description = "文件")
    MultipartFile file;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    String userId;

    /**
     * 项目ID
     */
    @Schema(description = "项目名称")
    String project;

    /**
     * 数据库名前缀
     */
    @Schema(description = "数据库名前缀")
    String prefix;

    /**
     * 版本号
     */
    @Schema(description = "版本号", defaultValue = "V1.0.0")
    String version;
}
