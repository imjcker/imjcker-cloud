package com.imjcker.manager.manage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.helper.ExceptionHelper;
import com.imjcker.manager.manage.helper.JsonUtil;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.ApiGroupService;
import com.imjcker.manager.manage.service.ApiService;
import com.imjcker.manager.manage.service.DebugService;
import com.imjcker.manager.manage.service.ExcelService;
import com.imjcker.manager.manage.vo.*;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.util.DateUtil;
import com.lemon.common.util.DownloadUtils;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.config.DocumentConfigurationProperties;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.ApiInfoLatestQuery;
import com.imjcker.manager.manage.po.query.AppQuery;
import com.imjcker.manager.manage.po.query.AutoTestQuery;
import com.imjcker.manager.manage.service.*;
import com.imjcker.manager.manage.service.impl.AppOldService;
import com.imjcker.manager.manage.validator.ApiValidate;
import com.imjcker.manager.manage.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月17日 上午10:33:59
 * @Title ApiController
 * @Description API控制器
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
@Slf4j
@Api(description = "开放API-API列表")
@RequestMapping("/api")
@RestController
public class ApiController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    public static final String FILE_PATH = System.getProperty("user.dir");//文件指定存放的路径;//文件指定存放的路径
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    ApiService apiService;
    @Resource
    private AppOldService appOldService;
    @Autowired
    private DebugService debugService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private DocumentConfigurationProperties docProperties;

    @Autowired
    private ApiGroupService apiGroupService;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 更新api
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "更新api", notes = "更新api")
    @PostMapping("update")
    public CommonResult update(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        ApiValidate.apiInfoTypeCheck(jsonObject, "apiInfo");
        ApiInfoWithBLOBs apiInfo = jsonObject.getObject("apiInfo", ApiInfoWithBLOBs.class);
        ApiValidate.apiCheck(apiInfo);
        JSONArray jsonArray1 = jsonObject.getJSONArray("requestParamsList");
        JSONArray jsonArray2 = jsonObject.getJSONArray("backendList");
        List<RequestParams> requestParamsList = null;
        List<BackendRequestParams> backendList = null;

        if (null != jsonArray1 && !jsonArray1.isEmpty()) {

            ApiValidate.requestParamTypeCheck(jsonArray1);
            requestParamsList = jsonArray1.toJavaList(RequestParams.class);
            ApiValidate.requestParamsCheck(requestParamsList);
            //Api内参数名称不重复校验
            List<ParamNames> paramNamesList = new ArrayList<>();
            BeanCustomUtils.copyPropertiesIgnoreNull(requestParamsList, paramNamesList);
            apiService.paramNameCheck(paramNamesList);
        }
        if (null != jsonArray2 && !jsonArray2.isEmpty()) {
            backendList = jsonArray2.toJavaList(BackendRequestParams.class);
            //参数校验
            ApiValidate.backendRequestParamsCheck(backendList);
            //Api内参数名称不重复校验
            List<ParamNames> backendparamNamesList = new ArrayList<>();
            BeanCustomUtils.copyPropertiesIgnoreNull(backendList, backendparamNamesList);
            apiService.paramNameCheck(backendparamNamesList);
        }

        apiService.update(apiInfo, requestParamsList, backendList);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    @ApiOperation(value = "更新指定API参数", notes = "更新指定API参数")
    @PostMapping("updateApiParams")
    public CommonResult updateApiParams(@RequestBody JSONObject jsonObject) {
        LOG.info(jsonObject.toJSONString());
        //参数类型检验
        ApiInfoWithBLOBs apiInfo = jsonObject.getObject("apiInfo", ApiInfoWithBLOBs.class);
        ApiValidate.apiIdCheck(apiInfo);
        JSONArray jsonArray1 = jsonObject.getJSONArray("requestParamsList");
        JSONArray jsonArray2 = jsonObject.getJSONArray("backendParamsList");
        List<RequestParams> requestParamsList = null;
        List<BackendDistributeParams> backendList = null;
        if (null != jsonArray1 && !jsonArray1.isEmpty()) {
            //参数类型检验
            ApiValidate.requestParamTypeCheck(jsonArray1);
            requestParamsList = jsonArray1.toJavaList(RequestParams.class);
            //参数校验
            ApiValidate.requestParamsCheck(requestParamsList);
            //Api内参数名称不重复校验
            List<ParamNames> paramNamesList = new ArrayList<>();
            BeanCustomUtils.copyPropertiesIgnoreNull(requestParamsList, paramNamesList);
            apiService.paramNameCheck(paramNamesList);
        }
        if (null != jsonArray2 && !jsonArray2.isEmpty()) {
            backendList = jsonArray2.toJavaList(BackendDistributeParams.class);
            //参数校验
            ApiValidate.backendDistributeParamsCheckList(backendList);
            //Api内参数名称不重复校验
            List<ParamNames> backendparamNamesList = new ArrayList<ParamNames>();
            BeanCustomUtils.copyPropertiesIgnoreNull(backendList, backendparamNamesList);
            apiService.paramNameCheck(backendparamNamesList);
        }
        apiService.updateApiParams(apiInfo, requestParamsList, backendList);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }


    /**
     * 新建api
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "新建API", notes = "新建API")
    @PostMapping("save")
    public CommonResult save(@RequestBody JSONObject jsonObject) {
        //参数类型检验
        ApiValidate.apiInfoTypeCheck(jsonObject, "apiInfo");
        ApiInfoWithBLOBs apiInfo = jsonObject.getObject("apiInfo", ApiInfoWithBLOBs.class);
        ApiValidate.apiCheck(apiInfo);
        JSONArray jsonArray1 = jsonObject.getJSONArray("requestParamsList");
        JSONArray jsonArray2 = jsonObject.getJSONArray("backEndRequestParamsList");
        List<RequestParams> requestParamsList = null;
        List<BackendRequestParams> backendList = null;
        if (null != jsonArray1 && !jsonArray1.isEmpty()) {
            //参数类型检验
            ApiValidate.requestParamTypeCheck(jsonArray1);
            requestParamsList = jsonArray1.toJavaList(RequestParams.class);
            //参数校验
            ApiValidate.requestParamsCheck(requestParamsList);
            //Api内参数名称不重复校验
            List<ParamNames> paramNamesList = new ArrayList<>();
            BeanCustomUtils.copyPropertiesIgnoreNull(requestParamsList, paramNamesList);
            apiService.paramNameCheck(paramNamesList);
        }
        if (null != jsonArray2 && !jsonArray2.isEmpty()) {
            backendList = jsonArray2.toJavaList(BackendRequestParams.class);
            //参数校验
            ApiValidate.backendRequestParamsCheck(backendList);
            //Api内参数名称不重复校验
            List<ParamNames> backendparamNamesList = new ArrayList<ParamNames>();
            BeanCustomUtils.copyPropertiesIgnoreNull(backendList, backendparamNamesList);
            apiService.paramNameCheck(backendparamNamesList);
        }
        //同步码值映射json和json导入字符串
        if (org.apache.commons.lang3.StringUtils.isNotBlank(apiInfo.getResponseTransParam())) {
            apiInfo.setResponseConfigJson(apiInfo.getResponseTransParam());
        }
        Integer id = apiService.save(apiInfo, requestParamsList, backendList);

        return new CommonResult(ResultStatusEnum.SUCCESS, id);
    }
//接口2.0------------------------------------------下面-----------------------------------------------------------------

    /**
     * 同步接口发布信息到redis中(第一次使用接口参数缓存调用，后续只需正常发布即可)
     */
    @ApiOperation(value = "同步redis", notes = "同步redis")
    @GetMapping("synchroLatest2Redis")
    public String synchroLatest2Redis(String type, String pattern) {
        String result = null;
        LOG.info("synchroLatest2Redis,type={}", type);
        switch (type) {
            case "add":
                result = apiService.synchroLatest2Redis().toString();
                break;
            case "count":
                result = apiService.countRedis(pattern);
                break;
            case "delete":
                apiService.deleteRedis(pattern);
                result = "success";
                break;
            case "query":
                result = apiService.queryRedis(pattern);
                break;
            default:
                break;
        }
        LOG.info("{},{}{}", type, pattern, result);
        return result;
    }

    /**
     * 发布版本
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "发布版本", notes = "发布版本")
    @PostMapping("publish")
    public CommonResult publish(@RequestBody JSONObject jsonObject) {
        ApiExpand apiExpand = jsonObject.toJavaObject(ApiExpand.class);
        //参数校验
        ApiValidate.apiExpandCheck(apiExpand);
        Integer id = apiService.publish(apiExpand);
        //发布接口维护debugInfo的api信息
//        JSONObject apiIdObject = new JSONObject();
//        apiIdObject.put("apiId", id);
//        debugInfo(apiIdObject);
        return CommonResult.success(id);
    }

    /**
     * 批量发布版本--脚本使用
     *
     * @param
     * @return
     */
    @ApiOperation(value = "批量发布版本", notes = "批量发布版本")
    @GetMapping("publishAll")
    public String publishAll(@RequestParam(name = "apiIdList") String apiIdList, @RequestParam(name = "env") String env, @RequestParam(name = "pubDescription") String pubDescription) {
        //apiId有效性校验
        Long start = System.currentTimeMillis();
        DateUtil.stampToDates(start.toString());
        String validApiIdList = apiService.chooseValidApiID(apiIdList);
        if (StringUtils.isBlank(validApiIdList)) {
            LOG.info("获取有效的apiId失败");
            return "fail to get apiId ,please input valid apiId!";
        }
        String[] validApiIdArray = validApiIdList.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        ApiExpand apiExpand = new ApiExpand();
        for (String apiId : validApiIdArray) {
            apiExpand.setEnv(Integer.parseInt(env));
            apiExpand.setId(Integer.parseInt(apiId));
            apiExpand.setPubDescription(pubDescription);
            Integer publishApiId = apiService.publish(apiExpand);//返回发布接口的apiId
            //发布接口维护debugInfo的api信息
            JSONObject apiIdObject = new JSONObject();
            apiIdObject.put("apiId", publishApiId);
            debugInfo(apiIdObject);

            stringBuilder.append(publishApiId + " ");
        }
        stringBuilder.append(" publish success!");
        Long end = System.currentTimeMillis();
        DateUtil.stampToDates(end.toString());
        LOG.info("start time：{}, end time: {},cost time: {}ms", start, end, end - start);
        return stringBuilder.toString();
    }

    /**
     * 批量发布获取有效API列表 -- 对应页面（发布API-搜索，其中搜索格式：XX XX-AA 或者 XX XX XX）
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "批量发布获取有效API列表", notes = "批量发布获取有效API列表")
    @PostMapping("publishListForFindAPI")
    public CommonResult publishListForFindAPI(@RequestBody JSONObject jsonObject) {
        String apiIdListStr = jsonObject.getString("apiIdList");
        if (StringUtils.isBlank(apiIdListStr))
            return new CommonResult(ResultStatusEnum.PARAM_NULL, "");
        String re = "(\\d-\\d\\s?|\\d\\s?)+";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(apiIdListStr);
        if (!m.matches())
            return new CommonResult(ResultStatusEnum.PARAM_NULL, "");
        List<Integer> validApiIdList = new ArrayList<>();
        ApiIdListQuery apiIdListQuery = jsonObject.toJavaObject(ApiIdListQuery.class);
        String apiIdList = apiIdListQuery.getApiIdList();
        validApiIdList = apiService.chooseValidListApiID(apiIdList);
        PageInfo<ApiExpand> apiExpandPageInfo = apiService.apiInfoListByApiIdList(apiIdListQuery, validApiIdList);
        JSONObject result = new JSONObject();
        result.put("apiList", apiExpandPageInfo);
        result.put("apiIdList", validApiIdList);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * 批量发布接口--接口平台页面中使用
     *
     * @param
     * @return
     */
    @ApiOperation(value = "批量发布接口", notes = "批量发布接口")
    @PostMapping("publishByApiIdList")
    public CommonResult publishByApiIdList(@RequestBody JSONObject jsonObject) {
        String env = jsonObject.getString("env");
        String pubDescription = jsonObject.getString("pubDescription");
        List<Integer> removeIdList = (List<Integer>) jsonObject.get("removeIdList");
        List<Integer> apiIdList = (List<Integer>) jsonObject.get("apiIdList");
        if (null != removeIdList && removeIdList.size() > 0) {
            for (Integer apiId : removeIdList)
                apiIdList.remove(apiId);
        }
        StringBuilder stringBuilder = new StringBuilder();
        String result;
        ApiExpand apiExpand = new ApiExpand();
        if (null != apiIdList && apiIdList.size() > 0) {
            for (Integer apiId : apiIdList) {
                apiExpand.setEnv(Integer.parseInt(env));
                apiExpand.setId(apiId);
                apiExpand.setPubDescription(pubDescription);
                Integer publishApiId = apiService.publish(apiExpand);//返回发布接口的apiId
                stringBuilder.append(publishApiId + " ");
            }
            result = stringBuilder.toString();
            LOG.info("env= {},批量发布成功的apiId: {}", env, result);
        } else {
            result = "没有需要发布的接口";
            LOG.info("env= {},没有需要发布的接口", env);
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    @ApiOperation(value = "自动测试api", notes = "自动测试api")
    @PostMapping("/autotest")
    public CommonResult autoTest(@RequestParam(required = false) Integer groupId) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            List<ApiInfoVersionLatestResponse> apiInfoList = apiService.findApiInfoVersionsLatestForAutoTest(groupId);
            CompletableFuture[] futures = apiInfoList.stream().map(apiInfo ->
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            return apiService.autoTest(apiInfo.getApiId());
                        } catch (IOException e) {
                            log.info(e.getMessage());
                        }
                        return null;
                    }, pool).whenComplete((m, e) -> {
                        if (m != null) {
                            try {
                                apiService.insertTestResult(JsonUtil.prettyJson(m), apiInfo.getApiId(), apiInfo.getApiGroupId(), apiInfo.getApiName());
                            } catch (JsonProcessingException e1) {
                                log.info(e1.getMessage());
                            }
                        }
                    })).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
        } catch (Exception e) {
            LOG.error("自动测试失败: {}", e.getMessage());
        } finally {
            pool.shutdown();
        }

        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    @ApiOperation(value = "配置测试api列表", notes = "配置测试api列表")
    @PostMapping("/page/testResult")
    public CommonResult pageInAutoTestConfig(@RequestBody ApiInfoLatestQuery query) {

        PageInfo<ApiInfoVersionLatestResponse> apiList = apiService.selectApiInfoLatestExample(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiList);
    }

    @ApiOperation(value = "自动测试结果分页", notes = "自动测试结果分页列表")
    @PostMapping("/page/apiTestConfig")
    public CommonResult pageInAutoTestResult(@RequestBody AutoTestQuery query) {

        PageInfo<AutoTestResult> testResultPageInfo = apiService.autoTestList(query);
        return new CommonResult(ResultStatusEnum.SUCCESS, testResultPageInfo);
    }

    @ApiOperation(value = "上线测试配置", notes = "下线测试配置")
    @PostMapping("/autotest/{apiId}/on")
    public CommonResult autoTestOn(@PathVariable Integer apiId) {

        apiService.configAutoTestOn(apiId);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    @ApiOperation(value = "下线测试配置", notes = "下线测试配置")
    @PostMapping("/autotest/{apiId}/off")
    public CommonResult autoTestOff(@PathVariable Integer apiId) {

        apiService.configAutoTestOff(apiId);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * API下线
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "API下线", notes = "API下线")
    @PostMapping("offline")
    public CommonResult offline(@RequestBody JSONObject jsonObject) {
        ApiExpand apiExpand = jsonObject.toJavaObject(ApiExpand.class);
        apiService.offline(apiExpand);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * API删除
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "API删除", notes = "API删除")
    @PostMapping("delete")
    public CommonResult delete(@RequestBody JSONObject jsonObject) {
        ApiInfoWithBLOBs apiInfo = jsonObject.toJavaObject(ApiInfoWithBLOBs.class);
        String message = apiService.delete(apiInfo);
        return new CommonResult(ResultStatusEnum.SUCCESS, message);
    }

    /**
     * 【唯一性验证】--分组类API名称唯一性:true:唯一 false：不唯一
     *
     * @param jsonObject
     * @return CommonResult
     */
    @ApiOperation(value = "分组内API名称唯一性", notes = "分组内API名称唯一性")
    @PostMapping("checkUnique")
    public CommonResult checkUnique(@RequestBody JSONObject jsonObject) {
        ApiQuery apiQuery = jsonObject.toJavaObject(ApiQuery.class);
        boolean unique = apiService.checkUnique(apiQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }

    @ApiOperation(value = "分组内API名称唯一性", notes = "分组内API名称唯一性")
    @PostMapping("checkUniqueApiNameGlobal")
    public CommonResult checkUniqueApiNameGlobal(@RequestBody JSONObject jsonObject) {
        ApiQuery apiQuery = jsonObject.toJavaObject(ApiQuery.class);
        boolean unique = apiService.checkUniqueApiNameGlobal(apiQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }

    //接口2.0------------------------------------------下面-----------------------------------------------------------------
    @ApiOperation(value = "全局上游接口名称唯一性", notes = "全局上游接口名称唯一性")
    @PostMapping("checkUniqueInterfaceName")
    public CommonResult checkUniqueInterfaceName(@RequestBody JSONObject jsonObject) {
        ApiQuery apiQuery = jsonObject.toJavaObject(ApiQuery.class);
        boolean unique = apiService.checkUniqueInterfaceName(apiQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }

    @ApiOperation(value = "全局上游接口httpPath唯一性", notes = "全局上游接口httpPath唯一性")
    @PostMapping("checkUniqueInterfaceHttpPath")
    public CommonResult checkUniqueInterfaceHttpPath(@RequestBody JSONObject jsonObject) {
        ApiQuery apiQuery = jsonObject.toJavaObject(ApiQuery.class);
        boolean unique = apiService.checkUniqueInterfaceHttpPath(apiQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }
    //接口2.0------------------------------------------上面-----------------------------------------------------------------

    /**
     * 【唯一性验证】--httpPath的唯一性：resultMsg not Null & null
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "httpPath唯一性", notes = "httpPath唯一性")
    @PostMapping("httpPathUnique")
    public CommonResult httpPathUnique(@RequestBody JSONObject jsonObject) {
        ApiInfoWithBLOBs apiInfo = jsonObject.toJavaObject(ApiInfoWithBLOBs.class);
        String resultMsg = apiService.httpPathUnique(apiInfo);
        return new CommonResult(ResultStatusEnum.SUCCESS, resultMsg);
    }

    /**
     * 【检查唯一】--请求参数唯一：不唯一则抛出异常信息
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "paramName检查唯一", notes = "paramName检查唯一")
    @PostMapping("paramNameCheck")
    public CommonResult paramNameCheck(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("paramNameList");
        if (null == jsonArray || jsonArray.size() == 0) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        List<ParamNames> paramNameList = jsonArray.toJavaList(ParamNames.class);
        apiService.paramNameCheck(paramNameList);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 重切换--切换历史版本(参数:apiInfoVersion的id,apiGroupId,pubDescription)，该方法原本是复制历史版本到当前编辑功能（copyToEdit），
     * 当时前段工作较重，于是在未修改接口和参数的情况下，修改内部方法，于是现在看来入参设置不大河里。
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "切换版本", notes = "切换版本")
    @PostMapping("reswitchVersion")
    public CommonResult reswitchVersion(@RequestBody JSONObject jsonObject) {
        ApiInfoVersionsWithBLOBs apiInfoVersion = jsonObject.toJavaObject(ApiInfoVersionsWithBLOBs.class);
        apiService.reswitchVersion(apiInfoVersion);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 复制历史版本到当前编辑(参数：apiInfoVersion的id和apiGroupId)
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "复制", notes = "复制")
    @PostMapping("copyToEdit")
    public CommonResult copyToEdit(@RequestBody JSONObject jsonObject) {
        ApiInfoVersionsWithBLOBs apiInfoVersion = jsonObject.toJavaObject(ApiInfoVersionsWithBLOBs.class);
        apiService.copyToEdit(apiInfoVersion);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * apiInfo详情（编辑表）
     *
     * @return
     */
    @ApiOperation(value = "apiInfo详情", notes = "apiInfo详情")
    @PostMapping("findApiInfo")
    public CommonResult findApiInfoByApiIdAndGroupId(@RequestBody JSONObject jsonObject) {
        ApiInfoWithBLOBs apiInfo = jsonObject.toJavaObject(ApiInfoWithBLOBs.class);
        Map<String, Object> result = apiService.findAllByApiId(apiInfo);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    @ApiOperation(value = "指定API参数信息", notes = "指定API参数信息")
    @PostMapping("getApiParams")
    public CommonResult getApiParams(@RequestBody JSONObject jsonObject) {
        ApiInfoWithBLOBs apiInfo = jsonObject.toJavaObject(ApiInfoWithBLOBs.class);
        Map<String, Object> result = apiService.getApiParams(apiInfo);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * apiInfoVersion详情（版本表）
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "apiInfoVersion详情", notes = "apiInfoVersion详情")
    @PostMapping("findApiInfoVersion")
    public CommonResult findApiInfoVersion(@RequestBody JSONObject jsonObject) {
        ApiInfoVersionsWithBLOBs apiInfoVersion = jsonObject.toJavaObject(ApiInfoVersionsWithBLOBs.class);
        Map<String, Object> result = apiService.findApiInfoVersion(apiInfoVersion);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * 返回apiInfo列表（条件查询）
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "apiInfo列表", notes = "apiInfo列表")
    @PostMapping("ApiInfoList")
    public CommonResult apiInfoList(@RequestBody JSONObject jsonObject) {
        ApiInfoQuery apiInfoQuery = jsonObject.toJavaObject(ApiInfoQuery.class);
        PageInfo<ApiExpand> apiExpandPageInfo = apiService.apiInfoList(apiInfoQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiExpandPageInfo);
    }

    /**
     * 返回apiInfo列表（条件查询）
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "apiInfo列表", notes = "apiInfo列表")
    @PostMapping("apiInfoListByGroup")
    public CommonResult apiInfoListByGroup(@RequestBody JSONObject jsonObject) {
        ApiInfoQuery apiInfoQuery = jsonObject.toJavaObject(ApiInfoQuery.class);
        PageInfo<ApiExpand> apiExpandPageInfo = apiService.apiInfoListByGroup(apiInfoQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiExpandPageInfo);
    }

    /**
     * 返回apiInfoVersion列表（条件查询）
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "apiInfoVersions列表", notes = "apiInfoVersions列表")
    @PostMapping("apiInfoVersionsList")
    public CommonResult apiInfoVersionsList(@RequestBody JSONObject jsonObject) {
        ApiInfoVersionQuery apiInfoVersionQuery = jsonObject.toJavaObject(ApiInfoVersionQuery.class);
        PageInfo<ApiVersionExpand> apiVersionExpandPageInfo = apiService.apiInfoVersionList(apiInfoVersionQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiVersionExpandPageInfo);
    }

    @ApiOperation(value = "apiInfoVersionsLatest列表", notes = "apiInfoVersionsLatest列表")
    @PostMapping("apiInfoVersionsListForFindAPI")
    public CommonResult apiInfoVersionsListForFindAPI(@RequestBody JSONObject jsonObject) {
        ApiInfoVersionQuery apiInfoVersionQuery = jsonObject.toJavaObject(ApiInfoVersionQuery.class);
        PageInfo<ApiVersionExpand> apiVersionExpandPageInfo = apiService.apiInfoVersionLatestList(apiInfoVersionQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiVersionExpandPageInfo);
    }

    /**
     * 调试API
     *
     * @param jsonObject
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "调试API", notes = "调试API")
    @PostMapping(value = "debugging", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public Object debugging(@RequestBody JSONObject jsonObject, HttpServletResponse response) throws IOException {
        LOG.debug("Starting debugging : {}", JsonUtil.prettyJson(jsonObject.toJSONString()));
        response.setCharacterEncoding("UTF-8");
        IDInfo idInfo = jsonObject.getObject("idInfo", IDInfo.class);
        JSONArray jsonArray1 = jsonObject.getJSONArray("headerList");
        JSONArray jsonArray2 = jsonObject.getJSONArray("queryList");
        JSONArray jsonArray3 = jsonObject.getJSONArray("bodyList");
        List<RequestParamAndValue> headerList = new ArrayList<>();
        List<RequestParamAndValue> queryList = new ArrayList<>();
        List<RequestParamAndValue> bodyList = new ArrayList<>();
        //对输入的ID进行校验
        ApiValidate.idInfoCheck(idInfo);
        if (null != jsonArray1 && !jsonArray1.isEmpty()) {
            headerList = jsonArray1.toJavaList(RequestParamAndValue.class);
        }
        if (null != jsonArray2 && !jsonArray2.isEmpty()) {
            queryList = jsonArray2.toJavaList(RequestParamAndValue.class);
        }
        if (null != jsonArray3 && !jsonArray3.isEmpty()) {
            bodyList = jsonArray3.toJavaList(RequestParamAndValue.class);
        }
        return apiService.debugging(idInfo, headerList, queryList, bodyList);
    }


    /**
     * 应用场景：api列表 授权操作  根据传递过来的apiID和环境ID和app的查询条件 查询出app,如果是已经授权的app,页面显示为灰色
     * 这里的app封装的是查询条件 如 appid appname
     */
    @RequestMapping(value = "/findNotAccredit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult findNotAccredit(@RequestBody JSONObject jsonObject) {
        AppQuery query = jsonObject.toJavaObject(AppQuery.class);
        try {
            List<App> list = appOldService.findNotAccredit(query);
            query.setElements(list);
            return new CommonResult(ResultStatusEnum.SUCCESS, query);
        } catch (Exception e) {
            LOG.error("AppController findApp error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * 授权信息_查询已经授权的app(lemon版本客户权限查看，因合约在风控一直未使用，后亿字节版本新增了app管理，也为使用到此部分)
     */
    @RequestMapping(value = "/findAccreditApps", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult findAccreditApps(@RequestBody JSONObject jsonObject) {
        AppQuery appQuery = jsonObject.toJavaObject(AppQuery.class);
        try {
            List<App> list = appOldService.queryAppPageByApiId(appQuery);
            appQuery.setElements(list);
            return new CommonResult(ResultStatusEnum.SUCCESS, appQuery);
        } catch (Exception e) {
            LOG.error("AppController findAccreditApps error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }


    /**
     * api详情
     */
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult details(@RequestBody JSONObject jsonObject) {
        com.imjcker.manager.manage.po.Api api = jsonObject.toJavaObject(com.imjcker.manager.manage.po.Api.class);
        try {
            api = appOldService.findApiByApiId(api);
            return new CommonResult(ResultStatusEnum.SUCCESS, api);
        } catch (Exception e) {
            LOG.error("AppController details error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * APi授权页面 查找所有未授权的APP
     */
    @ResponseBody
    @RequestMapping(value = "/findNoAccreditApps", method = RequestMethod.POST)
    public CommonResult findAllApis(@RequestBody JSONObject jsonObject) {
        QueryApiApp queryApiApp = jsonObject.toJavaObject(QueryApiApp.class);
        String appName = queryApiApp.getAppName();
        if (StringUtils.isNotBlank(appName)) queryApiApp.setAppName(appName.replace("_", "\\_"));
        try {
            List<App> list = appOldService.findNoAccreditApps(queryApiApp);
            return new CommonResult(ResultStatusEnum.SUCCESS, list);
        } catch (Exception e) {
            LOG.error("AppController findNoAccreditApis error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * 授权_api页面_api授权app
     */
    @RequestMapping(value = "/apiToApp", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult accredit(@RequestBody JSONObject jsonObject) {
        ApiAccreditApp record = jsonObject.toJavaObject(ApiAccreditApp.class);
        String appIds_str = record.getAppIds();
        String[] appIds = appIds_str.split(",");
        Integer env = record.getEnv();
        Integer apiId = record.getApiId();
        try {
            for (String appId : appIds) {
                ApiFindApp apiFindApp = new ApiFindApp();
                apiFindApp.setEnv(env);
                apiFindApp.setAppCertificationId(Integer.parseInt(appId));
                apiFindApp.setApiId(apiId);
                /**验证是否是已经授权过得*/
                int total = appOldService.countApiFindApp(apiFindApp);
                if (total > 0) {
                    continue;
                }
                appOldService.accredit(apiFindApp);
            }
            return new CommonResult(ResultStatusEnum.SUCCESS, null);
        } catch (Exception e) {
            LOG.error("ApiController apiToApp error...", e);
            return ExceptionHelper.handleException(e, null, 0);
        }
    }

    /**
     * @param @param  jsonObject
     * @param @return 设定文件
     * @return CommonResult    返回类型
     * @Title: 调试API页面初始化
     */
    @PostMapping("/debugInfo")
    public CommonResult debugInfo(@RequestBody JSONObject jsonObject) {
        DebugApiQuery queryApiApp = jsonObject.toJavaObject(DebugApiQuery.class);
        Map<String, Object> result = debugService.getApiDebugInfo(queryApiApp);
        if (result == null) {
            return new CommonResult(ResultStatusEnum.ERROR, "接口不存在");
        }
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * 获取服务器IP地址
     *
     * @return
     */
    @PostMapping("/getServerURL")
    public CommonResult getServerURL() {
        String serverIpAndPort = apiService.getServerIpAndPort();
        return new CommonResult(ResultStatusEnum.SUCCESS, serverIpAndPort);
    }

    /* api接口文档先关方法*/

    /**
     * 下载api的DOCX文档
     *
     * @param apiId
     * @param versionId
     * @param response
     * @return
     */
    @GetMapping("/downloadDocx")
    public void downloadDocx(@RequestParam(name = "apiId") String apiId, @RequestParam(name = "versionId") String versionId, HttpServletResponse response) {
        String apiName = apiService.downloadDocx(versionId, Integer.parseInt(apiId));
        String filePath = docProperties.getTemporaryPath() + File.separator + apiName + docProperties.getWordExtension();
        DownloadUtils.downloadFile(filePath, response);
    }

    @PostMapping("/showDocx")
    public CommonResult showDocx(@RequestBody JSONObject jsonObject) {
        if (StringUtils.isBlank(jsonObject.getString("apiId"))) {
            return new CommonResult(ResultStatusEnum.PARAM_NULL, null);
        }
        Map<String, Object> result = apiService.showDocx(jsonObject.getString("apiId"), jsonObject.getString("versionId"));
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    @PostMapping("/parseJson")
    public CommonResult parseJson(@RequestBody JSONObject jsonObject) throws Exception {
        if (StringUtils.isBlank(jsonObject.getString("apiId")) || StringUtils.isBlank(jsonObject.getString("json"))) {
            return new CommonResult(ResultStatusEnum.PARAM_NULL, null);
        }
        List<CallBackParam> result = apiService.parseJson(jsonObject.getString("apiId"), jsonObject.getString("json"));
        Map<String, Object> map = new HashMap<>();
        map.put("responseList", result);
        return new CommonResult(ResultStatusEnum.SUCCESS, map);
    }

//    @PostMapping("/saveDocx")
//    public CommonResult saveDocx(@RequestBody JSONObject jsonObject){
//        if (jsonObject ==null){
//            return new CommonResult(ResultStatusEnum.PARAM_NULL,null);
//        }
//        if(apiService.saveDocx(jsonObject)){
//            return new CommonResult(ResultStatusEnum.SUCCESS,null);
//        }else {
//            return new CommonResult(ResultStatusEnum.ERROR,null);
//        }
//    }

    @PostMapping("/editDocx")
    public CommonResult editDocx(@RequestBody JSONObject jsonObject) {
        if (jsonObject == null) {
            return new CommonResult(ResultStatusEnum.PARAM_NULL, null);
        }
        if (apiService.editDocx(jsonObject)) {
            return new CommonResult(ResultStatusEnum.SUCCESS, null);
        } else {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
    }

    @PostMapping("/deleteDocx")
    public CommonResult deleteDocx(@RequestBody JSONObject jsonObject) {
        if (jsonObject == null) {
            return new CommonResult(ResultStatusEnum.PARAM_NULL, null);
        }
        if (apiService.deleteDocx(jsonObject)) {
            return new CommonResult(ResultStatusEnum.SUCCESS, null);
        } else {
            return new CommonResult(ResultStatusEnum.ERROR, null);
        }
    }

    /* api接口文档先关方法*/

    /**
     * 已发布api绑定限流策略,解绑
     *
     * @param jsonObject
     * @return CommonResult
     * @Version 2.0
     */
    @PostMapping("/apiBindingStrategy")
    public CommonResult apiBindingStrategy(@RequestBody JSONObject jsonObject) {
        ApiInfo apiInfo = jsonObject.toJavaObject(ApiInfo.class);
        //参数校验
        if (null == apiInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiInfo.getId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        }
        apiService.apibindingStrategy(apiInfo.getId(), apiInfo.getLimitStrategyuuid(), apiInfo.getLimitStrategyTotal());
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    /**
     * 返回指定策略的API
     *
     * @param jsonObject limitStrategyuuid参数
     * @return
     * @Version 2.0
     */
    @PostMapping("/findApiByStrategy")
    public CommonResult findApiByStrategy(@RequestBody JSONObject jsonObject) {
        CurrentLimitStrategyQuery strategyQuery = jsonObject.toJavaObject(CurrentLimitStrategyQuery.class);
        //参数校验
        if (null == strategyQuery || StringUtils.isBlank(strategyQuery.getUuid())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_LIMIT_STRATEGY_UUID);
        }
        PageInfo<ApiInfoVersionsWithBLOBs> list = apiService.findApiByStrategy(strategyQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, list);
    }

    @ApiOperation(value = "无分组API", notes = "无分组API")
    @PostMapping("findApiAndNoGroup")
    public CommonResult findApiAndNoGroup(@RequestBody JSONObject jsonObject) {
        ApiInfoQuery apiInfoQuery = jsonObject.toJavaObject(ApiInfoQuery.class);
        PageInfo<ApiExpand> list = apiService.findApiAndNoGroup(apiInfoQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, list);
    }

    @ApiOperation(value = "检查分组下是否存在Api", notes = "检查分组下是否存在Api")
    @PostMapping("hasNoneOfApi")
    public CommonResult hasNoneOfApi(@RequestBody JSONObject jsonObject) {
        Integer groupId = Integer.parseInt(jsonObject.get("groupId").toString());
        boolean unique = apiService.hasNoneOfApi(groupId);
        return new CommonResult(ResultStatusEnum.SUCCESS, unique);
    }

    @ApiOperation(value = "给分组增加已存在的API", notes = "给分组增加已存在的API")
    @PostMapping("addExistedApi")
    public CommonResult addExistedApi(@RequestBody JSONObject jsonObject) {
        Integer groupId = Integer.parseInt(jsonObject.get("groupId").toString());
        Integer apiId = Integer.parseInt(jsonObject.get("apiId").toString());
        apiService.addExistedApi(groupId, apiId);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    @ApiOperation(value = "上传码值映射Excel", notes = "给API导入码值映射")
    @PostMapping("/{id}/uploadExcel")
    public CommonResult uploadExcel(@PathVariable(value = "id") Integer id, @RequestBody byte[] bytes) throws IOException {

        excelService.convertToPreviewModel(id, bytes);
        return new CommonResult(ResultStatusEnum.SUCCESS, null);
    }

    @ApiOperation(value = "转换码值映射为json字符串并格式化json串", notes = "转换码值映射为json字符串并格式化json串")
    @PostMapping("/{id}/pretty")
    public CommonResult prettyString(@PathVariable(value = "id") Integer id, @RequestBody JSONObject jsonObject) throws IOException {

        LOG.info("Starting format json come from api that id is %s : ", id);
        String prettyJson = excelService.formatJson(jsonObject.getString("responseTransParam"));

        return new CommonResult(ResultStatusEnum.SUCCESS, prettyJson);
    }

    @PostMapping(value = "/{apiId}/riskIndex", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public CommonResult showApiRiskIndex(@PathVariable(value = "apiId") Integer apiId, @RequestBody JSONObject jsonObject) throws IOException {
        LOG.info("Starting showApiRiskIndex : {}", JsonUtil.prettyJson(jsonObject.toJSONString()));
        IDInfo idInfo = jsonObject.getObject("idInfo", IDInfo.class);
        JSONArray jsonArray1 = jsonObject.getJSONArray("headerList");
        JSONArray jsonArray2 = jsonObject.getJSONArray("queryList");
        JSONArray jsonArray3 = jsonObject.getJSONArray("bodyList");
        List<RequestParamAndValue> headerList = new ArrayList<>();
        List<RequestParamAndValue> queryList = new ArrayList<>();
        List<RequestParamAndValue> bodyList = new ArrayList<>();
        //对输入的ID进行校验
        ApiValidate.idInfoCheck(idInfo);
        if (null != jsonArray1 && !jsonArray1.isEmpty()) {
            headerList = jsonArray1.toJavaList(RequestParamAndValue.class);
        }
        if (null != jsonArray2 && !jsonArray2.isEmpty()) {
            queryList = jsonArray2.toJavaList(RequestParamAndValue.class);
        }
        if (null != jsonArray3 && !jsonArray3.isEmpty()) {
            bodyList = jsonArray3.toJavaList(RequestParamAndValue.class);
        }
        String result = apiService.convertApiRiskIndex(idInfo, headerList, queryList, bodyList, apiId);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    @PostMapping(value = "/{apiId}/removeKey", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public CommonResult removeRedisKey(@PathVariable(value = "apiId") Integer apiId, @RequestBody JSONObject jsonObject) throws IOException {
        LOG.info("Starting remove redis cache : {}", JsonUtil.prettyJson(jsonObject.toJSONString()));
        IDInfo idInfo = jsonObject.getObject("idInfo", IDInfo.class);
        JSONArray jsonArray1 = jsonObject.getJSONArray("headerList");
        JSONArray jsonArray2 = jsonObject.getJSONArray("queryList");
        JSONArray jsonArray3 = jsonObject.getJSONArray("bodyList");
        List<RequestParamAndValue> headerList = new ArrayList<>();
        List<RequestParamAndValue> queryList = new ArrayList<>();
        List<RequestParamAndValue> bodyList = new ArrayList<>();
        //对输入的ID进行校验
        ApiValidate.idInfoCheck(idInfo);
        if (null != jsonArray1 && !jsonArray1.isEmpty()) {
            headerList = jsonArray1.toJavaList(RequestParamAndValue.class);
        }
        if (null != jsonArray2 && !jsonArray2.isEmpty()) {
            queryList = jsonArray2.toJavaList(RequestParamAndValue.class);
        }
        if (null != jsonArray3 && !jsonArray3.isEmpty()) {
            bodyList = jsonArray3.toJavaList(RequestParamAndValue.class);
        }
        String result = apiService.removeRedisKey(idInfo, headerList, queryList, bodyList, apiId);
        return new CommonResult(ResultStatusEnum.SUCCESS, result);
    }

    /**
     * 改造gateway服务
     * 原来：查询数据库（api_info表）,获取接口信息，
     * 改造后：先查询redis，未查到查询数据库(api_info_versions_latest表)
     * 原来改造latest表时未存储jsonconfig，因此开发接口同步api_info表中jsonconfig数据
     * 到latest表以及redis
     * 仅上线使用一次
     */

    @ApiOperation(value = "同步redis", notes = "同步redis")
    @GetMapping("synchroJsonConfig")
    public String synchroJsonConfig() {
        List<Integer> result = apiService.synchroJsonConfig();
        return result.toString();
    }

    /**
     * 通过分组号查询分组下可用接口列表
     */
    @PostMapping("/findApiByGroupId")
    public CommonResult findApiByGroupId(@RequestBody JSONObject jsonObject) {
        Integer groupId = jsonObject.getInteger("id");
        List<ApiInfoWithBLOBs> list = apiService.findByGroupIdAndStatus(groupId);
        return new CommonResult(ResultStatusEnum.SUCCESS, list);
    }

    /**
     * 批量下线版本--脚本使用
     *
     * @param
     * @return
     */
    @ApiOperation(value = "批量下线版本", notes = "批量下线版本")
    @GetMapping("offlineAll")
    public String offlineAll(@RequestParam(name = "apiIdList") String apiIdList, @RequestParam(name = "env") String env) {
        //apiId有效性校验
        String validApiIdList = apiService.chooseValidApiID(apiIdList);
        LOG.info("list={},env={}", apiIdList, env);
        if (StringUtils.isBlank(validApiIdList)) {
            LOG.info("获取有效的apiId失败");
            return "fail to get apiId ,please input valid apiId!";
        }
        String[] validApiIdArray = validApiIdList.split(" ");
        ApiExpand apiExpand = new ApiExpand();
        for (String apiId : validApiIdArray) {
            apiExpand.setEnv(Integer.parseInt(env));
            apiExpand.setId(Integer.parseInt(apiId));
            apiService.offline(apiExpand);//返回发布接口的apiId
        }
        return "success";
    }

    /**
     * 批量下线接口--接口平台页面中使用
     *
     * @param
     * @return
     */
    @ApiOperation(value = "批量下线接口", notes = "批量下线接口")
    @PostMapping("offlineByApiIdList")
    public CommonResult offlineByApiIdList(@RequestBody JSONObject jsonObject) {
        String env = jsonObject.getString("env");
        List<Integer> removeIdList = (List<Integer>) jsonObject.get("removeIdList");
        List<Integer> apiIdList = (List<Integer>) jsonObject.get("apiIdList");
        if (null != removeIdList && removeIdList.size() > 0) {
            for (Integer apiId : removeIdList)
                apiIdList.remove(apiId);
        }
        ApiExpand apiExpand = new ApiExpand();
        if (null != apiIdList && apiIdList.size() > 0) {
            for (Integer apiId : apiIdList) {
                if(apiService.isOffline(apiId))
                    continue;
                apiExpand.setEnv(Integer.parseInt(env));
                apiExpand.setId(apiId);
                apiService.offline(apiExpand);//下线
            }
            return new CommonResult(ResultStatusEnum.SUCCESS, null);
        }
        return new CommonResult(ResultStatusEnum.API_NULL, null);
    }

    /**
     * 复制API
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "复制API", notes = "复制API")
    @PostMapping("copy")
    public CommonResult copy(@RequestBody JSONObject jsonObject) {
        ApiExpand apiExpand = jsonObject.toJavaObject(ApiExpand.class);
        //接口id校验
        ApiValidate.apiIdCheck(apiExpand);
        Integer id = apiService.copy(apiExpand);
        return new CommonResult(ResultStatusEnum.SUCCESS, "接口id=" + id);
    }

    /**
     * 批量接口导出
     */
    @ApiOperation(value = "API导出", notes = "API导出")
    @GetMapping(value = "apiExport")
        public void apiExport(@RequestParam(name = "apiIdList") String apiIdList,
                @RequestParam(name = "removeIdList") String removeIdList,
                HttpServletResponse response) {
        String[] apiIdListArray;
        if (StringUtils.isNotBlank(apiIdList))
            apiIdListArray = apiIdList.split(",");
        else
            throw new BusinessException("接口id为空");
        List<String> idList = new ArrayList<>(Arrays.asList(apiIdListArray));
        if (StringUtils.isNotBlank(removeIdList)){
            String[] removeIdListArray = removeIdList.split(",");
            List<String> removeList = new ArrayList<>(Arrays.asList(removeIdListArray));
            if (!removeList.isEmpty()) {
                idList.removeAll(removeList);
            }
        }
        StringBuilder stringBuilder = new StringBuilder("导出成功接口:");
        String result;
        String filePath =  FILE_PATH ;
        String fileName = File.separator + DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss").format(LocalDateTime.now()) + ".sql";
        if (idList.size() > 0) {
            for (String apiId : idList) {
                Integer successApiId = apiService.apiExport(Integer.valueOf(apiId),filePath+fileName);//返回发布接口的apiId
                stringBuilder.append(successApiId + " ");
            }
            result = stringBuilder.toString();
            LOG.info("批量导出成功的apiId: {}", result);
        } else {
            LOG.info("没有需要导出的接口");
        }
        DownloadUtils.downloadFile(filePath+fileName, fileName.substring(1), response);
        LOG.info("没有需要导出的接口,{}",response.getStatus());
    }

    /**
     * 返回combinationApiList列表（条件查询）
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "combinationApiList", notes = "combinationApiList列表")
    @PostMapping("combinationApiList")
    public CommonResult combinationApiList(@RequestBody JSONObject jsonObject) {
        ApiInfoQuery apiInfoQuery = jsonObject.toJavaObject(ApiInfoQuery.class);
        PageInfo<ApiExpand> apiExpandPageInfo = apiService.combinationApiList(apiInfoQuery);
        return new CommonResult(ResultStatusEnum.SUCCESS, apiExpandPageInfo);
    }

    /**
     * 批量建立接口--接口平台页面中使用
     *
     * @param
     * @return
     */
    @ApiOperation(value = "批量建立接口", notes = "批量建立接口")
    @PostMapping("publicProContract")
    public CommonResult publicProContract(@RequestBody JSONObject jsonObject) {
        String appKey = jsonObject.getString("appKey");
        List<Integer> removeIdList = (List<Integer>) jsonObject.get("removeIdList");
        List<Integer> apiIdList = (List<Integer>) jsonObject.get("apiIdList");
        if (null != removeIdList && removeIdList.size() > 0) {
            for (Integer apiId : removeIdList)
                apiIdList.remove(apiId);
        }
        if (null != apiIdList && apiIdList.size() > 0) {
            boolean isSuccess = apiService.createProContract(apiIdList, appKey);
            return new CommonResult(ResultStatusEnum.SUCCESS, isSuccess);
        }
        return new CommonResult(ResultStatusEnum.API_NULL, null);
    }
    /**
     * 根据apiId查询接口分组名称
     */
    @PostMapping(value = "/getGroupNameByApiId", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public Object getGroupNameByApiId(@RequestBody JSONObject jsonObject)  {
        Integer id = jsonObject.getInteger("apiId");
        ApiInfoWithBLOBs api = apiService.findById(id);
        //根据分组id查询分组名称
        ApiGroup apiGroup = apiGroupService.findById(api.getApiGroupId());
        return apiGroup.getGroupName();
    }

    /**
     * 根据apiId查询接口名称
     */
    @PostMapping(value = "/getApiNameByApiId", produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public Object getApiNameByApiId(@RequestBody JSONObject jsonObject)  {
        Integer id = jsonObject.getInteger("apiId");
        ApiInfoWithBLOBs api = apiService.findById(id);
        return api.getApiName();
    }
}
