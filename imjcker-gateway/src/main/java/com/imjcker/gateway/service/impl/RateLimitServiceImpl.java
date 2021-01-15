package com.imjcker.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.util.RedisUtil;
import com.imjcker.gateway.util.Constant;
import com.imjcker.gateway.mapper.ApiInfoVersionsMapper;
import com.imjcker.gateway.mapper.RateLimitAppkeyApiMapper;
import com.imjcker.gateway.mapper.RateLimitAppkeyMapper;
import com.imjcker.gateway.po.ApiInfoVersions;
import com.imjcker.gateway.po.ApiInfoVersionsExample;
import com.imjcker.gateway.po.RateLimitAppkey;
import com.imjcker.gateway.po.RateLimitAppkeyApi;
import com.imjcker.gateway.service.RateLimitService;
import com.imjcker.gateway.model.ApiInfoRedisMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
public class RateLimitServiceImpl implements RateLimitService {
    private final RateLimitAppkeyMapper rateLimitAppkeyMapper;
    private final RateLimitAppkeyApiMapper rateLimitAppkeyApiMapper;
    private final ApiInfoVersionsMapper apiInfoVersionsMapper;

    @Autowired
    public RateLimitServiceImpl(RateLimitAppkeyMapper rateLimitAppkeyMapper, RateLimitAppkeyApiMapper rateLimitAppkeyApiMapper, ApiInfoVersionsMapper apiInfoVersionsMapper) {
        this.rateLimitAppkeyMapper = rateLimitAppkeyMapper;
        this.rateLimitAppkeyApiMapper = rateLimitAppkeyApiMapper;
        this.apiInfoVersionsMapper = apiInfoVersionsMapper;
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
        if (i == 1) {
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

    @Override
    public ApiInfoVersions getApiInfo(long apiId, HttpServletRequest request) {
        JSONObject jsonObject = RedisUtil.get("api:" + apiId);
        if (jsonObject != null) {
//            log.info("限流查询到redis中存在apiId={}接口信息", apiId);
            ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
            return apiInfoRedisMsg.getApiInfoWithBLOBs();
        } else {
            String contextPath = request.getContextPath();
            String httpPath = request.getRequestURI().replaceFirst(contextPath, "");

            ApiInfoVersionsExample example = new ApiInfoVersionsExample();
            ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
            criteria.andEnvEqualTo(Operator.getEnv(request.getHeader("env")));
            criteria.andHttpPathEqualTo(httpPath);
            criteria.andCurrentVersionEqualTo(Constant.IS_CURRENT_VERSION);
            List<ApiInfoVersions> list = apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
            return list.get(0);
        }
    }
}
