package com.imjcker.api.common.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CompanyApp implements Serializable{

    private Integer id;
    /**
     * 客户appKey
     */
    private String appKey;

    /**
     * 客户名称
     */
    private String appName;
    /**
     * 客户创建时间
     */
    private long createTime;
    /**
     * 客户修改时间
     */
    private long updateTime;
    /**
     * 客户是否可用，1表示可用，0表示不可用
     */
    private Integer statusFlag=1;
    /**
     * 客户描述
     */
    private String description;

    @Override
    public String toString() {
        return "CompanyApp{" +
                "id=" + id +
                ", app_key='" + appKey + '\'' +
                ", app_name='" + appName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status_flag=" + statusFlag +
                ", description='" + description + '\'' +
                '}';
    }
}
