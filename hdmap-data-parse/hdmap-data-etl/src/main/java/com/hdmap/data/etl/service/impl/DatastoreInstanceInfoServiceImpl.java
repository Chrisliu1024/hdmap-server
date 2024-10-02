package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.DatastoreInstanceInfo;
import com.hdmap.data.etl.service.DatastoreInstanceInfoService;
import com.hdmap.data.etl.mapper.DatastoreInstanceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【datastore_instance_info】的数据库操作Service实现
* @createDate 2024-07-10 17:20:31
*/
@Service
public class DatastoreInstanceInfoServiceImpl extends ServiceImpl<DatastoreInstanceInfoMapper, DatastoreInstanceInfo>
    implements DatastoreInstanceInfoService{

}




