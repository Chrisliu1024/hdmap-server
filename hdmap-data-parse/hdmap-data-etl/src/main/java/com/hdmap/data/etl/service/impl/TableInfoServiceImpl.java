package com.hdmap.data.etl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.data.etl.model.entity.TableInfo;
import com.hdmap.data.etl.service.TableInfoService;
import com.hdmap.data.etl.mapper.TableInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【table_info(表信息)】的数据库操作Service实现
* @createDate 2024-07-05 16:38:03
*/
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo>
    implements TableInfoService{

}




