package com.imjcker.manager.manage.po;

import com.imjcker.manager.manage.annotation.BusinessId;
import com.imjcker.manager.manage.annotation.ConditionParam;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "api_info")
public class Api {
    @ConditionParam
    @BusinessId
    @Column
    private Integer id;
    @Column
    private String apiName;
    @Column
    private Integer apiGroupId;
    @Column
    private String httpPath;
    @Column
    private Integer httpMethod;
    @Column
    private String backEndAddress;
    @Column
    private String backEndAddressB;
    @Column
    private String backEndPath;
    @Column
    private Integer backEndHttpMethod;
    @Column
    private Integer backEndTimeout;
    @Column
    private Integer isMock;
    @Column
    private Integer callBackType;
    @Column
    private Integer publishProductEnvStatus;
    @Column
    private String productEnvVersion;
    @Column
    private Integer publishTestEnvStatus;
    @Column
    private String testEnvVersion;
    @Column
    private Integer saveMongoDB;
    @Column
    private String mongodbURI;
    @Column
    private String mongodbCollectionName;
    @Column
    private Integer saveMQ;
    @Column
    private Integer mqType;
    @Column
    private String apiDescription;
    @Column
    private String mqAddress;
    @Column
    private String mqUserName;
    @Column
    private String mqPasswd;
    @Column
    private String mqTopicName;
    @Column
    private Integer status;
    @Column
    private Long createTime;
    @Column
    private Long updateTime;
    /**授权时间*/
    private Long accreditTime;
    /**这个字段在数据库是没有 对应的是 api分组里面的 组名*/
    private String apiGroupName;
    /**环境 如：测试环境 线上环境 */
    private Integer env;

    private Integer apiAppId;

    private Boolean accreditApp;

    public Boolean getAccreditApp() {
        return accreditApp;
    }

    public void setAccreditApp(Boolean accreditApp) {
        this.accreditApp = accreditApp;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription;
    }

    public Integer getApiAppId() {
        return apiAppId;
    }

    public void setApiAppId(Integer apiAppId) {
        this.apiAppId = apiAppId;
    }

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public Long getAccreditTime() {
        return accreditTime;
    }

    public void setAccreditTime(Long accreditTime) {
        this.accreditTime = accreditTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName == null ? null : apiName.trim();
    }

    public Integer getApiGroupId() {
        return apiGroupId;
    }

    public void setApiGroupId(Integer apiGroupId) {
        this.apiGroupId = apiGroupId;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath == null ? null : httpPath.trim();
    }

    public Integer getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(Integer httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getBackEndAddress() {
        return backEndAddress;
    }

    public void setBackEndAddress(String backEndAddress) {
        this.backEndAddress = backEndAddress == null ? null : backEndAddress.trim();
    }

    public String getBackEndAddressB() {
        return backEndAddressB;
    }

    public void setBackEndAddressB(String backEndAddressB) {
        this.backEndAddressB = backEndAddressB;
    }

    public String getBackEndPath() {
        return backEndPath;
    }

    public void setBackEndPath(String backEndPath) {
        this.backEndPath = backEndPath == null ? null : backEndPath.trim();
    }

    public Integer getBackEndHttpMethod() {
        return backEndHttpMethod;
    }

    public void setBackEndHttpMethod(Integer backEndHttpMethod) {
        this.backEndHttpMethod = backEndHttpMethod;
    }

    public Integer getBackEndTimeout() {
        return backEndTimeout;
    }

    public void setBackEndTimeout(Integer backEndTimeout) {
        this.backEndTimeout = backEndTimeout;
    }

    public Integer getIsMock() {
        return isMock;
    }

    public void setIsMock(Integer isMock) {
        this.isMock = isMock;
    }

    public Integer getCallBackType() {
        return callBackType;
    }

    public void setCallBackType(Integer callBackType) {
        this.callBackType = callBackType;
    }

    public Integer getPublishProductEnvStatus() {
        return publishProductEnvStatus;
    }

    public void setPublishProductEnvStatus(Integer publishProductEnvStatus) {
        this.publishProductEnvStatus = publishProductEnvStatus;
    }

    public String getProductEnvVersion() {
        return productEnvVersion;
    }

    public void setProductEnvVersion(String productEnvVersion) {
        this.productEnvVersion = productEnvVersion == null ? null : productEnvVersion.trim();
    }

    public Integer getPublishTestEnvStatus() {
        return publishTestEnvStatus;
    }

    public void setPublishTestEnvStatus(Integer publishTestEnvStatus) {
        this.publishTestEnvStatus = publishTestEnvStatus;
    }

    public String getTestEnvVersion() {
        return testEnvVersion;
    }

    public void setTestEnvVersion(String testEnvVersion) {
        this.testEnvVersion = testEnvVersion == null ? null : testEnvVersion.trim();
    }

    public Integer getSaveMongoDB() {
        return saveMongoDB;
    }

    public void setSaveMongoDB(Integer saveMongoDB) {
        this.saveMongoDB = saveMongoDB;
    }

    public String getMongodbURI() {
        return mongodbURI;
    }

    public void setMongodbURI(String mongodbURI) {
        this.mongodbURI = mongodbURI == null ? null : mongodbURI.trim();
    }

    public String getMongodbCollectionName() {
        return mongodbCollectionName;
    }

    public void setMongodbCollectionName(String mongodbCollectionName) {
        this.mongodbCollectionName = mongodbCollectionName == null ? null : mongodbCollectionName.trim();
    }

    public Integer getSaveMQ() {
        return saveMQ;
    }

    public void setSaveMQ(Integer saveMQ) {
        this.saveMQ = saveMQ;
    }

    public Integer getMqType() {
        return mqType;
    }

    public void setMqType(Integer mqType) {
        this.mqType = mqType;
    }

    public String getMqAddress() {
        return mqAddress;
    }

    public void setMqAddress(String mqAddress) {
        this.mqAddress = mqAddress == null ? null : mqAddress.trim();
    }

    public String getMqUserName() {
        return mqUserName;
    }

    public void setMqUserName(String mqUserName) {
        this.mqUserName = mqUserName == null ? null : mqUserName.trim();
    }

    public String getMqPasswd() {
        return mqPasswd;
    }

    public void setMqPasswd(String mqPasswd) {
        this.mqPasswd = mqPasswd == null ? null : mqPasswd.trim();
    }

    public String getMqTopicName() {
        return mqTopicName;
    }

    public void setMqTopicName(String mqTopicName) {
        this.mqTopicName = mqTopicName == null ? null : mqTopicName.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getApiGroupName() {
        return apiGroupName;
    }

    public void setApiGroupName(String apiGroupName) {
        this.apiGroupName = apiGroupName;
    }
}
