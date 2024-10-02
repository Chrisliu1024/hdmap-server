package com.hdmap.task.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.dto.TaskDivideResult;
import com.hdmap.task.model.dto.TaskSetRequest;
import com.hdmap.task.model.entity.TaskSetInfo;
import com.hdmap.task.service.TaskSetInfoService;
import com.hdmap.mybatis.utils.PageWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.opengis.referencing.FactoryException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 任务集信息表(TaskSetInfo)表控制层
 *
 * @author admin
 * @since 2024-01-29 14:08:06
 */
@RestController
@RequestMapping("taskSetInfo")
@Tag(name = "任务集信息表", description = "任务集信息表接口")
public class TaskSetInfoController {
    @Resource
    private PageWrapper<TaskSetInfo> pageWrapper;
    /**
     * 服务对象
     */
    @Resource
    private TaskSetInfoService taskSetInfoService;

    /**
     * 分页查询数据
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
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, taskset_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskSetInfo> getByPage(@RequestBody(required = false) TaskSetInfo info, @RequestParam Integer current, @RequestParam Integer pageSize,
                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskSetInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskSetInfoService.getByPage(page, info);
    }

    /**
     * 通过项目ID和用户ID查询数据
     *
     * @param projectId 项目ID
     * @param userId 用户ID
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @GetMapping("getByProjectIdAndCreateBy")
    @Operation(summary = "通过项目ID和用户ID查询数据", method = "GET")
    @Parameters({@Parameter(description="项目ID", name = "projectId", required = false, style = ParameterStyle.FORM),
            @Parameter(description="创建人ID", name = "userId", required = false, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, taskset_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskSetInfo> getByProjectIdAndCreateBy(@RequestParam(required = false) Serializable projectId, @RequestParam(required = false) Serializable userId,
                                                  @RequestParam Integer current, @RequestParam Integer pageSize,
                                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskSetInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskSetInfoService.getByProjectIdAndCreateBy(page, projectId, userId);
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
    public TaskSetInfo selectOne(@PathVariable Serializable id) {
        return this.taskSetInfoService.getById(id);
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
    public IPage<TaskSetInfo> selectListByProjectId(@PathVariable Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize,
                                              @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskSetInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskSetInfoService.selectListByProjectId(page, id);
    }

    /**
     * 获取任务划分结果
     *
     * @param id 主键
     * @return 多条数据
     */
    @GetMapping("getDivideTasks")
    @Operation(summary = "获取任务划分结果", method = "GET")
    @Parameters({@Parameter(description="任务集ID", name = "id", required = true, style = ParameterStyle.FORM),
            @Parameter(description="GeoHash字符数组", name = "geohashs", required = true, style = ParameterStyle.FORM)})
    public TaskDivideResult getDivideTasksById(@RequestParam Serializable id, @RequestParam List<String> geohashs) throws IOException, FactoryException {
        return this.taskSetInfoService.getDivideTasksById(id, geohashs);
    }

//    /**
//     * 新增数据
//     *
//     * @param taskSetInfo 实体对象
//     * @return 新增结果
//     */
//    @PostMapping
//    @Operation(summary = "新增数据", method = "POST")
//    public boolean insert(@RequestBody TaskSetInfo taskSetInfo) {
//        return this.taskSetInfoService.save(taskSetInfo);
//    }

    /**
     * 新增数据
     *
     * @param request 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody TaskSetRequest request) {
        return this.taskSetInfoService.save(request);
    }

    /**
     * 修改数据
     *
     * @param taskSetInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Operation(summary = "修改数据", method = "PUT")
    public boolean update(@RequestBody TaskSetInfo taskSetInfo) {
        return this.taskSetInfoService.updateById(taskSetInfo);
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
    public boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.taskSetInfoService.removeByIds(idList);
    }
}

