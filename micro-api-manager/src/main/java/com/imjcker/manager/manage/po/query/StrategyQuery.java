package com.imjcker.manager.manage.po.query;

import com.imjcker.manager.manage.po.Strategy;

/**
 * Created by lilinfeng on 2017/7/12.
 */
public class StrategyQuery extends PageQuery<Strategy>{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
