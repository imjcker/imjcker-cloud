package com.imjcker.api.common.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ResponseBuilder implements Serializable {

    private static final long serialVersionUID = 3512596671226980321L;

    private Integer errorCode; // 异常码

    private String uid;

    private String errorMsg; // 异常信息

    private Integer code;

    private String message;

}
