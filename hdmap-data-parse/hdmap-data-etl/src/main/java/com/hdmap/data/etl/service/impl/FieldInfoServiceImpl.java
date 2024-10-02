package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.FieldInfo;
import com.hdmap.data.etl.service.FieldInfoService;
import com.hdmap.data.etl.mapper.FieldInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【field_info(字段信息)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class FieldInfoServiceImpl extends ServiceImpl<FieldInfoMapper, FieldInfo>
    implements FieldInfoService{

}




