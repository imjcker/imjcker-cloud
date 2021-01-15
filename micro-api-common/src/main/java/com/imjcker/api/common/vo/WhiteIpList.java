package com.imjcker.api.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class WhiteIpList implements Serializable{

    private Integer id;

    private String appKey;

    private Integer appKeyId;

    private String ipAddress;

    private String description;

    private Integer status_flag=1;

    private Date createTime;

    private Date updateTime;

}
