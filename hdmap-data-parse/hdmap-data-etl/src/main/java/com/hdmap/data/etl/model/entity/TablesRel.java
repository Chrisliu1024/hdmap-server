package com.hdmap.data.etl.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 表关联
 * @TableName tables_rel
 */
@TableName(value ="tables_rel")
@Data
public class TablesRel implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Integer id;

    /**
     * 左关联表ID
     */
    private Long leftTableId;

    /**
     * 右关联表ID
     */
    private Long rightTableId;

    /**
     * 左关联字段ID
     */
    private Long leftFieldId;

    /**
     * 右关联字段ID
     */
    private Long rightFieldId;

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
        TablesRel other = (TablesRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLeftTableId() == null ? other.getLeftTableId() == null : this.getLeftTableId().equals(other.getLeftTableId()))
            && (this.getRightTableId() == null ? other.getRightTableId() == null : this.getRightTableId().equals(other.getRightTableId()))
            && (this.getLeftFieldId() == null ? other.getLeftFieldId() == null : this.getLeftFieldId().equals(other.getLeftFieldId()))
            && (this.getRightFieldId() == null ? other.getRightFieldId() == null : this.getRightFieldId().equals(other.getRightFieldId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLeftTableId() == null) ? 0 : getLeftTableId().hashCode());
        result = prime * result + ((getRightTableId() == null) ? 0 : getRightTableId().hashCode());
        result = prime * result + ((getLeftFieldId() == null) ? 0 : getLeftFieldId().hashCode());
        result = prime * result + ((getRightFieldId() == null) ? 0 : getRightFieldId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", leftTableId=").append(leftTableId);
        sb.append(", rightTableId=").append(rightTableId);
        sb.append(", leftFieldId=").append(leftFieldId);
        sb.append(", rightFieldId=").append(rightFieldId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}