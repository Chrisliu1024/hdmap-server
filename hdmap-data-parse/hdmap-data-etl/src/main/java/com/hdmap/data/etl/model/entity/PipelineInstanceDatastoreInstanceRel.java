package com.hdmap.data.etl.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.apache.ibatis.type.BooleanTypeHandler;

/**
 * 处理pipeline instance和数据存储的关联
 * @TableName pipeline_instance_datastore_instance_rel
 */
@TableName(value ="pipeline_instance_datastore_instance_rel")
@Data
public class PipelineInstanceDatastoreInstanceRel implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据处理pipeline instance ID
     */
    private Long pipelineInstanceId;

    /**
     * 数据存储实例ID
     */
    private Long datastoreInstanceId;

    /**
     * 是否是源数据存储（左）
     */
    @TableField(value = "\"left\"", typeHandler = BooleanTypeHandler.class)
    private Boolean left;

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
        PipelineInstanceDatastoreInstanceRel other = (PipelineInstanceDatastoreInstanceRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPipelineInstanceId() == null ? other.getPipelineInstanceId() == null : this.getPipelineInstanceId().equals(other.getPipelineInstanceId()))
            && (this.getDatastoreInstanceId() == null ? other.getDatastoreInstanceId() == null : this.getDatastoreInstanceId().equals(other.getDatastoreInstanceId()))
            && (this.getLeft() == null ? other.getLeft() == null : this.getLeft().equals(other.getLeft()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPipelineInstanceId() == null) ? 0 : getPipelineInstanceId().hashCode());
        result = prime * result + ((getDatastoreInstanceId() == null) ? 0 : getDatastoreInstanceId().hashCode());
        result = prime * result + ((getLeft() == null) ? 0 : getLeft().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pipelineInstanceId=").append(pipelineInstanceId);
        sb.append(", datastoreInstanceId=").append(datastoreInstanceId);
        sb.append(", left=").append(left);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}