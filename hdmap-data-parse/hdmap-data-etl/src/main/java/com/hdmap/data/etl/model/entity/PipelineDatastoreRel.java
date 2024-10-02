package com.hdmap.data.etl.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import org.apache.ibatis.type.BooleanTypeHandler;

/**
 * 处理pipeline和数据存储的关联
 * @TableName pipeline_datastore_rel
 */
@TableName(value ="pipeline_datastore_rel")
@Data
public class PipelineDatastoreRel implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 数据处理pipeline ID
     */
    private Long pipelineId;

    /**
     * 数据存储ID
     */
    private Long datastoreId;

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
        PipelineDatastoreRel other = (PipelineDatastoreRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPipelineId() == null ? other.getPipelineId() == null : this.getPipelineId().equals(other.getPipelineId()))
            && (this.getDatastoreId() == null ? other.getDatastoreId() == null : this.getDatastoreId().equals(other.getDatastoreId()))
            && (this.getLeft() == null ? other.getLeft() == null : this.getLeft().equals(other.getLeft()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPipelineId() == null) ? 0 : getPipelineId().hashCode());
        result = prime * result + ((getDatastoreId() == null) ? 0 : getDatastoreId().hashCode());
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
        sb.append(", pipelineId=").append(pipelineId);
        sb.append(", datastoreId=").append(datastoreId);
        sb.append(", left=").append(left);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}