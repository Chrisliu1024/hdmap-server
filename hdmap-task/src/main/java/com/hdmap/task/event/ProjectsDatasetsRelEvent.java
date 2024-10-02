package com.hdmap.task.event;

import com.hdmap.task.model.entity.ProjectsDatasetsRel;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/23 14:46
 * @description: 项目-数据集关联事件
 */
public class ProjectsDatasetsRelEvent extends ApplicationEvent {

    private List<ProjectsDatasetsRel> entitys;

    public ProjectsDatasetsRelEvent(Object source, List<ProjectsDatasetsRel> entitys) {
        super(source);
        this.entitys = entitys;
    }

    public List<ProjectsDatasetsRel> getEntitys() {
        return entitys;
    }


}
