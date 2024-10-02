package com.hdmap.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.mybatis.utils.PageConvertor;
import com.hdmap.task.service.TaskInfoService;
import com.hdmap.task.service.TaskPoolInfoService;
import com.hdmap.task.model.dto.TaskPoolResponse;
import com.hdmap.task.model.entity.TaskInfo;
import com.hdmap.task.model.entity.TaskPoolInfo;
import com.hdmap.task.mapper.TaskPoolInfoMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* @author admin
* @description 针对表【task_pool_info(任务池表，逻辑表，用于关联项目与数据，并记录数据的分配状态)】的数据库操作Service实现
* @createDate 2024-05-15 16:46:32
*/
@Service
public class TaskPoolInfoServiceImpl extends ServiceImpl<TaskPoolInfoMapper, TaskPoolInfo>
    implements TaskPoolInfoService {

    @Resource
    @Lazy
    private TaskInfoService taskService;

    @Resource
    private PageConvertor<TaskPoolInfo, TaskPoolResponse> pageConvertor;

    @Override
    public IPage<TaskPoolInfo> getShortByPage(IPage<TaskPoolInfo> page, TaskPoolInfo info) {
        return page(page, new QueryWrapper<>(info));
    }

    @Override
    @Transactional
    public IPage<TaskPoolResponse> getDetailByPage(IPage<TaskPoolInfo> page, TaskPoolInfo info) {
        IPage<TaskPoolInfo> pageResult = page(page, new QueryWrapper<>(info));
        List<TaskPoolInfo> records = pageResult.getRecords();
        // 遍历，若taskId存在，则查询task表，将task信息放入TaskPoolResponse中
        List<TaskPoolResponse> taskPoolResponses = new ArrayList<>();
        for (TaskPoolInfo taskPoolInfo : records) {
            TaskPoolResponse taskPoolResponse = BeanUtil.toBean(taskPoolInfo, TaskPoolResponse.class);
            Long taskId = taskPoolInfo.getTaskId();
            if (taskId != null) {
                TaskInfo taskInfo = taskService.getById(taskId);
                taskPoolResponse.setTaskInfo(taskInfo);
            }
            taskPoolResponses.add(taskPoolResponse);
        }
        return pageConvertor.convert(pageResult, taskPoolResponses);
    }

    @Override
    public TaskPoolInfo getByTaskId(Serializable id) {
        TaskPoolInfo taskPoolInfo = new TaskPoolInfo();
        taskPoolInfo.setTaskId(Long.valueOf(id.toString()));
        return this.baseMapper.selectOne(new QueryWrapper<>(taskPoolInfo));
    }

    @Override
    public TaskPoolInfo getOneTaskByProjectIdAndStatus(Serializable id, Serializable status) {
        return getOneTaskByProjectIdAndStatus(id, status, 1);
    }

    @Override
    public TaskPoolInfo getOneTaskByProjectIdAndStatus(Serializable id, Serializable status, int limit) {
        // 按优先级排序，升序，选择status的一个任务
        return this.baseMapper.getOneTaskByProjectIdAndStatus(id, status, limit);
    }

    @Override
    public boolean deleteBatchByProjectIdAndDatasetIdsAndStatus(Serializable projectId, List<Serializable> datasetIds, Integer status) {
        return this.baseMapper.deleteBatchByProjectIdAndDatasetIdsAndStatus(projectId, datasetIds, status);
    }

    @Override
    public List<TaskPoolInfo> getTaskByProjectIdAndDatasetIdAndStatus(Serializable projectId, Serializable datasetId, Serializable status) {
        TaskPoolInfo taskPoolInfo = new TaskPoolInfo();
        taskPoolInfo.setProjectId(Long.valueOf(projectId.toString()));
        taskPoolInfo.setDatasetId(Long.valueOf(datasetId.toString()));
        taskPoolInfo.setStatus(Integer.valueOf(status.toString()));
        return this.baseMapper.selectList(new QueryWrapper<>(taskPoolInfo));
    }

}




