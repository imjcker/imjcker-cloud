package com.imjcker.gateway.model;

import com.imjcker.gateway.po.ApiInfoVersions;
import com.imjcker.gateway.po.RequestParamsVersionsLatest;

import java.util.List;

public class ApiInfoRedisMsg {
    private ApiInfoVersions apiInfoWithBLOBs;
    private List<RequestParamsVersionsLatest> list;

    public ApiInfoVersions getApiInfoWithBLOBs() {
        return apiInfoWithBLOBs;
    }

    public void setApiInfoWithBLOBs(ApiInfoVersions apiInfoWithBLOBs) {
        this.apiInfoWithBLOBs = apiInfoWithBLOBs;
    }

    public List<RequestParamsVersionsLatest> getList() {
        return list;
    }

    public void setList(List<RequestParamsVersionsLatest> list) {
        this.list = list;
    }
}
