package com.demo.python_demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * 权限实体类
 */
public class Permission {
    @JsonProperty("permissionId")
    private Integer permissionId;
    
    @JsonProperty("permissionName")
    private String permissionName;
    
    @JsonProperty("permissionCode")
    private String permissionCode;
    
    private String resourceType;
    private String resourceId;
    private String description;
    private Date createTime;

    // 构造函数
    public Permission() {}

    public Permission(Integer permissionId, String permissionName, String permissionCode, String resourceType, String description) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.permissionCode = permissionCode;
        this.resourceType = resourceType;
        this.description = description;
    }

    // Getter和Setter方法
    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionId=" + permissionId +
                ", permissionName='" + permissionName + '\'' +
                ", permissionCode='" + permissionCode + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                '}';
    }
} 