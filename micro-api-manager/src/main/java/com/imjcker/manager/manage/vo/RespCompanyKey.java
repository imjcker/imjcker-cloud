package com.imjcker.manager.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author WT
 * @Date 20:19 2020/4/22
 * @Version RespCompanyKey v1.0
 * @Desicrption
 */
@Data
@ApiModel
public class RespCompanyKey implements Serializable {

    private static final long serialVersionUID = 6356363263247096629L;
    @ApiModelProperty(value="appKey机构标识",name="appKey")
    private String appKey;

    @ApiModelProperty(value="公钥",name="publicKey")
    private String publicKey;

    @ApiModelProperty(value="私钥",name="privateKey")
    private String privateKey;
}
