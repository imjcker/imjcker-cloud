package com.imjcker.manager.manage.po;

import com.imjcker.manager.manage.annotation.BusinessId;
import com.imjcker.manager.manage.annotation.ConditionParam;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 路由转发策略
 */
@Table(name = "api_mapping_info")
public class RoutingStrategy {
    @ConditionParam
    @BusinessId
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String path;
    @Column
    private String serviceId;
    @Column
    private String aUrl;
    @Column
    private String bUrl;
    @Column
    private String stripPrefix;
    @Column
    private String retryable;
    @Column
    private String enable;
    @Column
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getaUrl() {
        return aUrl;
    }

    public void setaUrl(String aUrl) {
        this.aUrl = aUrl;
    }

    public String getbUrl() {
        return bUrl;
    }

    public void setbUrl(String bUrl) {
        this.bUrl = bUrl;
    }

    public String getStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(String stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public String getRetryable() {
        return retryable;
    }

    public void setRetryable(String retryable) {
        this.retryable = retryable;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
