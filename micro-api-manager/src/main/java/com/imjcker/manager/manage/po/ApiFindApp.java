package com.imjcker.manager.manage.po;

import com.imjcker.manager.manage.annotation.BusinessId;
import com.imjcker.manager.manage.annotation.ConditionParam;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "api_app_relation")
public class ApiFindApp {
    @BusinessId
    @ConditionParam
    @Column
    private Integer id;
    @Column
    private Integer apiId;
    @Column
    private Integer appCertificationId;
    @Column
    private Integer status;
    @Column
    private Long createTime;
    @Column
    private Long updateTime;
    @Column
    private Integer env;

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getAppCertificationId() {
        return appCertificationId;
    }

    public void setAppCertificationId(Integer appCertificationId) {
        this.appCertificationId = appCertificationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
