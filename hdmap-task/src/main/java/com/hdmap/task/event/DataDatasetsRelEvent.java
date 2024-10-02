package com.hdmap.task.event;

import com.hdmap.task.model.entity.DataDatasetsRel;
import org.springframework.context.ApplicationEvent;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/23 15:00
 * @description: 数据集-数据关联事件
 */
public class DataDatasetsRelEvent extends ApplicationEvent {

    DataDatasetsRel entity;

    public DataDatasetsRelEvent(Object source, DataDatasetsRel entity) {
        super(source);
        this.entity = entity;
    }

    public DataDatasetsRel getEntity() {
        return entity;
    }

}
