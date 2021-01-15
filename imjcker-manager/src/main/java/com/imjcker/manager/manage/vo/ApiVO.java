package com.imjcker.manager.manage.vo;

import lombok.Data;

@Data
public class ApiVO {
    private Integer apiId;
    private String apiName;
    private String apiDescription;

    private Integer categoryId;
    private String categoryName;
}
