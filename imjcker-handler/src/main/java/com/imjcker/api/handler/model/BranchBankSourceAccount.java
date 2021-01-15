package com.imjcker.api.handler.model;

import java.io.Serializable;

/**
 * @Author WT
 * @Date 15:05 2019/6/28
 * @Version v1.0
 * 下级村镇银行第三方数据源账户
 */
public class BranchBankSourceAccount implements Serializable {

    private Integer id;
    private Integer apiId;          //接口 id
    private String apiName;         //接口名
    private Integer apiGroupId;     //分组id
    private String apiGroupName;    //分组名称
    private String appKey;
    private String dataConfig;      //账户信息
    private Integer sourceFlag;     //是否分组账户  0 是  1 不是
    private Integer status;         //是否可用  1 可用   2  不可用

    public Integer getId() {
        return id;
    }

    public BranchBankSourceAccount setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getApiId() {
        return apiId;
    }

    public BranchBankSourceAccount setApiId(Integer apiId) {
        this.apiId = apiId;
        return this;
    }

    public String getApiName() {
        return apiName;
    }

    public BranchBankSourceAccount setApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public Integer getApiGroupId() {
        return apiGroupId;
    }

    public BranchBankSourceAccount setApiGroupId(Integer apiGroupId) {
        this.apiGroupId = apiGroupId;
        return this;
    }

    public String getApiGroupName() {
        return apiGroupName;
    }

    public BranchBankSourceAccount setApiGroupName(String apiGroupName) {
        this.apiGroupName = apiGroupName;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public BranchBankSourceAccount setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getDataConfig() {
        return dataConfig;
    }

    public BranchBankSourceAccount setDataConfig(String dataConfig) {
        this.dataConfig = dataConfig;
        return this;
    }

    public Integer getSourceFlag() {
        return sourceFlag;
    }

    public BranchBankSourceAccount setSourceFlag(Integer sourceFlag) {
        this.sourceFlag = sourceFlag;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public BranchBankSourceAccount setStatus(Integer status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "BranchBankSourceAccount{" +
                "id=" + id +
                ", apiId=" + apiId +
                ", apiName='" + apiName + '\'' +
                ", apiGroupId=" + apiGroupId +
                ", apiGroupName='" + apiGroupName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", dataConfig='" + dataConfig + '\'' +
                ", sourceFlag=" + sourceFlag +
                ", status=" + status +
                '}';
    }
}
