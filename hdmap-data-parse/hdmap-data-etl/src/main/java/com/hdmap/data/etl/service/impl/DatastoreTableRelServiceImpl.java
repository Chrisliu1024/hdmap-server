package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.DatastoreTableRel;
import com.hdmap.data.etl.service.DatastoreTableRelService;
import com.hdmap.data.etl.mapper.DatastoreTableRelMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【datastore_table_rel(数据存储模板与表的关联)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class DatastoreTableRelServiceImpl extends ServiceImpl<DatastoreTableRelMapper, DatastoreTableRel>
    implements DatastoreTableRelService{

}




