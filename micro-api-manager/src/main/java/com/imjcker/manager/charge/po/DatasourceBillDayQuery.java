package com.imjcker.manager.charge.po;

import com.lemon.common.vo.BaseQuery;

/**
 * DatasourceBillDay查询类
 */
public class DatasourceBillDayQuery extends BaseQuery {
    private String sourceName;//数据源
    private String dateType;//详情账单区分日-月-年
    private Long startTime;
    private Long endTime;

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
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
