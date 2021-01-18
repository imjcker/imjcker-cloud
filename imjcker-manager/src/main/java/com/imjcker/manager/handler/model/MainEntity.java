package com.imjcker.api.handler.model;

import com.imjcker.api.handler.po.ApiInfoVersionsWithBLOBs;
import com.imjcker.api.handler.po.BackendRequestParamsVersions;
import com.imjcker.api.handler.po.RequestParamsVersions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yezhiyuan
 * @version V2.0
 * @Title: 主接口参数集合类
 * @Package com.lemon.client.logic.entity
 * @date 2017年9月28日 下午4:40:04
 */
public class MainEntity {
    /*
     * 前后端接口信息
     */
    private ApiInfoVersionsWithBLOBs apiInfo;
    /*
     * 前端请求参数
     */
    private List<RequestParamsVersions> requestParams;
    /*
     * 后端请求参数
     */
    private List<BackendRequestParamsVersions> backParams;

    private Params params;

    public ApiInfoVersionsWithBLOBs getApiInfo() {
        return apiInfo;
    }

    public void setApiInfo(ApiInfoVersionsWithBLOBs apiInfo) {
        this.apiInfo = apiInfo;
    }

    public List<RequestParamsVersions> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(List<RequestParamsVersions> requestParams) {
        this.requestParams = requestParams;
    }

    public List<BackendRequestParamsVersions> getBackParams() {
        return backParams;
    }

    public void setBackParams(List<BackendRequestParamsVersions> backParams) {
        this.backParams = backParams;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public String getParamFromPrams(String key) {
        Map<String, String> headerVariables = this.params.getHeaderVariables();
        Map<String, String> queryVariables = this.params.getQueryVariables();
        Map<String, String> bodyVariables = this.params.getBodyVariables();
        Map<String, String> map = new HashMap<>();
        map.putAll(headerVariables);
        map.putAll(queryVariables);
        map.putAll(bodyVariables);
        return map.get(key);
    }

    public static class Params {

        private Map<String, String> headerVariables;    //header中的参数
        private Map<String, String> queryVariables;        //query的参数,get方式有
        private Map<String, String> bodyVariables;        //body中的参数,post方式有
        private String json;                            //json参数
        private String xml;                                //xml参数

        public Params(Map<String, String> headerVariables, Map<String, String> queryVariables,
                      Map<String, String> bodyVariables, String json, String xml) {
            super();
            this.headerVariables = headerVariables;
            this.queryVariables = queryVariables;
            this.bodyVariables = bodyVariables;
            this.json = json;
            this.xml = xml;
        }

        //前端参数不含json和xml
        public Params(Map<String, String> headerVariables, Map<String, String> queryVariables,
                      Map<String, String> bodyVariables) {
            super();
            this.headerVariables = headerVariables;
            this.queryVariables = queryVariables;
            this.bodyVariables = bodyVariables;
        }

        public Params() {
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
