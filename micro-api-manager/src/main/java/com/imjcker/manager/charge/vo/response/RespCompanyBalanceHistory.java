package com.imjcker.manager.charge.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author qiuwen
 * @Date 10:07 2020/4/1
 * @Desicrption
 */
@Data
public class RespCompanyBalanceHistory implements Serializable {

    private static final long serialVersionUID = -7723404039204119294L;
    private Integer id;

    /**
     * 客户
     */
    private String appKey;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 余量
     */
    private Long stock;

    /**
     * 日期
     */
    private Long createTime;
}
