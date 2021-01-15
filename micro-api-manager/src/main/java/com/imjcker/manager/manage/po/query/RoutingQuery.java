package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.po.RoutingStrategy;

/**
 * Created by lilinfeng on 2017/7/12.
 */
public class RoutingQuery extends PageQuery<RoutingStrategy>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
