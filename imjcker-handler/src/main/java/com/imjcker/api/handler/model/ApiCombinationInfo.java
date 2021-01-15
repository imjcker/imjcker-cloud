package com.imjcker.api.handler.model;

import java.io.Serializable;

/**
 * @Author WT
 * @Date 15:22 2019/8/20
 * @Version ApiCombinationInfo v1.0
 * @Desicrption
 */
public class ApiCombinationInfo implements Serializable {

    private Integer id;
    private Integer combinationId;
    private Integer apiGroupId;
    private Integer apiId;
    private String versionId;
    private String apiName;
    private String httpPath;
    private Integer weight;
    private Double score;
    private Integer status;
    private Long createTime;
    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public ApiCombinationInfo setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getCombinationId() {
        return combinationId;
    }

    public ApiCombinationInfo setCombinationId(Integer combinationId) {
        this.combinationId = combinationId;
        return this;
    }

    public Integer getApiGroupId() {
        return apiGroupId;
    }

    public ApiCombinationInfo setApiGroupId(Integer apiGroupId) {
        this.apiGroupId = apiGroupId;
        return this;
    }

    public Integer getApiId() {
        return apiId;
    }

    public ApiCombinationInfo setApiId(Integer apiId) {
        this.apiId = apiId;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public ApiCombinationInfo setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public String getApiName() {
        return apiName;
    }

    public ApiCombinationInfo setApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public ApiCombinationInfo setHttpPath(String httpPath) {
        this.httpPath = httpPath;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }

    public ApiCombinationInfo setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public Double getScore() {
        return score;
    }

    public ApiCombinationInfo setScore(Double score) {
        this.score = score;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public ApiCombinationInfo setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApiCombinationInfo setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public ApiCombinationInfo setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
