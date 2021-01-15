package com.imjcker.manager.charge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.charge.mapper.*;
import com.imjcker.manager.charge.po.*;
import com.imjcker.manager.charge.service.*;
import com.imjcker.manager.charge.vo.*;
import com.imjcker.manager.charge.vo.request.ReqSomeByPage;
import com.imjcker.manager.charge.vo.response.RespCompanyBalanceHistory;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.util.CsvUtil;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.RedisUtil;
import com.imjcker.manager.elastic.elasticsearch.CustomerBillClient;
import com.imjcker.manager.elastic.model.ApiChargeCount;
import com.imjcker.manager.elastic.model.CustomerChargeCount;
import com.imjcker.manager.elastic.model.QueryInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerBillServiceImpl implements CustomerBillService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String FILE_PATH = System.getProperty("user.dir");//文件指定存放的路径
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private CustomerBillClient customerBillClien;

    @Autowired
    private CompanyAppsAuthMapper companyAppsAuthMapper;

    @Autowired
    private CompanyBillDayDatailMapper companyBillDayDatailMapper;

    @Autowired
    private CompanyBillDayMapper companyBillDayMapper;

    @Autowired
    private CompanyBillMonthMapper companyBillMonthMapper;

    @Autowired
    private CompanyBillYearMapper companyBillYearMapper;

    @Autowired
    private BillingRulesService billingRulesService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DatasourceChargeService datasourceChargeService;

    @Autowired
    private WebBackendService webBackendService;

    @Autowired
    private CompanyBalanceHistoryMapper companyBalanceHistoryMapper;

    @Value("${es.indexZuul}")
    private String index;//ES索引名称

    @Value("${es.typeZuul}")
    private String type;//ES类型名称

    /**
     * 年账单查询
     *
     * @param query
     * @return
     */
    @Override
    public ShowVOCompanyBillYear listBillByYear(CompanyBillDayQuery query) {
        String appKey = query.getAppkey();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        List<CompanyBillYear> result = new ArrayList<>();
        ShowVOCompanyBillYear showVO = new ShowVOCompanyBillYear();
        //查询今年账单（当查询截止时间大于今年的起始时间，查询今年的账单）
        try {
            long thisYearBeginMills = DateUtil.yearTimeInMillis(0, null);
            if (endTime > thisYearBeginMills) {
                List<CompanyBillYear> thisYearBill = getThisYearBill(thisYearBeginMills, System.currentTimeMillis(), appKey);//年初到此刻
                if (null != thisYearBill && thisYearBill.size() > 0)
                    result.addAll(thisYearBill);
            }
            //查询往年账单（数据库，根据起始截止时间的年份查询）
            Example yearExample = new Example(CompanyBillYear.class);
            Example.Criteria yearCriteria = yearExample.createCriteria();
            yearExample.setOrderByClause("create_time DESC");
            if (StringUtils.isNotBlank(appKey)) {
                yearCriteria.andLike("appKey", "%" + appKey + "%");
            }
            if (null != startTime && null != endTime) {
                //获取startTime所在年份的起始时间戳，endTime所在年份的截止时间戳
                yearCriteria.andBetween("createTime", DateUtil.yearTimeInMillis(0, startTime), DateUtil.yearTimeInMillis(1, endTime) - 1);
            }
            List<CompanyBillYear> otherYearBill = companyBillYearMapper.selectByExample(yearExample);
            if (null != otherYearBill && otherYearBill.size() > 0)
                result.addAll(otherYearBill);
            showVO.setTotalCount(result.size());
            List<CompanyBillYear> page = result.stream()
                    .sorted(Comparator.comparing(CompanyBillYear::getCreateTime).reversed())// 排序
                    .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                    .collect(Collectors.toList());
            showVO.setList(page);
        } catch (Exception e) {
            LOGGER.error("查询年账单出错,{}", e.getMessage());
        }
        return showVO;
    }

    /**
     * 月账单查询
     *
     * @param query
     * @return
     */
    @Override
    public ShowVOCompanyBillMonth listBillByMonth(CompanyBillDayQuery query) {
        List<CompanyBillMonth> result = new ArrayList<>();
        String appKey = query.getAppkey();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        ShowVOCompanyBillMonth showVO = new ShowVOCompanyBillMonth();
        try {
            long thisMonthMills = DateUtil.monthTimeInMillis(0, null);
            if (endTime > thisMonthMills) {//当查询截止时间超过当月起始时间，才会实时查询当月数据
                List<CompanyBillDay> thisMonthBillList = new ArrayList<>();
                //1-查询当月数据
                result = getThisMonthBill(thisMonthMills, endTime, appKey);
            }
            //2-数据库查询其他月份
            Example example = new Example(CompanyBillMonth.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(appKey)) {
                criteria.andLike("appKey", "%" + appKey + "%");
            }
            if (null != startTime && null != endTime) {
                criteria.andBetween("createTime", DateUtil.monthTimeInMillis(0, startTime), DateUtil.monthTimeInMillis(1, endTime) - 1);
            }
            example.setOrderByClause("create_time DESC");
            List<CompanyBillMonth> otherMonthBill = companyBillMonthMapper.selectByExample(example);
            if (null != otherMonthBill && otherMonthBill.size() > 0)
                result.addAll(otherMonthBill);
            showVO.setTotalCount(result.size());
            List<CompanyBillMonth> page = result.stream()
                    .sorted(Comparator.comparing(CompanyBillMonth::getCreateTime).reversed())// 排序
                    .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                    .collect(Collectors.toList());
            showVO.setList(page);
        } catch (Exception e) {
            LOGGER.error("查询月账单出错,{}", e.getMessage());
        }
        return showVO;
    }

    /**
     * 获取日账单
     *
     * @return
     * @throws ParseException
     */
    @Override
    public ShowVO listBillByDay(CompanyBillDayQuery query) {
        String appKey = query.getAppkey();
        Long startTime = query.getStartTime();
        Long endTime = query.getEndTime();
        if (null != endTime && endTime > System.currentTimeMillis())
            endTime = System.currentTimeMillis();
        List<CompanyBillDay> result = new ArrayList<>();
        long todayMills = DateUtil.dayTimeInMillis(0, null);
        if (endTime > todayMills) {//ES查询当日数据
            List<CompanyBillDay> todayBill = getTodayBill(todayMills, endTime, appKey);
            if (null != todayBill && todayBill.size() > 0)
                result.addAll(todayBill);
        }
        //数据库获取账单
        Example example = new Example(CompanyBillDay.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("create_time DESC");
        if (StringUtils.isNotBlank(appKey)) {
            criteria.andLike("appKey", "%" + appKey + "%");
        }
        //查询范围为起始日期的起始时间戳，截止日期的截止时间戳
        if (null != startTime && null != endTime) {
            criteria.andBetween("createTime", DateUtil.dayTimeInMillis(0, startTime), DateUtil.dayTimeInMillis(1, endTime) - 1);
        }
        List<CompanyBillDay> otherDayBill = companyBillDayMapper.selectByExample(example);
        if (null != otherDayBill && otherDayBill.size() > 0)
            result.addAll(otherDayBill);
        ShowVOCompanyBillDay showVO = new ShowVOCompanyBillDay();
        showVO.setTotalCount(result.size());
        List<CompanyBillDay> page = result.stream()
                .sorted(Comparator.comparing(CompanyBillDay::getCreateTime).reversed())// 排序
                .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                .collect(Collectors.toList());
        showVO.setList(page);
        return showVO;
    }

    /**
     * 获取账单详情
     *
     * @param query
     * @return
     */
    @Override
    public ShowVOCompanyBillDetail listBillByDayDatail(CompanyBillDayQuery query) {
        ShowVOCompanyBillDetail showVO = new ShowVOCompanyBillDetail();
        List<CompanyBillDayDatailAndBillingName> result = new ArrayList<>();
        List<CompanyBillDayDatail> datailList = new ArrayList<>();
        try {
            String appKey = query.getAppkey();
            String dateType = query.getDateType();
            //根据日/月/年账单分类确定详情查询时间范围
            Long startTime = getTimeByDateType(dateType, query.getStartTime(), 0);
            Long endTime = getTimeByDateType(dateType, query.getStartTime(), 1) - 1;
            query.setStartTime(startTime);
            query.setEndTime(endTime);
            if (isCurrentDayOrMonthOrYear(dateType, startTime)) {//查询当天
                datailList = getTodayDatail(DateUtil.dayTimeInMillis(0, null), query.getEndTime(), appKey);
                List<CompanyBillDayDatailAndBillingName> datailAndBillingNameslList = new ArrayList<>();//获取当日账单详情,含有规则名称
                if (null != datailList && datailList.size() > 0) {
                    datailList.forEach(datail -> {
                        CompanyBillDayDatailAndBillingName dayDatailAndBillingName = new CompanyBillDayDatailAndBillingName();
                        BeanCustomUtils.copyPropertiesIgnoreNull(datail, dayDatailAndBillingName);
                        dayDatailAndBillingName.setStrategyName(billingRulesService.selectByBillingRules(datail.getBillingRulesUuid()).getName());
                        datailAndBillingNameslList.add(dayDatailAndBillingName);
                    });
                    result.addAll(datailAndBillingNameslList);
                }
            }
            if (!(("day".equals(dateType)) && (startTime == DateUtil.dayTimeInMillis(0, null)))) {//不是当日的，都要查数据库
                /*Example example = new Example(CompanyBillDayDatail.class);
                Example.Criteria criteria = example.createCriteria();
                if (StringUtils.isNotBlank(appKey))
                    criteria.andLike("app_key",appKey);
                if (null != startTime && null != endTime)
                    criteria.andBetween("create_time",startTime, endTime);*/
                List<CompanyBillDayDatailAndBillingName> otherDayDetail = companyBillDayDatailMapper.selectBillDayDatailAndBillingNameBySome(query);
                if (null != otherDayDetail && otherDayDetail.size() > 0)
                    result.addAll(otherDayDetail);
            }
            //详情按照apiId分组统计
            List<CompanyBillDayDatailAndBillingName> resultList = new ArrayList<>();
            Map<Integer, List<CompanyBillDayDatailAndBillingName>> map = result.stream().collect(
                    Collectors.groupingBy(CompanyBillDayDatailAndBillingName::getApiId));
            for (Map.Entry<Integer, List<CompanyBillDayDatailAndBillingName>> entry : map.entrySet()) {
                CompanyBillDayDatailAndBillingName companyBillDayDatailAndBillingName = entry.getValue().get(0);
                long called = entry.getValue().stream().mapToLong(CompanyBillDayDatail::getCount).sum();
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDayDatail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                companyBillDayDatailAndBillingName.setCount(called);
                companyBillDayDatailAndBillingName.setAmount(amount);
//                //余额、余量获取(TODO)
//                redisService.get(entry.getKey() + ":balance");
////                redisService.get()
                resultList.add(companyBillDayDatailAndBillingName);
            }
            showVO.setTotalCount(result.size());
            List<CompanyBillDayDatail> page = resultList.stream()
                    .sorted(Comparator.comparing(CompanyBillDayDatail::getCreateTime).reversed())// 排序
                    .skip((query.getPageNum() - 1) * query.getPageSize()).limit(query.getPageSize())// 分页
                    .collect(Collectors.toList());
            showVO.setList(page);
        } catch (Exception e) {
            LOGGER.error("详情查询出错,{}", e.getMessage());
        }
        return showVO;
    }


    /**
     * 获取账单详情,含有分组名称，用于计算利润
     *
     * @param query
     * @return
     */
    @Override
    public Map<String, BigDecimal> listBillDatail(ProfitQuery query) {
        List<CompanyBillDayDatailVo> result = new ArrayList<>();
        Map<String, BigDecimal> resultMap = new TreeMap<>();
        List<CompanyBillDayDatail> datailList = new ArrayList<>();
        String sourceName = query.getSourceName();
        try {
            Long startTime = query.getStartTime();
            Long endTime = query.getEndTime();
            if (null != endTime && endTime > System.currentTimeMillis())
                endTime = System.currentTimeMillis();
            long todayMills = DateUtil.dayTimeInMillis(0, null);
            if (endTime > todayMills) {//ES查询当日数据
                datailList = getTodayDatail(todayMills, endTime, null);
            }
            //查询历史数据
            Example example = new Example(CompanyBillDayDatail.class);
            Example.Criteria criteria = example.createCriteria();
            if (null != startTime && null != endTime) {
                criteria.andBetween("createTime", startTime, endTime);
            }
            List<CompanyBillDayDatail> otherDayDetail = companyBillDayDatailMapper.selectByExample(example);//(TODO，加入分组判断
            if (null != otherDayDetail && otherDayDetail.size() > 0) {
                datailList.addAll(otherDayDetail);
            }
            if (null != datailList && datailList.size() > 0) {
                datailList.forEach(datail -> {
                    CompanyBillDayDatailVo companyBillDayDatailVo = new CompanyBillDayDatailVo();
                    BeanCustomUtils.copyPropertiesIgnoreNull(datail, companyBillDayDatailVo);
                    String groupName = webBackendService.getGroupNameByApiId(new JSONObject().fluentPut("apiId", datail.getApiId()));
                    companyBillDayDatailVo.setGroupName(groupName);//获取分组名称
                    if (StringUtils.isNotBlank(sourceName)) {//筛选
                        if (sourceName.equals(groupName))
                            result.add(companyBillDayDatailVo);
                    } else {
                        result.add(companyBillDayDatailVo);
                    }
                });
            }
            //详情按照分组统计
            Map<String, List<CompanyBillDayDatailVo>> map = result.stream().collect(
                    Collectors.groupingBy(CompanyBillDayDatailVo::getGroupName));
            for (Map.Entry<String, List<CompanyBillDayDatailVo>> entry : map.entrySet()) {
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDayDatail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                resultMap.put(entry.getKey(), amount);
            }
        } catch (Exception e) {
            LOGGER.error("详情查询出错,{}", e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map<Integer, BigDecimal> listBillApiDatail(ProfitQuery query) {
        Map<Integer, BigDecimal> resultMap = new HashMap<>();
        List<CompanyBillDayDatail> resultList = new ArrayList<>();
        String sourceName = query.getSourceName();
        try {
            Long startTime = query.getStartTime();
            Long endTime = query.getEndTime();
            if (null != endTime && endTime > System.currentTimeMillis())
                endTime = System.currentTimeMillis();
            long todayMills = DateUtil.dayTimeInMillis(0, null);
            List<CompanyBillDayDatailVo> result = new ArrayList<>();//获取当日账单详情,含有规则名称
            if (endTime > todayMills) {//ES查询当日数据
                resultList = getTodayDatail(todayMills, endTime, null);
            }
            //查询历史数据
            Example example = new Example(CompanyBillDayDatail.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<CompanyBillDayDatail> otherDayDetail = companyBillDayDatailMapper.selectByExample(example);//(TODO，加入接口数据源分组判断
            if (null != otherDayDetail && otherDayDetail.size() > 0) {
                resultList.addAll(otherDayDetail);
            }
            if (null != resultList && resultList.size() > 0) {
                resultList.forEach(datail -> {
                    CompanyBillDayDatailVo companyBillDayDatailVo = new CompanyBillDayDatailVo();
                    BeanCustomUtils.copyPropertiesIgnoreNull(datail, companyBillDayDatailVo);
                    String groupName = webBackendService.getGroupNameByApiId(new JSONObject().fluentPut("apiId", datail.getApiId()));
                    companyBillDayDatailVo.setGroupName(groupName);//获取接口分组名称
                    if (StringUtils.isNotBlank(sourceName)) {//筛选
                        if (sourceName.equals(groupName))
                            result.add(companyBillDayDatailVo);
                    } else {
                        result.add(companyBillDayDatailVo);
                    }
                });
                //详情按照接口id分组统计
                Map<Integer, List<CompanyBillDayDatail>> mapResult = result.stream().collect(
                        Collectors.groupingBy(CompanyBillDayDatail::getApiId));
                for (Map.Entry<Integer, List<CompanyBillDayDatail>> entry : mapResult.entrySet()) {
                    BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDayDatail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    resultMap.put(entry.getKey(), amount);
                }
            }
        } catch (Exception e) {
            LOGGER.error("详情查询出错,{}", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 判断是否当日、当月、当年
     *
     * @param dateType
     * @param time
     * @return
     * @throws ParseException
     */
    private boolean isCurrentDayOrMonthOrYear(String dateType, long time) throws ParseException {
        if (("day".equals(dateType)) && (time == DateUtil.dayTimeInMillis(0, null)))
            return true;
        if (("month".equals(dateType)) && (time == DateUtil.monthTimeInMillis(0, null)))
            return true;
        if (("year".equals(dateType)) && (time == DateUtil.yearTimeInMillis(0, null)))
            return true;
        return false;
    }

    /**
     * 根据传入日期类型（day/month/year），生成time对应的时间戳,num表示上下年/月/日
     *
     * @param dateType 日期类型
     * @param time
     * @param num
     * @return
     */
    private Long getTimeByDateType(String dateType, Long time, int num) {
        Long result = null;
        try {
            if ("day".equals(dateType)) {
                result = DateUtil.dayTimeInMillis(num, time);//num=0时，获取time所在日起始时间戳
            } else if ("month".equals(dateType)) {
                result = DateUtil.monthTimeInMillis(num, time);//num=1时，获取time所在月份下个月的起始时间戳
            } else {
                result = DateUtil.yearTimeInMillis(num, time);//num=-1时，获取time所在年份上一年的起始时间戳
            }
        } catch (Exception e) {
            LOGGER.error("根据日期类型获取时间戳出错,{}", e.getMessage());
        }
        return result;
    }

    /**
     * 持久化去年账单-定时任务调用
     */
    @Override
    public void saveCustomerChargeByYear(Long startTime, Long endTime) {
        try {
            if (null == startTime && null == endTime) {
                startTime = DateUtil.yearTimeInMillis(-1, null);//去年的起始时间
                endTime = DateUtil.yearTimeInMillis(0, null) - 1;//去年的截止时间
            }
            Example example = new Example(CompanyBillMonth.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<CompanyBillMonth> companyBillMonthList = companyBillMonthMapper.selectByExample(example);
            Map<String, List<CompanyBillMonth>> map = companyBillMonthList.stream().collect(
                    Collectors.groupingBy(CompanyBillMonth::getAppKey));
            for (Map.Entry<String, List<CompanyBillMonth>> entry : map.entrySet()) {
                CompanyBillYear companyBillYear = new CompanyBillYear();
                String appKey = entry.getKey();
                companyBillYear.setAppKey(appKey);
                long called = 0L;
                for (CompanyBillMonth companyBillMonth : entry.getValue()) {
                    long companyBillMonthCalled = companyBillMonth.getCalled();
                    called += companyBillMonthCalled;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillMonth::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                companyBillYear.setCalled(called);
                companyBillYear.setAmount(amount);
                companyBillYear.setCreateTime(startTime + 10);
                //获取去年余额、余量
                ReqSomeByPage reqSomeByPage = new ReqSomeByPage();
                reqSomeByPage.setAppKey(appKey);
                reqSomeByPage.setStartTime(startTime);
                reqSomeByPage.setEndTime(endTime);
                List<RespCompanyBalanceHistory> list = companyBalanceHistoryMapper.listBySome(reqSomeByPage);
                if (list != null && list.size() > 0) {
                    long stock = list.stream().mapToLong(RespCompanyBalanceHistory::getStock).count();
                    BigDecimal balance = list.stream().filter(a -> null != a.getBalance()).map(RespCompanyBalanceHistory::getBalance).reduce(BigDecimal::add).get();
                    companyBillYear.setStock(stock);
                    companyBillYear.setBalance(balance);
                }
                companyBillYearMapper.insertSelective(companyBillYear);
            }
        } catch (ParseException e) {
            LOGGER.error("持久化去年账单出错,{}", e.getMessage());
        }
    }

    /*
    持久化上月账单（显示每天客户余额余量）
     */
    @Override
    public void saveCustomerChargeByMonth(Long startTime, Long endTime) {
        String todayDate = DateUtil.getTodayDate();
        try {
            if (null == startTime && null == endTime) {
                startTime = DateUtil.monthTimeInMillis(-1, null);////获取上月起始时间戳，定时任务在月初第一天
                endTime = DateUtil.dateToStampLastMill(todayDate);//获取指定日期前一天的截止时间戳，定时任务在月初第一天，因此为上月月末截止时间
            }
            //查询上月日账单数据list
            Example example = new Example(CompanyBillDay.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<CompanyBillDay> companyBillDayList = companyBillDayMapper.selectByExample(example);
            //查询当日账单详情，生成日账单
            //根据客户分组
            Map<String, List<CompanyBillDay>> map = companyBillDayList.stream().collect(
                    Collectors.groupingBy(CompanyBillDay::getAppKey));
            for (Map.Entry<String, List<CompanyBillDay>> entry : map.entrySet()) {
                CompanyBillMonth companyBillMonth = new CompanyBillMonth();
                String appKey = entry.getKey();
                companyBillMonth.setAppKey(appKey);
                long called = 0L;
                for (CompanyBillDay companyBillDay : entry.getValue()) {
                    long companyBillDayCalled = companyBillDay.getCalled();
                    called += companyBillDayCalled;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDay::getAmount).reduce(BigDecimal::add).get();
                companyBillMonth.setCalled(called);
                companyBillMonth.setAmount(amount);
                companyBillMonth.setCreateTime(startTime + 10);
                //获取上个月余额、余量
                ReqSomeByPage reqSomeByPage = new ReqSomeByPage();
                reqSomeByPage.setAppKey(appKey);
                reqSomeByPage.setStartTime(startTime);
                reqSomeByPage.setEndTime(endTime);
                List<RespCompanyBalanceHistory> list = companyBalanceHistoryMapper.listBySome(reqSomeByPage);
                if (list != null && list.size() > 0) {
                    long stock = list.stream().mapToLong(RespCompanyBalanceHistory::getStock).count();
                    BigDecimal balance = list.stream().filter(a -> null != a.getBalance()).map(RespCompanyBalanceHistory::getBalance).reduce(BigDecimal::add).get();
                    companyBillMonth.setStock(stock);
                    companyBillMonth.setBalance(balance);
                }
                companyBillMonthMapper.insertSelective(companyBillMonth);
            }
        } catch (Exception e) {
            LOGGER.error("持久化下游月账单出错，{}", e);
        }
    }

    /**
     * 定时任务调用入口-持久化日账单(需先持久化详情，才能持久化日账单)
     */
    @Override
    public void saveCustomerChargeByDay(Long startTime, Long endTime) {
        try {
            if (null == startTime && null == endTime) {
                startTime = Long.parseLong(DateUtil.getBeforeTimestamp(-1));//昨天起始时间戳
                endTime = Long.parseLong(DateUtil.getBeforeTimestamp(0)) - 1;//昨天截止时间戳
            }
            saveCustomerChargeByDetail(startTime, endTime);//持久化账单详情
            //并且获取详情list
            Example example = new Example(CompanyBillDayDatail.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startTime, endTime);
            List<CompanyBillDayDatail> companyBillDayDatailList = companyBillDayDatailMapper.selectByExample(example);
            //查询当日账单详情，生成日账单
            //根据客户分组
            Map<String, List<CompanyBillDayDatail>> map = companyBillDayDatailList.stream().collect(
                    Collectors.groupingBy(CompanyBillDayDatail::getAppKey));
            for (Map.Entry<String, List<CompanyBillDayDatail>> entry : map.entrySet()) {
                CompanyBillDay companyBillDay = new CompanyBillDay();
                String appKey = entry.getKey();
                companyBillDay.setAppKey(appKey);
                long called = 0L;
                for (CompanyBillDayDatail companyBillDayDatail : entry.getValue()) {
                    long count = companyBillDayDatail.getCount();
                    called += count;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDayDatail::getAmount).reduce(BigDecimal::add).get();
                companyBillDay.setCalled(called);
                companyBillDay.setAmount(amount);
                companyBillDay.setCreateTime(startTime + 10);
                //获取昨日余额、余量获取
                ReqSomeByPage reqSomeByPage = new ReqSomeByPage();
                reqSomeByPage.setAppKey(appKey);
                reqSomeByPage.setStartTime(startTime);
                reqSomeByPage.setEndTime(endTime);
                List<RespCompanyBalanceHistory> list = companyBalanceHistoryMapper.listBySome(reqSomeByPage);
                if (list != null && list.size() > 0) {
                    RespCompanyBalanceHistory respCompanyBalanceHistory = list.get(0);
                    companyBillDay.setStock(respCompanyBalanceHistory.getStock());
                    companyBillDay.setBalance(respCompanyBalanceHistory.getBalance());
                }
                companyBillDayMapper.insertSelective(companyBillDay);
            }
        } catch (Exception e) {
            LOGGER.error("持久化下游日账单出错，{}", e.getMessage());
        }
    }

    /**
     * 定时任务调用-持久化详情
     */
    public void saveCustomerChargeByDetail(long startTime, long endTime) {
        List<CompanyBillDayDatail> list = new ArrayList<>();
        try {
            //获取当日计费基础数据
            List<CustomerChargeCount> customerChargeCountList = customerBillClien.customerCount(index, type, startTime, endTime, null);
            //持久化到数据库
            customerChargeCountList.forEach(customerChargeCount -> {
                List<ApiChargeCount> apiChargeCountsList = customerChargeCount.getCustomerApiChargelistCount();
                apiChargeCountsList.forEach(customerApiChargeCount -> {
                    if (customerApiChargeCount.getChargeUuid() == null) {
                        return;
                    }
                    CompanyBillDayDatail companyBillDayDatail = new CompanyBillDayDatail();
                    companyBillDayDatail.setApiId(customerApiChargeCount.getApiId());
                    companyBillDayDatail.setBillingRulesUuid(customerApiChargeCount.getChargeUuid());
                    companyBillDayDatail.setCount(customerApiChargeCount.getCount());
                    companyBillDayDatail.setPrice(customerApiChargeCount.getPrice());
                    // 计费规则为空,不计算
                    if (StringUtils.isNotEmpty(customerApiChargeCount.getChargeUuid())) {
                        companyBillDayDatail.setAmount(calculateAmount(customerApiChargeCount, customerChargeCount.getAppKey()));
                    } else {
                        LOGGER.error("客户:[{}]的接口[{}],计费异常,没有计费规则", companyBillDayDatail.getAppKey(), companyBillDayDatail.getApiId());
                    }
                    companyBillDayDatail.setAppKey(customerChargeCount.getAppKey());
                    companyBillDayDatail.setCreateTime(startTime + 10);
                    companyBillDayDatailMapper.insertSelective(companyBillDayDatail);
                    list.add(companyBillDayDatail);
                });
            });
        } catch (Exception e) {
            LOGGER.error("持久化下游账单详情出错，{}", e);
        }
    }

    /*
    根据计费规则，计算消费额
     */
    private BigDecimal calculateAmount(ApiChargeCount apiChargeCount, String appKey) {
        String chargeUuid = apiChargeCount.getChargeUuid();
        BigDecimal price = apiChargeCount.getPrice();
        Long count = apiChargeCount.getCount();
        Integer apiId = apiChargeCount.getApiId();
        BigDecimal amount;
        BillingRules billingRules = billingRulesService.selectByBillingRules(chargeUuid);//查询计费规则
        if (null != billingRules.getBillingType()) {
            // 按条计费,计算消费额
            if (2 == billingRules.getBillingType()) {
                amount = price.multiply(BigDecimal.valueOf(count));
            } else {//包时计费，按天计算
                //时间计费的在周期内消费不计算消费额，因为在配置合约时已经一次性扣费
                //查询合约时间，确定计费周期内的天数
                CompanyAppsAuth companyAppsAuth = new CompanyAppsAuth();
                JSONObject jsonObject = RedisUtil.get("auth:" + appKey + ":" + apiId);
                if (jsonObject != null) {
                    companyAppsAuth = jsonObject.toJavaObject(CompanyAppsAuth.class);
                } else {
                    List<CompanyAppsAuth> authList = companyAppsAuthMapper.findAppKeyAndApiId(appKey, apiId, null);
                    if (authList != null && authList.size() > 0)
                        companyAppsAuth = authList.get(0);
                }
                //根据合约起止时间计算天数
                int days = DateUtil.getDayBetween(companyAppsAuth.getStartTime(), companyAppsAuth.getEndTime());
                amount = price.divide(BigDecimal.valueOf(days), 2, BigDecimal.ROUND_HALF_DOWN);//一天费用
            }
            return amount;
        }
        return null;// 为null是为了区别调用量为0的按条计费，消费额为0的情况
    }

    /**
     * 查询时间段内的ES全量数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<QueryInfo> getQueryInfoListByTime(long startTime, long endTime) {
        //获取当日计费基础数据
        List<QueryInfo> customerChargeCountList = customerBillClien.getQueryInfoList(index, type, startTime, endTime);
        //持久化到数据库
        return customerChargeCountList;
    }

    /**
     * 实时获取今年账单
     *
     * @param startYearTime
     * @param currentTime
     * @return
     */
    public List<CompanyBillYear> getThisYearBill(long startYearTime, long currentTime, String appKey) {
        List<CompanyBillYear> result = new ArrayList<>();
        try {
            //今年的账单一定是含当月账单
            List<CompanyBillMonth> monthBillList = new ArrayList<>();
            long thisMonthBeginMills = DateUtil.monthTimeInMillis(0, null);
            //当月账单（实时）
            List<CompanyBillMonth> thisMonthBillList = getThisMonthBill(thisMonthBeginMills, currentTime, appKey);
            if (thisMonthBillList.size() > 0)
                monthBillList.addAll(thisMonthBillList);
            //历史月份账单
            Example example = new Example(CompanyBillMonth.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andBetween("createTime", startYearTime, currentTime);
            if (StringUtils.isNotBlank(appKey)) {
                criteria.andLike("appKey", "%" + appKey + "%");
            }
            List<CompanyBillMonth> otherMonthBillList = companyBillMonthMapper.selectByExample(example);
            if (otherMonthBillList.size() > 0)
                monthBillList.addAll(otherMonthBillList);
            Map<String, List<CompanyBillMonth>> map = monthBillList.stream().collect(
                    Collectors.groupingBy(CompanyBillMonth::getAppKey));
            for (Map.Entry<String, List<CompanyBillMonth>> entry : map.entrySet()) {
                CompanyBillYear companyBillYear = new CompanyBillYear();
                String appKeyTemp = entry.getKey();
                companyBillYear.setAppKey(appKeyTemp);
                long called = 0L;
                for (CompanyBillMonth companyBillMonth : entry.getValue()) {
                    long companyBillMonthCalled = companyBillMonth.getCalled();
                    called += companyBillMonthCalled;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillMonth::getAmount).reduce(BigDecimal::add).get();
                companyBillYear.setCalled(called);
                companyBillYear.setAmount(amount);
                companyBillYear.setCreateTime(currentTime);
                //余额、余量获取(TODO)
                redisService.get(appKeyTemp + ":balance");
//                redisService.get()
                result.add(companyBillYear);
            }
        } catch (ParseException e) {
        }
        return result;
    }

    /**
     * 获取当月账单,必然包含当日实时账单
     *
     * @param thisMonthBeginMills
     * @param currentTime
     * @return
     */
    public List<CompanyBillMonth> getThisMonthBill(long thisMonthBeginMills, long currentTime, String appKey) {
        List<CompanyBillMonth> result = new ArrayList<>();
        List<CompanyBillDay> thisMonthBillList = new ArrayList<>();
        // 1.1-实时查询ES当日账单
        long todayMills = DateUtil.dayTimeInMillis(0, null);
        List<CompanyBillDay> todayBill = getTodayBill(todayMills, currentTime, appKey);
        if (null != todayBill && todayBill.size() > 0)
            thisMonthBillList.addAll(todayBill);
        //1.2-数据库查询当月（其他日）的日账单
        Example dayExample = new Example(CompanyBillDay.class);
        Example.Criteria dayCriteria = dayExample.createCriteria();
        if (StringUtils.isNotBlank(appKey)) {
            dayCriteria.andLike("appKey", "%" + appKey + "%");
        }
        dayCriteria.andBetween("createTime", thisMonthBeginMills, currentTime);//查询当月其他天数据库存储信息
        List<CompanyBillDay> otherDayBill = companyBillDayMapper.selectByExample(dayExample);
        if (null != otherDayBill && otherDayBill.size() > 0)
            thisMonthBillList.addAll(otherDayBill);
        if (null != thisMonthBillList && thisMonthBillList.size() > 0) {
            Map<String, List<CompanyBillDay>> map = thisMonthBillList.stream().collect(
                    Collectors.groupingBy(CompanyBillDay::getAppKey));
            for (Map.Entry<String, List<CompanyBillDay>> entry : map.entrySet()) {
                CompanyBillMonth companyBillMonth = new CompanyBillMonth();
                String appKeyTemp = entry.getKey();
                companyBillMonth.setAppKey(appKeyTemp);
                long called = 0L;
                for (CompanyBillDay companyBillDay : entry.getValue()) {
                    long companyBillDayCalled = companyBillDay.getCalled();
                    called += companyBillDayCalled;
                }
                BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDay::getAmount).reduce(BigDecimal::add).get();
                companyBillMonth.setCalled(called);
                companyBillMonth.setAmount(amount);
                companyBillMonth.setCreateTime(currentTime);
                //余额、余量获取(TODO)
                redisService.get(appKeyTemp + ":balance");
//                redisService.get()
                result.add(companyBillMonth);
            }
        }
        return result;
    }

    /**
     * 获取当日账单
     *
     * @param todayMills
     * @param currentTime
     * @return
     */
    public List<CompanyBillDay> getTodayBill(long todayMills, long currentTime, String appKey) {
        List<CompanyBillDay> billDayList = new ArrayList<>();
        List<CompanyBillDayDatail> companyBillDayDatailList = getTodayDatail(todayMills, currentTime, appKey);
        if (null == companyBillDayDatailList || companyBillDayDatailList.size() <= 0)
            return billDayList;
        //查询当日账单详情，生成日账单
        //根据客户分组
        Map<String, List<CompanyBillDayDatail>> map = companyBillDayDatailList.stream().collect(
                Collectors.groupingBy(CompanyBillDayDatail::getAppKey));
        for (Map.Entry<String, List<CompanyBillDayDatail>> entry : map.entrySet()) {
            CompanyBillDay companyBillDay = new CompanyBillDay();
            String appKeyTemp = entry.getKey();
            companyBillDay.setAppKey(appKeyTemp);
            long called = entry.getValue().stream().mapToLong(CompanyBillDayDatail::getCount).sum();
            BigDecimal amount = entry.getValue().stream().filter(a -> null != a.getAmount()).map(CompanyBillDayDatail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            companyBillDay.setCalled(called);
            companyBillDay.setAmount(amount);
            companyBillDay.setCreateTime(currentTime);
            companyBillDay.setStock(null);
            companyBillDay.setBalance(null);
            //余额、余量获取(TODO)
            redisService.get(appKeyTemp + ":balance");
//                redisService.get()
            billDayList.add(companyBillDay);
        }
        return billDayList;
    }

    /**
     * 获取当日账单详情
     *
     * @param todayMills
     * @param currentTime
     * @return
     */
    public List<CompanyBillDayDatail> getTodayDatail(long todayMills, long currentTime, String sourceName) {
        //查询截止时间大于当日起始时间，表示要查询实时数据（包含当日未持久化的数据）
        if (currentTime >= todayMills) {
            //查询当日账单详情
            List<CustomerChargeCount> customerChargeCountList = customerBillClien.customerCount(index, type, todayMills, currentTime, sourceName);
            List<CompanyBillDayDatail> datailList = new ArrayList<>();
            customerChargeCountList.forEach(customerChargeCount -> {
                List<ApiChargeCount> apiChargeCountsList = customerChargeCount.getCustomerApiChargelistCount();
                apiChargeCountsList.forEach(customerApiChargeCount -> {
                    CompanyBillDayDatail companyBillDayDatail = new CompanyBillDayDatail();
                    companyBillDayDatail.setApiId(customerApiChargeCount.getApiId());
                    companyBillDayDatail.setBillingRulesUuid(customerApiChargeCount.getChargeUuid());
                    companyBillDayDatail.setCount(customerApiChargeCount.getCount());
                    companyBillDayDatail.setPrice(customerApiChargeCount.getPrice());
                    companyBillDayDatail.setAmount(calculateAmount(customerApiChargeCount, customerChargeCount.getAppKey()));
                    companyBillDayDatail.setAppKey(customerChargeCount.getAppKey());
                    companyBillDayDatail.setCreateTime(currentTime);
                    datailList.add(companyBillDayDatail);
                });
            });
            return datailList;
        }
        return null;
    }

    /**
     * 获取当日账单详情
     *
     * @param todayMills
     * @param currentTime
     * @return
     */
    public List<CompanyBillDayDatailVo> getTodayDatailVo(long todayMills, long currentTime, String sourceName) {
        //查询截止时间大于当日起始时间，表示要查询实时数据（包含当日未持久化的数据）
        if (currentTime > todayMills) {
            //查询当日账单详情
            List<CustomerChargeCount> customerChargeCountList = customerBillClien.customerCount(index, type, todayMills, currentTime, sourceName);
            List<CompanyBillDayDatailVo> datailList = new ArrayList<>();
            customerChargeCountList.forEach(customerChargeCount -> {
                List<ApiChargeCount> apiChargeCountsList = customerChargeCount.getCustomerApiChargelistCount();
                apiChargeCountsList.forEach(customerApiChargeCount -> {
                    CompanyBillDayDatailVo companyBillDayDatailVo = new CompanyBillDayDatailVo();
                    Integer apiId = customerApiChargeCount.getApiId();
                    companyBillDayDatailVo.setApiId(customerApiChargeCount.getApiId());
                    companyBillDayDatailVo.setBillingRulesUuid(customerApiChargeCount.getChargeUuid());
                    companyBillDayDatailVo.setCount(customerApiChargeCount.getCount());
                    companyBillDayDatailVo.setPrice(customerApiChargeCount.getPrice());
                    companyBillDayDatailVo.setAmount(calculateAmount(customerApiChargeCount, customerChargeCount.getAppKey()));
                    companyBillDayDatailVo.setAppKey(customerChargeCount.getAppKey());
                    companyBillDayDatailVo.setCreateTime(currentTime);
                    String groupName = webBackendService.getGroupNameByApiId(new JSONObject().fluentPut("apiId", apiId));
                    companyBillDayDatailVo.setGroupName(groupName);
                    datailList.add(companyBillDayDatailVo);
                });
            });
            return datailList;
        }
        return null;
    }

    /**
     * 根据appKey，起止时间查询账单详情list
     *
     * @param appKey
     * @param startTime
     * @param endTime
     * @return
     */
    public List<CompanyBillDayDatailAndBillingName> searchBillDatailByAppKeyAndTime(String appKey, long startTime, long endTime) {
        List<CompanyBillDayDatailAndBillingName> result = new ArrayList<>();
        List<CompanyBillDayDatailAndBillingName> orderList = new ArrayList<>();
        List<CompanyBillDayDatail> datailList = new ArrayList<>();
        try {
            //查询范围包含当天，查询ES当日账单明细
            if (endTime > DateUtil.dayTimeInMillis(0, null)) {
                datailList = getTodayDatail(DateUtil.dayTimeInMillis(0, null), System.currentTimeMillis(), appKey);
                List<CompanyBillDayDatailAndBillingName> datailAndBillingNameslList = new ArrayList<>();//获取当日账单详情,含有规则名称
                if (null != datailList && datailList.size() > 0) {
                    CompanyBillDayDatailAndBillingName dayDatailAndBillingName = new CompanyBillDayDatailAndBillingName();
                    datailList.forEach(datail -> {
                        BeanCustomUtils.copyPropertiesIgnoreNull(datail, dayDatailAndBillingName);
                        dayDatailAndBillingName.setStrategyName(billingRulesService.selectByBillingRules(datail.getBillingRulesUuid()).getName());
                        datailAndBillingNameslList.add(dayDatailAndBillingName);
                    });
                    result.addAll(datailAndBillingNameslList);
                }
            }
            //查询数据库账单详情表
            CompanyBillDayQuery query = new CompanyBillDayQuery();
            if (StringUtils.isNotBlank(appKey)) {
                query.setAppkey(appKey);
            }
            query.setStartTime(startTime);
            query.setEndTime(endTime);
            List<CompanyBillDayDatailAndBillingName> otherDayDetail = companyBillDayDatailMapper.selectBillDayDatailAndBillingNameBySome(query);
            if (null != otherDayDetail && otherDayDetail.size() > 0)
                result.addAll(otherDayDetail);
            //排序(按照日期排序)
            orderList = result.stream()
                    .sorted(Comparator.comparing(CompanyBillDayDatailAndBillingName::getCreateTime).reversed()).collect(Collectors.toList());
            ;
        } catch (Exception e) {
            LOGGER.error("导出账单详情查询出错,{}", e.getMessage());
        }
        return orderList;
    }

    /**
     * 下载账单详情
     *
     * @param appKey    客户名称
     * @param startTime 起始时间
     * @param endTime   截止时间
     * @return
     */
    @Override
    public void downloadBill(String appKey, Long startTime, Long endTime, String filePath, String fileName) throws IOException {
        if (StringUtils.isNotBlank(appKey) && null == startTime || null == endTime)
            throw new BusinessException(ExceptionInfo.NOT_NULL_PARAMS);
        //根据appKey，起止时间查询账单详情
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        List<CompanyBillDayDatailAndBillingName> result = searchBillDatailByAppKeyAndTime(appKey, startTime, endTime);
        if (null != result && result.size() > 0) {//统计
            long called = result.stream().mapToLong(CompanyBillDayDatail::getCount).sum();
            BigDecimal amount = result.stream().filter(a -> null != a.getAmount()).map(CompanyBillDayDatail::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            CompanyBillDayDatailAndBillingName sumObject = new CompanyBillDayDatailAndBillingName();
            sumObject.setAppKey("总计");
            sumObject.setCount(called);
            sumObject.setAmount(amount);
            sumObject.setCreateTime(System.currentTimeMillis());
            result.add(sumObject);
            result.forEach(object -> {
                LinkedHashMap<String, Object> mapResult = new LinkedHashMap<>();
                mapResult.put("1", object.getAppKey() == null ? "" : object.getAppKey());
                mapResult.put("2", object.getApiId() == null ? "" : object.getApiId());
                mapResult.put("3", object.getStrategyName() == null ? "" : object.getStrategyName());
                mapResult.put("4", object.getPrice() == null ? "" : object.getPrice());
                mapResult.put("5", object.getCount() == null ? 0 : object.getCount());
                mapResult.put("6", object.getAmount() == null ? 0 : object.getAmount());
                mapResult.put("7", DateUtil.stampToDateNoTime(object.getCreateTime()));
                list.add(mapResult);
            });
        }
        LinkedHashMap<String, Object> header = new LinkedHashMap<>();//设置列名
        header.put("1", "客户名称");
        header.put("2", "接口id");
        header.put("3", "计费规则");
        header.put("4", "单价");
        header.put("5", "调用量");
        header.put("6", "消费额");
        header.put("7", "调用时间");
        CsvUtil.createCSVFile(list, header, filePath, fileName);
    }
}
