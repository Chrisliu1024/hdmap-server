package com.hdmap.data.etl.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.hdmap.mybatis.config.JsonTypeHandler;
import lombok.Data;

/**
 * 数据存储信息-模板
 * @TableName datastore_template_info
 */
@TableName(value ="datastore_template_info", autoResultMap = true)
@Data
public class DatastoreTemplateInfo implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：0-postgresql，1-csv，2-geojson，3-shapefile，4-mysql，5-geopackage，6-mongodb，7-kml，8-wfs
     */
    private Integer type;

    /**
     * 数据库连接参数样例
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private Object connectionParams;

    /**
     * 描述
     */
    private String description;

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
        DatastoreTemplateInfo other = (DatastoreTemplateInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getConnectionParams() == null ? other.getConnectionParams() == null : this.getConnectionParams().equals(other.getConnectionParams()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getConnectionParams() == null) ? 0 : getConnectionParams().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
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
        sb.append(", connectionParams=").append(connectionParams);
        sb.append(", description=").append(description);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}