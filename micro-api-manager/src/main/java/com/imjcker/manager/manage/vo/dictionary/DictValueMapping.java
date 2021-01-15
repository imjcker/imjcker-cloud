package com.imjcker.manager.manage.vo.dictionary;

import java.util.List;

/**
 * .
 * User: lxl
 * Date: 2017/10/19
 * Time: 17:49
 * Description:
 */
public class DictValueMapping {

    private String deep;
    private String key;
    private String transKey;
    private String isData;
    private String type;
    private List<DictOptionMapping> option;

    public String getDeep() {
        return deep;
    }

    public void setDeep(String deep) {
        this.deep = deep;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTransKey() {
        return transKey;
    }

    public void setTransKey(String transKey) {
        this.transKey = transKey;
    }

    public String getIsData() {
        return isData;
    }

    public void setIsData(String isData) {
        this.isData = isData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DictOptionMapping> getOption() {
        return option;
    }

    public void setOption(List<DictOptionMapping> option) {
        this.option = option;
    }
}
