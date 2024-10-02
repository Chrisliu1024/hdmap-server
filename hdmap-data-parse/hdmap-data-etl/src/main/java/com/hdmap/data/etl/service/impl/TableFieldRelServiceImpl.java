package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.TableFieldRel;
import com.hdmap.data.etl.service.TableFieldRelService;
import com.hdmap.data.etl.mapper.TableFieldRelMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【table_field_rel(表和字段的关联)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class TableFieldRelServiceImpl extends ServiceImpl<TableFieldRelMapper, TableFieldRel>
    implements TableFieldRelService{

}




