package com.imjcker.gateway.service.impl;

import com.imjcker.api.common.util.RedisUtil;
import com.imjcker.api.common.util.CollectionUtil;
import com.imjcker.gateway.po.*;
import com.imjcker.gateway.util.Constant;
import com.imjcker.gateway.mapper.ApiInfoVersionsMapper;
import com.imjcker.gateway.mapper.CurrentLimitStrategyMapper;
import com.imjcker.gateway.mapper.RequestParamsVersionsMapper;
import com.imjcker.gateway.service.ZuulProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ZuulProxyServiceImpl implements ZuulProxyService {

    @Autowired
    private ApiInfoVersionsMapper apiInfoVersionsMapper;

	@Autowired
	private RequestParamsVersionsMapper requestParamsVersionsMapper;

    @Autowired
    private CurrentLimitStrategyMapper currentLimitStrategyMapper;

    private static ConcurrentHashMap<String, List<BranchBankSourceAccount>> accountMap = new ConcurrentHashMap<>();

    private static Logger logger = LoggerFactory.getLogger(ZuulProxyServiceImpl.class);

    @Override
    public List<ApiInfoVersions> getCurrentApiInfo(String httpPath, Integer env) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andEnvEqualTo(env);
        criteria.andHttpPathEqualTo(httpPath);
        criteria.andCurrentVersionEqualTo(Constant.IS_CURRENT_VERSION);
        List<ApiInfoVersions> list = apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        return list;
    }

    @Override
    @Cacheable(cacheNames = "limitStrategy", key = "'limitStrategy:'+#limitStrategyuuid", condition = "#result != null ")
    public CurrentLimitStrategy getLimitStrategy(String limitStrategyuuid) {
        CurrentLimitStrategyExample example = new CurrentLimitStrategyExample();
        CurrentLimitStrategyExample.Criteria criteria = example.createCriteria();
        criteria.andUuidEqualTo(limitStrategyuuid)
                .andStatusEqualTo(1);
        List<CurrentLimitStrategy> list = currentLimitStrategyMapper.selectByExample(example);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

	@Override
	public List<RequestParamsVersions> getEntitybyVersionId(String versionId) {
		RequestParamsVersionsExample example = new RequestParamsVersionsExample();
		RequestParamsVersionsExample.Criteria criteria = example.createCriteria();
		criteria.andVersionIdEqualTo(versionId);
		List<RequestParamsVersions> list = requestParamsVersionsMapper.selectByExample(example);
		return list;
	}

    @Override
    public String getHttpPathByApiId(Integer apiId) {
        return apiInfoVersionsMapper.getHttpPathByApiId(apiId);
    }

    /**
     * 获取村镇银行第三方数据源帐号
     * @param appKey
     * @param apiGroupId
     * @param apiId
     * @return
     */
    @Override
    public String getDataConfigWithTownBank(String appKey, Integer apiGroupId, Integer apiId) {
        String key = appKey + ":" + apiGroupId;
        //从本地缓存中读取
        List<BranchBankSourceAccount> accountList = accountMap.get(key);
        if (CollectionUtil.isNotEmpty(accountList)) {
            return filterBranchBankAccount(accountList, apiId);
        }

        //从Redis 缓存中读取
        accountList = RedisUtil.getList(key, BranchBankSourceAccount.class);
        if (CollectionUtil.isNotEmpty(accountList)) {
            accountMap.put(key, accountList);
            return filterBranchBankAccount(accountList, apiId);
        }

        //从数据库中读取
        accountList = apiInfoVersionsMapper.getBranchBankAccountList(apiGroupId, appKey);
        if (CollectionUtil.isNotEmpty(accountList)) {
            accountMap.put(key, accountList);
            RedisUtil.setToCaches(key, accountList);
            return filterBranchBankAccount(accountList, apiId);
        }
        // 应对缓存穿透，不要一直访问数据库
        accountList = new ArrayList<>();
        accountList.add(new BranchBankSourceAccount());
        accountMap.put(key, accountList);
        RedisUtil.setToCaches(key, accountList);
        return null;
    }

    @Override
    public void flushAgencyAccountCache(String cacheKey) {
        accountMap.remove(cacheKey);
    }

    /**
     * 判断是分组账户还是接口账户，并返回账户dataConfig
     * @param accountList
     * @param apiId
     * @return
     */
    private String filterBranchBankAccount(List<BranchBankSourceAccount> accountList, Integer apiId) {
        return Objects.equals(accountList.get(0).getSourceFlag(), 0) ? accountList.get(0).getDataConfig() :
                accountList.stream()
                        .filter(account ->
                                Objects.equals(account.getApiId(), apiId))
                        .findAny()
                        .orElse(new BranchBankSourceAccount())
                        .getDataConfig();
    }
}
