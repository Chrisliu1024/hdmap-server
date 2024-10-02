package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.dto.TaskBatchRequest;
import com.hdmap.task.model.dto.TaskRequest;
import com.hdmap.task.model.entity.TaskInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.Serializable;

/**
* @author admin
* @description 针对表【task_info(任务信息表)】的数据库操作Service
* @createDate 2024-01-29 15:58:35
*/
public interface TaskInfoService extends IService<TaskInfo> {

    boolean saveBatch(TaskBatchRequest request);
    IPage<TaskInfo> getByPage(IPage<TaskInfo> page, TaskInfo info);
    IPage<TaskInfo> getByProjectIdAndInfo(IPage<TaskInfo> page, Serializable id, TaskInfo info);
    IPage<TaskInfo> getByTaskSetId(IPage<TaskInfo> page, Serializable id);
    TaskInfo getOneTaskByProjectId(TaskRequest request);
    boolean updateStatusById(TaskInfo info);
    boolean upload(Serializable id, Serializable userId, MultipartFile file);
    void download(Serializable id, ServletOutputStream outputStream) throws Exception;
}
