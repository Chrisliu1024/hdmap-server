package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.PipelineInstance;
import com.hdmap.data.etl.service.PipelineInstanceService;
import com.hdmap.data.etl.mapper.PipelineInstanceMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【pipeline_instance(数据映射任务)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class PipelineInstanceServiceImpl extends ServiceImpl<PipelineInstanceMapper, PipelineInstance>
    implements PipelineInstanceService{

}




