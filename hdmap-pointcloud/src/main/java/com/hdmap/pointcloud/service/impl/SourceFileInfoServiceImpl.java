package com.hdmap.pointcloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdmap.pointcloud.entity.SourceFileInfo;
import com.hdmap.pointcloud.mapper.SourceFileInfoMapper;
import com.hdmap.pointcloud.service.SourceFileInfoService;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【source_file_info(上传原始文件信息表，记录文件基础信息及状态)】的数据库操作Service实现
* @createDate 2024-04-17 16:51:45
*/
@Service
public class SourceFileInfoServiceImpl extends ServiceImpl<SourceFileInfoMapper, SourceFileInfo>
    implements SourceFileInfoService {

}




