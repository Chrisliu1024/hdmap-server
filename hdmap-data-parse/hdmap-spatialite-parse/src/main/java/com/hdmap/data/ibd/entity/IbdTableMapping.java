package com.hdmap.data.ibd.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName ibd_table_mapping
 */
@TableName(value ="ibd_table_mapping")
@Data
public class IbdTableMapping implements Serializable {
    /**
     * ID
     */
    @TableId
    private Object id;

    /**
     * 源表名
     */
    private String sourceTableName;

    /**
     * 目标表名
     */
    private String targetTableName;

    /**
     * 源表与目标表之间的键值映射JSON
     */
    private Object tableMappingJson;

    /**
     * 优先级，数字越小，优先级越高
     */
    private Object prorityLevel;

    /**
     * 是否可用
     */
    private Boolean isUseable;

    /**
     * 版本号
     */
    private String version;

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
        IbdTableMapping other = (IbdTableMapping) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSourceTableName() == null ? other.getSourceTableName() == null : this.getSourceTableName().equals(other.getSourceTableName()))
            && (this.getTargetTableName() == null ? other.getTargetTableName() == null : this.getTargetTableName().equals(other.getTargetTableName()))
            && (this.getTableMappingJson() == null ? other.getTableMappingJson() == null : this.getTableMappingJson().equals(other.getTableMappingJson()))
            && (this.getProrityLevel() == null ? other.getProrityLevel() == null : this.getProrityLevel().equals(other.getProrityLevel()))
            && (this.getIsUseable() == null ? other.getIsUseable() == null : this.getIsUseable().equals(other.getIsUseable()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSourceTableName() == null) ? 0 : getSourceTableName().hashCode());
        result = prime * result + ((getTargetTableName() == null) ? 0 : getTargetTableName().hashCode());
        result = prime * result + ((getTableMappingJson() == null) ? 0 : getTableMappingJson().hashCode());
        result = prime * result + ((getProrityLevel() == null) ? 0 : getProrityLevel().hashCode());
        result = prime * result + ((getIsUseable() == null) ? 0 : getIsUseable().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sourceTableName=").append(sourceTableName);
        sb.append(", targetTableName=").append(targetTableName);
        sb.append(", tableMappingJson=").append(tableMappingJson);
        sb.append(", prorityLevel=").append(prorityLevel);
        sb.append(", isUseable=").append(isUseable);
        sb.append(", version=").append(version);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}