package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.entity.DatastoreTemplateInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author admin
* @description 针对表【datastore_template_info(数据存储信息-模板)】的数据库操作Service
* @createDate 2024-07-05 16:38:03
*/
public interface DatastoreTemplateInfoService extends IService<DatastoreTemplateInfo> {

    List<DatastoreTemplateInfo> getByType(Integer type);

}
