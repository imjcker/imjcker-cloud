package com.imjcker.manager.charge.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 接口利润
 */
@Data
public class ProfitDatail {
    /*
    接口id
     */
    private Integer apiId;

    /*
     接口名称
     */
    private String apiName;
    /*
    接口利润
     */
    private BigDecimal profit;
    /**
     * 接口支出
     */
    private BigDecimal cost;
    /**
     * 接口收入
     */
    private BigDecimal income;

}
