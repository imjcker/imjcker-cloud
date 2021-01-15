package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
@Table(name = "company_balance_history")
public class CompanyBalanceHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Integer id;

    /**
     * 客户
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="客户",name="appKey")
    private String appKey;

    /**
     * 余额
     */
    @ApiModelProperty(value="余额",name="balance")
    private BigDecimal balance;

    /**
     * 余量
     */
    @ApiModelProperty(value="余量",name="stock")
    private Integer stock;

    /**
     * 日期
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="日期",name="createTime")
    private Long createTime;

    /**
     * 记录统计的时间
     */
    @Column(name = "record_time")
    @ApiModelProperty(value="记录统计的时间",name="recordTime")
    private Date recordTime;
}
