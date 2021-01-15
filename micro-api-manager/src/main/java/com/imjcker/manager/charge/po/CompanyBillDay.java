package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "company_bill_day")
public class CompanyBillDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Integer id;

    /**
     * 客户标识
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="客户标识",name="appKey")
    private String appKey;

    /**
     * 调用量
     */
    @ApiModelProperty(value="调用量",name="called")
    private Long called;

    /**
     * 剩余量
     */
    @ApiModelProperty(value="剩余量",name="stock")
    private Long stock;

    /**
     * 消费额
     */
    @ApiModelProperty(value="消费额",name="amount")
    private BigDecimal amount;

    /**
     * 余额
     */
    @ApiModelProperty(value="余额",name="balance")
    private BigDecimal balance;

    /**
     * 日期-天
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="日期-天",name="createTime")
    private Long createTime;
}
