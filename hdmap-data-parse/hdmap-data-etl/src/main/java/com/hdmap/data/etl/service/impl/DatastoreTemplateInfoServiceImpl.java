package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.DatastoreTemplateInfo;
import com.hdmap.data.etl.service.DatastoreTemplateInfoService;
import com.hdmap.data.etl.mapper.DatastoreTemplateInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author admin
* @description 针对表【datastore_template_info(数据存储信息-模板)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class DatastoreTemplateInfoServiceImpl extends ServiceImpl<DatastoreTemplateInfoMapper, DatastoreTemplateInfo>
    implements DatastoreTemplateInfoService{

    @Override
    public List<DatastoreTemplateInfo> getByType(Integer type) {
        DatastoreTemplateInfo info = new DatastoreTemplateInfo();
        info.setType(type);
        return this.baseMapper.selectList(new QueryWrapper<>(info));
    }
}




