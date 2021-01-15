package com.imjcker.manager.manage.model;

import java.io.Serializable;

public class PageInfo implements Serializable{

    private Integer currentPage=1;

    private Integer pageSize=10;

    private Integer totalCount;

    private Integer totalPage;

    public PageInfo() {
    }

    public PageInfo(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        int temp = this.totalCount/this.pageSize;
        this.totalPage = this.totalCount%this.pageSize == 0 ? temp : temp+1;
        return totalPage;
    }
}
