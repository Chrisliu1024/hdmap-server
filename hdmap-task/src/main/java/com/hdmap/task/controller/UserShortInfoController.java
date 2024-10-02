package com.hdmap.task.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.ProjectsUsersRel;
import com.hdmap.task.service.ProjectsUsersRelService;
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
 * @author admin
 * @version 1.0
 * @date 2024/5/20 18:23
 * @description: 用户简要信息
 */
@RestController
@RequestMapping("userShortInfo")
@Tag(name = "用户简要信息", description = "用户简要信息接口")
public class UserShortInfoController {

    @Resource
    private PageWrapper<ProjectsUsersRel> pageWrapper;

    @Resource
    private ProjectsUsersRelService projectsUsersRelService;

    /**
     * 通过项目主键查询数据列表
     *
     * @param id 主键
     * @return 数据列表
     */
    @GetMapping("projectId/{id}")
    @Operation(summary = "通过主键查询单条数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM),
                @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
                @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
                @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, dataset_id"),
                @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<ProjectsUsersRel> selectListByProjectId(@PathVariable Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize,
                                              @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<ProjectsUsersRel> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        ProjectsUsersRel rel = new ProjectsUsersRel();
        rel.setProjectId(Long.valueOf(id.toString()));
        return this.projectsUsersRelService.page(page, new QueryWrapper<>(rel));
    }
}
