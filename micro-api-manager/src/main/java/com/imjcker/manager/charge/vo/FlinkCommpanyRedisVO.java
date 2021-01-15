package com.imjcker.manager.charge.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author qiuwen
 * @Date 10:47 2020/5/12
 * @Desicrption
 */
@Data
public class FlinkCommpanyRedisVO implements Serializable {
    private static final long serialVersionUID = -2727394294977089042L;

    /**  余额 */
    private BigDecimal balance;

    /**  余量 */
    private Integer stock;

    /**  appKey */
    private String appKey;

    /**  appKey下的合约 */
    private List<AuthSetVO> authSet;
}
