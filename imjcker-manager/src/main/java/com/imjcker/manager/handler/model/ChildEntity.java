package com.imjcker.api.handler.model;


import com.imjcker.api.handler.po.ApiInfoVersions;
import com.imjcker.api.handler.po.ApiRateDistributeWithBLOBs;
import com.imjcker.api.handler.po.BackendDistributeParams;
import com.imjcker.api.handler.po.RequestParamsVersions;

import java.util.List;
import java.util.Map;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: 子接口参数集合类
 * @Package com.lemon.client.logic.entity
 * @date 2017年9月28日 下午5:04:38
 */
public class ChildEntity {
    /*
     * 前端接口信息
     */
    private ApiInfoVersions apiInfo;
    /*
     * 后端接口信息
     */
    private ApiRateDistributeWithBLOBs childApi;
    /*
     * 前端请求参数
     */
    private List<RequestParamsVersions> requestParams;
    /*
     * 子接口后端请求参数
     */
    private List<BackendDistributeParams> childParams;

    public ApiInfoVersions getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfoVersions apiInfo) {
        this.apiInfo = apiInfo;
    }

    public ApiRateDistributeWithBLOBs getChildApi() {
        return childApi;
    }

    public void setChildApi(ApiRateDistributeWithBLOBs childApi) {
        this.childApi = childApi;
    }

    public List<RequestParamsVersions> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(List<RequestParamsVersions> requestParams) {
        this.requestParams = requestParams;
    }

    public List<BackendDistributeParams> getChildParams() {
        return childParams;
    }

    public void setChildParams(List<BackendDistributeParams> childParams) {
        this.childParams = childParams;
    }

    public static class Params {

        private Map<String, String> headerVariables;    //header中的参数
        private Map<String, String> queryVariables;        //query的参数,get方式有
        private Map<String, String> bodyVariables;        //body中的参数,post方式有
        private String json;                            //json参数
        private String xml;                                //xml参数

        public Params(Map<String, String> headerVariables, Map<String, String> queryVariables,
                      Map<String, String> bodyVariables, String json, String xml) {
            this.headerVariables = headerVariables;
            this.queryVariables = queryVariables;
            this.bodyVariables = bodyVariables;
            this.json = json;
            this.xml = xml;
        }

        public Map<String, String> getHeaderVariables() {
            return headerVariables;
        }

        public void setHeaderVariables(Map<String, String> headerVariables) {
            this.headerVariables = headerVariables;
        }

        public Map<String, String> getQueryVariables() {
            return queryVariables;
        }

        public void setQueryVariables(Map<String, String> queryVariables) {
            this.queryVariables = queryVariables;
        }

        public Map<String, String> getBodyVariables() {
            return bodyVariables;
        }

        public void setBodyVariables(Map<String, String> bodyVariables) {
            this.bodyVariables = bodyVariables;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public String getXml() {
            return xml;
        }

        public void setXml(String xml) {
            this.xml = xml;
        }

        @Override
        public String toString() {
            return headerVariables.toString() + queryVariables.toString()
                    + bodyVariables.toString() + json + xml;
        }

    }
}
