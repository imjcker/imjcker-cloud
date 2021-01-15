package com.imjcker.manager.manage.po;

public class BackendRequestParamsVersions {
    private Integer id;

    private String versionId;

    private Integer backendParamsId;

    private Integer requestParamsId;

    private Integer paramsType;

    private String paramName;

    private String paramValue;

    private Integer paramsLocation;

    private String paramDescription;

    private Long createTime;

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

    public Integer getBackendParamsId() {
        return backendParamsId;
    }

    public void setBackendParamsId(Integer backendParamsId) {
        this.backendParamsId = backendParamsId;
    }

    public Integer getRequestParamsId() {
        return requestParamsId;
    }

    public void setRequestParamsId(Integer requestParamsId) {
        this.requestParamsId = requestParamsId;
    }

    public Integer getParamsType() {
        return paramsType;
    }

    public void setParamsType(Integer paramsType) {
        this.paramsType = paramsType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue == null ? null : paramValue.trim();
    }

    public Integer getParamsLocation() {
        return paramsLocation;
    }

    public void setParamsLocation(Integer paramsLocation) {
        this.paramsLocation = paramsLocation;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription == null ? null : paramDescription.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
