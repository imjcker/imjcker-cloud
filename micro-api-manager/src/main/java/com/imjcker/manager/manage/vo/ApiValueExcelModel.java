package com.imjcker.manager.manage.vo;

import java.util.List;

/**
 * 码值映射entity ，用于excel 上传码值映射
 *
 */
public class ApiValueExcelModel {

    private String deep;

    private String key;

    private String transKey;

    private String type;

    private String isData;

    private String remark;

    List<ValueModel> option;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsData() {
        return isData;
    }

    public void setIsData(String isData) {
        this.isData = isData;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ValueModel> getOption() {
        return option;
    }

    public void setOption(List<ValueModel> option) {
        this.option = option;
    }
}
