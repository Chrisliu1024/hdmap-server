package com.hdmap.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hdmap.task.model.entity.DataInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【data_info(数据信息表)】的数据库操作Service
* @createDate 2024-01-29 15:58:35
*/
public interface DataInfoService extends IService<DataInfo> {
    IPage<DataInfo> getByDataSetId(IPage<DataInfo> page, Serializable id);

    IPage<DataInfo> getByProjectId(IPage<DataInfo> page, Serializable id);

    IPage<DataInfo> getResultDataInfoByTaskId(IPage<DataInfo> page, Serializable id);

    DataInfo getByDataSetIdAndGeohashAndType(Serializable id, String geohash, Integer type);

    boolean removeByIdsCustom(List<Serializable> idList);

    String downloadToLocalById(Serializable id) throws Exception;

    String downloadById(Serializable id, HttpServletResponse outputStream) throws Exception;

    String downloadByTaskId(Serializable id, HttpServletResponse response) throws Exception;

    boolean uploadAndHandle(Serializable datasetId, String localPath, String sourceLocation) throws Exception;

    void uploadAndSave(Serializable datasetId, String localPath, String geohash) throws Exception;

}
