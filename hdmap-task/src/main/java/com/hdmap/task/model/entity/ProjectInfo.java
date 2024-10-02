package com.hdmap.task.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hdmap.mybatis.config.GeometryDeserializer;
import com.hdmap.mybatis.config.GeometrySerializer;
import com.hdmap.mybatis.config.GeometryTypeHandler;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

/**
 * 项目表
 * @TableName project_info
 */
@TableName(value ="project_info")
@Data
public class ProjectInfo implements Serializable {
    /**
     * 项目ID
     */
    @TableId()
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目状态：0：未开启 1：进行中 2：已完成 3：延期进行中 4：延期完成 5：已取消
     */
    private Integer status;

    /**
     * 项目类型：0：高精制图项目 1：4D标注项目
     */
    private Integer type;

    /**
     * 所属项目经理
     */
    private Long owner;

    /**
     * 项目预估工作量，4D标注（预估帧数），高精地图（预估里程）
     */
    private Float estimateWorkload;

    /**
     * 优先级：0 高，1 中，2 低
     */
    private Integer priority;

    /**
     * 计划开始时间
     */
    private Date planStartTime;

    /**
     * 实际结束时间
     */
    private Date actualStartTime;

    /**
     * 计划结束时间
     */
    private Date planEndTime;

    /**
     * 实际结束时间
     */
    private Date actualEndTime;

    /**
     * 项目所在地理范围
     */
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    @TableField(typeHandler = GeometryTypeHandler.class)
    private Geometry geom;

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

    /**
     * 关联父级项目ID
     */
    private Long parentId;

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
        ProjectInfo other = (ProjectInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getOwner() == null ? other.getOwner() == null : this.getOwner().equals(other.getOwner()))
            && (this.getEstimateWorkload() == null ? other.getEstimateWorkload() == null : this.getEstimateWorkload().equals(other.getEstimateWorkload()))
            && (this.getPriority() == null ? other.getPriority() == null : this.getPriority().equals(other.getPriority()))
            && (this.getPlanStartTime() == null ? other.getPlanStartTime() == null : this.getPlanStartTime().equals(other.getPlanStartTime()))
            && (this.getActualStartTime() == null ? other.getActualStartTime() == null : this.getActualStartTime().equals(other.getActualStartTime()))
            && (this.getPlanEndTime() == null ? other.getPlanEndTime() == null : this.getPlanEndTime().equals(other.getPlanEndTime()))
            && (this.getActualEndTime() == null ? other.getActualEndTime() == null : this.getActualEndTime().equals(other.getActualEndTime()))
            && (this.getGeom() == null ? other.getGeom() == null : this.getGeom().equals(other.getGeom()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getOwner() == null) ? 0 : getOwner().hashCode());
        result = prime * result + ((getEstimateWorkload() == null) ? 0 : getEstimateWorkload().hashCode());
        result = prime * result + ((getPriority() == null) ? 0 : getPriority().hashCode());
        result = prime * result + ((getPlanStartTime() == null) ? 0 : getPlanStartTime().hashCode());
        result = prime * result + ((getActualStartTime() == null) ? 0 : getActualStartTime().hashCode());
        result = prime * result + ((getPlanEndTime() == null) ? 0 : getPlanEndTime().hashCode());
        result = prime * result + ((getActualEndTime() == null) ? 0 : getActualEndTime().hashCode());
        result = prime * result + ((getGeom() == null) ? 0 : getGeom().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
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
        sb.append(", status=").append(status);
        sb.append(", type=").append(type);
        sb.append(", owner=").append(owner);
        sb.append(", estimateWorkload=").append(estimateWorkload);
        sb.append(", priority=").append(priority);
        sb.append(", planStartTime=").append(planStartTime);
        sb.append(", actualStartTime=").append(actualStartTime);
        sb.append(", planEndTime=").append(planEndTime);
        sb.append(", actualEndTime=").append(actualEndTime);
        sb.append(", geom=").append(geom);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", memo=").append(memo);
        sb.append(", parentId=").append(parentId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}