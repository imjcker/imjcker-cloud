package com.imjcker.manager.charge.po;

import java.io.Serializable;

/**
 * @Author WT
 * @Date 15:24 2020/2/24
 * @Version CompanyAppParam v1.0
 * @Desicrption
 */
public class CompanyAppParam implements Serializable {

    private String appKey;
    private Integer pageNum;
    private Integer pageSize;

    public String getAppKey() {
        return appKey;
    }

    public CompanyAppParam setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public CompanyAppParam setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public CompanyAppParam setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String toString() {
        return "CompanyAppParam{" +
                "appKey='" + appKey + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
