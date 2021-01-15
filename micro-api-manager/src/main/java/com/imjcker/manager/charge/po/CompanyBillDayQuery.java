package com.imjcker.manager.charge.po;

import com.lemon.common.vo.BaseQuery;

/**
 * CompanyBillDay查询类
 */
public class CompanyBillDayQuery  extends BaseQuery {
    private String appkey;
    private String dateType;//详情账单区分日-月-年
    private Long startTime;
    private Long endTime;

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
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
