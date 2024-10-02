package com.hdmap.data.etl.controller;

import com.hdmap.data.etl.model.dto.FieldsRelInfo;
import com.hdmap.data.etl.model.dto.FieldsRelResult;
import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelInfo;
import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelResult;
import com.hdmap.data.etl.service.PipelineFieldsRelRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 字段关联(PipelineFieldRelRel)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:20
 */
@RestController
@RequestMapping("pipelineFieldRelRel")
@Tag(name = "处理管线pipeline和字段关联的关联", description = "处理管线pipeline和字段关联的关联相关接口")
public class PipelineFieldRelRelController {
    /**
     * 服务对象
     */
    @Resource
    private PipelineFieldsRelRelService pipelineFieldsRelRelService;

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public PipelineFieldsRelRelResult insert(@RequestBody PipelineFieldsRelRelInfo info) {
        return this.pipelineFieldsRelRelService.save(info);
    }

}

