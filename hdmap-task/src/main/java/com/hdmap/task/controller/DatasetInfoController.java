package com.hdmap.task.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.DatasetInfo;
import com.hdmap.task.service.DatasetInfoService;
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
 * 数据集信息表(DatasetInfo)表控制层
 *
 * @author admin
 * @since 2024-01-29 14:08:06
 */
@RestController
@RequestMapping("datasetInfo")
@Tag(name = "数据集信息表", description = "数据集信息表接口")
public class DatasetInfoController {

    @Resource
    private PageWrapper<DatasetInfo> pageWrapper;
    /**
     * 服务对象
     */
    @Resource
    private DatasetInfoService datasetInfoService;

    /**
     * 分页查询所有数据
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
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, dataset_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<DatasetInfo> selectAll(@RequestBody(required = false) DatasetInfo info, @RequestParam Integer current, @RequestParam Integer pageSize,
                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DatasetInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.datasetInfoService.getByPage(page, info);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("id/{id}")
    @Operation(summary = "通过主键查询单条数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public DatasetInfo selectOne(@PathVariable Serializable id) {
        return this.datasetInfoService.getById(id);
    }

    /**
     * 通过项目主键查询数据列表
     *
     * @param id 主键
     * @return 数据列表
     */
    @GetMapping("projectId/{id}")
    @Operation(summary = "通过项目ID查询数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM),
                @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
                @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
                @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, dataset_id"),
                @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<DatasetInfo> selectListByProjectId(@PathVariable Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize,
                                              @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DatasetInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.datasetInfoService.selectListByProjectId(page, id);
    }

    /**
     * 新增数据
     *
     * @param datasetInfo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody DatasetInfo datasetInfo) {
        return this.datasetInfoService.save(datasetInfo);
    }

    /**
     * 修改数据
     *
     * @param datasetInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Operation(summary = "修改数据", method = "PUT")
    public boolean update(@RequestBody DatasetInfo datasetInfo) {
        return this.datasetInfoService.updateById(datasetInfo);
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
        return this.datasetInfoService.removeByIdsCustom(idList);
    }

    /**
     * 回收未分配数据
     *
     * @param datasetId 数据集ID
     * @param projectId 项目ID
     * @return 回收结果
     */
    @GetMapping("recycleUnallocatedData")
    @Operation(summary = "回收未分配数据", method = "PUT")
    @Parameters({@Parameter(description="数据集ID", name = "datasetId", required = true, style = ParameterStyle.FORM),
            @Parameter(description="项目ID", name = "projectId", required = true, style = ParameterStyle.FORM)})
    public DatasetInfo recycleUnallocatedData(@RequestParam("datasetId") Serializable datasetId, @RequestParam("projectId") Serializable projectId) {
        return this.datasetInfoService.recycleUnallocatedDataByDatasetIdAndProjectId(datasetId, projectId);
    }
}

