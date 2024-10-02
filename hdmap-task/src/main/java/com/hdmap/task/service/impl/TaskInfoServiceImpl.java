package com.hdmap.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.core.utils.SnowFlakeIdUtil;
import com.hdmap.pointcloud.service.FileHandleService;
import com.hdmap.pointcloud.utils.ResourceUtil;
import com.hdmap.task.enums.DataTypeEnum;
import com.hdmap.task.enums.TaskStatusEnum;
import com.hdmap.task.enums.UserRoleEnum;
import com.hdmap.task.event.TaskInfoStatusChangeListener;
import com.hdmap.task.mapper.TaskInfoMapper;
import com.hdmap.task.model.dto.TaskBatchRequest;
import com.hdmap.task.model.dto.TaskRequest;
import com.hdmap.task.model.entity.*;
import com.hdmap.task.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* @author admin
* @description 针对表【task_info(任务信息表)】的数据库操作Service实现
* @createDate 2024-02-02 18:29:14
*/
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo>
    implements TaskInfoService {
    @Resource
    private TaskIbdDataRelService taskIbdDataRelService;
    @Resource
    private DataInfoService dataInfoService;
    @Resource
    private TasksTaskSetsRelService tasksTaskSetsRelService;
    @Resource
    private FileHandleService fileHandleService;
    @Resource
    private TaskInfoStatusChangeListener taskInfoStatusChangeListener;
    @Resource
    private TaskPoolInfoService taskPoolInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(TaskBatchRequest request) {
        // 保存任务信息
        saveBatch(request.getTasks());
        // 保存关联关系
        Long taskSetId = request.getTaskSetId();
        List<TasksTaskSetsRel> relList = new ArrayList<>();
        for (TaskInfo taskInfo : request.getTasks()) {
            TasksTaskSetsRel rel = new TasksTaskSetsRel();
            rel.setTaskId(taskInfo.getId());
            rel.setTaskSetId(taskSetId);
            relList.add(rel);
        }
        this.tasksTaskSetsRelService.saveBatch(relList);
        return true;
    }

    @Override
    public IPage<TaskInfo> getByPage(IPage<TaskInfo> page, TaskInfo info) {
        return page(page, new QueryWrapper<>(info));
    }

    @Override
    public IPage<TaskInfo> getByProjectIdAndInfo(IPage<TaskInfo> page, Serializable id, TaskInfo info) {
        return this.baseMapper.selectByProjectIdAndInfo(page, id, info);
    }

    @Override
    public IPage<TaskInfo> getByTaskSetId(IPage<TaskInfo> page, Serializable id) {
        return this.baseMapper.selectByTaskSetId(page, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskInfo getOneTaskByProjectId(TaskRequest request) {
        // 创建任务信息
        TaskInfo taskInfo = new TaskInfo(taskInfoStatusChangeListener);
        taskInfo.setId(SnowFlakeIdUtil.getId());
        Serializable status;
        Integer updateStatus;
        if (UserRoleEnum.OPERATOR.getDesc().equals(request.getRole())) {
            status = TaskStatusEnum.UNASSIGNED.getCode();
            updateStatus = TaskStatusEnum.ASSIGNED.getCode();
            taskInfo.setProducerId(request.getUserId());
        } else if (UserRoleEnum.CHECKER.getDesc().equals(request.getRole())) {
            status = TaskStatusEnum.TO_BE_QUALITY_CHECKED.getCode();
            updateStatus = TaskStatusEnum.CHECKING.getCode();
            taskInfo.setCheckerId(request.getUserId());
        } else {
            throw new IllegalArgumentException("不支持该角色");
        }

        // 从任务池中获取一个任务
        TaskPoolInfo taskPoolInfo = this.taskPoolInfoService.getOneTaskByProjectIdAndStatus(request.getProjectId(), status);
        if (taskPoolInfo == null) {
            return null;
        }
        // 关联任务池
        taskPoolInfo.setTaskId(taskInfo.getId());
        this.taskPoolInfoService.updateById(taskPoolInfo);
        // 更新任务信息
        taskInfo.setStatus(updateStatus);
        taskInfo.setPriority(taskPoolInfo.getPriority());
        taskInfo.setCreateBy(request.getUserId());
        // 保存任务信息
        save(taskInfo);
        return taskInfo;
    }

    public boolean updateStatusById(TaskInfo info) {
        TaskInfo linkInfo = BeanUtil.copyProperties(info, TaskInfo.class);
        // 更新任务信息（主动触发）
        linkInfo.registerChangeListener(taskInfoStatusChangeListener);
        linkInfo.setStatus(info.getStatus());
        return updateById(linkInfo);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean upload(Serializable id, Serializable userId, MultipartFile file) {
        // 上传文件
        String cloudPath = fileHandleService.storeFileToCloud(file);
        // 更新任务信息
        TaskInfo taskInfo = new TaskInfo(taskInfoStatusChangeListener);;
        taskInfo.setId(Long.valueOf(id.toString()));
        //taskInfo.setStatus(TaskStatusEnum.COMPLETED.getCode());
        taskInfo.setUpdateBy(Long.valueOf(userId.toString()));
        updateById(taskInfo);
        // 新建数据信息
        DataInfo dataInfo = new DataInfo();
        dataInfo.setId(SnowFlakeIdUtil.getId());
        dataInfo.setName(file.getOriginalFilename());
        dataInfo.setType(DataTypeEnum.IBD.getCode());
        dataInfo.setFileName(ResourceUtil.getWholeFileNameByPath(cloudPath));
        dataInfo.setLocation(cloudPath);
        dataInfo.setCreateBy(Long.valueOf(userId.toString()));
        dataInfoService.save(dataInfo);
        // 保存任务-IBD文件关联关系
        TaskIbdDataRel taskIbdDataRel = new TaskIbdDataRel();
        taskIbdDataRel.setTaskId(Long.valueOf(id.toString()));
        taskIbdDataRel.setDataId(dataInfo.getId());
        taskIbdDataRelService.save(taskIbdDataRel);
        return true;
    }

    @Override
    public void download(Serializable id, ServletOutputStream outputStream) throws Exception {
        TaskIbdDataRel rel = new TaskIbdDataRel();
        rel.setTaskId(Long.valueOf(id.toString()));
        List<TaskIbdDataRel> rels = taskIbdDataRelService.list(new QueryWrapper<>(rel));
        if (rels.isEmpty()) {
            return;
        }
        List<String> localPaths = new ArrayList<>();
        for (TaskIbdDataRel taskIbdDataRel : rels) {
            Long dataId = taskIbdDataRel.getDataId();
            String localPath = dataInfoService.downloadToLocalById(dataId);
            localPaths.add(localPath);
        }
        fileHandleService.zipFiles(localPaths, outputStream);
        // 删除本地文件及所在的文件夹
        for (String localPath : localPaths) {
            fileHandleService.deleteFile(localPath);
            // 删除文件夹
            fileHandleService.deleteFile(localPath.substring(0, localPath.lastIndexOf("/")));
        }
    }

}




