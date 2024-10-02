package com.hdmap.data.etl.controller;

import com.hdmap.data.etl.model.dto.*;
import com.hdmap.data.etl.service.CreateRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字段关联(FieldsRel)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:20
 */
@RestController
@RequestMapping("createRel")
@Tag(name = "关联创建", description = "关联创建相关接口")
public class CreateRelController {
    /**
     * 服务对象
     */
    @Resource
    private CreateRelService createRelService;

    /**
     * 新增数据
     *
     * @param pipelineAndLocations 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public PipelineDetailInfo insert(@RequestBody PipelineAndLocations pipelineAndLocations) {
        return this.createRelService.createRel(pipelineAndLocations.getInfo(), pipelineAndLocations.getFieldRelLocations());
    }

}

