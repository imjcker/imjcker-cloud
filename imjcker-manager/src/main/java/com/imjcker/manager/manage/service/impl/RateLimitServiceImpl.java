package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.mapper.CurrentLimitStrategyMapper;
import com.imjcker.manager.manage.mapper.RateLimitAppkeyApiMapper;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.RateLimitAppKeyApiQuery;
import com.imjcker.manager.manage.po.query.RateLimitAppKeyQuery;
import com.imjcker.manager.manage.vo.RateLimitAppKeyApiVO;
import com.imjcker.manager.manage.vo.RateLimitAppKeyVO;
import com.lemon.common.util.BeanCustomUtils;
import com.imjcker.manager.manage.mapper.ApiInfoVersionsLatestMapper;
import com.imjcker.manager.manage.mapper.RateLimitAppkeyMapper;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.RateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RateLimitServiceImpl implements RateLimitService {
    private RateLimitAppkeyMapper rateLimitAppkeyMapper;
    private RateLimitAppkeyApiMapper rateLimitAppkeyApiMapper;
    private final ApiInfoVersionsLatestMapper apiInfoVersionsLatestMapper;
    private final CurrentLimitStrategyMapper currentLimitStrategyMapper;

    @Autowired
    public RateLimitServiceImpl(RateLimitAppkeyMapper rateLimitAppkeyMapper, RateLimitAppkeyApiMapper rateLimitAppkeyApiMapper, ApiInfoVersionsLatestMapper apiInfoVersionsLatestMapper, CurrentLimitStrategyMapper currentLimitStrategyMapper) {
        this.rateLimitAppkeyMapper = rateLimitAppkeyMapper;
        this.rateLimitAppkeyApiMapper = rateLimitAppkeyApiMapper;
        this.apiInfoVersionsLatestMapper = apiInfoVersionsLatestMapper;
        this.currentLimitStrategyMapper = currentLimitStrategyMapper;
    }

    @Override
    public void searchAppKey(RateLimitAppKeyQuery query) {
        RateLimitAppkeyExample example = new RateLimitAppkeyExample();
        RateLimitAppkeyExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(query.getAppKey())) {
            criteria.andAppKeyLike("%" + query.getAppKey() + "%");
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<RateLimitAppkey> rateLimitAppkeys = rateLimitAppkeyMapper.selectByExample(example);
        PageInfo<RateLimitAppkey> pageInfo = new PageInfo<>(rateLimitAppkeys);
        query.setCount((int) pageInfo.getTotal());
        query.setPageCount(rateLimitAppkeys.size());
        //设置额外的值
        CurrentLimitStrategyExample currentLimitStrategyExample = new CurrentLimitStrategyExample();
        CurrentLimitStrategyExample.Criteria currentLimitStrategyExampleCriteria = currentLimitStrategyExample.createCriteria();
        currentLimitStrategyExampleCriteria.andStatusEqualTo(1);
        List<CurrentLimitStrategy> currentLimitStrategies = currentLimitStrategyMapper.selectByExample(currentLimitStrategyExample);
        ArrayList<RateLimitAppKeyVO> rateLimitAppKeyVOS = new ArrayList<>();
        rateLimitAppkeys.forEach(rateLimitAppkey -> {
            RateLimitAppKeyVO rateLimitAppKeyVO = new RateLimitAppKeyVO();
            BeanCustomUtils.copyPropertiesIgnoreNull(rateLimitAppkey, rateLimitAppKeyVO);
            currentLimitStrategies.forEach(currentLimitStrategy -> {
                if (StringUtils.isNotEmpty(rateLimitAppkey.getStrategy()) && currentLimitStrategy.getUuid().equals(rateLimitAppkey.getStrategy())) {
                    rateLimitAppKeyVO.setStrategyName(currentLimitStrategy.getName());
                }
            });
            rateLimitAppKeyVOS.add(rateLimitAppKeyVO);
        });
        query.setElements(rateLimitAppKeyVOS);
    }

    @Override
    public void searchAppKeyApi(RateLimitAppKeyApiQuery query) {
        RateLimitAppkeyApiExample example = new RateLimitAppkeyApiExample();
        RateLimitAppkeyApiExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(query.getAppKey())) {
            criteria.andAppKeyLike("%" + query.getAppKey() + "%");
        }
        if (query.getApiId() != null) {
            criteria.andApiIdEqualTo(query.getApiId());
        }
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<RateLimitAppkeyApi> rateLimitAppkeyApis = rateLimitAppkeyApiMapper.selectByExample(example);
        PageInfo<RateLimitAppkeyApi> pageInfo = new PageInfo<>(rateLimitAppkeyApis);
        query.setCount((int) pageInfo.getTotal());
        query.setPageCount(rateLimitAppkeyApis.size());
        //设置额外值
        CurrentLimitStrategyExample currentLimitStrategyExample = new CurrentLimitStrategyExample();
        CurrentLimitStrategyExample.Criteria currentLimitStrategyExampleCriteria = currentLimitStrategyExample.createCriteria();
        currentLimitStrategyExampleCriteria.andStatusEqualTo(1);
        List<CurrentLimitStrategy> currentLimitStrategies = currentLimitStrategyMapper.selectByExample(currentLimitStrategyExample);
        ArrayList<RateLimitAppKeyApiVO> rateLimitAppKeyApiVOS = new ArrayList<>();
        rateLimitAppkeyApis.forEach(rateLimitAppkeyApi -> {
            RateLimitAppKeyApiVO vo = new RateLimitAppKeyApiVO();
            BeanCustomUtils.copyPropertiesIgnoreNull(rateLimitAppkeyApi, vo);
            currentLimitStrategies.forEach(currentLimitStrategy -> {
                //设置策略名称
                if (StringUtils.isNotEmpty(rateLimitAppkeyApi.getStrategy()) && currentLimitStrategy.getUuid().equals(rateLimitAppkeyApi.getStrategy())) {
                    vo.setStrategyName(currentLimitStrategy.getName());
                }
                //设置api名称
                ApiInfoVersionsExample example1 = new ApiInfoVersionsExample();
                example1.createCriteria().andApiIdEqualTo(rateLimitAppkeyApi.getApiId());
                List<ApiInfoVersions> apiInfoVersions = apiInfoVersionsLatestMapper.selectByExample(example1);
                if (apiInfoVersions != null && apiInfoVersions.size()> 0) {
                    vo.setApiName(apiInfoVersions.get(0).getApiName());
                }
            });
            rateLimitAppKeyApiVOS.add(vo);
        });
        query.setElements(rateLimitAppKeyApiVOS);
    }

    @Override
    @CachePut(cacheNames = "rl_appKey_strategy", key = "'rl_appKey_strategy:'+#rateLimitAppkey.appKey")
    public RateLimitAppkey appKeyBindingStrategy(RateLimitAppkey rateLimitAppkey) {
        rateLimitAppkeyMapper.updateByPrimaryKey(rateLimitAppkey);
        return rateLimitAppkeyMapper.selectByPrimaryKey(rateLimitAppkey.getAppKey());
    }

    @Override
    @CachePut(cacheNames = "rl_appKey_api_strategy", key = "'rl_appKey_api_strategy:'+#rateLimitAppkeyAppi.appKey+#rateLimitAppkeyAppi.apiId")
    public RateLimitAppkeyApi appKeyApiBindingStrategy(RateLimitAppkeyApi rateLimitAppkeyAppi) {
        rateLimitAppkeyApiMapper.updateByPrimaryKey(rateLimitAppkeyAppi);
        return rateLimitAppkeyApiMapper.selectByPrimaryKey(rateLimitAppkeyAppi.getAppKey(), rateLimitAppkeyAppi.getApiId());
    }


    @Override
    @Cacheable(cacheNames = "rl_appKey_strategy", key = "'rl_appKey_strategy:'+#appKey", condition = "#result != null ")
    public RateLimitAppkey getAppKey(String appKey) {
        return rateLimitAppkeyMapper.selectByPrimaryKey(appKey);
    }

    @Override
    @CachePut(cacheNames = "rl_appKey_strategy", key = "'rl_appKey_strategy:'+#rateLimitAppkey.appKey")
    public RateLimitAppkey saveAppKey(RateLimitAppkey rateLimitAppkey) {
        int i = rateLimitAppkeyMapper.insertSelective(rateLimitAppkey);
        if (i==1) {
            return rateLimitAppkey;
        }
        return rateLimitAppkeyMapper.selectByPrimaryKey(rateLimitAppkey.getAppKey());
    }

    @Override
    @Cacheable(cacheNames = "rl_appKey_api_strategy", key = "'rl_appKey_api_strategy:'+#appKey+#apiId", condition = "#result != null ")
    public RateLimitAppkeyApi getAppKeyApi(String appKey, int apiId) {
        return rateLimitAppkeyApiMapper.selectByPrimaryKey(appKey, apiId);
    }

    @Override
    @CachePut(cacheNames = "rl_appKey_api_strategy", key = "'rl_appKey_api_strategy:'+#appKeyApiRateLimit.appKey+#appKeyApiRateLimit.apiId")
    public RateLimitAppkeyApi saveAppKeyApi(RateLimitAppkeyApi appKeyApiRateLimit) {
        int i = rateLimitAppkeyApiMapper.insertSelective(appKeyApiRateLimit);
        if (i == 1) {
            return appKeyApiRateLimit;
        }
        return rateLimitAppkeyApiMapper.selectByPrimaryKey(appKeyApiRateLimit.getAppKey(), appKeyApiRateLimit.getApiId());
    }
}
