package com.imjcker.gateway.po;

public class ApiInfoVersions {
    private Integer id;

    private String versionId;

    private Integer apiId;

    private String apiName;

    private Integer apiGroupId;

    private String httpPath;

    private Integer httpMethod;

    private String backEndAddress;

    private String backEndAddressB;

    private String backEndPath;

    private Integer backEndHttpMethod;

    private Integer backEndTimeout;

    private Integer isMock;

    private Integer callBackType;

    private Integer env;

    private Integer currentVersion;

    private String pubDescription;

    private Integer saveMongoDB;

    private String mongodbURI;

    private String mongodbDBName;

    private String mongodbCollectionName;

    private Integer saveMQ;

    private Integer mqType;

    private String mqAddress;

    private String mqUserName;

    private String mqPasswd;

    private String mqTopicName;

    private Long createTime;

    private Integer weight;

    private Integer charge;

    private String uniqueUuid;

    private String limitStrategyuuid;

    private String interfaceName;

    private Integer backendProtocolType;

    private Integer cacheUnit;

    private Integer cacheNo;

    private Integer limitStrategyTotal;

    private String apiDescription;

    private String mockData;

    private String callBackSuccessExample;

    private String callBackFailExample;

    private String responseTransParam;

    private String responseConfigJson;

    private String jsonConfig;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
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
        this.backEndAddressB = backEndAddressB == null ? null : backEndAddressB.trim();
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

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public Integer getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getPubDescription() {
        return pubDescription;
    }

    public void setPubDescription(String pubDescription) {
        this.pubDescription = pubDescription == null ? null : pubDescription.trim();
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getCharge() {
        return charge;
    }

    public void setCharge(Integer charge) {
        this.charge = charge;
    }

    public String getUniqueUuid() {
        return uniqueUuid;
    }

    public void setUniqueUuid(String uniqueUuid) {
        this.uniqueUuid = uniqueUuid == null ? null : uniqueUuid.trim();
    }

    public String getLimitStrategyuuid() {
        return limitStrategyuuid;
    }

    public void setLimitStrategyuuid(String limitStrategyuuid) {
        this.limitStrategyuuid = limitStrategyuuid == null ? null : limitStrategyuuid.trim();
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
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

    public Integer getLimitStrategyTotal() {
        return limitStrategyTotal;
    }

    public void setLimitStrategyTotal(Integer limitStrategyTotal) {
        this.limitStrategyTotal = limitStrategyTotal;
    }

    public String getApiDescription() {
        return apiDescription;
    }

    public void setApiDescription(String apiDescription) {
        this.apiDescription = apiDescription == null ? null : apiDescription.trim();
    }

    public String getMockData() {
        return mockData;
    }

    public void setMockData(String mockData) {
        this.mockData = mockData == null ? null : mockData.trim();
    }

    public String getCallBackSuccessExample() {
        return callBackSuccessExample;
    }

    public void setCallBackSuccessExample(String callBackSuccessExample) {
        this.callBackSuccessExample = callBackSuccessExample == null ? null : callBackSuccessExample.trim();
    }

    public String getCallBackFailExample() {
        return callBackFailExample;
    }

    public void setCallBackFailExample(String callBackFailExample) {
        this.callBackFailExample = callBackFailExample == null ? null : callBackFailExample.trim();
    }

    public String getResponseTransParam() {
        return responseTransParam;
    }

    public void setResponseTransParam(String responseTransParam) {
        this.responseTransParam = responseTransParam == null ? null : responseTransParam.trim();
    }

    public String getResponseConfigJson() {
        return responseConfigJson;
    }

    public void setResponseConfigJson(String responseConfigJson) {
        this.responseConfigJson = responseConfigJson == null ? null : responseConfigJson.trim();
    }

    public String getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(String jsonConfig) {
        this.jsonConfig = jsonConfig == null ? null : jsonConfig.trim();
    }
}
