package com.imjcker.api.common.vo;

import lombok.Data;

/**
 * @Author qiuwen
 * @Date 10:37 2020/4/1
 * @Desicrption
 */
@Data
public class PageBase {

    private Integer pageNum=1;
    private Integer pageSize=10;
}
