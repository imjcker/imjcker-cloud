package com.imjcker.manager.manage.po;

import com.imjcker.manager.manage.vo.dictionary.DictValueMapping;

import java.util.List;

public class ApiRateDistributeWithDictionary {
    private Integer id;

    private String interfaceName;

    private String responseTransParam;

    private List<DictValueMapping> mapping;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getResponseTransParam() {
        return responseTransParam;
    }

    public void setResponseTransParam(String responseTransParam) {
        this.responseTransParam = responseTransParam;
    }

    public List<DictValueMapping> getMapping() {
        return mapping;
    }

    public void setMapping(List<DictValueMapping> mapping) {
        this.mapping = mapping;
    }
}
