package com.hdmap.task.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdmap.task.event.TaskInfoChangeEvent;
import com.hdmap.task.event.TaskInfoChangeListener;
import com.hdmap.task.event.TaskInfoStatusChangeEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务信息表
 * @TableName task_info
 */
@TableName(value ="task_info")
@Data
@NoArgsConstructor
public class TaskInfo implements Serializable {
    /**
     * 任务ID
     */
    @TableId()
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态：0：结束 1：待领取 2：已领取 3：作业中 4：已过期
     */
    private Integer status;

    /**
     * 优先级：0 高，1 中，2 低
     */
    private Integer priority;

    /**
     * 质检员ID
     */
    private Long checkerId;

    /**
     * 作业员ID
     */
    private Long producerId;

    /**
     * 采集员ID
     */
    private Long collectorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 备注
     */
    private String memo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private TaskInfoChangeListener listener;

    public TaskInfo(TaskInfoChangeListener listener) {
        this.listener = listener;
    }

    public void setStatus(Integer status) {
        Integer oldStatus = this.status;
        this.status = status;
        changeEventNotify(new TaskInfoStatusChangeEvent(this, oldStatus, this.status));
    }

    public void registerChangeListener(TaskInfoChangeListener listener) {
        this.listener = listener;
    }

    private void changeEventNotify(TaskInfoChangeEvent event) {
        if (listener != null) {
            listener.changed(event);
        }
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TaskInfo other = (TaskInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPriority() == null ? other.getPriority() == null : this.getPriority().equals(other.getPriority()))
            && (this.getCheckerId() == null ? other.getCheckerId() == null : this.getCheckerId().equals(other.getCheckerId()))
            && (this.getProducerId() == null ? other.getProducerId() == null : this.getProducerId().equals(other.getProducerId()))
            && (this.getCollectorId() == null ? other.getCollectorId() == null : this.getCollectorId().equals(other.getCollectorId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
        result = prime * result + ((getCheckerId() == null) ? 0 : getCheckerId().hashCode());
        result = prime * result + ((getProducerId() == null) ? 0 : getProducerId().hashCode());
        result = prime * result + ((getCollectorId() == null) ? 0 : getCollectorId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", priority=").append(priority);
        sb.append(", checkerId=").append(checkerId);
        sb.append(", producerId=").append(producerId);
        sb.append(", collectorId=").append(collectorId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", memo=").append(memo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}