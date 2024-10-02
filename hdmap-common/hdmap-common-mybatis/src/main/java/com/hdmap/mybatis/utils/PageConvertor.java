package com.hdmap.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/2/4 09:53
 * @description: 分页转换器
 */

@Component
public class PageConvertor<T, R> {

        public IPage<R> convert(IPage<T> page, List<R> records) {
            IPage<R> iPage = new Page<>();
            iPage.setTotal(page.getTotal());
            iPage.setPages(page.getPages());
            iPage.setSize(page.getSize());
            iPage.setCurrent(page.getCurrent());
            iPage.setRecords(records);
            return iPage;
        }
}
