package com.imjcker.api.handler.po;

public class ApiRateDistribute {
    private Integer id;

    private Integer apiId;

    private String uniqueUuid;

    private String interfaceName;

    private String backEndAddress;

    private String backEndAddressB;

    private String backEndPath;

    private Integer backEndHttpMethod;

    private Integer backEndTimeout;

    private Integer callBackType;

    private Integer status;

    private Integer weight;

    private Long createTime;

    private Long updateTime;

    private Integer backendProtocolType;

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

    public String getUniqueUuid() {
        return uniqueUuid;
    }

    public void setUniqueUuid(String uniqueUuid) {
        this.uniqueUuid = uniqueUuid == null ? null : uniqueUuid.trim();
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
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

    public Integer getCallBackType() {
        return callBackType;
    }

    public void setCallBackType(Integer callBackType) {
        this.callBackType = callBackType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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

    public Integer getBackendProtocolType() {
        return backendProtocolType;
    }

    public void setBackendProtocolType(Integer backendProtocolType) {
        this.backendProtocolType = backendProtocolType;
    }
}
