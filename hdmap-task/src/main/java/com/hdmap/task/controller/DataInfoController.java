package com.hdmap.task.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdmap.task.model.entity.DataInfo;
import com.hdmap.task.service.DataInfoService;
import com.hdmap.mybatis.utils.PageWrapper;
import com.hdmap.pointcloud.service.FileHandleService;
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
import java.util.List;

/**
 * 数据信息表(DataInfo)表控制层
 *
 * @author admin
 * @since 2024-01-29 14:08:06
 */
@RestController
@RequestMapping("dataInfo")
@Tag(name = "数据信息表", description = "数据信息表接口")
public class DataInfoController {

    @Resource
    private PageWrapper<DataInfo> pageWrapper;
    /**
     ** 服务对象
     */
    @Resource
    private DataInfoService dataInfoService;

    @Resource
    private FileHandleService fileHandleService;

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
    public IPage<DataInfo> selectAll(@RequestBody(required = false) DataInfo info, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DataInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.dataInfoService.page(page, new QueryWrapper<>(info));
    }

    /**
     * 通过项目主键查询数据
     *
     * @param id 主键
     * @return 数据列表
     */
    @GetMapping("projectId/{id}")
    @Operation(summary = "通过项目ID查询数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, data_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<DataInfo> selectListByProjectId(@PathVariable Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize,
                                              @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DataInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.dataInfoService.getByProjectId(page, id);
    }

/**
     * 通过数据集主键查询数据
     *
     * @param id 数据集主键
     * @return 数据列表
     */
    @GetMapping("datasetId/{id}")
    @Operation(summary = "通过数据集ID查询数据", method = "GET")
    @Parameters({@Parameter(description="数据集主键", name = "id", required = true, style = ParameterStyle.FORM),
            @Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, data_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<DataInfo> selectByDataSetId(@PathVariable Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize,
                                          @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DataInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.dataInfoService.getByDataSetId(page, id);
    }

    /**
     * 通过任务ID获取任务绑定结果文件列表
     *
     * @param id 任务ID
     * @return 修改结果
     */
    @GetMapping("taskId/{id}")
    @Operation(summary = "通过任务ID获取任务绑定结果文件列表", method = "GET")
    @Parameters({@Parameter(description="当前页", name = "current", required = true, style = ParameterStyle.FORM),
            @Parameter(description="页长", name = "pageSize", required = true, style = ParameterStyle.FORM),
            @Parameter(description="排序字段", name = "orderColumnList", required = false, style = ParameterStyle.FORM, example = "id, data_id"),
            @Parameter(description="是否倒序", name = "isDesc", required = false, style = ParameterStyle.FORM)})
    public IPage<DataInfo> selectResult(@PathVariable Serializable id, @RequestParam Integer current, @RequestParam Integer pageSize, @RequestParam(required = false) List<String> orderColumnList, @RequestParam(required = false) boolean isDesc) {
        IPage<DataInfo> page = pageWrapper.wrap(current, pageSize, orderColumnList, isDesc);
        return this.dataInfoService.getResultDataInfoByTaskId(page, id);
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
    public DataInfo selectOne(@PathVariable Serializable id) {
        return this.dataInfoService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param dataInfo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @Operation(summary = "新增数据", method = "POST")
    public boolean insert(@RequestBody DataInfo dataInfo) {
        return this.dataInfoService.save(dataInfo);
    }

    /**
     * 修改数据
     *
     * @param dataInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @Operation(summary = "修改数据", method = "PUT")
    public boolean update(@RequestBody DataInfo dataInfo) {
        return this.dataInfoService.updateById(dataInfo);
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
        return this.dataInfoService.removeByIdsCustom(idList);
    }


    /**
     * 通过主键下载单条数据下的文件
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("download/dataId/{id}")
    @Operation(summary = "通过数据ID下载数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public void downloadOneByDataId(@PathVariable Serializable id, HttpServletResponse response) throws Exception {
        this.dataInfoService.downloadById(id, response);
    }

    /**
     * 通过任务主键下载单条数据下的文件
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("download/taskId/{id}")
    @Operation(summary = "通过任务ID下载数据", method = "GET")
    @Parameters({@Parameter(description="主键", name = "id", required = true, style = ParameterStyle.FORM)})
    public void downloadOneByTaskId(@PathVariable Serializable id, HttpServletResponse response) throws Exception {
        this.dataInfoService.downloadByTaskId(id, response);
    }

    @PostMapping("upload")
    @Operation(summary = "数据上传", method = "POST")
    @Parameters({@Parameter(description="数据集ID", name = "datasetId", required = true, style = ParameterStyle.FORM),
            @Parameter(description="上传的文件", name = "file", required = true, style = ParameterStyle.FORM),
            @Parameter(description="文件来源", name = "sourceLocation", required = false, style = ParameterStyle.FORM)})
    public boolean upload(@RequestParam Serializable datasetId, MultipartFile file, @RequestParam(required = false) String sourceLocation) throws Exception {
        // store file to local
        String localPath = this.fileHandleService.storeFileToLocal(file);
        boolean uploaded = this.dataInfoService.uploadAndHandle(datasetId, localPath, sourceLocation);
        // delete local file
        boolean deleted = this.fileHandleService.deleteFile(localPath);
        return uploaded && deleted;
    }


}

