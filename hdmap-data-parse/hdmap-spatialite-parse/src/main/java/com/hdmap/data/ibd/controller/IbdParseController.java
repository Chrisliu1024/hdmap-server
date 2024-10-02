package com.hdmap.data.ibd.controller;

import com.hdmap.core.model.BaseResponse;
import com.hdmap.core.utils.ResponseUtil;
import com.hdmap.data.ibd.dto.IbdUpload;
import com.hdmap.data.ibd.service.IbdParseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author admin
 * @version 1.0
 * @description: 质检服务engine对外http接口
 * @date 2023/7/6 16:12
 */
@RestController
@RequestMapping("/data/ibd")
public class  IbdParseController {

    @Resource
    private IbdParseService ibdParseService;

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "上传ibd，并解析入库", method = "POST")
    @Parameters({@Parameter(description="上传ibd文件", name = "file", required = true, style = ParameterStyle.FORM),
            @Parameter(description="用户id", name = "userId", required = false, style = ParameterStyle.FORM),
            @Parameter(description="项目名称", name = "project", example = "xlab", required = false, style = ParameterStyle.FORM),
            @Parameter(description="数据库名前缀", name = "prefix", example = "ruqi_map_user", required = false, style = ParameterStyle.FORM),
            @Parameter(description="版本号", name = "version",example = "V1.0.0",required = false, style = ParameterStyle.FORM)})
    public boolean importAndParseIbd(MultipartFile file, String userId, String project, String prefix, String version) throws Exception {
        // 默认参数
        if (StringUtils.isBlank(project)) {
            project = "public";
        }
        if (StringUtils.isBlank(prefix)) {
            prefix = "ruqi_map_user";
        }
        if (StringUtils.isBlank(version)) {
            version = "V1.0.0";
        }
        IbdUpload params = new IbdUpload(file, userId, project, prefix, version);
        if (params.getFile() == null) {
            throw new Exception("ibd文件不能为空");
        }
        return ibdParseService.parse(params);
    }

}
