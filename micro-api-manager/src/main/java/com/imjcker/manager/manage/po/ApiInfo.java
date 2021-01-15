package com.imjcker.manager.manage.po;

public class ApiInfo {
    private Integer id;

    private String apiName;

    private Integer apiGroupId;

    private String httpPath;

    private Integer httpMethod;

    private String backEndAddress;

    private String backEndAddressB;//配置请求B中心的地址

    private String backEndPath;

    private Integer backEndHttpMethod;

    private Integer backEndTimeout;

    private Integer isMock;

    private Integer callBackType;

    private Integer publishProductEnvStatus;

    private String productEnvVersion;

    private Integer publishTestEnvStatus;

    private String testEnvVersion;

    private Integer saveMongoDB;

    private String mongodbURI;

    private String mongodbDBName;

    private String mongodbCollectionName;

    private Integer saveMQ;

    private Integer mqType;

    private String interfaceName;

    private String mqAddress;

    private String uniqueUuid;

    private String mqUserName;

    private String mqPasswd;

    private String mqTopicName;

    private Integer status;

    private Long createTime;

    private Long updateTime;

    private Integer weight;

    private String limitStrategyuuid;
    private Integer limitStrategyTotal;

    private Integer charge;

    private Integer backendProtocolType;

    private Integer cacheUnit;

    private Integer cacheNo;

    public Integer getLimitStrategyTotal() {
        return limitStrategyTotal;
    }

    public void setLimitStrategyTotal(Integer limitStrategyTotal) {
        this.limitStrategyTotal = limitStrategyTotal;
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

    public String getMongodbDBName() {
        return mongodbDBName;
    }

    public void setMongodbDBName(String mongodbDBName) {
        this.mongodbDBName = mongodbDBName == null ? null : mongodbDBName.trim();
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

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
    }

    public String getMqAddress() {
        return mqAddress;
    }

    public void setMqAddress(String mqAddress) {
        this.mqAddress = mqAddress == null ? null : mqAddress.trim();
    }

    public String getUniqueUuid() {
        return uniqueUuid;
    }

    public void setUniqueUuid(String uniqueUuid) {
        this.uniqueUuid = uniqueUuid == null ? null : uniqueUuid.trim();
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getLimitStrategyuuid() {
        return limitStrategyuuid;
    }

    public void setLimitStrategyuuid(String limitStrategyuuid) {
        this.limitStrategyuuid = limitStrategyuuid == null ? null : limitStrategyuuid.trim();
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public Integer getBackendProtocolType() {
        return backendProtocolType;
    }

    public void setBackendProtocolType(Integer backendProtocolType) {
        this.backendProtocolType = backendProtocolType;
    }

    public Integer getCacheUnit() {
        return cacheUnit;
    }

    public void setCacheUnit(Integer cacheUnit) {
        this.cacheUnit = cacheUnit;
    }

    public Integer getCacheNo() {
        return cacheNo;
    }

    public void setCacheNo(Integer cacheNo) {
        this.cacheNo = cacheNo;
    }
}
