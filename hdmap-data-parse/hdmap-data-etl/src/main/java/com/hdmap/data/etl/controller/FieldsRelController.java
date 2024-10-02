package com.hdmap.data.etl.controller;

import com.hdmap.data.etl.model.dto.FieldsRelInfo;
import com.hdmap.data.etl.model.dto.FieldsRelResult;
import com.hdmap.data.etl.service.FieldsRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 字段关联(FieldsRel)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:20
 */
@RestController
@RequestMapping("fieldsRel")
@Tag(name = "字段关联", description = "字段关联相关接口")
public class FieldsRelController {
    /**
     * 服务对象
     */
    @Resource
    private FieldsRelService fieldsRelService;

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public FieldsRelResult insert(@RequestBody FieldsRelInfo info) {
        return this.fieldsRelService.save(info);
    }

}

