package com.imjcker.manager.manage.service.impl;

import com.google.common.base.Preconditions;
import com.imjcker.manager.manage.mapper.CurrentLimitStrategyMapper;
import com.imjcker.manager.manage.po.App;
import com.imjcker.manager.manage.po.CurrentLimitStrategy;
import com.imjcker.manager.manage.po.Strategy;
import com.imjcker.manager.manage.po.query.AppQuery;
import com.imjcker.manager.manage.po.query.StrategyQuery;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.UUIDUtil;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.AppOldMapper;
import com.imjcker.manager.manage.mapper.StrategyMapper;
import com.imjcker.manager.manage.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lilinfeng on 2017/7/11.
 */

@Service
public class StrategyService {
    @Autowired
    private StrategyMapper strategyMapper;
    @Autowired
    private AppOldMapper appMapper;
    @Autowired
    private ApiService apiService;
    @Autowired
    private CurrentLimitStrategyMapper currentLimitStrategyMapper;


    public List queryByPage(StrategyQuery query) {
        query.setCount(this.queryCount(query));
        List<Strategy> strategies=strategyMapper.queryByPage(query);
        return strategies;
    }
    public int queryCount(StrategyQuery query) {
        return strategyMapper.queryCount(query);
    }
    public int queryCount(AppQuery query) {
        return appMapper.queryCount(query);
    }

    public void delete(Integer strategyId) {
        Strategy strategy=new Strategy();
        strategy.setId(strategyId);
        strategy.setStatus(CommonStatus.DISENABLE);
        strategyMapper.delete(strategy);
    }

    public void addNew(Strategy app) {
        checkAddParam(app);
        app.setUuid(UUIDUtil.creatUUID());
        app.setStatus(CommonStatus.ENABLE);
        app.setCreateTime(System.currentTimeMillis());
        app.setUpdateTime(System.currentTimeMillis());
        strategyMapper.insert(app);
    }
    private void checkAddParam(Strategy entity) {
        Preconditions.checkState(
                entity.getUnit() != null && entity.getName() != null && entity.getNo() != null,
                "addStrategy missing param");
    }

    @CachePut(cacheNames = "limitStrategy", key = "'limitStrategy:'+#entity.uuid", condition = "#result!=null")
    public Strategy update(Strategy entity) {
            checkAddParam(entity);
            entity.setUpdateTime(System.currentTimeMillis());
            strategyMapper.updateStrategy(entity);
            return entity;
    }


    public int queryCountByStrategy(AppQuery query,String limitStrategyUid) {
        return appMapper.queryCountByStrategy(query,limitStrategyUid);
    }
    /**根据策略UUID 查找已经授权的app*/
    public List<App> findApp(AppQuery query) {
        query.setCount(appMapper.queryCountByStrategy(query,query.getUuid()));
        List<App> apps=appMapper.queryPageByStrategy(query,query.getUuid());
        return apps;
    }
    /**解除授权*/
    public void unApiAppRelation(Integer appId) {
        appMapper.setNullUuid(appId);
    }
    /**找出所有的APP 如果是已经授权的了 isAccreditStrategy 为 true*/
    public List<App> findNotAccredit(AppQuery query) {
        /**查询出所有的app*/
        query.setCount(this.queryCount(query));
        List<App> appList= appMapper.queryByPage(query);
        String limitStrategyUid =query.getUuid();

        /**java8版本   线上目前不支持*/
/*        appList.forEach(d->{
            int sum= appMapper.checkAccredit(limitStrategyUid,d.getId());
            d.setAccreditStrategy(sum==0?false:true);
        });*/
        /** 如果是已经授权的了设置 isAccreditStrategy*/
        for(App app:appList){
            int sum= appMapper.checkAccredit(limitStrategyUid,app.getId());
            app.setAccreditStrategy(sum==0?false:true);
        }
        return appList;
    }
    /**授权*/
    public void accredit(App record) {
        String limitStrategyuuid;
        Long currentTime=System.currentTimeMillis();
        Integer StrategyId=record.getStrategyId();
        limitStrategyuuid=strategyMapper.queryStrategyUid(StrategyId);
        record.setLimitStrategyuuid(limitStrategyuuid);
        record.setUpdateTime(currentTime);
        appMapper.updateUuid(record);
    }
    /**详情*/
    public Strategy findOne(Strategy strategy) {
        return strategyMapper.select(strategy);
    }

    public boolean checkName(String name) {
        int num=strategyMapper.countName(name);
        return num>0?false:true;
    }
	public CurrentLimitStrategy findStrategyById(Integer id) {
		if (null == id) {
			throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
		}
		CurrentLimitStrategy strategy = currentLimitStrategyMapper.selectByPrimaryKey(id);
        if (null == strategy || CommonStatus.DISENABLE.equals(strategy.getStatus())) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_LIMIT_STRATEGY);
        }
		return strategy;
	}
}
