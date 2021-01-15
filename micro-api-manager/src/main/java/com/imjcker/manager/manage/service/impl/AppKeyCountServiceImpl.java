package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.mapper.ApiInfoMapper;
import com.imjcker.manager.manage.model.CountApiByAppKeyVO;
import com.imjcker.manager.manage.model.ShowVO;
import com.imjcker.manager.manage.po.ApiInfoRedisMsg;
import com.imjcker.manager.manage.po.ApiInfoVersionsWithBLOBs;
import com.imjcker.manager.manage.po.ApiInfoWithBLOBs;
import com.lemon.common.util.BeanCustomUtils;
import com.imjcker.manager.manage.service.AppKeyCountService;
import com.imjcker.manager.manage.service.RedisService;
import com.imjcker.manager.elastic.elasticsearch.EsRestClient;
import com.imjcker.manager.elastic.model.ApiIdCount;
import com.imjcker.manager.elastic.model.AppKeyCount;
import com.imjcker.manager.elastic.model.AppKeyQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppKeyCountServiceImpl implements AppKeyCountService {
    private static ThreadLocal<String> groupType = new ThreadLocal<>();
    @Autowired
    private EsRestClient esRestClient;
    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public ShowVO index(String index, String type, AppKeyQuery query) {
        Map<String, List<AppKeyCount>> appKeyCountMap = this.getAppKeyCountMap(index, type, query);
        ShowVO showVO = new ShowVO();
        List<AppKeyCount> appKeyCountList = new LinkedList<>();
        List<String> t = new ArrayList<>();
        List<Long> c = new ArrayList<>();
        appKeyCountMap.forEach(((time, appKeyCounts) -> {
            t.add(time);
            long sumTotal = appKeyCounts.stream().collect(Collectors.summarizingLong(AppKeyCount::getCount)).getSum();
            c.add(sumTotal);
            appKeyCountList.addAll(appKeyCounts);
        }));
        ShowVO.QueryGroup queryGroup = new ShowVO.QueryGroup();
        queryGroup.setTime(t);
        queryGroup.setCount(c);
        showVO.setGroupCount(queryGroup);
        long sumTotal = appKeyCountList.stream().collect(Collectors.summarizingLong(AppKeyCount::getCount)).getSum();
        long sumTotalSuccess = appKeyCountList.stream().collect(Collectors.summarizingLong(AppKeyCount::getCountSuccess)).getSum();
        long sumTotalFail = appKeyCountList.stream().collect(Collectors.summarizingLong(AppKeyCount::getCountFail)).getSum();
        showVO.setTotalCount(sumTotal);
        showVO.setTotalSuccess(sumTotalSuccess);
        showVO.setTotalFail(sumTotalFail);

        List<AppKeyCount> appKeyCountAll = new LinkedList<>();
        Map<String, List<AppKeyCount>> appKeyAppKeyCountMap = appKeyCountList.stream().collect(Collectors.groupingBy(AppKeyCount::getAppKey));
        appKeyAppKeyCountMap.forEach((appKey, appKeyCounts) -> {
            AppKeyCount appKeyCount = appKeyCounts.get(0);
            long count = appKeyCounts.stream().collect(Collectors.summarizingLong(AppKeyCount::getCount)).getSum();
            long countSuccess = appKeyCounts.stream().collect(Collectors.summarizingLong(AppKeyCount::getCountSuccess)).getSum();
            long countFail = appKeyCounts.stream().collect(Collectors.summarizingLong(AppKeyCount::getCountFail)).getSum();
            List<ApiIdCount> list = new LinkedList<>();
            appKeyCounts.forEach(ac -> list.addAll(ac.getApiCountList()));
            appKeyCount.setCount(count);
            appKeyCount.setCountSuccess(countSuccess);
            appKeyCount.setCountFail(countFail);
            appKeyCount.setApiCountList(list);
            appKeyCountAll.add(appKeyCount);
        });
        showVO.setTotal(appKeyCountAll.size());

        List<AppKeyCount> page = appKeyCountAll.stream()
                .sorted(Comparator.comparing(AppKeyCount::getCount).reversed())// 排序
                .skip((query.getPageNum() - 1L) * query.getPageSize()).limit(query.getPageSize())// 分页
                .collect(Collectors.toList());
        showVO.setAppKeyCountList(page);

        return showVO;
    }

    private Map<String, List<AppKeyCount>> getAppKeyCountMap(String index, String type, AppKeyQuery query) {
        Map<String, List<AppKeyCount>> map = new ConcurrentSkipListMap<>(Comparator.comparingInt(Integer::valueOf));
        List<AppKeyQuery> queryList = groupStrategy(query); //分段查询
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            String gType = groupType.get();
            Calendar cal = Calendar.getInstance();
            CompletableFuture[] futures = queryList.stream().map(subQuery ->
                    CompletableFuture.supplyAsync(() -> this.searchByApiId(index, type, subQuery), pool)
                            .whenComplete((appKeyCountVOList, exp) -> {
                                        cal.setTimeInMillis(subQuery.getStartTime());
                                        map.put(formatDate(gType, cal), appKeyCountVOList);
                                    }
                            )
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            pool.shutdown();
        }
        return map;
    }

    /**
     * 分组查询条件
     *
     * @param query 原始查询条件
     * @return 分组后的查询条件的map
     */
    private static List<AppKeyQuery> groupStrategy(AppKeyQuery query) {
        List<AppKeyQuery> map = new ArrayList<>();
        long startTime = query.getStartTime();
        long endTime = query.getEndTime();

        if (DateUtils.isSameDay(new Date(startTime), new Date(endTime))) {
            // group by hour
            groupType.set("H");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(startTime));
            while (cal.getTimeInMillis() <= endTime) {
                AppKeyQuery sourceQuery = new AppKeyQuery();
                BeanUtils.copyProperties(query, sourceQuery);
                sourceQuery.setStartTime(cal.getTimeInMillis());
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                if (cal.getTimeInMillis() <= endTime) {
                    sourceQuery.setEndTime(cal.getTimeInMillis());
                } else {
                    sourceQuery.setEndTime(endTime);
                }
                map.add(sourceQuery);
                cal.add(Calendar.MILLISECOND, 1);
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(startTime));
            cal.add(Calendar.MONTH, 1);
            if (endTime <= cal.getTimeInMillis()) {
                //group by day
                groupType.set("D");
                cal.setTime(new Date(startTime));
                while (cal.getTimeInMillis() <= endTime) {
                    AppKeyQuery sourceQuery = new AppKeyQuery();
                    BeanUtils.copyProperties(query, sourceQuery);
                    sourceQuery.setStartTime(cal.getTimeInMillis());
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    if (cal.getTimeInMillis() <= endTime) {
                        sourceQuery.setEndTime(cal.getTimeInMillis());
                    } else {
                        sourceQuery.setEndTime(endTime);
                    }
                    map.add(sourceQuery);
                    cal.add(Calendar.MILLISECOND, 1);
                }
            } else {
                cal.setTime(new Date(startTime));
                cal.add(Calendar.YEAR, 1);
                if (endTime <= cal.getTimeInMillis()) {
                    // group by month
                    groupType.set("M");
                    cal.setTime(new Date(startTime));
                    while (cal.getTimeInMillis() <= endTime) {
                        AppKeyQuery sourceQuery = new AppKeyQuery();
                        BeanUtils.copyProperties(query, sourceQuery);
                        sourceQuery.setStartTime(cal.getTimeInMillis());
                        int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cal.set(Calendar.DAY_OF_MONTH, actualMaximum);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.MILLISECOND, 999);
                        if (cal.getTimeInMillis() <= endTime) {
                            sourceQuery.setEndTime(cal.getTimeInMillis());
                        } else {
                            sourceQuery.setEndTime(endTime);
                        }
                        map.add(sourceQuery);
                        cal.add(Calendar.MILLISECOND, 1);
                    }
                } else {
                    groupType.set("M");
//                    log.error("时间范围过大{}--{}，查询当年记录。", new Date(startTime), new Date(endTime));
                    query.setEndTime(cal.getTimeInMillis()-1);
                    map.addAll(groupStrategy(query));
                }
            }
        }
        return map;
    }

    private String formatDate(String type, Calendar calendar) {
        int re;
        switch (type) {
            case "H":
                re = calendar.get(Calendar.HOUR_OF_DAY);
                break;
            case "D":
                re = calendar.get(Calendar.DAY_OF_MONTH);
                break;
            case "M":
                re = calendar.get(Calendar.MONTH) + 1;
                break;
            default:
                re = calendar.get(Calendar.HOUR_OF_DAY);
        }
        return re + "";
    }

    /**
     * 根据apiId查询统计量
     *
     * @param index
     * @param type
     * @param query
     * @return
     */
    @Override
    public List<AppKeyCount> searchByApiId(String index, String type, AppKeyQuery query) {
        List<AppKeyCount> appKeyCounts = esRestClient.countByAppKey(index, type, query);
        appKeyCounts.forEach(appKeyCount -> {
            appKeyCount.getApiCountList().forEach(apiIdCount -> {
                JSONObject apiInfoObject = redisService.get("api:" + apiIdCount.getApiId());
                if (apiInfoObject != null) {
                    ApiInfoRedisMsg apiInfoRedisMsg = apiInfoObject.toJavaObject(ApiInfoRedisMsg.class);
                    ApiInfoVersionsWithBLOBs apiInfo = apiInfoRedisMsg.getApiInfoWithBLOBs();
                    apiIdCount.setApiName(apiInfo.getApiName());
                } else {
                    ApiInfoWithBLOBs apiInfo = apiInfoMapper.selectByPrimaryKey(apiIdCount.getApiId());
                    if (apiInfo != null) {
                        apiIdCount.setApiName(apiInfo.getApiName());
                    }
                }
            });
        });
        return appKeyCounts;
    }

    @Override
    public List<AppKeyCount> test(String index, String type, AppKeyQuery query) {
        List<AppKeyCount> appKeyCounts = esRestClient.countByAppKey(index, type, query);
        return appKeyCounts;
    }

    @Override
    public CountApiByAppKeyVO countApiByAppKey(String index, String type, AppKeyQuery query) {
        Assert.notNull(query.getAppKey(), "appKey不允许为空");
        // 构造新的查询条件
        AppKeyQuery temp = new AppKeyQuery();
        BeanCustomUtils.copyPropertiesIgnoreNull(query, temp);
        temp.setPageNum(1);
        //查询es
        ShowVO showVO = this.index(index, type, temp);
        //构造新VO
        CountApiByAppKeyVO vo = new CountApiByAppKeyVO();
        BeanCustomUtils.copyPropertiesIgnoreNull(showVO, vo);
        AppKeyCount appKeyCount = showVO.getAppKeyCountList().get(0);
        List<ApiIdCount> apiCountList = appKeyCount.getApiCountList();
        //按apiId分组并遍历计算参数
        Map<Integer, List<ApiIdCount>> collect = apiCountList.stream().collect(Collectors.groupingBy(ApiIdCount::getApiId));
        List<ApiIdCount> apiCountList_new = new ArrayList<>();
        collect.forEach((apiId, list)->{
            ApiIdCount apiIdCount = list.get(0);
            long sum = list.stream().collect(Collectors.summarizingLong(ApiIdCount::getCount)).getSum();
            long sum_s = list.stream().collect(Collectors.summarizingLong(ApiIdCount::getCountSuccess)).getSum();
            long sum_f = list.stream().collect(Collectors.summarizingLong(ApiIdCount::getCountFail)).getSum();
            int avg = list.stream().collect(Collectors.averagingInt(ApiIdCount::getAvgSuccessSpendTime)).intValue();
            apiIdCount.setCount(sum);
            apiIdCount.setCountSuccess(sum_s);
            apiIdCount.setCountFail(sum_f);
            apiIdCount.setAvgSuccessSpendTime(avg);
            apiCountList_new.add(apiIdCount);
        });
        //排序和分页
        List<ApiIdCount> page = apiCountList_new.stream()
                .sorted(Comparator.comparing(ApiIdCount::getCount).reversed())// 排序
                .skip((query.getPageNum() - 1L) * query.getPageSize()).limit(query.getPageSize())// 分页
                .collect(Collectors.toList());
        vo.setApiCountList(page);
        vo.setTotal(apiCountList_new.size());

        return vo;
    }
}
