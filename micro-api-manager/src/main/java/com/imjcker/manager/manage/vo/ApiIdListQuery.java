package com.imjcker.manager.manage.vo;

import com.lemon.common.vo.BaseQuery;
/**

 * @Title ApiIdListQuery
 * @Description ApiInfo的查询类，根据apiIdList查询
 */
public class ApiIdListQuery extends BaseQuery {

    private String apiIdList;

    public String getApiIdList() {
        return apiIdList;
    }
    public void setApiIdList(String apiIdList) {
        this.apiIdList = apiIdList;
    }
}
