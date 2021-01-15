package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "customer_different_balance")
public class CustomerDifferentBalance {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键",name="id")
    private Long id;

    /**
     * 客户账号
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="客户账号",name="appKey")
    private String appKey;

    /**
     * 余额差值
     */
    @Column(name = "balance_value")
    @ApiModelProperty(value="余额差值",name="balanceValue")
    private BigDecimal balanceValue;

    /**
     * 余量差值
     */
    @Column(name = "stock_value")
    @ApiModelProperty(value="余量差值",name="stockValue")
    private Integer stockValue;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="创建时间",name="createTime")
    private Long createTime;
}
