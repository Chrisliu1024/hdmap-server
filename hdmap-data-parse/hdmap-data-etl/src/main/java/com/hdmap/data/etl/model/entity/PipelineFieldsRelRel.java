package com.hdmap.data.etl.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * pipeline关联field的映射的关联
 * @TableName pipeline_fields_rel_rel
 */
@TableName(value ="pipeline_fields_rel_rel")
@Data
public class PipelineFieldsRelRel implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 处理管线pipeline ID
     */
    private Long pipelineId;

    /**
     * 字段关联的ID
     */
    private Long fieldsRelId;

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
        PipelineFieldsRelRel other = (PipelineFieldsRelRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPipelineId() == null ? other.getPipelineId() == null : this.getPipelineId().equals(other.getPipelineId()))
            && (this.getFieldsRelId() == null ? other.getFieldsRelId() == null : this.getFieldsRelId().equals(other.getFieldsRelId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPipelineId() == null) ? 0 : getPipelineId().hashCode());
        result = prime * result + ((getFieldsRelId() == null) ? 0 : getFieldsRelId().hashCode());
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
        sb.append(", fieldsRelId=").append(fieldsRelId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}