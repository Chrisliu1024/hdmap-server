package com.hdmap.task.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.ProjectsTaskSetsRel;
import com.hdmap.task.service.ProjectsTaskSetsRelService;
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
@RequestMapping("projectsTaskSetsRel")
@Tag(name = "项目-任务集关联表", description = "项目-任务集关联表接口")
public class ProjectsTaskSetsRelController {

    @Resource
    private PageWrapper<ProjectsTaskSetsRel> pageWrapper;

    @Resource
    private ProjectsTaskSetsRelService projectsTaskSetsRelService;

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
    public IPage<ProjectsTaskSetsRel> selectAll(@RequestBody(required = false) ProjectsTaskSetsRel rel, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<ProjectsTaskSetsRel> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.projectsTaskSetsRelService.page(page, new QueryWrapper<>(rel));
    }

    /**
     * 新增数据
     *
     * @param rels 实体列表
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody List<ProjectsTaskSetsRel> rels) {
        return this.projectsTaskSetsRelService.saveBatch(rels);
    }

    /**
     * 删除数据
     *
     * @param taskSetIds 主键集合
     * @return 删除是否成功
     */
    @DeleteMapping
    @Operation(summary = "删除数据", method = "DELETE")
    public boolean deleteByUserIds(@RequestParam Serializable projectId, @RequestParam List<Serializable> taskSetIds) {
        return this.projectsTaskSetsRelService.removeBatchByProjectIdAndTaskSetIds(projectId, taskSetIds);
    }

}

