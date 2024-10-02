package com.hdmap.task.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import com.hdmap.task.service.ProjectsDatasetsRelService;
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
 * 项目-用户关联表(ProjectsUsersRel)表控制层
 *
 * @author makejava
 * @since 2024-05-21 10:59:24
 */
@RestController
@RequestMapping("projectsDatasetsRel")
@Tag(name = "项目-数据集关联表", description = "项目-谁集关联表接口")
public class ProjectsDatasetsRelController {

    @Resource
    private PageWrapper<ProjectsDatasetsRel> pageWrapper;

    @Resource
    private ProjectsDatasetsRelService projectsDatasetsRelService;

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
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, data_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<ProjectsDatasetsRel> selectAll(@RequestBody(required = false) ProjectsDatasetsRel rel, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<ProjectsDatasetsRel> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.projectsDatasetsRelService.page(page, new QueryWrapper<>(rel));
    }

    /**
     * 新增数据
     *
     * @param rels 实体列表
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody List<ProjectsDatasetsRel> rels) {
        return this.projectsDatasetsRelService.saveBatchAndInsertTaskPool(rels);
    }

    /**
     * 删除数据
     *
     * @param projectId 项目ID
     * @param datasetIds 主键集合
     * @param isDeleteRel 是否删除关联关系
     * @return 删除是否成功
     */
    @DeleteMapping
    @Operation(summary = "删除数据", method = "DELETE")
    @Parameters({@Parameter(description="项目ID", name = "projectId", required = true, style = ParameterStyle.FORM),
                @Parameter(description="数据集ID列表", name = "datasetIds", required = true, style = ParameterStyle.FORM),
                @Parameter(description="是否删除关联关系", name = "isDeleteRel", required = false, style = ParameterStyle.FORM)})
    public boolean delete(@RequestParam Serializable projectId, @RequestParam List<Serializable> datasetIds, @RequestParam(required = false) boolean isDeleteRel) {
        return this.projectsDatasetsRelService.removeBatchByProjectIdAndDatasetIds(projectId, datasetIds, isDeleteRel);
    }

}

