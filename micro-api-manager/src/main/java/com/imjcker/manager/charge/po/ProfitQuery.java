package com.imjcker.manager.charge.po;

import com.lemon.common.vo.BaseQuery;

/**
 * DatasourceBillDay查询类
 */
public class ProfitQuery extends BaseQuery {
    private String sourceName;//数据源
    private Long startTime;
    private Long endTime;

    public ProfitQuery(String sourceName, Long startTime, Long endTime) {
        this.sourceName = sourceName;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
