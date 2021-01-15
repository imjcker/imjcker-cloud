package com.imjcker.manager.manage.po.query;

import com.lemon.common.vo.BaseQuery;

/**
 * .
 * User: lxl
 * Date: 2017/9/22
 * Time: 15:11
 * Description:
 */
public class ApiInfoWithSubApiQuery extends BaseQuery{

    private String apiName;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
