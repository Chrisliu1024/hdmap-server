package com.hdmap.task.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.dto.TaskPoolResponse;
import com.hdmap.task.model.entity.TaskPoolInfo;
import com.hdmap.task.service.TaskPoolInfoService;
import com.hdmap.mybatis.utils.PageWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务信息表(TaskInfo)表控制层
 *
 * @author admin
 * @since 2024-01-29 14:08:06
 */
@RestController
@RequestMapping("taskPoolInfo")
@Tag(name = "任务池信息表", description = "任务池信息表接口")
public class TaskPoolInfoController {
    @Resource
    private PageWrapper<TaskPoolInfo> pageWrapper;
    @Resource
    private TaskPoolInfoService taskPoolInfoService;

    /**
     * 分页查询所有数据
     *
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @PostMapping("selectByPage/short")
    @Operation(summary = "分页查询", method = "POST")
    @Parameters({@Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, task_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskPoolInfo> selectShortAll(@RequestBody(required = false) TaskPoolInfo info, @RequestParam Integer current, @RequestParam Integer pageSize,
                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskPoolInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskPoolInfoService.getShortByPage(page, info);
    }

    /**
     * 分页查询所有数据（详细）
     *
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @PostMapping("selectByPage")
    @Operation(summary = "分页查询（详细）", method = "GET")
    @Parameters({@Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, task_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskPoolResponse> selectDetailAll(@RequestBody(required = false) TaskPoolInfo info, @RequestParam Integer current, @RequestParam Integer pageSize,
                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskPoolInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskPoolInfoService.getDetailByPage(page, info);
    }

}

