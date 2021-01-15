package com.imjcker.manager.charge.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
@Data
@Table(name = "company_apps_recharge")
public class CompanyAppsRecharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="",name="id")
    private Long id;

    /**
     * appKey机构标识
     */
    @Column(name = "app_key")
    @ApiModelProperty(value="appKey机构标识",name="appKey")
    private String appKey;

    /**
     * app名称
     */
    @Column(name = "app_name")
    @ApiModelProperty(value="app名称",name="appName")
    private String appName;

    /**
     * 金额
     */
    @ApiModelProperty(value="金额",name="amount")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value="创建时间",name="createTime")
    private Long createTime;

    /**
     * 充值备注
     */
    @ApiModelProperty(value="充值备注",name="remark")
    private String remark;
}
