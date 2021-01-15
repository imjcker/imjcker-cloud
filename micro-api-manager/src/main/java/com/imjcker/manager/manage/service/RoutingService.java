package com.imjcker.manager.manage.service;

import com.google.common.base.Preconditions;
import com.imjcker.manager.manage.mapper.RoutingMapper;
import com.imjcker.manager.manage.po.RoutingStrategy;
import com.imjcker.manager.manage.po.query.RoutingQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lilinfeng on 2017/7/11.
 */

@Service
public class RoutingService {

    @Autowired
    private RoutingMapper routingMapper;

    public List queryByPage(RoutingQuery query) {
        query.setCount(this.queryCount(query));
        List<RoutingStrategy> strategies = routingMapper.queryByPage(query);
        return strategies;
    }

    public int queryCount(RoutingQuery query) {
        return routingMapper.queryCount(query);
    }

    public void delete(RoutingStrategy strategy) {
        routingMapper.deleteRoutingStrategy(strategy);
    }


    public boolean checkName(RoutingStrategy routingStrategy) {
        int num = routingMapper.countName(routingStrategy);
        return num <= 0;
    }

    public boolean checkPath(RoutingStrategy routingStrategy) {
        int num = routingMapper.countPath(routingStrategy);
        return num <= 0;
    }

    private void checkAddParam(RoutingStrategy entity) {
        Preconditions.checkState(
                entity.getPath() != null && entity.getName() != null && entity.getaUrl() != null,
                "addRoutingStrategy missing param");
    }

    public void addNew(RoutingStrategy newStrategy) {
        routingMapper.insert(newStrategy);
    }

    public void update(RoutingStrategy entity) {
        routingMapper.updateRoutingStrategy(entity);
    }
}
