package com.hdmap.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/2/1 18:11
 * @description: 分页包装器
 */
@Component
public class PageWrapper<T> {

    public Page<T> wrap(Integer current, Integer pageSize, List<String> orderColumnList, boolean isDesc) {
        Page<T> page = new Page<>(current, pageSize);
        List<OrderItem> orders = new ArrayList<>();
        if (orderColumnList != null && !orderColumnList.isEmpty()) {
            for (String orderColumn : orderColumnList) {
                // 驼峰转下划线
                orderColumn = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(orderColumn), "_").toLowerCase();
                if (isDesc) {
                    orders.add(OrderItem.desc(orderColumn));
                } else {
                    orders.add(OrderItem.asc(orderColumn));
                }
            }
        }
        page.setOrders(orders);
        return page;
    }
}
