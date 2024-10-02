package com.hdmap.task.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.dto.TaskBatchRequest;
import com.hdmap.task.model.dto.TaskRequest;
import com.hdmap.task.service.TaskInfoService;
import com.hdmap.task.model.entity.TaskInfo;
import com.hdmap.mybatis.utils.PageWrapper;
import com.hdmap.pointcloud.utils.ResourceUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;

/**
 * 任务信息表(TaskInfo)表控制层
 *
 * @author admin
 * @since 2024-01-29 14:08:06
 */
@RestController
@RequestMapping("taskInfo")
@Tag(name = "任务信息表", description = "任务信息表接口")
public class TaskInfoController {
    @Resource
    private PageWrapper<TaskInfo> pageWrapper;
    /**
     * 服务对象
     */
    @Resource
    private TaskInfoService taskInfoService;

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
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, task_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskInfo> selectAll(@RequestBody(required = false) TaskInfo info, @RequestParam Integer current, @RequestParam Integer pageSize,
                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskInfoService.getByPage(page, info);
    }

    /**
     * 通过项目ID和用户信息查询数据
     *
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @PostMapping("getByProjectIdAndCreateBy")
    @Operation(summary = "通过项目ID和用户信息查询数据", method = "GET")
    @Parameters({@Parameter(description="项目ID", name = "projectId", required = true, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, task_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskInfo> getByProjectIdAndCreateBy( @RequestParam Serializable projectId, @RequestBody(required = false) TaskInfo info, @RequestParam Integer current, @RequestParam Integer pageSize,
                                  @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskInfoService.getByProjectIdAndInfo(page, projectId, info);
    }

    /**
     * 分页查询所有数据（根据任务集ID）
     *
     * @param id 领取人ID
     * @param current 当前页码
     * @param pageSize 页长
     * @param orderColumnList 排序字段
     * @param isDesc 是否倒序
     * @return 分页数据
     */
    @GetMapping("getByTaskSetId")
    @Operation(summary = "分页查询（根据任务集ID）", method = "GET")
    @Parameters({@Parameter(description="根据任务集ID", name = "id", required = true, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, task_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<TaskInfo> getByTaskSetId(@RequestParam Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<TaskInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.taskInfoService.getByTaskSetId(page, id);
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
    public TaskInfo selectOne(@PathVariable Serializable id) {
        return this.taskInfoService.getById(id);
    }

//    /**
//     * 新增数据
//     *
//     * @param taskInfo 实体对象
//     * @return 新增结果
//     */
//    @PostMapping
//    @Operation(summary = "新增数据", method = "POST")
//    public BaseResponse insert(@RequestBody TaskInfo taskInfo) {
//        return this.taskInfoService.save(taskInfo));
//    }

    /**
     * 新增数据（批量）
     *
     * @param request 实体对象
     * @return 新增结果
     */
    @PostMapping("batch")
    @Operation(summary = "新增数据（批量）", method = "POST")
    public boolean insert(@RequestBody TaskBatchRequest request) {
        return this.taskInfoService.saveBatch(request);
    }

    /**
     * 领取任务
     *
     * @param request 实体对象
     * @return 领取结果
     */
    @PostMapping("task")
    @Operation(summary = "领取任务", method = "POST")
    public TaskInfo getOneTask(@RequestBody TaskRequest request) {
        TaskInfo result = this.taskInfoService.getOneTaskByProjectId(request);
        if (result == null) {
            throw new RuntimeException("没有任务可领取");
        }
        return result;
    }

    /**
     * 修改数据
     *
     * @param taskInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Operation(summary = "修改数据", method = "PUT")
    public boolean update(@RequestBody TaskInfo taskInfo) {
        return this.taskInfoService.updateById(taskInfo);
    }

    /**
     * 修改任务状态，触发联动修改任务池
     *
     * @param taskInfo 实体对象
     * @return 修改结果
     */
    @PutMapping("status")
    @Operation(summary = "修改状态", method = "PUT")
    public boolean updateStatus(@RequestBody TaskInfo taskInfo) {
        return this.taskInfoService.updateStatusById(taskInfo);
    }

    /**
     * 修改数据（批量）
     *
     * @param taskInfos 实体对象
     * @return 修改结果
     */
    @PutMapping("batch")
    @Operation(summary = "修改数据（批量）", method = "PUT")
    public boolean update(@RequestBody List<TaskInfo> taskInfos) {
        return this.taskInfoService.updateBatchById(taskInfos);
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
        return this.taskInfoService.removeByIds(idList);
    }

    /**
     * 上传结果文件
     *
     * @param id 任务ID
     * @param userId 用户ID
     * @param file 文件
     * @return 修改结果
     */
    @PostMapping
    @Operation(summary = "上传结果文件", method = "POST")
    @Parameters({@Parameter(description="任务ID", name = "id", required = true, style = ParameterStyle.FORM),
            @Parameter(description="用户ID", name = "userId", required = true, style = ParameterStyle.FORM),
            @Parameter(description="文件", name = "file", required = true, style = ParameterStyle.FORM)})
    public boolean upload(@RequestParam Serializable id, @RequestParam Serializable userId, MultipartFile file) {
        return this.taskInfoService.upload(id, userId, file);
    }

    /**
     * 下载任务绑定结果文件列表
     *
     * @param id 任务ID
     * @return 修改结果
     */
    @GetMapping("download")
    @Operation(summary = "下载任务下的全部成果文件", method = "GET")
    @Parameters({@Parameter(description="任务ID", name = "id", required = true, style = ParameterStyle.FORM)})
    public void download(@RequestParam Serializable id, HttpServletResponse response) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(ResourceUtil.getUniquePrefix(id) + ".zip", "UTF-8"));

        this.taskInfoService.download(id, response.getOutputStream());
    }

}

