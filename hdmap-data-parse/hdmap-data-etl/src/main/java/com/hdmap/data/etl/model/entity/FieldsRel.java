package com.hdmap.data.etl.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 字段关联
 * @TableName fields_rel
 */
@TableName(value ="fields_rel")
@Data
public class FieldsRel implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 左关联字段ID
     */
    private Long leftFieldId;

    /**
     * 右关联字段ID
     */
    private Long rightFieldId;

    /**
     * 转换器实例ID
     */
    private Long transformerInstanceId;
    /**
     * 多对一映射时的序列号
     */
    //@TableField(value = "sequence")
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
        FieldsRel other = (FieldsRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLeftFieldId() == null ? other.getLeftFieldId() == null : this.getLeftFieldId().equals(other.getLeftFieldId()))
            && (this.getRightFieldId() == null ? other.getRightFieldId() == null : this.getRightFieldId().equals(other.getRightFieldId()))
            && (this.getTransformerInstanceId() == null ? other.getTransformerInstanceId() == null : this.getTransformerInstanceId().equals(other.getTransformerInstanceId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLeftFieldId() == null) ? 0 : getLeftFieldId().hashCode());
        result = prime * result + ((getRightFieldId() == null) ? 0 : getRightFieldId().hashCode());
        result = prime * result + ((getTransformerInstanceId() == null) ? 0 : getTransformerInstanceId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", leftFieldId=").append(leftFieldId);
        sb.append(", rightFieldId=").append(rightFieldId);
        sb.append(", transformerInstanceId=").append(transformerInstanceId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}