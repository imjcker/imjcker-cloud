package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@Table(name = "auth_stock_history")
public class AuthStockHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="自增id",name="id")
    private Integer id;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    @ApiModelProperty(value="接口id",name="apiId")
    private Integer apiId;

    /**
     * 客户
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="客户",name="appKey")
    private String appKey;

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
