package com.imjcker.manager.manage.po;

public class RequestParams {
    private Integer id;

    private Integer apiId;

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

    private Integer status;

    private Long createTime;

    private Long updateTime;

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
