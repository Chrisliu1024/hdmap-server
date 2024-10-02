package com.hdmap.task.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 错误记录表
 * @TableName error_record_info
 */
@TableName(value ="error_record_info")
@Data
public class ErrorRecordInfo implements Serializable {
    /**
     * 主键ID
     */
    @TableId()
    private Integer id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 质检员ID
     */
    private Long checkerId;

    /**
     * 作业员ID
     */
    private Long producerId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 错误类型
     */
    private Integer type;

    /**
     * 错误状态：0：待修正、1：已修正/作业错误、2：无需修正/质检错误、3：待确认/争议
     */
    private Integer status;

    /**
     * 错误对象id
     */
    private Long objectId;

    /**
     * 错误对象表（lane、road等）
     */
    private String objectTable;

    /**
     * 错误对象几何信息
     */
    private Object objectGeom;

    /**
     * 错误描述，只能追加描述，质检员和作业员描述都在此字段
     */
    private String description;

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
        ErrorRecordInfo other = (ErrorRecordInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
            && (this.getCheckerId() == null ? other.getCheckerId() == null : this.getCheckerId().equals(other.getCheckerId()))
            && (this.getProducerId() == null ? other.getProducerId() == null : this.getProducerId().equals(other.getProducerId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getObjectId() == null ? other.getObjectId() == null : this.getObjectId().equals(other.getObjectId()))
            && (this.getObjectTable() == null ? other.getObjectTable() == null : this.getObjectTable().equals(other.getObjectTable()))
            && (this.getObjectGeom() == null ? other.getObjectGeom() == null : this.getObjectGeom().equals(other.getObjectGeom()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
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
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getCheckerId() == null) ? 0 : getCheckerId().hashCode());
        result = prime * result + ((getProducerId() == null) ? 0 : getProducerId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getObjectId() == null) ? 0 : getObjectId().hashCode());
        result = prime * result + ((getObjectTable() == null) ? 0 : getObjectTable().hashCode());
        result = prime * result + ((getObjectGeom() == null) ? 0 : getObjectGeom().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
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
        sb.append(", taskId=").append(taskId);
        sb.append(", checkerId=").append(checkerId);
        sb.append(", producerId=").append(producerId);
        sb.append(", projectId=").append(projectId);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", objectId=").append(objectId);
        sb.append(", objectTable=").append(objectTable);
        sb.append(", objectGeom=").append(objectGeom);
        sb.append(", description=").append(description);
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