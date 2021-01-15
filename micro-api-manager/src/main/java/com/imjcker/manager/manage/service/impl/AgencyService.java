package com.imjcker.manager.manage.service.impl;

import com.imjcker.manager.manage.po.Agency;
import com.imjcker.manager.manage.po.query.AgencyQuery;
import com.lemon.common.http.proxy.OkHttp;
import com.lemon.common.util.RedisUtil;
import com.lemon.common.util.collections.CollectionUtil;
import com.lemon.common.vo.ZuulHeader;
import com.imjcker.manager.manage.mapper.AgencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class AgencyService {
    @Autowired
    private AgencyMapper agencyMapper;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${notify-cache.service.name}")
    private String zuulServiceName;

    private static ExecutorService executors = new ThreadPoolExecutor(5, 10,
            60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(32));

    private static final Logger logger = LoggerFactory.getLogger(AgencyService.class);

    public List<Agency> agencyInfoList(AgencyQuery query) {
        query.setCount(this.queryCount(query));
        List<Agency> agencies=agencyMapper.queryByPage(query);
        return agencies;
    }

    private int queryCount(AgencyQuery query) {
        return  agencyMapper.queryCount(query);
    }

    public void delete(Agency agency) {
        Agency select = agencyMapper.select(agency);
        agencyMapper.deleteAgency(agency);
        executors.execute(() -> flushZuul(select.getAppKey(), select.getApiGroupId()));
    }

    public void add(Agency agency) {
        agencyMapper.insert(agency);
        executors.execute(() -> flushZuul(agency.getAppKey(), agency.getApiGroupId()));
    }

    public void update(Agency agency) {
        agencyMapper.update(agency);
        executors.execute(() -> flushZuul(agency.getAppKey(), agency.getApiGroupId()));
    }

    public  List<Agency> checkUnique(Agency agency) {
        return agencyMapper.selectAgency(agency);
    }

    public boolean isSourceExit(Agency agency) {
        List<Agency> list = agencyMapper.selectBySourceAndGroup(agency);
        return (list != null && list.size()>0)?true:false;
    }


    private void flushZuul(String appKey, Integer groupId) {
        List<Agency> list = agencyMapper.queryByAppKeyAndgroup(appKey, groupId);
        String key = appKey + ":" + groupId;
        if (CollectionUtil.isNotEmpty(list)) RedisUtil.setToCaches(key, list);
        else RedisUtil.delete(key);

        List<ServiceInstance> instances = discoveryClient.getInstances(zuulServiceName);
        Map<String, String> header = new HashMap<>();
        header.put(ZuulHeader.FLUSH_ZUUL_AGENCY_ACCOUNT, key);
        for (ServiceInstance instance : instances) {
            String url = instance.getUri().toString();
            String result = OkHttp.get(url, null, header, 10000L, "HTTP", "true", 3,false);
            logger.info("访问刷新缓存结果url：{}, result: {}", url, result);
        }
    }
}
