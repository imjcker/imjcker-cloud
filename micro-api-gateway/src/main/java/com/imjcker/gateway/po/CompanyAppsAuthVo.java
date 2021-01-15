package com.imjcker.gateway.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CompanyAppsAuthVo implements Serializable{

    private Integer id;

    /**
     * 接口id
     */
    private Integer apiId;
    /**
     * 接口平常
     */
    private String apiName;
    /**
     * 客户appKey
     */
    private String appKey;
    /**
     * 有效开始时间
     */
    private long startTime;
    /**
     * 有效截止时间
     */
    private long endTime;
    /**
     * 客户创建时间
     */
    private long createTime;
    /**
     * 客户修改时间
     */
    private long updateTime;
    /**
     * 是否可用，1表示可用，0表示不可用
     */
    private Integer status=1;

    private BillingRules billingRules;
    private Integer called;
    private BigDecimal price;

    @Override
    public String toString() {
        return "CompanyAppsAuthVo{" +
                "id=" + id +
                ", apiId=" + apiId +
                ", apiName='" + apiName + '\'' +
                ", appKey='" + appKey + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", billingRules=" + billingRules +
                ", called=" + called +
                ", price=" + price +
                '}';
    }
}
