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
 * @TableName pipeline_instance_datastore_rel
 */
@TableName(value ="pipeline_instance_datastore_rel")
@Data
public class PipelineInstanceDatastoreRel implements Serializable {
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
     * 数据存储ID
     */
    private Long datastoreId;

    /**
     * 是否是源数据存储（左）
     */
    @TableField(value = "\"left\"", typeHandler = BooleanTypeHandler.class)
    private Boolean left;

    /**
     * 同隶属同侧时，标识序列号
     */
    private Short sequence;

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
        PipelineInstanceDatastoreRel other = (PipelineInstanceDatastoreRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPipelineInstanceId() == null ? other.getPipelineInstanceId() == null : this.getPipelineInstanceId().equals(other.getPipelineInstanceId()))
            && (this.getDatastoreId() == null ? other.getDatastoreId() == null : this.getDatastoreId().equals(other.getDatastoreId()))
            && (this.getLeft() == null ? other.getLeft() == null : this.getLeft().equals(other.getLeft()))
            && (this.getSequence() == null ? other.getSequence() == null : this.getSequence().equals(other.getSequence()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPipelineInstanceId() == null) ? 0 : getPipelineInstanceId().hashCode());
        result = prime * result + ((getDatastoreId() == null) ? 0 : getDatastoreId().hashCode());
        result = prime * result + ((getLeft() == null) ? 0 : getLeft().hashCode());
        result = prime * result + ((getSequence() == null) ? 0 : getSequence().hashCode());
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
        sb.append(", datastoreId=").append(datastoreId);
        sb.append(", left=").append(left);
        sb.append(", sequence=").append(sequence);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}