package com.hdmap.data.etl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.dto.DatastoreAndLeftRel;
import com.hdmap.data.etl.model.dto.PipelineInstanceAndDatastore;
import com.hdmap.data.etl.model.entity.DatastoreInstanceInfo;
import com.hdmap.data.etl.model.entity.Pipeline;
import com.hdmap.data.etl.model.entity.PipelineInstance;
import com.hdmap.data.etl.model.entity.PipelineInstanceDatastoreInstanceRel;
import com.hdmap.data.etl.service.DatastoreInstanceInfoService;
import com.hdmap.data.etl.service.PipelineInstanceDatastoreInstanceRelService;
import com.hdmap.data.etl.mapper.PipelineInstanceDatastoreInstanceRelMapper;
import com.hdmap.data.etl.service.PipelineInstanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author admin
* @description 针对表【pipeline_instance_datastore_instance_rel(处理pipeline instance和数据存储的关联)】的数据库操作Service实现
* @createDate 2024-07-10 10:45:44
*/
@Service
public class PipelineInstanceDatastoreInstanceRelServiceImpl extends ServiceImpl<PipelineInstanceDatastoreInstanceRelMapper, PipelineInstanceDatastoreInstanceRel>
    implements PipelineInstanceDatastoreInstanceRelService{

    @Resource
    private PipelineInstanceService pipelineInstanceService;
    @Resource
    private DatastoreInstanceInfoService datastoreInstanceInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PipelineInstanceAndDatastore save(PipelineInstanceAndDatastore info) {
        PipelineInstance pipelineInstance = BeanUtil.copyProperties(info, PipelineInstance.class);
        if (pipelineInstance != null && pipelineInstance.getId() != null) {
            pipelineInstanceService.updateById(pipelineInstance);
        } else {
            pipelineInstanceService.save(pipelineInstance);
            // complete the id
            info.setId(pipelineInstance.getId());
        }
        List<DatastoreAndLeftRel> datastoreAndLeftRelList = info.getDatastoreAndLeftRelList();
        for (DatastoreAndLeftRel datastoreAndLeftRel : datastoreAndLeftRelList) {
            DatastoreInstanceInfo datastoreInstanceInfo = BeanUtil.copyProperties(datastoreAndLeftRel, DatastoreInstanceInfo.class);
            if (datastoreInstanceInfo != null && datastoreInstanceInfo.getId() != null) {
                datastoreInstanceInfoService.updateById(datastoreInstanceInfo);
            } else {
                datastoreInstanceInfoService.save(datastoreInstanceInfo);
                // complete the id
                datastoreAndLeftRel.setId(datastoreInstanceInfo.getId());
            }
            PipelineInstanceDatastoreInstanceRel pipelineInstanceDatastoreInstanceRel = new PipelineInstanceDatastoreInstanceRel();
            pipelineInstanceDatastoreInstanceRel.setPipelineInstanceId(pipelineInstance.getId());
            pipelineInstanceDatastoreInstanceRel.setDatastoreInstanceId(datastoreInstanceInfo.getId());
            pipelineInstanceDatastoreInstanceRel.setLeft(datastoreAndLeftRel.getLeft());
            save(pipelineInstanceDatastoreInstanceRel);
        }
        return info;
    }

}




