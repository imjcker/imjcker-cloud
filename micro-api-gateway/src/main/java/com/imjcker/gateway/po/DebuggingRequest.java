package com.imjcker.gateway.po;

import java.util.List;

public class DebuggingRequest {

    private ApiIdRequest idInfo;

    private List<RequestParamAndValue> headerList;

    private List<RequestParamAndValue> queryList;

    private List<RequestParamAndValue> bodyList;

    public ApiIdRequest getIdInfo() {
        return idInfo;
    }

    public void setIdInfo(ApiIdRequest idInfo) {
        this.idInfo = idInfo;
    }

    public List<RequestParamAndValue> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<RequestParamAndValue> headerList) {
        this.headerList = headerList;
    }

    public List<RequestParamAndValue> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<RequestParamAndValue> queryList) {
        this.queryList = queryList;
    }

    public List<RequestParamAndValue> getBodyList() {
        return bodyList;
    }

    public void setBodyList(List<RequestParamAndValue> bodyList) {
        this.bodyList = bodyList;
    }
}
