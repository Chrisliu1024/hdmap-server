package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.Pipeline;
import com.hdmap.data.etl.service.PipelineService;
import com.hdmap.data.etl.mapper.PipelineMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【pipeline(处理管线)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class PipelineServiceImpl extends ServiceImpl<PipelineMapper, Pipeline>
    implements PipelineService{

}




