package com.hdmap.task.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.ProjectInfo;
import com.hdmap.task.model.entity.ProjectsUsersRel;
import com.hdmap.task.service.ProjectInfoService;
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
 * 项目表(ProjectInfo)表控制层
 *
 * @author admin
 * @since 2024-01-29 14:08:06
 */
@RestController
@RequestMapping("projectInfo")
@Tag(name = "项目表", description = "项目表接口")
public class ProjectInfoController {

    @Resource
    private PageWrapper<ProjectInfo> pageWrapper;
    /**
     * 服务对象
     */
    @Resource
    private ProjectInfoService projectInfoService;

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
    @Parameters({@Parameter(description="用户ID", name = "id", required = false, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, project_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<ProjectInfo> selectAll(@RequestBody(required = false) ProjectInfo info, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<ProjectInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.projectInfoService.getByPage(page, info);
    }

    /**
     * 分页查询数据（通过用户信息）
     *
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @PostMapping("selectByPage/withUserInfo")
    @Operation(summary = "分页查询（通过用户信息）", method = "GET")
    @Parameters({@Parameter(description="用户ID", name = "id", required = false, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, project_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<ProjectInfo> selectAllByUserInfo(@RequestBody(required = false) ProjectsUsersRel projectsUsersRel, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<ProjectInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.projectInfoService.getByPage(page, projectsUsersRel);
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
    public ProjectInfo selectOne(@PathVariable Serializable id) {
        return this.projectInfoService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param projectInfo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody ProjectInfo projectInfo) {
        return this.projectInfoService.save(projectInfo);
    }

    /**
     * 修改数据
     *
     * @param projectInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Operation(summary = "修改数据", method = "PUT")
    public boolean update(@RequestBody ProjectInfo projectInfo) {
        return this.projectInfoService.updateById(projectInfo);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping
    @Operation(summary = "删除数据", method = "DELETE")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public boolean delete(@RequestParam("id") Serializable id) {
        return this.projectInfoService.removeByIdCustom(id);
    }
}

