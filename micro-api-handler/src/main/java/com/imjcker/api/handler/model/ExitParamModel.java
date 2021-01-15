package com.imjcker.api.handler.model;

import java.io.Serializable;
import java.util.Map;

public class ExitParamModel implements Serializable {
    private static final long serialVersionUID = -1014236678822278993L;
    private String uid;
    private String url;
    private String protocol;
    private String type;
    private String method;
    private Integer apiId;
    private String sourceName;
    private String apiName;
    private Long timeout;
    private String retryFlag;
    private Integer retryCount;
    private boolean isIgnoreVerify;
    private Data data;


    public static class Data implements Serializable {
        private static final long serialVersionUID = -2178659426494668187L;
        private Map<String, String> headerList;
        private Map<String, String> queryList;
        private Map<String, String> bodyList;
        private String json;
        private String xml;

        public Map<String, String> getBodyList() {
            return bodyList;
        }

        public void setBodyList(Map<String, String> bodyList) {
            this.bodyList = bodyList;
        }

        public Map<String, String> getHeaderList() {
            return headerList;
        }

        public void setHeaderList(Map<String, String> headerList) {
            this.headerList = headerList;
        }

        public Map<String, String> getQueryList() {
            return queryList;
        }

        public void setQueryList(Map<String, String> queryList) {
            this.queryList = queryList;
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
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public String getRetryFlag() {
        return retryFlag;
    }

    public void setRetryFlag(String retryFlag) {
        this.retryFlag = retryFlag;
    }

    public boolean isIgnoreVerify() {
        return isIgnoreVerify;
    }

    public void setIgnoreVerify(boolean ignoreVerify) {
        isIgnoreVerify = ignoreVerify;
    }

    public Integer getApiId() {
        return apiId;
    }

    public ExitParamModel setApiId(Integer apiId) {
        this.apiId = apiId;
        return this;
    }

    public String getApiName() {
        return apiName;
    }

    public ExitParamModel setApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
