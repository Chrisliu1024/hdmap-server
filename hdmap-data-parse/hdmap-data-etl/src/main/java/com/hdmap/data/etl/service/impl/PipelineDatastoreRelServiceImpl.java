package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.dto.PipelineDatastoreRelInfo;
import com.hdmap.data.etl.model.dto.PipelineDatastoreRelResult;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.model.entity.Pipeline;
import com.hdmap.data.etl.model.entity.PipelineDatastoreRel;
import com.hdmap.data.etl.service.DatastoreInfoService;
import com.hdmap.data.etl.service.PipelineDatastoreRelService;
import com.hdmap.data.etl.mapper.PipelineDatastoreRelMapper;
import com.hdmap.data.etl.service.PipelineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author admin
* @description 针对表【pipeline_datastore_rel(处理pipeline和数据存储的关联)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class PipelineDatastoreRelServiceImpl extends ServiceImpl<PipelineDatastoreRelMapper, PipelineDatastoreRel>
    implements PipelineDatastoreRelService{

    @Resource
    private PipelineService pipelineService;
    @Resource
    private DatastoreInfoService datastoreInfoService;

    @Override
    public PipelineDatastoreRelResult save(PipelineDatastoreRelInfo info) {
        Pipeline pipeline = info.getPipeline();
        List<DatastoreInfo> datastoreInfos = info.getDatastoreList();
        Boolean left = info.getLeft();
        boolean pipelineDatastoreRelExist = true;
        if (pipeline != null && pipeline.getId() == null) {
            pipelineService.save(pipeline);
            pipelineDatastoreRelExist = false;
        }
        List<Long> datastoreIds = new ArrayList<>();
        for (DatastoreInfo datastoreInfo : datastoreInfos) {
            if (datastoreInfo != null) {
                continue;
            }
            if (datastoreInfo.getId() == null) {
                datastoreInfoService.save(datastoreInfo);
                pipelineDatastoreRelExist = false;

            }
            if (!pipelineDatastoreRelExist) {
                // 创建关联
                createPipelineDatastoreRel(pipeline.getId(), datastoreInfo.getId(), left);
            }
            datastoreIds.add(datastoreInfo.getId());
        }
        PipelineDatastoreRelResult result = new PipelineDatastoreRelResult();
        result.setPipelineId(pipeline.getId());
        result.setDatastoreIdList(datastoreIds);
        return result;
    }

    private void createPipelineDatastoreRel(Long pipelineId, Long datastoreId, Boolean left) {
        PipelineDatastoreRel pipelineDatastoreRel = new PipelineDatastoreRel();
        pipelineDatastoreRel.setPipelineId(pipelineId);
        pipelineDatastoreRel.setDatastoreId(datastoreId);
        pipelineDatastoreRel.setLeft(left);
        save(pipelineDatastoreRel);
    }

}




