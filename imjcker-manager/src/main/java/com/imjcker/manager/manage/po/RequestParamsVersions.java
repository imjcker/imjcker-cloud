package com.imjcker.manager.manage.po;

public class RequestParamsVersions {
    private Integer id;

    private String versionId;

    private Integer requestParamsId;

    private String paramName;

    private Integer paramsType;

    private Integer paramsLocation;

    private Integer paramsMust;

    private String paramsDefaultValue;

    private String paramsExample;

    private String paramsDescription;

    private Integer minLength;

    private Integer maxLength;

    private String regularExpress;

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

    public Integer getRequestParamsId() {
        return requestParamsId;
    }

    public void setRequestParamsId(Integer requestParamsId) {
        this.requestParamsId = requestParamsId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public Integer getParamsType() {
        return paramsType;
    }

    public void setParamsType(Integer paramsType) {
        this.paramsType = paramsType;
    }

    public Integer getParamsLocation() {
        return paramsLocation;
    }

    public void setParamsLocation(Integer paramsLocation) {
        this.paramsLocation = paramsLocation;
    }

    public Integer getParamsMust() {
        return paramsMust;
    }

    public void setParamsMust(Integer paramsMust) {
        this.paramsMust = paramsMust;
    }

    public String getParamsDefaultValue() {
        return paramsDefaultValue;
    }

    public void setParamsDefaultValue(String paramsDefaultValue) {
        this.paramsDefaultValue = paramsDefaultValue == null ? null : paramsDefaultValue.trim();
    }

    public String getParamsExample() {
        return paramsExample;
    }

    public void setParamsExample(String paramsExample) {
        this.paramsExample = paramsExample == null ? null : paramsExample.trim();
    }

    public String getParamsDescription() {
        return paramsDescription;
    }

    public void setParamsDescription(String paramsDescription) {
        this.paramsDescription = paramsDescription == null ? null : paramsDescription.trim();
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getRegularExpress() {
        return regularExpress;
    }

    public void setRegularExpress(String regularExpress) {
        this.regularExpress = regularExpress == null ? null : regularExpress.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
