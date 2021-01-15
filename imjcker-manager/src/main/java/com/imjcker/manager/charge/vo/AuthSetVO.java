package com.imjcker.manager.charge.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author qiuwen
 * @Date 10:57 2020/5/12
 * @Desicrption
 */
@Data
public class AuthSetVO implements Serializable {

    /**  合约apiID */
    private Integer apiId;

    /**  合约余量 */
    private Integer count;
}
