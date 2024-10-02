package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.dto.DatastoreAndLeft;
import com.hdmap.data.etl.model.entity.DatastoreInfo;
import com.hdmap.data.etl.service.DatastoreInfoService;
import com.hdmap.data.etl.mapper.DatastoreInfoMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
* @author admin
* @description 针对表【datastore_info(数据存储信息)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class DatastoreInfoServiceImpl extends ServiceImpl<DatastoreInfoMapper, DatastoreInfo>
    implements DatastoreInfoService{

    @Override
    public List<DatastoreAndLeft> getByPipelineId(Serializable id) {
        return this.baseMapper.getByPipelineId(id);
    }

}




