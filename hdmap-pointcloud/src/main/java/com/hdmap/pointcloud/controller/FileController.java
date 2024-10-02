package com.hdmap.pointcloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.pointcloud.entity.SourceFileInfo;
import com.hdmap.core.model.BaseResponse;
import com.hdmap.core.utils.ResponseUtil;
import com.hdmap.mybatis.utils.PageWrapper;
import com.hdmap.pointcloud.service.FileHandleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/4/18 17:06
 * @description: 文件下载
 */
@RestController
@RequestMapping("file")
@Tag(name = "文件操作", description = "上传文件信息查询")
public class FileController {

    @Resource
    private FileHandleService fileHandleService;
    @Resource
    private PageWrapper<SourceFileInfo> pageWrapper;

//    @PostMapping
//    @Operation(summary = "文件上传", method = "POST")
//    @Parameters({@Parameter(description="标志ID", name = "identifier", required = true, style = ParameterStyle.FORM),
//            @Parameter(description="文件的本地路径", name = "location", required = true, style = ParameterStyle.FORM),
//            @Parameter(description="geohash长度", name = "precision", required = false, style = ParameterStyle.FORM),
//            @Parameter(description="上传的las文件", name = "file", required = true, style = ParameterStyle.FORM)})
//    public BaseResponse upload(@RequestParam Long identifier, @RequestParam String location, @RequestParam Integer precision, MultipartFile file) throws Exception {
//        // store file to local
//        String localPath = fileHandleService.storeFileToLocal(file);
//        return ResponseUtil.success(fileHandleService.uploadAndClipFile(identifier, localPath, location, precision));
//    }

    @GetMapping("list")
    @Operation(summary = "分页查询", method = "POST")
    @Parameters({@Parameter(description="标志ID", name = "identifier", required = false, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, project_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public BaseResponse selectAll(@RequestParam(required = false) Serializable identifier, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<SourceFileInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return ResponseUtil.success(fileHandleService.getSourceFileInfo(page, identifier));
    }

}
