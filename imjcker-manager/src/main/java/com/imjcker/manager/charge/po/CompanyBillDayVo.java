package com.imjcker.manager.charge.po;

/**
 * 下游客户账单，增加分组字段用于上下游分组利润统计
 */
public class CompanyBillDayVo extends CompanyBillDay {

    /*
     * 接口分组
     */
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
