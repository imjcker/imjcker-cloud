package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.mapper.ApiInfoMapper;
import com.imjcker.manager.manage.model.IndexVO;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.vo.ShowVOSourceLogInfo;
import com.lemon.common.http.proxy.OkHttp1;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.ExtractUtil;
import com.imjcker.manager.manage.service.CountService;
import com.imjcker.manager.manage.service.RedisService;
import com.imjcker.manager.elastic.elasticsearch.EsRestClient;
import com.imjcker.manager.elastic.model.ApiCount;
import com.imjcker.manager.elastic.model.GroupCount;
import com.imjcker.manager.elastic.model.SourceLogInfo;
import com.imjcker.manager.elastic.model.SourceQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CountServiceImpl implements CountService {
    private static ThreadLocal<String> groupType = new ThreadLocal<>();
    private static SourceQuery previousQuery;// 用于保存上次查询条件
    private static List<ApiCountVO> previousResult;// 用于保存上次查询结果
    private static IndexVO previousIndexVO;// 用于保存上次查询结果

    @Autowired
    private EsRestClient esRestClient;
    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Autowired
    private RedisService redisService;

    @Value("${logPlatform.url}")
    private String logUrl;

    @Value("${logPlatform.loginPath}")
    private String loginPath;

    @Value("${logPlatform.logPath}")
    private String logPath;

    @Value("${logPlatform.username}")
    private String logUsername;

    @Value("${logPlatform.password}")
    private String logPassword;

    /**
     * 根据apiId查询统计量
     *
     * @param index
     * @param type
     * @param
     * @return
     */
    @Override
    public List<ApiCountVO> searchByApiId(String index, String type, SourceQuery query) {

        List<ApiCountVO> result = new ArrayList<>();
        Map<Integer, ApiCount> apiCountMap = esRestClient.countByApiId(index, type, query);
        apiCountMap.forEach((apiId, v) -> {
            ApiCountVO apiCountVO = new ApiCountVO();
            BeanCustomUtils.copyPropertiesIgnoreNull(v, apiCountVO);
            JSONObject apiInfoObject = redisService.get("api:" + apiId);
            if (apiInfoObject != null) {
                ApiInfoRedisMsg apiInfoRedisMsg = apiInfoObject.toJavaObject(ApiInfoRedisMsg.class);
                ApiInfoVersionsWithBLOBs apiInfoVersions = apiInfoRedisMsg.getApiInfoWithBLOBs();
                apiCountVO.setApiGroupId(apiInfoVersions.getApiGroupId());
                apiCountVO.setApiName(apiInfoVersions.getApiName());
                apiCountVO.setApiId(apiId);
                result.add(apiCountVO);
            } else {
                ApiInfoExample example = new ApiInfoExample();
                ApiInfoExample.Criteria criteria = example.createCriteria();
                criteria.andIdEqualTo(apiId);
                //根据apiid查询api信息
                List<ApiInfoWithBLOBs> list = apiInfoMapper.selectByExampleWithBLOBs(example);
                if (list.size() != 0) {
                    ApiInfoWithBLOBs apiInfo = list.get(0);
                    apiCountVO.setApiGroupId(apiInfo.getApiGroupId());
                    apiCountVO.setApiName(apiInfo.getApiName());
                    apiCountVO.setApiId(apiId);
                    result.add(apiCountVO);
                }
            }
        });
        return result;
    }

    @Override
    public IndexVO index(String index, String type, SourceQuery query) {
        Map<String, List<ApiCountVO>> apiCountMap;
        IndexVO indexVO = new IndexVO();
        List<ApiCountVO> result = new ArrayList<>();

        if (query.equals(previousQuery) && null != previousIndexVO && null != previousResult && previousResult.size() > 0) {
            indexVO = previousIndexVO;
            result = previousResult;
        } else {
            long st = System.currentTimeMillis();
            apiCountMap = this.getApiCountList(index, type, query);
            log.info("API统计耗时:{}", System.currentTimeMillis() - st);
            long st2 = System.currentTimeMillis();
            List<ApiCountVO> apiCountVOList = new ArrayList<>();
            IndexVO.QueryGroup queryGroup = new IndexVO.QueryGroup();
            queryGroup.setType(groupType.get());
            List<String> t = new ArrayList<>();
            List<Long> c = new ArrayList<>();
            apiCountMap.forEach((time, apiCounts) -> {
                t.add(time);
                long sum = apiCounts.stream().collect(Collectors.summarizingLong(ApiCountVO::getCount)).getSum();
                c.add(sum);
                apiCountVOList.addAll(apiCounts);
            });
            queryGroup.setTime(t);
            queryGroup.setCount(c);

            //查询结果总量统计
            LongSummaryStatistics totalCount = apiCountVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCount));
            LongSummaryStatistics totalCountSuccess = apiCountVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCountSuccess));
            LongSummaryStatistics totalCountFail = apiCountVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCountFail));
            LongSummaryStatistics totalCountException = apiCountVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCountExp));
            indexVO.setGroup(queryGroup);
            indexVO.setTotalCount(totalCount.getSum());
            indexVO.setTotalSuccess(totalCountSuccess.getSum());
            indexVO.setTotalFail(totalCountFail.getSum());
            indexVO.setTotalException(totalCountException.getSum());

            //按apiId分组
            Map<Integer, List<ApiCountVO>> apiGroupByApiId = apiCountVOList.stream().collect(Collectors.groupingBy(ApiCountVO::getApiId));
            //分别统计各个api情况
            List<ApiCountVO> finalResult = new ArrayList<>();
            apiGroupByApiId.forEach((apiId, countVOList) -> {
                ApiCountVO apiCountVO = countVOList.get(0);
                Double collect = countVOList.stream().collect(Collectors.averagingLong(ApiCountVO::getAvgSuccessResponseTime));
                LongSummaryStatistics count = countVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCount));
                LongSummaryStatistics countSuccess = countVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCountSuccess));
                LongSummaryStatistics countFailed = countVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCountFail));
                LongSummaryStatistics countException = countVOList.stream().collect(Collectors.summarizingLong(ApiCountVO::getCountExp));
                apiCountVO.setAvgSuccessResponseTime(collect.intValue());
                apiCountVO.setCount(count.getSum());
                apiCountVO.setCountSuccess(countSuccess.getSum());
                apiCountVO.setCountFail(countFailed.getSum());
                apiCountVO.setCountExp(countException.getSum());
                finalResult.add(apiCountVO);
            });

            //排序
            result = finalResult.stream().sorted(Comparator.comparing(ApiCountVO::getCount).reversed()).collect(Collectors.toList());

            indexVO.setTotal(result.size());
            log.info("API统计计算耗时耗时:{}", System.currentTimeMillis() - st2);

            long st1 = System.currentTimeMillis();
            List<GroupCount> groupCounts = new ArrayList<>(esRestClient.countBySourceName(index, type, query).values());
            log.info("数据源统计耗时:{}", System.currentTimeMillis() - st1);
            indexVO.setGroupCountList(groupCounts);

            //保存当前查询状态
            previousQuery = query;
            previousIndexVO = indexVO;
            previousResult = result;
        }
        //分页
        List<ApiCountVO> page = result.stream().skip((query.getPageNum() - 1L) * query.getPageSize()).limit(query.getPageSize()).collect(Collectors.toList());
        indexVO.setApiCountList(page);

        return indexVO;
    }

    /**
     * 根据不同的时间范围分组并发查询结果
     *
     * @param index 索引
     * @param type  索引类型
     * @param query 查询条件
     * @return 合并结果集
     */
    private Map<String, List<ApiCountVO>> getApiCountList(String index, String type, SourceQuery query) {
        Map<String, List<ApiCountVO>> map = new ConcurrentSkipListMap<>(Comparator.comparingInt(Integer::valueOf));
        List<SourceQuery> queryList = groupStrategy(query);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            String gType = groupType.get();
            Calendar cal = Calendar.getInstance();
            CompletableFuture[] futures = queryList.stream().map(subQuery ->
                    CompletableFuture.supplyAsync(() -> this.searchByApiId(index, type, subQuery), pool)
                            .whenComplete((apiCounts, exp) -> {
                                        cal.setTimeInMillis(subQuery.getStartTime());
                                        if ("F".equals(query.getStatus())) {
                                            apiCounts = apiCounts.stream().filter(apiCount -> apiCount.getCountFail() != 0).collect(Collectors.toList());
                                        } else if ("E".equals(query.getStatus())) {
                                            apiCounts = apiCounts.stream().filter(apiCount -> apiCount.getCountExp() != 0).collect(Collectors.toList());
                                        }
                                        map.put(formatDate(gType, cal), apiCounts);
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
     * 根据数据源查询统计量
     *
     * @param index
     * @param type
     * @param
     * @return
     */
    @Override
    public IndexVO searchBySourceName(String index, String type, SourceQuery query) {
        Map<String, GroupCount> map = esRestClient.countBySourceName(index, type, query);
        List<GroupCount> groupCountList = new ArrayList<>();
        map.forEach((sourceName, groupCount) -> groupCountList.add(groupCount));
        LongSummaryStatistics totalCount = groupCountList.stream().collect(Collectors.summarizingLong(GroupCount::getCountBySourceName));
        LongSummaryStatistics totalCountSuccess = groupCountList.stream().collect(Collectors.summarizingLong(GroupCount::getCountSuccessBySourceName));
        LongSummaryStatistics totalCountFail = groupCountList.stream().collect(Collectors.summarizingLong(GroupCount::getCountFailBySourceName));
        LongSummaryStatistics totalCountExcp = groupCountList.stream().collect(Collectors.summarizingLong(GroupCount::getCountExceptionBySourceName));
        IndexVO indexVO = new IndexVO();
//        indexVO.setTotalCount(totalCount.getSum());
        indexVO.setTotalSuccess(totalCountSuccess.getSum());
        indexVO.setTotalFail(totalCountFail.getSum());
        indexVO.setTotalException(totalCountExcp.getSum());
        indexVO.setGroupCountList(groupCountList);
        return indexVO;
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
     * 分组查询条件
     *
     * @param query 原始查询条件
     * @return 分组后的查询条件的map
     */
    private static List<SourceQuery> groupStrategy(SourceQuery query) {
        List<SourceQuery> map = new ArrayList<>();
        long startTime = query.getStartTime();
        long endTime = query.getEndTime();

        if (DateUtils.isSameDay(new Date(startTime), new Date(endTime))) {
            // group by hour
            groupType.set("H");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(startTime));
            while (cal.getTimeInMillis() <= endTime) {
                SourceQuery sourceQuery = new SourceQuery();
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
                    SourceQuery sourceQuery = new SourceQuery();
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
                        SourceQuery sourceQuery = new SourceQuery();
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
                    log.error("时间范围过大{}--{}，查询当年记录。", new Date(startTime), new Date(endTime));
                    query.setEndTime(cal.getTimeInMillis());
                    map.addAll(groupStrategy(query));
                }
            }
        }
        return map;
    }

    @Override
    public JSONObject getInfoByUid(String index, String type, String indexZuul, String typeZuul, String uid) {
        String info = esRestClient.get(index, type, uid);
        String queryInfo = esRestClient.get(indexZuul, typeZuul, uid);
        JSONObject object = new JSONObject();
        if (StringUtils.isBlank(info))
            object.put("sourceInfo", "未查询到上游信息");
        else
            object.put("sourceInfo", ExtractUtil.xml2Json(info));

        if (StringUtils.isBlank(queryInfo))
            object.put("queryInfo", "未查询到下游信息");
        else
            object.put("queryInfo", ExtractUtil.xml2Json(queryInfo));
//        System.out.print(object);
        return object;
    }

    /**
     * 查询调用失败列表
     *
     * @param index
     * @param type
     * @param sourceQuery
     */
    @Override
    public ShowVOSourceLogInfo errorListPage(String index, String type, SourceQuery sourceQuery, int size) {
        List<SourceLogInfo> list = esRestClient.queryErrorList(index, type, sourceQuery, size);
        ShowVOSourceLogInfo showVO = new ShowVOSourceLogInfo();
        showVO.setTotalCount(list.size());
        List<SourceLogInfo> page = list.stream()
                .sorted(Comparator.comparing(SourceLogInfo::getSourceResponseCode).reversed())// 排序
                .skip((sourceQuery.getPageNum() - 1) * sourceQuery.getPageSize()).limit(sourceQuery.getPageSize())// 分页
                .collect(Collectors.toList());
        showVO.setList(page);
        return showVO;
    }

    /**
     * 根据uid和app请求日志平台，返回日志数据
     *
     * @param jsonObject
     * @return
     */
    @Override
    public List<String> queryLogByUid(JSONObject jsonObject) {
        //登录验证,获取token，token有效期为10分钟
        String loginUrl = logUrl + loginPath;
        String queryUrl = logUrl + logPath;
        String token = getToken(loginUrl, logUsername, logPassword);
        //发起日志查询请求
        if (null != jsonObject) {
            String uid = jsonObject.getString("uid");
            String app = jsonObject.getString("app");
            Long sourceCreateTime = jsonObject.getLong("sourceCreateTime");
            if (StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(app) && null != sourceCreateTime) {
                return sendRequestToLog(queryUrl, uid, app, sourceCreateTime, token);
            }
        }
        return null;
    }

    /**
     * 获取日志平台登录token
     *
     * @param loginUrl
     * @return
     */
    private String getToken(String loginUrl, String logUsername, String logPassword) {
        String token = null;
        JSONObject tokenObject = redisService.get("logPlatform:" + logUsername + ":token");
        //token不存在，发起登录请求
        if (null == tokenObject) {
            //拼接登录参数
            JSONObject user = new JSONObject();
            user.put("username", logUsername);
            user.put("password", logPassword);
            String json = user.toString();
            String tokenStr = OkHttp1.postJson(loginUrl, null, json, null, 30000L, "http", "false", 0, false).getResult();
            if (StringUtils.isNotBlank(tokenStr)) {
                JSONObject tokenResult = JSON.parseObject(tokenStr);
                Integer code = tokenResult.getInteger("code");
                if (null != code && 200 == code.intValue()) {
                    if (null != tokenResult.getJSONObject("content")) {
                        token = tokenResult.getJSONObject("content").getString("token");
                        JSONObject tokenParam = new JSONObject();
                        tokenParam.put("token",token);
                        redisService.set("logPlatform:" + logUsername + ":token", tokenParam,600);//10分钟过期时间
                    }
                }
            }
        } else {
            token=tokenObject.getString("token");
        }
        return token;
    }

    /**
     * 向日志平台发送日志上下文请求
     *
     * @param
     * @param
     * @param
     * @param sourceCreateTime
     */
    private List<String> sendRequestToLog(String url, String context, String appName, Long sourceCreateTime, String token) {
        List<String> resultList = new ArrayList<>();
        //解析time为日期和时间，格式为"date": "2020-05-12","time": "15:21:04|16:21:04",
        String date = DateUtil.stampToDateNoTime(sourceCreateTime);//获取日期
        String timeStart = DateUtil.stampToTimeNoDate(sourceCreateTime - 3600000);//获取时间
        String timeEnd = DateUtil.stampToTimeNoDate(sourceCreateTime + 3600000);//获取时间
        //包装时间范围，前后一小时
        String time = timeStart + "|" + timeEnd;
        //包装请求数据
        JSONObject sortObject = new JSONObject();
        sortObject.put("sortFiled", "logTime");
        sortObject.put("sortType", "asc");
        JSONObject requestParam = new JSONObject();
        requestParam.put("date",date);
        requestParam.put("context", "\""+context+"\"");
        requestParam.put("pageSize", 100);
        requestParam.put("time", time);
        requestParam.put("sortStr", sortObject);

        JSONArray filterArray = new JSONArray();
        JSONObject appFilter = new JSONObject();
        appFilter.put("condition", "app");
        appFilter.put("params", appName);
        appFilter.put("disabled", false);
        filterArray.add(appFilter);
        JSONObject logTypeFilter = new JSONObject();
        logTypeFilter.put("condition", "logType");
        logTypeFilter.put("params", "all");
        logTypeFilter.put("disabled", false);
        filterArray.add(logTypeFilter);
        JSONObject systemFilter = new JSONObject();
        systemFilter.put("condition", "system");
        systemFilter.put("params", "123010151");
        systemFilter.put("disabled", false);
        filterArray.add(systemFilter);

        requestParam.put("filter", filterArray);
        String json = requestParam.toString();
        //header
        Map<String, String> header = new HashMap<>();
        header.put("token", token);

        String resultStr = OkHttp1.postJson(url, null, json, header, 30000L, "http", "false", 0, false).getResult();
        //返回数据解析
        JSONObject resultObject = JSON.parseObject(resultStr);
        Integer code = resultObject.getInteger("code");
        Integer status = resultObject.getInteger("status");
        if (null != code && 200 == code.intValue()) {//正常获取数据
            JSONObject content = resultObject.getJSONObject("content");
            JSONArray hitsArray = content.getJSONArray("hits");
            Iterator<Object> iterator = hitsArray.iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                JSONObject hit = (JSONObject) iterator.next();
                resultList.add(hit.getJSONObject("_source").getString("message"));
            }
            return resultList;
        } else if (null != status && 2000 == status.intValue()) {//token无效导致获取日志失败，重新获取token发起请求
            log.info("{}",resultObject);
            resultList.add("权限校验失败");
        } else {
            log.info("{}",resultObject);
            resultList.add("未知码值");
        }
        return resultList;
    }

    public String test() {
        Map<Integer, ApiCount> result = null;
        try {
            result = esRestClient.countByApiIdTest("inmgr90", "inmgr90", null);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "1";
    }
}

