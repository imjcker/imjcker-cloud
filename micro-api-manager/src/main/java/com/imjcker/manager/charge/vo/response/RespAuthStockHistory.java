package com.imjcker.manager.charge.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 10:32 2020/4/1
 * @Desicrption
 */
@Data
public class RespAuthStockHistory implements Serializable {
    private static final long serialVersionUID = -4262857877796229126L;

    /**
     * 自增id
     */
    private Integer id;

    /**
     * 接口id
     */
    private Integer apiId;

    /**
     * 客户
     */
    private String appKey;

    /**
     * 余量
     */
    private Integer stock;

    /**
     * 日期
     */
    private Long createTime;
}
