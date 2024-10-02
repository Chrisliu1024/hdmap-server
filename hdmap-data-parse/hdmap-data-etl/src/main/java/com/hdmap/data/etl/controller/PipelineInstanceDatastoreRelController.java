package com.hdmap.data.etl.controller;

import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelInfo;
import com.hdmap.data.etl.model.dto.PipelineFieldsRelRelResult;
import com.hdmap.data.etl.model.dto.PipelineInstanceAndDatastore;
import com.hdmap.data.etl.service.PipelineInstanceDatastoreInstanceRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 处理pipeline instance和数据存储的关联(PipelineInstanceDatastoreRel)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:23
 */
@RestController
@RequestMapping("pipelineInstanceDatastoreInstanceRel")
@Tag(name = "处理pipeline instance和数据存储instance的关联" , description = "处理pipeline instance和数据存储instance的关联接口")
public class PipelineInstanceDatastoreRelController {
    /**
     * 服务对象
     */
    @Resource
    private PipelineInstanceDatastoreInstanceRelService pipelineInstanceDatastoreInstanceRelService;

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public PipelineInstanceAndDatastore insert(@RequestBody PipelineInstanceAndDatastore info) {
        return this.pipelineInstanceDatastoreInstanceRelService.save(info);
    }

}

