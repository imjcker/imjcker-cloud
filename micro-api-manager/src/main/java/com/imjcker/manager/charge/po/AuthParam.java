package com.imjcker.manager.charge.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author WT
 * @Date 14:21 2020/2/25
 * @Version AuthParam v1.0
 * @Desicrption
 */
@Data
public class AuthParam implements Serializable {

    private String appKey;
    private String apiId;
    private Integer pageNum;
    private Integer pageSize;

    @Override
    public String toString() {
        return "AuthParam{" +
                "appKey='" + appKey + '\'' +
                ", apiId='" + apiId + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
