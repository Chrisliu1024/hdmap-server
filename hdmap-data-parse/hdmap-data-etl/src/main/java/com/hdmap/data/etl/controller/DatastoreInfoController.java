package com.hdmap.data.etl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.data.etl.model.dto.DatastoreAndLeft;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.service.DatastoreInfoService;
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
 * 数据存储信息(DatastoreInfo)表控制层
 *
 * @author makejava
 * @since 2024-07-05 11:04:16
 */
@RestController
@RequestMapping("datastoreInfo")
@Tag(name = "数据存储信息", description = "数据存储信息相关接口")
public class DatastoreInfoController {

    @Resource
    private PageWrapper<DatastoreInfo> pageWrapper;

    /**
     * 服务对象
     */
    @Resource
    private DatastoreInfoService datastoreInfoService;

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
    public IPage<DatastoreInfo> selectAll(@RequestBody(required = false) DatastoreInfo info, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DatastoreInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.datastoreInfoService.page(page, new QueryWrapper<>(info));
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
    public DatastoreInfo selectOne(@PathVariable Serializable id) {
        return this.datastoreInfoService.getById(id);
    }

    /**
     * 通过pipeline主键查询数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/pipeline/{id}")
    @Operation(summary = "通过pipeline主键查询数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public List<DatastoreAndLeft> selectByPipelineId(@PathVariable Serializable id) {
        return this.datastoreInfoService.getByPipelineId(id);
    }

    /**
     * 新增数据
     *
     * @param info 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody DatastoreInfo info) {
        return this.datastoreInfoService.save(info);
    }

    /**
     * 编辑数据
     *
     * @param info 实体
     * @return 编辑结果
     */
    @PutMapping
    @Operation(summary = "编辑数据", method = "PUT")
    public boolean edit(@RequestBody DatastoreInfo info) {
        return this.datastoreInfoService.updateById(info);
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
        return this.datastoreInfoService.removeByIds(idList);
    }

}
