package com.hdmap.data.etl.controller;

import com.hdmap.data.etl.model.dto.FieldsRelInfo;
import com.hdmap.data.etl.model.dto.FieldsRelResult;
import com.hdmap.data.etl.model.dto.TablesRelInfo;
import com.hdmap.data.etl.model.dto.TablesRelResult;
import com.hdmap.data.etl.service.TablesRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 表关联(TablesRel)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:20
 */
@RestController
@RequestMapping("tablesRel")
@Tag(name = "表关联", description = "表关联相关接口")
public class TablesRelController {
    /**
     * 服务对象
     */
    @Resource
    private TablesRelService tablesRelService;

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public TablesRelResult insert(@RequestBody TablesRelInfo info) {
        return this.tablesRelService.save(info);
    }

}

