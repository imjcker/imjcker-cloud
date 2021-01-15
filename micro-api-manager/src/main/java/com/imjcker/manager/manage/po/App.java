package com.imjcker.manager.manage.po;

import com.imjcker.manager.manage.annotation.BusinessId;
import com.imjcker.manager.manage.annotation.ConditionParam;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by lilinfeng on 2017/7/11.
 */
@Table(name = "app_certification")
public class App {
    @ConditionParam
    @BusinessId
    @Column
    private Integer id;
    @Column
    private String appName;
    @Column
    private String appKey;
    @Column
    /**密钥*/
    private String appSecret;
    @Column
    private String description;
    @Column
    /**状态码, 1:状态可用, 2:状态不可用.*/
    private Integer status;
    @Column
    private Long createTime;
    @Column
    private Long updateTime;
    @Column
    /**该用户绑定的限流策略 id*/
    private String limitStrategyuuid;

    private Boolean accreditApi;

    private Boolean accreditStrategy;

    private Strategy strategy;

    private Integer strategyId;

    private String ids;

    private Integer env;

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Boolean getAccreditApi() {
        return accreditApi;
    }

    public void setAccreditApi(Boolean accreditApi) {
        this.accreditApi = accreditApi;
    }

    public Boolean getAccreditStrategy() {
        return accreditStrategy;
    }

    public void setAccreditStrategy(Boolean accreditStrategy) {
        this.accreditStrategy = accreditStrategy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLimitStrategyuuid() {
        return limitStrategyuuid;
    }

    public void setLimitStrategyuuid(String limitStrategyuuid) {
        this.limitStrategyuuid = limitStrategyuuid;
    }
}
