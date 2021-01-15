package com.imjcker.manager.manage.po;

import java.util.ArrayList;
import java.util.List;

public class ApiInfoRedisMsg {
    private ApiInfoVersionsWithBLOBs apiInfoWithBLOBs;
    private List<RequestParamsVersionsLatest> list = new ArrayList<>();

    public ApiInfoVersionsWithBLOBs getApiInfoWithBLOBs() {
        return apiInfoWithBLOBs;
    }

    public void setApiInfoWithBLOBs(ApiInfoVersionsWithBLOBs apiInfoWithBLOBs) {
        this.apiInfoWithBLOBs = apiInfoWithBLOBs;
    }

    public List<RequestParamsVersionsLatest> getList() {
        return list;
    }

    public void setList(List<RequestParamsVersionsLatest> list) {
        this.list = list;
    }
}
