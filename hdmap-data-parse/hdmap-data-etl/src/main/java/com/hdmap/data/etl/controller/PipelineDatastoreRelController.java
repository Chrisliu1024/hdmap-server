package com.hdmap.data.etl.controller;

import com.hdmap.data.etl.model.dto.PipelineDatastoreRelInfo;
import com.hdmap.data.etl.model.dto.PipelineDatastoreRelResult;
import com.hdmap.data.etl.service.PipelineDatastoreRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 处理pipeline和数据存储的关联(PipelineDatastoreRel)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:22
 */
@RestController
@RequestMapping("pipelineDatastoreRel")
@Tag(name = "处理pipeline和数据存储的关联", description = "处理pipeline和数据存储的关联相关接口")
public class PipelineDatastoreRelController {
    /**
     * 服务对象
     */
    @Resource
    private PipelineDatastoreRelService pipelineDatastoreRelService;

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public PipelineDatastoreRelResult insert(@RequestBody PipelineDatastoreRelInfo info) {
        return this.pipelineDatastoreRelService.save(info);
    }

}

