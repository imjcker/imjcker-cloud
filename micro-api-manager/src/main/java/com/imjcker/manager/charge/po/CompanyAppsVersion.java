package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "company_apps_version")
public class CompanyAppsVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Long id;

    @Column(name = "app_key")
    @ApiModelProperty(value="",name="appKey")
    private String appKey;

    /**
     * 计费规则
     */
    @Column(name = "strategy_uuid")
    @ApiModelProperty(value="计费规则",name="strategyUuid")
    private String strategyUuid;

    @Column(name = "create_time")
    @ApiModelProperty(value="",name="createTime")
    private Long createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value="",name="updateTime")
    private Long updateTime;

    /**
     * 单价
     */
    @ApiModelProperty(value="单价",name="price")
    private BigDecimal price;
}
