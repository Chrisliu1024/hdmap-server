package com.hdmap.data.etl.service;

import com.hdmap.data.etl.model.dto.TablesRelInfo;
import com.hdmap.data.etl.model.dto.TablesRelResult;
import com.hdmap.data.etl.model.entity.TablesRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author admin
* @description 针对表【tables_rel(表关联)】的数据库操作Service
* @createDate 2024-07-05 16:38:03
*/
public interface TablesRelService extends IService<TablesRel> {

    TablesRelResult save(TablesRelInfo info);

}
