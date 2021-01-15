package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "datasource_bill_year")
public class DatasourceBillYear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Integer id;

    /**
     * 数据源标识
     */
    @Column(name = "group_name")
    @ApiModelProperty(value="数据源标识",name="groupName")
    private String groupName;

    /**
     * 调用量
     */
    @ApiModelProperty(value="调用量",name="called")
    private Long called;

    /**
     * 消费额
     */
    @ApiModelProperty(value="消费额",name="amount")
    private BigDecimal amount;

    /**
     * 日期-月份
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="日期-月份",name="createTime")
    private Long createTime;
}
