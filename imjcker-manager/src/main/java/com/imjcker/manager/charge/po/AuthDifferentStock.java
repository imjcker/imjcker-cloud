package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@ApiModel
@Data
@Table(name = "auth_different_stock")
public class AuthDifferentStock {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键",name="id")
    private Integer id;

    /**
     * 客户appKey
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="客户appKey",name="appKey")
    private String appKey;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    @ApiModelProperty(value="接口id",name="apiId")
    private Integer apiId;

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
