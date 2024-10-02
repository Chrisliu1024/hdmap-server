package com.hdmap.data.etl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.data.etl.model.entity.DatastoreTemplateInfo;
import com.hdmap.data.etl.service.DatastoreTemplateInfoService;
import com.hdmap.mybatis.utils.PageWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 数据存储信息(DatastoreTemplateInfo)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:16
 */
@RestController
@RequestMapping("datastoreTemplateInfo")
@Tag(name = "数据存储Template信息", description = "数据存储信息Template相关接口")
public class DatastoreTemplateInfoController {

    @Resource
    private PageWrapper<DatastoreTemplateInfo> pageWrapper;

    /**
     * 服务对象
     */
    @Resource
    private DatastoreTemplateInfoService datastoreTemplateInfoService;

    /**
     * 分页查询
     *
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @PostMapping("selectByPage")
    @Operation(summary = "分页查询", method = "POST")
    @Parameters({@Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, task_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<DatastoreTemplateInfo> selectAll(@RequestBody(required = false) DatastoreTemplateInfo info, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DatastoreTemplateInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.datastoreTemplateInfoService.page(page, new QueryWrapper<>(info));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @Operation(summary = "通过主键查询单条数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public DatastoreTemplateInfo selectOne(@PathVariable Serializable id) {
        return this.datastoreTemplateInfoService.getById(id);
    }

    /**
     * 通过类型查询数据
     *
     * @param type 类型
     * @return 单条数据
     */
    @GetMapping("type/{type}")
    @Operation(summary = "通过主键查询单条数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public List<DatastoreTemplateInfo> selectByType(@PathVariable Integer type) {
        return this.datastoreTemplateInfoService.getByType(type);
    }

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody DatastoreTemplateInfo info) {
        return this.datastoreTemplateInfoService.save(info);
    }

    /**
     * 编辑数据
     *
     * @param info 实体
     * @return 编辑结果
     */
    @PutMapping
    @Operation(summary = "编辑数据", method = "PUT")
    public boolean edit(@RequestBody DatastoreTemplateInfo info) {
        return this.datastoreTemplateInfoService.updateById(info);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @Operation(summary = "删除数据", method = "DELETE")
    @Parameters({@Parameter(description="主键列表", name = "idList", required = true, style = ParameterStyle.FORM)})
    public boolean delete(@RequestParam("idList") List<Serializable> idList) {
        return this.datastoreTemplateInfoService.removeByIds(idList);
    }

}

