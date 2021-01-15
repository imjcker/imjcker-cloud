package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.helper.InsertSqlUtil;
import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.*;
import com.imjcker.manager.manage.vo.*;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.collections.CollectionUtil;
import com.lemon.common.util.encrypt.RSAUtil;
import com.imjcker.manager.manage.model.ApiRiskIndex;
import com.imjcker.manager.manage.po.query.ApiInfoLatestQuery;
import com.imjcker.manager.manage.po.query.AutoTestQuery;
import com.imjcker.manager.manage.validator.ApiValidate;
import com.imjcker.manager.util.WordUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Lemon.kiana
 * @version 1.0
 *          2017年7月17日 上午10:35:53
 * @Title ApiServiceImpl
 * @Description ApiService服务实现层
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
@Service
public class ApiServiceImpl implements ApiService {

    private Logger log = LoggerFactory.getLogger(ApiServiceImpl.class);
    public static final String FILE_NAME = "insert_api.sql";//要创建的文件名
    public static final String FILE_PATH = System.getProperty("user.dir");//文件指定存放的路径;//文件指定存放的路径
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Value("${request.back.appkey}")
    private String appKey;

//    @Value("${request.back.env}")
    private String env=null;

    private static final String DATA = "data";

    private static final String API_CACHE = "API_CACHE";
    @Autowired
    ApiInfoMapper apiInfoMapper;

    @Autowired
    ApiInfoVersionsMapper apiInfoVersionsMapper;
    @Autowired
    ApiInfoVersionsLatestMapper apiInfoVersionsLatestMapper;

    @Autowired
    RequestParamsMapper requestParamsMapper;
    @Autowired
    RequestParamsVersionsMapper requestParamsVersionsMapper;
    @Autowired
    RequestParamsVersionsLatestMapper requestParamsVersionsLatestMapper;

    @Autowired
    BackendRequestParamsMapper backendMapper;
    @Autowired
    BackendRequestParamsVersionsMapper backendVersionsMapper;

    @Autowired
    ApiresultsettingsMapper resultSettingMapper;

    @Autowired
    ApiResultEtlMapper apiResultEtlMapper;

    @Autowired
    ApiresultsettingsVersionsMapper resultSettingVersionsMapper;

    @Autowired
    ApiAppRelationMapper apiAppRelationMapper;

    @Autowired
    SystemErrorCodeMapper systemErrorCodeMapper;

    @Autowired
    ApiGroupService apiGroupService;

    @Autowired
    StrategyAuthService strategyAuthService;

    @Autowired
    ApiRateDistributeMapper subApiInfoMapper;

    @Autowired
    ApiGroupRelationMapper apiGroupRelationMapper;

    @Autowired
    ApiGroupMapper apiGroupMapper;

    @Autowired
    SubUpStreamApiService subApiService;

    @Autowired
    BackendDistributeParamsMapper backendDistrbiteMapper;

    @Autowired
    private AutoTestMapper autoTestMapper;

    @Autowired
    private CallBackParamMapper callBackParamMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ModifyCombinationParamService modifyCombinationParamService;

    @Autowired
    private ApiCategoryRelationMapper apiCategoryRelationMapper;


    @Autowired
    private AuthService authService;
    @Autowired
    private  ApiOfflineRecordMapper apiOfflineRecordMapper;

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private ApiGroupRelationMapper apiGroupRelationMapper;

    @Value("${notify-cache.service.name}")
    private String zuulServiceName;

    @Value("${spring.application.name}")
    private String selfServiceName;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    /**
     * 保存，api基本信息，下游请求参数，上游请求参数，返回码设置
     *
     * @param apiInfo
     * @param requestParamsList
     * @param backendList
     * @param requestParamsList
     * @return Integer 返回api_Info的ID
     */
    @Transactional
    @Override
    public Integer save(ApiInfoWithBLOBs apiInfo, List<RequestParams> requestParamsList,
                        List<BackendRequestParams> backendList) {
        //保存apiInfo并返回apiInfo的id
        Integer apiId = this.save(apiInfo);
        //接口2.0--新建api与分组的关系表
        if (null != apiInfo.getApiGroupId()) {
            this.addRelationOfApiAndGroup(apiId, apiInfo.getApiGroupId());
        }
        //保存下游请求参数
        List<Integer> requestParamsIdList = null;
        if (null != requestParamsList && requestParamsList.size() > 0) {
            RequestParams requestParams;
            requestParamsIdList = new ArrayList<Integer>();
            Iterator<RequestParams> iterator = requestParamsList.iterator();
            while (iterator.hasNext()) {
                requestParams = iterator.next();
                requestParams.setApiId(apiId);
                Integer requestParamsId = this.save(requestParams);
                requestParamsIdList.add(requestParamsId);
            }
        }
        //保存上游请求参数

        if (null != backendList && backendList.size() > 0) {
            Integer i = 0;
            BackendRequestParams backendParams;
            Iterator<BackendRequestParams> backendIterator = backendList.iterator();
            while (backendIterator.hasNext()) {
                backendParams = backendIterator.next();
                backendParams.setApiId(apiId);
                //参数校验
                ApiValidate.backendRequestParamsCheck(backendParams);
                if (null != requestParamsIdList && requestParamsIdList.size() > 0) {
                    //当id仍有值且改后台参数为变量时，绑定下游参数ID
                    if (i < requestParamsIdList.size() && backendParams.getParamsType() == CommonStatus.VARIABLE) {
                        backendParams.setRequestParamsId(requestParamsIdList.get(i++));
                    }
                }
                this.save(backendParams);
            }
        }
        return apiId;
    }
    /**
     * 同步发布信息到redis中
     */
    @Override
    public String synchroLatest2Redis() {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andCurrentVersionEqualTo(CommonStatus.ENABLE);
        //查询latest表中所有接口信息
        List<ApiInfoVersionsWithBLOBs> apiList = apiInfoVersionsLatestMapper.selectByExampleWithBLOBs(example);
        if(apiList == null || apiList.size()==0){
            return null;
        }
        ApiInfoRedisMsg apiInfoRedisMsg = new ApiInfoRedisMsg();
        Integer count = 0;
        //依次遍历接口，查询latest中请求参数信息
        for (ApiInfoVersionsWithBLOBs apiInfoVersionsWithBLOBs: apiList){
            Integer apiId = apiInfoVersionsWithBLOBs.getApiId();
            apiInfoRedisMsg.setApiInfoWithBLOBs(apiInfoVersionsWithBLOBs);
            List<RequestParamsVersionsLatest> listParam = requestParamsVersionsLatestMapper.selectApiByApiId(apiId);
            apiInfoRedisMsg.setList(listParam);
            //存入redis缓存中
            redisService.setToCaches("api:"+apiId,apiInfoRedisMsg);
            log.info("api:{},参数个数={}",apiId,apiInfoRedisMsg.getList().size());
            count++;
        }
        return String.valueOf(count);
    }

    @Override
    public String countRedis(String pattern) {
        Set set = redisService.keys(pattern);
        for(Object it:set){
            log.info("{}查询到的key={}",pattern,it);
        }
        log.info("queryRedis:{}",set.size());
        return String.valueOf(set.size());
    }
    @Override
    public String queryRedis(String pattern) {
        com.alibaba.fastjson.JSONObject value = redisService.get(pattern);
        if (value != null)
            return String.valueOf(value);
        else
            return "value=null";
    }

    @Override
    public void  deleteRedis(String pattern) {
         redisService.keysDel(pattern);
    }

    /**
     * 新建api与分组的关系表
     *
     * @param apiId
     * @param groupId
     * @Version 2.0
     */
    private void addRelationOfApiAndGroup(Integer apiId, Integer groupId) {
        //检查该API是否与其他分组存在关系
        ApiGroupRelationExample example = new ApiGroupRelationExample();
        ApiGroupRelationExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        List<ApiGroupRelation> relationList = apiGroupRelationMapper.selectByExample(example);
        if (null != relationList && relationList.size() > 0) {
            Integer usedGroupId = relationList.get(0).getGroupId();
            String usedGroupName = apiGroupService.findById(usedGroupId).getGroupName();
            throw new BusinessException(StringUtils.join(ExceptionInfo.ALREADY_BINDING, usedGroupName));
        }

        //获取多级分组信息
        String path = this.getRelationPath(groupId, apiId);
        String fullPathName = this.getFullPathName(groupId);

        ApiGroupRelation apiGroupRelation = new ApiGroupRelation();
        apiGroupRelation.setId(null);
        apiGroupRelation.setApiId(apiId);
        apiGroupRelation.setGroupId(groupId);
        apiGroupRelation.setPath(path);
        apiGroupRelation.setFullPathName(fullPathName);
        apiGroupRelation.setCreateTime(System.currentTimeMillis());
        apiGroupRelationMapper.insert(apiGroupRelation);
    }


    /**
     * 获取api分组全路径
     *
     * @param groupId
     * @return
     * @Version 2.0
     */
    @Override
    public String getFullPathName(Integer groupId) {
        String fullPathName = null;
        ApiGroup group = null;
        String pathName = null;
        while (null != groupId) {
            group = apiGroupService.findById(groupId);
            groupId = group.getParentId();
            pathName = group.getGroupName();
            fullPathName = StringUtils.join(pathName, ".", fullPathName);
        }
        return StringUtils.join(".",fullPathName);
    }

    /**
     * 获取api与上层分组关系的path
     *
     * @param groupId
     * @param apiId
     * @Version 2.0
     */
    private String getRelationPath(Integer groupId, Integer apiId) {
        String path = apiId.toString();
        Integer pathId = groupId;
        ApiGroup group;
        while (null != pathId) {
            path = StringUtils.join(pathId, ".", path);
            group = apiGroupService.findById(pathId);
            if (group != null) {
                pathId = group.getParentId();
            }
        }
        path = StringUtils.join(".",path);
        return path;
    }

    @Override
    public void physicallyDeleteRelation(Integer groupId, Integer apiId, String pathId) {
        ApiGroupRelationExample example = new ApiGroupRelationExample();
        ApiGroupRelationExample.Criteria criteria = example.createCriteria();
        if (null != groupId) {
            criteria.andGroupIdEqualTo(groupId);
        }
        if (null != apiId) {
            criteria.andApiIdEqualTo(apiId);
        }
        if (StringUtils.isNotBlank(pathId)) {
            pathId = StringUtils.wrap(pathId, ".");
            criteria.andPathLike(StringUtils.wrap(pathId, "%"));
        }
        apiGroupRelationMapper.deleteByExample(example);
    }

    /**
     * 发布版本
     *
     * @param  apiExpand
     * @return Integer
     */
    @Override
    public Integer publish(ApiExpand apiExpand) {
        //从apiinfo获取最新信息，更新原api版本号为2，插入最新版本到apiinfoversion中，版本号为1
        //保存api_Info_Versions表
        log.info("发布接口apiId = {}",apiExpand.getId());
        ApiInfoRedisMsg apiInfoRedisMsg = new ApiInfoRedisMsg();
        ApiInfoVersionsWithBLOBs apiVersions = this.publishApiVersion(apiExpand);

        //根据api发布运行环境，设置apiinfo对应接口参数值
        //插入发布的环境和版本号到api_Info
        this.updateApi(apiVersions);

        //保存下游的请求参数到对应版本表
        List<RequestParamsVersionsLatest> list = this.publishRequestParams(apiVersions.getVersionId(), apiExpand);
        //保存上游的请求参数到对应版本表(----版本号)
        this.publishBackendRequest(apiVersions.getVersionId(), apiExpand);
        apiInfoRedisMsg.setApiInfoWithBLOBs(apiVersions);
        apiInfoRedisMsg.setList(list);
        log.info("发布接口更新redis缓存");
        redisService.setToCaches("api:"+apiExpand.getId(),apiInfoRedisMsg);

        //更新redis-httpPath缓存
        redisService.setRedisHttpPath2Cache(apiVersions);

        // wt 修改参数 查询关联的组合接口,更新组合接口请求参数
        modifyCombinationParamService.updateCombInfWithPublish(apiExpand.getId());
        //返回新发布api的ID
        return  apiVersions.getApiId();
    }

    /**
     * 插入发布的环境和版本号到api_Info
     *
     * @param apiVersions
     */
    private void updateApi(ApiInfoVersionsWithBLOBs apiVersions) {
        ApiInfoWithBLOBs apiInfo = this.findById(apiVersions.getApiId());
        Integer env = apiVersions.getEnv();
        String versionId = apiVersions.getVersionId();
        //如果版本库表里env为1，则是发布到线上，2则是发布到测试
        if (CommonStatus.ONLINE == env) {
            apiInfo.setPublishProductEnvStatus(CommonStatus.ENABLE);
            apiInfo.setProductEnvVersion(versionId);
        } else {
//            apiInfo.setPublishTestEnvStatus(CommonStatus.ENABLE);
//            apiInfo.setTestEnvVersion(versionId);
            LOG.error("env参数错误");
        }
        apiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.updateByPrimaryKeyWithBLOBs(apiInfo);
    }


    /**
     * 发布上游输入参数到对应版本表
     *
     * @param versionId
     * @param apiExpand
     */
    private void publishBackendRequest(String versionId, ApiExpand apiExpand) {
        //根据apiId从数据库取出上游请求参数列表
        List<BackendRequestParams> backendList = this.findBackendParamsByApiIdAndStatus(apiExpand.getId());
        //若该api下参数列表不为空则复制到对应的版本表
        if (null != backendList && backendList.size() > 0) {
            BackendRequestParams backendParams;
            BackendRequestParamsVersions backendParamsVersions;
            Iterator<BackendRequestParams> iterator = backendList.iterator();
            while (iterator.hasNext()) {
                backendParams = iterator.next();
                backendParamsVersions = new BackendRequestParamsVersions();
                BeanCustomUtils.copyPropertiesIgnoreNull(backendParams, backendParamsVersions);
                backendParamsVersions.setId(null);
                backendParamsVersions.setVersionId(versionId);
                //版本切换，在backend_request_params_versions表增加backendParamsId字段与backend_request_params表中的id关联
                backendParamsVersions.setBackendParamsId(backendParams.getId());
                backendParamsVersions.setCreateTime(System.currentTimeMillis());

                backendVersionsMapper.insert(backendParamsVersions);
            }
        }
    }

    /**
     * 根据apiId获取上游请求参数表（变量and常量）
     *
     * @param apiId
     * @return
     */
    private List<BackendRequestParams> findBackendParamsByApiId(Integer apiId) {
        if (null == apiId) {
            throw new DataValidationException("用于查找BackendRequestParams的apiId值为空");
        }
        BackendRequestParamsExample example = new BackendRequestParamsExample();
        BackendRequestParamsExample.Criteria criteria = example.createCriteria();

        criteria.andApiIdEqualTo(apiId);
        return backendMapper.selectByExample(example);
    }

    /**
     * 发布下游输入参数到对应版本表
     *
     * @param versionId
     * @param apiExpand
     */
    private  List<RequestParamsVersionsLatest> publishRequestParams(String versionId, ApiExpand apiExpand) {
        //根据apiId从数据库取出下游请求参数列表
        List<RequestParams> requestParamsList = this.findRequestParamsListByApiIdAndStatus(apiExpand.getId());
        List<RequestParamsVersionsLatest> requestParamsVersionsLatestList = new ArrayList<>();
        requestParamsVersionsLatestMapper.deleteByPrimaryKey(apiExpand.getId());
        //若该api下参数列表不为空则复制到对应的版本表
        if (null != requestParamsList && requestParamsList.size() > 0) {
            RequestParams requestParams;
            RequestParamsVersions requestParamsVersions;
            RequestParamsVersionsLatest requestParamsVersionsLatest;
            Iterator<RequestParams> iterator = requestParamsList.iterator();
            while (iterator.hasNext()) {
                requestParams = iterator.next();
                requestParamsVersions = new RequestParamsVersions();
                BeanCustomUtils.copyPropertiesIgnoreNull(requestParams, requestParamsVersions);
                requestParamsVersions.setId(null);
                requestParamsVersions.setVersionId(versionId);
                requestParamsVersions.setRequestParamsId(requestParams.getId());
                requestParamsVersions.setCreateTime(System.currentTimeMillis());
                //插入最新版本请求参数信息
                requestParamsVersionsMapper.insert(requestParamsVersions);
                //kjy --插入最新版本请求参数信息
                requestParamsVersionsLatest = new RequestParamsVersionsLatest();
                BeanCustomUtils.copyPropertiesIgnoreNull(requestParams, requestParamsVersionsLatest);
                requestParamsVersionsLatest.setId(null);
                requestParamsVersionsLatest.setVersionId(versionId);
                requestParamsVersionsLatest.setRequestParamsId(requestParams.getId());
                requestParamsVersionsLatest.setCreateTime(System.currentTimeMillis());
                requestParamsVersionsLatest.setApiId(apiExpand.getId());
                //把最新版本接口参数信息放在request_params_version_latest表中；
                requestParamsVersionsLatestList.add(requestParamsVersionsLatest);
                requestParamsVersionsLatestMapper.insert(requestParamsVersionsLatest);
            }
            log.info("发布接口请求参数插入完成");
        }
        return requestParamsVersionsLatestList;
    }

    /**
     * 根据apiId获取相应的下游请求参数列表
     *
     * @param apiId
     * @return
     */
    private List<RequestParams> findRequestParamsByApiId(Integer apiId) {
        if (null == apiId) {
            throw new DataValidationException("用于查找RequestParams的apiId值为空");
        }
        RequestParamsExample example = new RequestParamsExample();
        RequestParamsExample.Criteria criteria = example.createCriteria();

        criteria.andApiIdEqualTo(apiId);
        return requestParamsMapper.selectByExample(example);
    }

    /**
     * 发布apiInfo到对应版本表
     *
     * @param apiExpand
     * @return
     */
    private ApiInfoVersionsWithBLOBs publishApiVersion(ApiExpand apiExpand) {
        //根据apiId从数据库取出apiInfo
        ApiInfoWithBLOBs apiInfo = this.findById(apiExpand.getId());
        if (null == apiInfo || CommonStatus.DISENABLE == apiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        ApiInfoVersionsWithBLOBs apiVersions = new ApiInfoVersionsWithBLOBs();
        //把apiInfo的数据set入对应版本库表
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfo, apiVersions);
        Long time = System.currentTimeMillis();

//        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//        Date date = new Date(time);

        //设置其他参数
        apiVersions.setId(null);
        apiVersions.setVersionId(DateUtil.stampToDates(time.toString()));
        apiVersions.setApiId(apiExpand.getId());
        apiVersions.setEnv(apiExpand.getEnv());
        apiVersions.setCurrentVersion(CommonStatus.ENABLE);
        apiVersions.setPubDescription(apiExpand.getPubDescription());
        apiVersions.setCreateTime(time);
//        apiVersions.setJsonConfig(apiInfo.getJsonConfig());//jsonconfig

        //修改同一api下的同种环境（线上、测试）的当前状态（currentVersion）为否(2)
        ApiInfoVersionsExample oldExample = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria oldCriteria = oldExample.createCriteria();

        oldCriteria.andApiIdEqualTo(apiExpand.getId());
        oldCriteria.andEnvEqualTo(apiExpand.getEnv());

        ApiInfoVersionsWithBLOBs tempApi = new ApiInfoVersionsWithBLOBs();
        tempApi.setCurrentVersion(CommonStatus.DISENABLE);

        //更新版本号为CurrentVersion=2
        apiInfoVersionsMapper.updateByExampleSelective(tempApi, oldExample);

        //插入新的版本
        apiInfoVersionsMapper.insert(apiVersions);

        //插入新的接口版本信息（---------插入该数据到api_info_version_latest---------）
        Integer  countId = this.findApiInfoVersionsLatestById(apiInfo.getId());
        if(countId == null ){
            apiInfoVersionsLatestMapper.insert(apiVersions);
            log.info("发布,api_info_versions_latest接口最新版本表中无apiId={}接口,插入接口信息",apiInfo.getId());
        }else{
            apiInfoVersionsLatestMapper.updateByPrimaryKeyWithBLOBs(apiVersions);
            log.info("发布,api_info_versions_latest接口最新版本表中存在apiId={}接口,更新接口信息",apiInfo.getId());
        }
        //更新redis缓存
        return apiVersions;
    }

    /**
     * 保存单条上游请求参数
     *
     * @param backendParams
     * @return
     */
    @Transactional
    @Override
    public void save(BackendRequestParams backendParams) {
        //验证当参数类型为常量时，参数值不为null
        Integer paramsType = backendParams.getParamsType();
        String paramValue = backendParams.getParamValue();
        if (CommonStatus.CONSTANT == paramsType && StringUtils.isBlank(paramValue)) {
            throw new BusinessException(StringUtils.join("参数类型为常量，paramValue", ExceptionInfo.NOT_NULL));
        }
        if ((CommonStatus.JSON == paramsType || CommonStatus.XML == paramsType) && StringUtils.isBlank(paramValue)) {
            //判断常量json或者xml参数是否为 "" 空串 空白
           backendParams.setParamValue(null);
        }
        Long createTime = System.currentTimeMillis();
        backendParams.setId(null);
        backendParams.setCreateTime(createTime);
        backendParams.setUpdateTime(createTime);
        backendParams.setStatus(CommonStatus.ENABLE);
        backendMapper.insert(backendParams);
    }

    /**
     * 保存单条下游请求参数
     *
     * @param requestParams
     * @return Integer
     */
    @Transactional
    @Override
    public Integer save(RequestParams requestParams) {
        //验证参数必填相关属性的逻辑正确性：即属性必填时，默认值应为null
        Integer paramsMust = requestParams.getParamsMust();
        String defaultValue = requestParams.getParamsDefaultValue();
        if (CommonStatus.ENABLE == paramsMust && StringUtils.isNotBlank(defaultValue)) {
            throw new BusinessException(ExceptionInfo.PARAMS_MUST_ERROR);
        }

        //设置状态和时间
        Long createTime = System.currentTimeMillis();
        requestParams.setId(null);
        requestParams.setStatus(CommonStatus.ENABLE);
        requestParams.setCreateTime(createTime);
        requestParams.setUpdateTime(createTime);
        requestParamsMapper.insert(requestParams);
        return requestParams.getId();
    }

    /**
     * 保存api基本信息
     *
     * @param apiInfo
     * @return
     */
    @Transactional
    @Override
    public Integer save(ApiInfoWithBLOBs apiInfo) {
        //检查api名称是否存在
        ApiQuery apiQuery = new ApiQuery();
        String apiName = apiInfo.getApiName().replaceAll(" ", "");
        Integer apiGroupId = apiInfo.getApiGroupId();

        apiQuery.setApiName(apiName);
        if (null != apiGroupId) {
            apiQuery.setApiGroupId(apiGroupId);
        }

        if (!checkUnique(apiQuery)) {
            throw new BusinessException(ExceptionInfo.EXIST_APINAME);
        }

        //mock数据参数业务校验
        Integer isMock = apiInfo.getIsMock();
        String mockData = apiInfo.getMockData();

        if (CommonStatus.ENABLE == isMock && StringUtils.isBlank(mockData)) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MOCK_DATA);
        }
        if (CommonStatus.DISENABLE == isMock && StringUtils.isNotBlank(mockData)) {
            throw new BusinessException(ExceptionInfo.SETTING_MOCK_ERROR);
        }

        //前后端路径与地址的对比

        String httpPath = StringUtils.trim(apiInfo.getHttpPath());
        String backEndAddress = StringUtils.trim(apiInfo.getBackEndAddress());
        String backEndAddressB = StringUtils.trim(apiInfo.getBackEndAddressB());
        String backEndPath = StringUtils.trim(apiInfo.getBackEndPath());

        //全局对比地址httpPath
        String repeatName2 = this.httpPathUnique(apiInfo);
        if (StringUtils.isNotBlank(repeatName2)) {
            throw new BusinessException(StringUtils.join(repeatName2, ExceptionInfo.IS_REPEAT));
        }

        //保存mongodb数据的相关处理
        Integer saveMongoDB = apiInfo.getSaveMongoDB();
        String mongodbURI = apiInfo.getMongodbURI();
        String dbName = apiInfo.getMongodbDBName();
        String collectionName = apiInfo.getMongodbCollectionName();

        if (CommonStatus.ENABLE == saveMongoDB && (StringUtils.isBlank(mongodbURI) || StringUtils.isBlank(collectionName) || StringUtils.isBlank(dbName))) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MONGODB);
        }
        if (CommonStatus.DISENABLE == saveMongoDB && (StringUtils.isNotBlank(mongodbURI) || StringUtils.isNotBlank(collectionName) || StringUtils.isNotBlank(dbName))) {
            throw new BusinessException(ExceptionInfo.SETTING_MONGODB_ERROR);
        }

        //保存MQ的相关处理
        Integer saveMQ = apiInfo.getSaveMQ();
        String mqAddress = apiInfo.getMqAddress();
        String mqUserName = apiInfo.getMqUserName();
        String mqPassword = apiInfo.getMqPasswd();
        String mqTopicName = apiInfo.getMqTopicName();

        if (CommonStatus.ENABLE == saveMQ && (StringUtils.isBlank(mqAddress) || StringUtils.isBlank(mqTopicName))) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MQ);
        }
        if (CommonStatus.DISENABLE == saveMQ && (StringUtils.isNotBlank(mqAddress) || StringUtils.isNotBlank(mqUserName) || StringUtils.isNotBlank(mqPassword) || StringUtils.isNotBlank(mqTopicName))) {
            throw new BusinessException(ExceptionInfo.SETTING_MQ_ERROR);
        }

        //接口2.0--------------------------下面-----------------------------------------
        //uniqueUuid
        String uniqueUuid = ShortUuidUtil.generateShortUuid();
        //主要上游接口名称（包括唯一性校验）
        String interfaceName = apiInfo.getInterfaceName();
        ApiQuery apiQuery2 = new ApiQuery();
        apiQuery2.setInterfaceName(interfaceName);
        if (!checkUniqueInterfaceName(apiQuery2)) {
            throw new BusinessException(ExceptionInfo.EXIST_INTERFACE_NAME);
        }

        //接口2.0--------------------------上面-----------------------------------------

        //保存apiInfo的配置
        apiInfo.setId(null);
        apiInfo.setApiName(apiName);
        apiInfo.setHttpPath(httpPath);
        apiInfo.setBackEndAddress(backEndAddress);
        apiInfo.setBackEndAddressB(backEndAddressB);
        apiInfo.setBackEndPath(backEndPath);
        apiInfo.setApiDescription(apiInfo.getApiDescription().replaceAll("\\s{2,}", " "));
        apiInfo.setPublishProductEnvStatus(CommonStatus.DISENABLE);
        apiInfo.setProductEnvVersion(null);
        apiInfo.setPublishTestEnvStatus(CommonStatus.DISENABLE);
        apiInfo.setTestEnvVersion(null);
        //接口2.0-------------下面---------------
        apiInfo.setUniqueUuid(uniqueUuid);
        apiInfo.setInterfaceName(interfaceName);
        //接口2.0-------------上面---------------
        apiInfo.setStatus(CommonStatus.ENABLE);
        Long createTime = System.currentTimeMillis();
        apiInfo.setCreateTime(createTime);
        apiInfo.setUpdateTime(createTime);
        apiInfoMapper.insert(apiInfo);

        //添加接口领域分类关联表数据 20200519
        /*int insert = apiCategoryRelationMapper.insert(new ApiCategoryRelation(apiInfo.getId(), apiInfo.getApiCategoryId()));
        if (insert != 1) {
            log.error("新增接口领域分类关联失败:接口{},分类{}", apiInfo.getId(), apiInfo.getApiCategoryId());
        }*/

        return apiInfo.getId();
    }

    /**
     * 验证上游接口名称全局唯一（api_info表和子上游api表）
     *
     * @param apiQuery
     * @return
     */
    @Override
    public boolean checkUniqueInterfaceName(ApiQuery apiQuery) {
        //查询条件为空，则返回false
        if (null == apiQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_QUERY);
        }

        String interfaceName = apiQuery.getInterfaceName();
        Integer apiId = apiQuery.getApiId();

        if (StringUtils.isBlank(interfaceName)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_INTERFACE_NAME);
        }

        ApiInfoExample example1 = new ApiInfoExample();
        ApiInfoExample.Criteria criteria1 = example1.createCriteria();

        criteria1.andInterfaceNameEqualTo(interfaceName);
        criteria1.andStatusEqualTo(CommonStatus.ENABLE);
        if (null != apiId) {
            criteria1.andIdNotEqualTo(apiId);
        }

        List<ApiInfoWithBLOBs> apiList = apiInfoMapper.selectByExampleWithBLOBs(example1);
        boolean partA = (null == apiList || apiList.size() == 0);

        ApiRateDistributeExample example2 = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andInterfaceNameEqualTo(interfaceName);
        criteria2.andStatusEqualTo(CommonStatus.ENABLE);
        Integer disId = apiQuery.getDisId();
        if (null != disId) {
            criteria2.andIdNotEqualTo(disId);
        }

        List<ApiRateDistribute> subApiList = subApiInfoMapper.selectByExample(example2);
        boolean partB = (null == subApiList || subApiList.size() == 0);
        return (partA && partB);
    }

    /**
     * 验证分组类内api名称唯一性
     *
     * @param apiQuery
     * @return
     */
    @Override
    public boolean checkUnique(ApiQuery apiQuery) {
        //查询条件为空，则返回false
        if (null == apiQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_QUERY);
        }
        String apiName = apiQuery.getApiName();
        Integer apiId = apiQuery.getApiId();
        if (StringUtils.isBlank(apiName)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APINAME);
        }
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        if (null != apiId) {
            criteria.andIdNotEqualTo(apiId);
        }
        criteria.andApiNameEqualTo(apiName);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> apiList = apiInfoMapper.selectByExampleWithBLOBs(example);
        return null == apiList || apiList.size() == 0;
    }

    @Override
    public void update(ApiInfoWithBLOBs apiInfo, List<RequestParams> requestParamsList,
                       List<BackendRequestParams> backendList) {
        //根据apiInfo的id查找数据库
        ApiInfoWithBLOBs tempApiInfo = this.findById(apiInfo.getId());
        //更新api_Info表
        this.update(apiInfo);
        //查询下游请求参数ID--用于修改子接口参数对应requestParamId
        List<Integer> oldRPIdList = this.getOldRequestParamsOfApi(tempApiInfo.getId());
        //操作下游请求参数表（修改、新增、逻辑删除）
        List<Integer> requestParamsIdList = this.updateRequestParams(tempApiInfo.getId(), requestParamsList);
        //操作上游请求参数表（修改、新增、逻辑删除）
        this.updateBackendRequest(tempApiInfo.getId(), backendList, requestParamsIdList);
        //修改子接口参数对应的requestParamId
        this.updateBackendDistributeParam(tempApiInfo.getId(),oldRPIdList,requestParamsIdList);

        //更新接口领域分类关联表数据 20200519
        /*ApiCategoryRelationExample apiCategoryRelationExample = new ApiCategoryRelationExample();
        apiCategoryRelationExample.createCriteria()
                .andApiIdEqualTo(apiInfo.getId())
                .andCategoryIdEqualTo(tempApiInfo.getApiCategoryId());
        int i = apiCategoryRelationMapper.updateByExampleSelective(new ApiCategoryRelation(apiInfo.getId(), apiInfo.getApiCategoryId()), apiCategoryRelationExample);
        if (i != 1) {
            log.error("接口:[{}]更新领域分类失败", apiInfo.getId());
        }*/
    }



	@Override
	public void updateApiParams(ApiInfoWithBLOBs apiInfo,List<RequestParams> requestParamsList, List<BackendDistributeParams> backendList) {
		//操作下游请求参数表（修改、新增、逻辑删除）
        List<Integer> requestParamsIdList = this.updateRequestParams(apiInfo.getId(), requestParamsList);
        //取出主子上游的参数--判断条件为disId为空
        List<BackendDistributeParams> backendDistributeParamsList = new ArrayList<BackendDistributeParams>();
        List<BackendRequestParams> backendRequestParamsList = new ArrayList<BackendRequestParams>();
        if (null != backendList && backendList.size() > 0) {
        	Iterator<BackendDistributeParams> iterator = backendList.iterator();
//        	BackendDistributeParams param = new BackendDistributeParams();
            BackendDistributeParams param;
        	while (iterator.hasNext()) {
        		BackendRequestParams bRParam = new BackendRequestParams();
        		param = iterator.next();
        		if (null == param.getDisId()) {
        			BeanCustomUtils.copyPropertiesIgnoreNull(param, bRParam);
        			backendRequestParamsList.add(bRParam);
        		} else {
        			backendDistributeParamsList.add(param);
        		}
        	}
        }
        //操作主上游请求参数表（修改、新增、逻辑删除）
        this.updateBackendRequest(apiInfo.getId(), backendRequestParamsList, requestParamsIdList);
        //操作子上游请求参数表（修改、新增、逻辑删除）
        this.updateBackendParams(apiInfo.getId(), backendDistributeParamsList, requestParamsIdList);
	}

    /**
     * 导出API文档时,指定web backend入口
     * @return 随机 webBackend地址
     */
    @Override
    public String getServerIpAndPort() {
        List<ServiceInstance> instances = discoveryClient.getInstances(selfServiceName);
        String url = ((EurekaDiscoveryClient.EurekaServiceInstance) instances.get(0)).getInstanceInfo().getHomePageUrl();
        return url.substring(0, url.length()-1);
    }

    @Override
    public int updateApiResponseTransParam(Integer apiId, String transParamJson) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", apiId);
        map.put("transParamJson", transParamJson);
        return apiInfoMapper.updateApiTransParamById(map);
    }

    public List<ApiRiskIndex> selectApiRiskIndexByApiId(Integer apiId) {
        Map<String, Object> params = new HashMap<>();
        params.put("apiId", apiId);
        return apiInfoMapper.selectApiRiskIndexByApiId(params);
    }

    /**
	 * 操作上游请求参数表（修改、新增、逻辑删除）
	 *
	 * @param apiId
	 * @param backendList
	 * @param requestParamsIdList
	 */
	private void updateBackendRequest(Integer apiId, List<BackendRequestParams> backendList,
	                                  List<Integer> requestParamsIdList) {
	    Iterator<BackendRequestParams> backendIterator = null;
	    //1.删除数据库之前的参数
	    BackendRequestParamsExample example = new BackendRequestParamsExample();
	    BackendRequestParamsExample.Criteria criteria = example.createCriteria();
	    criteria.andApiIdEqualTo(apiId);
	    BackendRequestParams backendParam = new BackendRequestParams();
	    backendParam.setStatus(CommonStatus.DISENABLE);
	    backendParam.setUpdateTime(System.currentTimeMillis());
	    backendMapper.updateByExampleSelective(backendParam, example);
	    //2.插入上游请求参数
	    if (null != backendList && backendList.size() > 0) {
	        Integer i = 0;
	        BackendRequestParams backendParams;
	        backendIterator = backendList.iterator();
	        while (backendIterator.hasNext()) {
	            backendParams = backendIterator.next();
	            backendParams.setApiId(apiId);
	            if (null != requestParamsIdList && requestParamsIdList.size() > 0) {
	                //当id仍有值且改后台参数为变量时，绑定下游参数ID
	                if (i < requestParamsIdList.size() && backendParams.getParamsType() == CommonStatus.VARIABLE) {
	                    backendParams.setRequestParamsId(requestParamsIdList.get(i++));
	                }
	            }
	            this.save(backendParams);
	        }
	    }
	    //----------------------------------------------可爱的分割线----------------------------------------------//
	}

	/**
	 * 更新API的子上游参数字段
	 * @param apiId
	 * @param backendList
	 * @param requestParamsIdList
	 * @Version 2.0
	 */
	private void updateBackendParams(Integer apiId, List<BackendDistributeParams> backendList,
			List<Integer> requestParamsIdList) {
		 //1.删除数据库之前的参数
		BackendDistributeParamsExample example = new BackendDistributeParamsExample();
		BackendDistributeParamsExample.Criteria criteria = example.createCriteria();
		criteria.andApiIdEqualTo(apiId);
		BackendDistributeParams param = new BackendDistributeParams();
		param.setStatus(CommonStatus.DISENABLE);
		param.setUpdateTime(System.currentTimeMillis());
		backendDistrbiteMapper.updateByExampleSelective(param, example);
		//2.按照disId进行区分
		if (null != backendList && backendList.size() > 0) {
			List<List<BackendDistributeParams>> totalList = new ArrayList<List<BackendDistributeParams>>();
			List<BackendDistributeParams> list = new ArrayList<BackendDistributeParams>();
			Integer disId = backendList.get(0).getDisId();
			for (BackendDistributeParams p : backendList) {
				if (disId.equals(p.getDisId())) {
					list.add(p);
				} else {
					disId = p.getDisId();
					totalList.add(list);
					list = new ArrayList<BackendDistributeParams>();
				}
			}
			totalList.add(list);
			//3.插入该API的所有子上游的参数
			for (List<BackendDistributeParams> l : totalList) {
				if (null != l && l.size() > 0) {
					Integer i = 0;
					BackendDistributeParams bDParam;
					Iterator<BackendDistributeParams> iterator = l.iterator();
					while (iterator.hasNext()) {
						bDParam = iterator.next();
						bDParam.setApiId(apiId);
						if (null != requestParamsIdList && requestParamsIdList.size() > 0) {
							//当id仍有值且改后台参数为变量时，绑定下游参数ID
							if (i < requestParamsIdList.size() && bDParam.getParamsType() == CommonStatus.VARIABLE) {
								bDParam.setRequestParamsId(requestParamsIdList.get(i++));
							}
						}
						subApiService.saveBackendParams(bDParam);
					}

				}
			}
		}
	}


	/**
	 * 修改子接口参数对应的requestParamId
	 * @param apiId
	 * @param oldRPIdList
	 * @param requestParamsIdList
	 * @Version 2.0
	 */
	private void updateBackendDistributeParam(Integer apiId, List<Integer> oldRPIdList, List<Integer> requestParamsIdList) {
		if (null != oldRPIdList && oldRPIdList.size() > 0) {
			if (oldRPIdList.size() != requestParamsIdList.size()) {
				throw new BusinessException(ExceptionInfo.NUM_OF_API_PARAM_ERROR);
			}
			for (int i = 0; i < oldRPIdList.size(); i++) {
				BackendDistributeParamsExample example = new BackendDistributeParamsExample();
				BackendDistributeParamsExample.Criteria criteria = example.createCriteria();
				criteria.andApiIdEqualTo(apiId);
				criteria.andStatusEqualTo(CommonStatus.ENABLE);
				criteria.andRequestParamsIdEqualTo(oldRPIdList.get(i));
				BackendDistributeParams param = new BackendDistributeParams();
				param.setRequestParamsId(requestParamsIdList.get(i));
				backendDistrbiteMapper.updateByExampleSelective(param, example);
			}
		}
	}

	/**
     * 获取该API修改更新前的参数ID
     * @param id
     * @return
     * @Version 2.0
     */
    private List<Integer> getOldRequestParamsOfApi(Integer id) {
    	RequestParamsExample example = new RequestParamsExample();
        RequestParamsExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(id);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<RequestParams> oldRPList = requestParamsMapper.selectByExample(example);
        List<Integer> oldRPIdList = new ArrayList<Integer>();
        for (RequestParams param : oldRPList) {
        	oldRPIdList.add(param.getId());
        }
		return oldRPIdList;
	}

	/**
     * 三方结果的参数抽取与转换 更新
     *
     * @param apiId
     * @param apiResultEtlList
     */
    private void updateApiResultEtlList(Integer apiId, List<ApiResultEtl> apiResultEtlList) {

        //2.删除编辑库表之前的参数
        ApiResultEtlExample example = new ApiResultEtlExample();
        ApiResultEtlExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        ApiResultEtl param = new ApiResultEtl();
        param.setStatus(CommonStatus.DISENABLE);
        param.setUpdateTime(System.currentTimeMillis());
        apiResultEtlMapper.updateByExampleSelective(param, example);

        if (CollectionUtil.isNotEmpty(apiResultEtlList)) {
            for (ApiResultEtl s : apiResultEtlList) {
                s.setApiId(apiId);
                this.save(s);
            }
        }

    }

    /**
     * 通过id查询上游请求参数
     *
     * @param id
     * @return
     */
    private BackendRequestParams findBackendParamsById(Integer id) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_PARAMS_ID);
        }
        return backendMapper.selectByPrimaryKey(id);
    }

    /**
     * 操作下游请求参数表（修改、新增、逻辑删除）
     *
     * @param apiId
     * @param requestParamsList
     */
    private List<Integer> updateRequestParams(Integer apiId, List<RequestParams> requestParamsList) {
        Iterator<RequestParams> iterator = null;
        //1.删除编辑库表之前的参数
        RequestParamsExample example = new RequestParamsExample();
        RequestParamsExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        RequestParams requestParam = new RequestParams();
        requestParam.setStatus(CommonStatus.DISENABLE);
        requestParam.setUpdateTime(System.currentTimeMillis());
        requestParamsMapper.updateByExampleSelective(requestParam, example);
        //2.保存下游请求参数
        List<Integer> requestParamsIdList = null;
        if (null != requestParamsList && requestParamsList.size() > 0) {
            requestParamsIdList = new ArrayList<Integer>();
            RequestParams requestParams;
            iterator = requestParamsList.iterator();
            while (iterator.hasNext()) {
                requestParams = iterator.next();
                requestParams.setApiId(apiId);
                Integer requestParamsId = this.save(requestParams);
                requestParamsIdList.add(requestParamsId);
            }
        }

        //----------------------------------------------------可爱的分割线----------------------------------------
        return requestParamsIdList;
    }

    /**
     * 根据id查找下游请求参数数据
     *
     * @param id
     * @return
     */
    private RequestParams findRequestParamsById(Integer id) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_REQUEST_PARAMS_ID);
        }
        return requestParamsMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据ID查找apiInfo
     *
     * @param id
     * @return ApiInfoWithBLOBs
     */
    @Override
    public ApiInfoWithBLOBs findById(Integer id) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        }
        return apiInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新apiInfo表
     *
     * @param apiInfo
     */
    private void update(ApiInfoWithBLOBs apiInfo) {
        //参数校验(返回类型和返回示例校验)
        ApiValidate.callBackCheck(apiInfo);
        //根据apiInfo的id查找数据库
        ApiInfoWithBLOBs oldApiInfo = this.findById(apiInfo.getId());
        //判断该api是否仍存在（防止api已被删除）
        if (null == oldApiInfo || CommonStatus.DISENABLE == oldApiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }

        //检查允许修改项

        //1.检查修改项apiName(检验apiName组内是否唯一)
        ApiQuery apiQuery = new ApiQuery();
        String apiName = apiInfo.getApiName().replaceAll(" ", "");
        Integer apiGroupId = oldApiInfo.getApiGroupId();
        Integer apiId = apiInfo.getId();

        apiQuery.setApiName(apiName);
        apiQuery.setApiGroupId(apiGroupId);
        apiQuery.setApiId(apiId);
        if (!this.checkUnique(apiQuery)) {
            throw new BusinessException(ExceptionInfo.EXIST_APINAME);
        }

        //2.全局检查httpPath（对外提供地址）
        String repeatName2 = this.httpPathUnique(apiInfo);
        if (StringUtils.isNotBlank(repeatName2)) {
            throw new BusinessException(StringUtils.join(repeatName2, ExceptionInfo.IS_REPEAT));
        }

        //3.检查Mock数据
        Integer isMock = apiInfo.getIsMock();
        String mockData = apiInfo.getMockData();
        if (CommonStatus.ENABLE == isMock && StringUtils.isBlank(mockData)) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MOCK_DATA);
        }

        //4.保存mongodb数据的相关处理
        Integer saveMongoDB = apiInfo.getSaveMongoDB();
        String mongodbURI = apiInfo.getMongodbURI();
        String dbName = apiInfo.getMongodbDBName();
        String collectionName = apiInfo.getMongodbCollectionName();

        if (CommonStatus.ENABLE == saveMongoDB && (StringUtils.isBlank(mongodbURI) || StringUtils.isBlank(collectionName) || StringUtils.isBlank(dbName))) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MONGODB);
        }

        //5.保存MQ的相关处理
        Integer saveMQ = apiInfo.getSaveMQ();
        String mqAddress = apiInfo.getMqAddress();
        String mqTopicName = apiInfo.getMqTopicName();

        if (CommonStatus.ENABLE == saveMQ && (StringUtils.isBlank(mqAddress) || StringUtils.isBlank(mqTopicName))) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MQ);
        }

        //接口2.0---------------------------------下面-----------------------------------------
        //6.上游接口名称的（interfaceName）唯一性检验
        String interfaceName = apiInfo.getInterfaceName();
        ApiQuery apiQuery2 = new ApiQuery();
        apiQuery2.setInterfaceName(interfaceName);
        apiQuery2.setApiId(apiId);
        if (!checkUniqueInterfaceName(apiQuery2)) {
            throw new BusinessException(ExceptionInfo.EXIST_INTERFACE_NAME);
        }

        //7.修改apiGroup的关系表
        //①有分组->有分组（分组变化）
        if (null != oldApiInfo.getApiGroupId() && null != apiInfo.getApiGroupId()) {
            if (!oldApiInfo.getApiGroupId().equals(apiInfo.getApiGroupId())) {
                this.physicallyDeleteRelation(oldApiInfo.getApiGroupId(), apiId, null);
                //接口2.0--新建api与分组的关系表
                this.addRelationOfApiAndGroup(apiId, apiInfo.getApiGroupId());
            }
        } else if (null == oldApiInfo.getApiGroupId() && null != apiInfo.getApiGroupId()) {//②无分组->有分组
            //接口2.0--新建api与分组的关系表
            this.addRelationOfApiAndGroup(apiId, apiInfo.getApiGroupId());
        } else if (null != oldApiInfo.getApiGroupId() && null == apiInfo.getApiGroupId()) {//③有分组->无分组
            this.physicallyDeleteRelation(oldApiInfo.getApiGroupId(), apiId, null);
        }

        //接口2.0---------------------------------上面-----------------------------------------
        //更新数据库表api_Info
        apiInfo.setUpdateTime(System.currentTimeMillis());

        apiInfoMapper.updateByPrimaryKeyWithBLOBs(apiInfo);
    }

    @Override
    public void offline(ApiExpand apiExpand) {
        //参数校验
        ApiValidate.offlineCheck(apiExpand);
        //从数据库取出该条数据
        ApiInfoWithBLOBs oldApiInfo = this.findById(apiExpand.getId());
        //判断该api是否仍存在（防止api已被删除）
        if (null == oldApiInfo || CommonStatus.DISENABLE == oldApiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        //修改oldApiInfo表（对应环境版本信息置空，状态为不可用）
        String versionId = null;
        //线上环境--下线
        if (CommonStatus.ONLINE == apiExpand.getEnv() && CommonStatus.ENABLE == oldApiInfo.getPublishProductEnvStatus()) {
            oldApiInfo.setPublishProductEnvStatus(CommonStatus.DISENABLE);
            versionId = oldApiInfo.getProductEnvVersion();
            oldApiInfo.setProductEnvVersion(null);
        }
        //预发（测试）环境--下线
//        else if (CommonStatus.ADVANCE == apiExpand.getEnv() && CommonStatus.ENABLE == oldApiInfo.getPublishTestEnvStatus()) {
//            oldApiInfo.setPublishTestEnvStatus(CommonStatus.DISENABLE);
//            versionId = oldApiInfo.getTestEnvVersion();
//            oldApiInfo.setTestEnvVersion(null);
//        }
//        else {
//            throw new BusinessException(ExceptionInfo.OPERATION_IS_INVALID);
//        }
        oldApiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.updateByPrimaryKeyWithBLOBs(oldApiInfo);

        //修改对应版本表
        ApiInfoVersionsWithBLOBs apiVersions = new ApiInfoVersionsWithBLOBs();
        apiVersions.setCurrentVersion(CommonStatus.DISENABLE);

        ApiInfoVersionsExample versionsExample = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria versionsCriteria = versionsExample.createCriteria();
        versionsCriteria.andApiIdEqualTo(apiExpand.getId());
        versionsCriteria.andEnvEqualTo(apiExpand.getEnv());
        versionsCriteria.andVersionIdEqualTo(versionId);
        //update apiInfoVesion
        apiInfoVersionsMapper.updateByExampleSelective(apiVersions, versionsExample);

        //查询被更新记录，更新到redis-httpPath
        List<ApiInfoVersionsWithBLOBs> list = apiInfoVersionsMapper.selectByExampleWithBLOBs(versionsExample);
        if (list!=null){
            redisService.delRedisHttpPathCache(list.get(0));
        }
        //下线，更新latest接口状态
        apiInfoVersionsLatestMapper.updateByExampleSelective(apiVersions, versionsExample);
        //更新redis-api
//        List<ApiInfoVersionsWithBLOBs> list = apiInfoVersionsLatestMapper.selectByExampleWithBLOBs(versionsExample);
//        if (list !=null) {
//            com.alibaba.fastjson.JSONObject jsonObject = redisService.get("api:" + apiExpand.getId());
//            if (jsonObject != null) {
//                ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
//                apiInfoRedisMsg.setApiInfoWithBLOBs(list.get(0));
//                redisService.setToCaches("api:" + apiExpand.getId(),apiInfoRedisMsg);
//            }
//        }
        redisService.delete("api:" + apiExpand.getId());

        // wt 修改参数 同时下线所关联的组合接口
        modifyCombinationParamService.updateCombInfWithStatus(apiExpand.getId(),3);
        LOG.info("接口id:{}，名称:{}下线成功",apiExpand.getId(),apiExpand.getApiName());

        // 保存下线记录
        Integer id=apiExpand.getId();
        Integer groupId=apiExpand.getApiGroupId();
        ApiOfflineRecord apiOfflineRecord= new ApiOfflineRecord();
        apiOfflineRecord.setApiId(id);
        apiOfflineRecord.setApiGroupId(groupId);
        apiOfflineRecord.setRemarks(apiExpand.getRemarks());
        apiOfflineRecord.setCreateTime(System.currentTimeMillis());
        apiOfflineRecordMapper.insert(apiOfflineRecord);
    }

    /**
     * 循环List，将域名包装到apiExpand
     *
     * @param apiInfoList
     * @return
     */
    private List<ApiExpand> supply(List<ApiInfoWithBLOBs> apiInfoList) {
        if (null == apiInfoList || apiInfoList.size() == 0) {
            return Collections.emptyList();
        }
        List<ApiExpand> apiExpandList = new ArrayList<>();
        ApiInfoWithBLOBs apiInfo;
        Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
        while (iterator.hasNext()) {
            apiInfo = iterator.next();
            apiExpandList.add(this.supply(apiInfo));
        }
        return apiExpandList;
    }

    /**
     * 对每一个apiInfo进行包装,拼装url = 分组域名 + httpPath
     *
     * @param apiInfo
     * @return
     */
    private ApiExpand supply(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            return null;
        }
        ApiExpand apiExpand = new ApiExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfo, apiExpand);
        apiExpand.setUrl(StringUtils.join(apiGroupService.findById(apiInfo.getApiGroupId()).getGroupDomainName(), apiInfo.getHttpPath()));
        return apiExpand;
    }

    /**
     * 返回所有status为正常的api
     */
    private List<ApiInfoWithBLOBs> findList() {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();

        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        return apiInfoMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public Map<String, Object> findAllByApiId(ApiInfoWithBLOBs apiInfo) {
        //参数校验
        ApiValidate.apiIdCheck(apiInfo);
        Integer apiId = apiInfo.getId();
        //查询apiInfo表
        ApiInfoWithBLOBs tempApiInfo = this.findById(apiId);
        ApiExpand apiExpand = new ApiExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(tempApiInfo, apiExpand);
        if (null != apiExpand.getApiGroupId()) {
            //根据GroupId查找分组信息
            ApiGroup apiGroup = apiGroupService.findById(apiExpand.getApiGroupId());
            apiExpand.setGroupName(apiGroup.getGroupName());
            apiExpand.setGroupDomainName(apiGroup.getGroupDomainName());
            apiExpand.setUrl(StringUtils.join(apiGroup.getGroupDomainName(), tempApiInfo.getHttpPath()));
        }
        //判断该api是否仍存在（防止api已被删除）
        if (null == tempApiInfo || CommonStatus.DISENABLE == tempApiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("apiInfo", apiExpand);
        //查找request_params表
        List<RequestParams> requestParamsList = this.findRequestParamsListByApiIdAndStatus(apiId);
        if (null == requestParamsList || requestParamsList.size() == 0) {
            requestParamsList = Collections.emptyList();
        }
        map.put("requestParamsList", requestParamsList);
        //查找backend_request_params表
        List<BackendRequestParams> backendList = this.findBackendParamsByApiIdAndStatus(apiId);
        if (null == backendList || backendList.size() == 0) {
            backendList = Collections.emptyList();
        }
        map.put("backendList", backendList);
        //查找api_result_etl
        List<ApiResultEtl> apiResultEtlList = this.findApiResultEtlByApiIdAndStatus(apiId);
        if (null == apiResultEtlList || apiResultEtlList.size() == 0) {
            apiResultEtlList = Collections.emptyList();
        }
        map.put("apiResultEtlList", apiResultEtlList);
        //查找子接口
        List<ApiRateDistributeWithBLOBs> subApiList = subApiService.querySubApiListByApiId(apiId);
        if (null == subApiList || subApiList.size() == 0) {
            subApiList = Collections.emptyList();
        }
        map.put("subApiList", subApiList);
        return map;
    }

    @Override
	public Map<String, Object> getApiParams(ApiInfoWithBLOBs apiInfo) {
    	//参数校验
        ApiValidate.apiIdCheck(apiInfo);
        Integer apiId = apiInfo.getId();

        ApiInfoWithBLOBs tempApiInfo = this.findById(apiId);
        //判断该api是否仍存在（防止api已被删除）
        if (null == tempApiInfo || CommonStatus.DISENABLE == tempApiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }

        Map<String, Object> map = new HashMap<>();
        //API名称
        map.put("apiName", tempApiInfo.getApiName());
        //新增上游接口名称列表
        //查找request_params
        List<RequestParams> requestParamsList = this.findRequestParamsListByApiIdAndStatus(apiId);
        if (null == requestParamsList || requestParamsList.size() == 0) {
            requestParamsList = Collections.emptyList();
        }
        map.put("requestParamsList", requestParamsList);

        //参照backend_distribute_params,处理子上游
        //返回结构
        List<InterfaceParams> backendParamsList = new ArrayList<InterfaceParams>();
        //获取所有子上游基础信息，通过子上游IdL来查找对应参数列表--初步读库结构
        List<ApiRateDistribute> subApiList = this.getUpstreamInterfaceNameList(apiId);
        //封装interfaceName结构
        List<BackendParamsExpand> subParamsList = new ArrayList<BackendParamsExpand>();
        //遍历多个子上游
        if (null != subApiList && subApiList.size() > 0) {
        	for (ApiRateDistribute api : subApiList) {
        		List<BackendDistributeParams> bpList = this.findBackendDistributeParamsByApiIdAndStatus(apiId,api.getId());
        		InterfaceParams interfaceParams = new InterfaceParams();
                if (null == bpList || bpList.size() == 0) {
                	bpList = Collections.emptyList();
                } else {
                	subParamsList = this.packageInterfaceName(bpList);
                }
                interfaceParams.setParamList(subParamsList);
                interfaceParams.setInterfaceName(api.getInterfaceName());
                interfaceParams.setDisId(api.getId());
                backendParamsList.add(interfaceParams);
        	}
        }
        //查找backend_request_params
        //读库内容初步结构
        List<BackendRequestParams> backendList = this.findBackendParamsByApiIdAndStatus(apiId);
        //封装接口名称结构
        List<BackendParamsExpand> backendExpandList = new ArrayList<BackendParamsExpand>();
        //返回后端参数的单个对象结构
        InterfaceParams basicInterfaceParam = new InterfaceParams();
        if (backendList != null && backendList.size() != 0) {
        	for (BackendRequestParams param : backendList) {
        		BackendParamsExpand distributeParamExpand = new BackendParamsExpand();
        		BeanCustomUtils.copyPropertiesIgnoreNull(param, distributeParamExpand);
        		distributeParamExpand = this.packageInterfaceName(distributeParamExpand);
        		backendExpandList.add(distributeParamExpand);
        	}
        }
        basicInterfaceParam.setParamList(backendExpandList);
        basicInterfaceParam.setInterfaceName(tempApiInfo.getInterfaceName());
        backendParamsList.add(basicInterfaceParam);
        map.put("backendParamsList", backendParamsList);
		return map;
	}
    /**
     * 根据ID查找apiInfoVersionsLatest
     *
     * @param id
     * @return ApiInfoWithBLOBs
     */
    @Override
    public Integer findApiInfoVersionsLatestById(Integer id) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        }
        return apiInfoVersionsLatestMapper.selectByApiId(id);
    }
    /**
     * 根据requestParamsId查找requestParamsVersionsLatest
     *
     * @param apiId
     * @return ApiInfoWithBLOBs
     */
    @Override
    public Integer findRequestParamsVersionsLatestById(Integer apiId) {
        if (null == apiId) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        }
        return requestParamsVersionsLatestMapper.selectByPrimaryKey(apiId);
    }
	/**
	 * 目前仅包含子上游接口名称，主上游不包括
	 * @param apiId
	 * @return
	 * @Version 2.0
	 */
	private List<ApiRateDistribute> getUpstreamInterfaceNameList(Integer apiId) {
		ApiRateDistributeExample example = new ApiRateDistributeExample();
		ApiRateDistributeExample.Criteria criteria = example.createCriteria();
		criteria.andApiIdEqualTo(apiId);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		List<ApiRateDistribute> list = subApiInfoMapper.selectByExample(example);
//		if (null != list && list.size() > 0) {
//			for (ApiRateDistribute subApi : list) {
//				interfaceNameList.add(subApi.getInterfaceName());
//			}
//		}
		return list;
	}

	/**
	 * 包装上游接口名称
	 * @param bpList
	 * @return
	 * @Version 2.0
	 */
	private List<BackendParamsExpand> packageInterfaceName(List<BackendDistributeParams> bpList) {
		if (null == bpList || bpList.size() == 0) {
			return Collections.emptyList();
		}
		List<BackendParamsExpand> list = new ArrayList<BackendParamsExpand>();
		Iterator<BackendDistributeParams> iterator = bpList.iterator();
		while (iterator.hasNext()) {
			list.add(this.packageInterfaceName(iterator.next()));
		}
		return list;
	}

	/**
	 * 包装上游接口名称(具体一条)
	 * @param param
	 * @return
	 * @Version 2.0
	 */
	private BackendParamsExpand packageInterfaceName(BackendDistributeParams param) {
		if(null  == param) {
			return null;
		}
		int apiId = param.getApiId();
		Integer disId = param.getDisId();
		BackendParamsExpand paramExpand = new BackendParamsExpand();
		BeanCustomUtils.copyPropertiesIgnoreNull(param, paramExpand);
		//主子上游接口
		if (null == disId) {
			paramExpand.setInterfaceName(this.findById(apiId).getInterfaceName());
		} else {
			paramExpand.setInterfaceName(this.findApiRateDistributeByDisId(disId).getInterfaceName());
		}
		return paramExpand;
	}

	private ApiRateDistribute findApiRateDistributeByDisId(Integer disId) {
		ApiRateDistributeExample example = new ApiRateDistributeExample();
		ApiRateDistributeExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(disId);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		return subApiInfoMapper.selectByExample(example).get(0);
	}

	/**
	 * 通过Apiid获取所有子上游的上游请求参数（status == 1）
	 * @param apiId
	 * @return
	 * @Version 2.0
	 */
	private List<BackendDistributeParams> findBackendDistributeParamsByApiIdAndStatus(Integer apiId,Integer disId) {
		BackendDistributeParamsExample example = new BackendDistributeParamsExample();
		BackendDistributeParamsExample.Criteria criteria = example.createCriteria();
		criteria.andApiIdEqualTo(apiId);
		criteria.andDisIdEqualTo(disId);
		criteria.andStatusEqualTo(CommonStatus.ENABLE);
		return backendDistrbiteMapper.selectByExample(example);
	}

	@Override
    public Map<String, Object> findApiInfoVersion(ApiInfoVersionsWithBLOBs apiInfoVersion) {
        //参数校验
        Integer id = apiInfoVersion.getId();
        String versionId = apiInfoVersion.getVersionId();
        if (null == id && StringUtils.isBlank(versionId)) {
            throw new DataValidationException(ExceptionInfo.PARAMS_ERROR);
        }
        //获取apiInfoVersions信息
        ApiInfoVersionsWithBLOBs tempApiInfoVersion = null;
        if (StringUtils.isNotBlank(versionId)) {
            tempApiInfoVersion = this.findApiInfoVersionsByVersionId(versionId);
        } else {
            tempApiInfoVersion = this.findApiInfoVersionsById(id);
        }
        if (null == tempApiInfoVersion) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
        }

        versionId = tempApiInfoVersion.getVersionId();

        ApiVersionExpand apiVersionExpand = new ApiVersionExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(tempApiInfoVersion, apiVersionExpand);
        if (null != tempApiInfoVersion.getApiGroupId()) {
            ApiGroup temp = apiGroupService.findById(tempApiInfoVersion.getApiGroupId());
            apiVersionExpand.setGroupName(temp.getGroupName());
            apiVersionExpand.setUrl(StringUtils.join(temp.getGroupDomainName(), apiVersionExpand.getHttpPath()));
        }
        Map<String, Object> map = new HashMap<>();
        //装载apiInfoVersions表的信息
        map.put("apiInfoVersion", apiVersionExpand);

        //获取request_params_version信息
        List<RequestParamsVersions> requestParamsVersionsList = this.findRequestParamsVersionsListByVersionId(versionId);
        if (null == requestParamsVersionsList || requestParamsVersionsList.size() == 0) {
            requestParamsVersionsList = Collections.emptyList();
        }
        //装载request_params_version信息
        map.put("requestParamsVersionsList", requestParamsVersionsList);

        //获取backend_request_params_version信息
        List<BackendRequestParamsVersions> backendVersionsList = this.findBackendParamsVersionsListByVersionId(versionId);
        if (null == backendVersionsList || backendVersionsList.size() == 0) {
            backendVersionsList = Collections.emptyList();
        }
        //装载backend_request_params_version信息
        map.put("backendVersionsList", backendVersionsList);

        return map;
    }

    /**
     * 通过版本号查询apiInfoVersion
     *
     * @param versionId
     * @return
     */
    private ApiInfoVersionsWithBLOBs findApiInfoVersionsByVersionId(String versionId) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        List<ApiInfoVersionsWithBLOBs> list = apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        if (null == list || list.size() == 0) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
        }
        return list.get(0);
    }

    /**
     * 通过apiId三方结果的参数抽取与转换（默认status为正常）
     *
     * @param apiId
     * @return
     */
    private List<ApiResultEtl> findApiResultEtlByApiIdAndStatus(Integer apiId) {
        ApiResultEtlExample example = new ApiResultEtlExample();
        ApiResultEtlExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        return apiResultEtlMapper.selectByExample(example);
    }

    /**
     * 通过apiId查找上游请求参数（默认status为正常）
     *
     * @param apiId
     * @return
     */
    private List<BackendRequestParams> findBackendParamsByApiIdAndStatus(Integer apiId) {
        BackendRequestParamsExample example = new BackendRequestParamsExample();
        BackendRequestParamsExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        example.setOrderByClause("paramsLocation, id asc");
        return backendMapper.selectByExample(example);
    }

    /**
     * 通过apiId查找下游请求参数（默认status为正常）
     *
     * @param apiId
     * @return
     */
    private List<RequestParams> findRequestParamsListByApiIdAndStatus(Integer apiId) {
        RequestParamsExample example = new RequestParamsExample();
        RequestParamsExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        example.setOrderByClause("paramsLocation, id asc");
        return requestParamsMapper.selectByExample(example);
    }

    @Override
    public String delete(ApiInfoWithBLOBs apiInfo) {
        //参数校验
        ApiValidate.apiIdCheck(apiInfo);
        ApiInfoWithBLOBs oldApiInfo = this.findById(apiInfo.getId());
        //判断该api是否仍存在（防止api已被删除）
        if (null == oldApiInfo || CommonStatus.DISENABLE == oldApiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        //判断该api下是否有正在运行的版本
        if (CommonStatus.ENABLE == oldApiInfo.getPublishProductEnvStatus() || CommonStatus.ENABLE == oldApiInfo.getPublishTestEnvStatus()) {
            throw new BusinessException(ExceptionInfo.DELETE_API_FAILED);
        }
        // 检查该API 是否有绑定合约,如有绑定合约,不可删除API
        /*CommonResult<Boolean> result = microChargeService.checkApiAuth(apiInfo.getId());
        if (ResultStatusEnum.SUCCESS.getCode() == result.getCode()) {
            if (result.getData())
                throw new BusinessException("该接口已绑定合约,不可删除");
        }*/
        //接口2.0-删除API则置空groupId
        oldApiInfo.setApiGroupId(null);
        oldApiInfo.setStatus(CommonStatus.DISENABLE);
        oldApiInfo.setUpdateTime(System.currentTimeMillis());
        Integer isSuccess = apiInfoMapper.updateByPrimaryKey(oldApiInfo);
        if (isSuccess == 0) {
            throw new BusinessException(ExceptionInfo.SQL_DB_EXCEPTION);
        }
        //修改api下的状态参数和返回码状态
        Integer apiId = apiInfo.getId();
        this.updateRequestParamsStatus(apiId);
        this.updateBackendParamsStatus(apiId);
        //逻辑删除api下的其他子api（包括参数在内）
        this.deleteSubApiByApiId(apiId);
        //删除api与app之间的联系
        this.deleteAutorizationByApiId(apiInfo.getId());
        if (null != apiInfo.getApiGroupId()) {
            //接口2.0-删除api与分组的关系
            this.physicallyDeleteRelation(apiInfo.getApiGroupId(), apiInfo.getId(), null);
        }

        //删除接口后，删除latest表中的对应接口
        apiInfoVersionsLatestMapper.deleteByPrimaryKey(apiInfo.getId());
        //删除接口后，删除latest表中的对应参数
        requestParamsVersionsLatestMapper.deleteByPrimaryKey(apiInfo.getId());
        redisService.delete("api:"+apiInfo.getId());
        log.info("删除redis缓存,key = {}","api:"+apiInfo.getId());

        //删除redis-httpPath缓存
        redisService.delRedisCacheByHttpPath(apiInfo.getHttpPath());

        // wt 修改参数同时删除所关联的组合接口信息
        modifyCombinationParamService.updateCombInfWithStatus(apiId,CommonStatus.DISENABLE);

        //删除合约
        authService.deleteByApiId(apiId);

        //删除接口领域分类关联表数据 20200519
        /*ApiCategoryRelationExample apiCategoryRelationExample = new ApiCategoryRelationExample();
        ApiCategoryRelationExample.Criteria criteria = apiCategoryRelationExample.createCriteria();
        criteria.andApiIdEqualTo(apiId).andCategoryIdEqualTo(oldApiInfo.getApiCategoryId());
        int i = apiCategoryRelationMapper.deleteByExample(apiCategoryRelationExample);
        if (i != 1) {
            log.error("删除关联的分类表数据失败,接口为: {}, 分类为: {}", apiId, oldApiInfo.getApiCategoryId());
        }*/
        return "删除成功";
    }

    /**
     * 通过APIID删除子api
     * @param apiId
     * @Version 2.0
     */
    private void deleteSubApiByApiId(Integer apiId) {
    	ApiRateDistributeExample example = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiRateDistribute> list = subApiInfoMapper.selectByExample(example);
        if (null != list && list.size() > 0) {
	        for (int i = 0; i <list.size(); i++) {
	        	subApiService.del(list.get(i));
	        }
        }
	}

	/**
     * 通过apiId和groupId锁定api
     *
     * @param id
     * @param groupId
     * @return
     */
    private ApiInfoWithBLOBs findByIdAndGroupId(Integer id, Integer groupId) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        }
        if (null == groupId) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
        }
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andApiGroupIdEqualTo(groupId);
        List<ApiInfoWithBLOBs> apiInfoList = apiInfoMapper.selectByExampleWithBLOBs(example);
        if (null == apiInfoList || apiInfoList.size() == 0) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        return apiInfoList.get(0);
    }

    @Override
    public void deleteAutorizationByApiId(Integer apiId) {
        ApiAppRelationExample example = new ApiAppRelationExample();
        ApiAppRelationExample.Criteria criteria = example.createCriteria();

        criteria.andApiIdEqualTo(apiId);

        ApiAppRelation apiAppRelation = new ApiAppRelation();
        apiAppRelation.setStatus(CommonStatus.DISENABLE);
        apiAppRelation.setUpdateTime(System.currentTimeMillis());
        apiAppRelationMapper.updateByExampleSelective(apiAppRelation, example);
    }

    @Override
    public List<ApiInfoWithBLOBs> findByGroupIdAndStatus(Integer id) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
        }
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();

        criteria.andApiGroupIdEqualTo(id);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        return apiInfoMapper.selectByExampleWithBLOBs(example);
    }
    @Transactional
    @Override
    public void reswitchVersion(ApiInfoVersionsWithBLOBs apiInfoVersion) {
        //参数校验
        ApiValidate.apiIdCheck(apiInfoVersion);
//        ApiValidate.apiGroupIdCheck(apiInfoVersion);
        if (StringUtils.isBlank(apiInfoVersion.getPubDescription())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PUBDESCRIPTION);
        }
        //获取apiInfoVersion表内容
        ApiInfoVersionsWithBLOBs oldApiInfoVersion = null;
        oldApiInfoVersion = this.findApiInfoVersionsById(apiInfoVersion.getId());//根据切换版本的id查找是否存在于apiInfoVersion表

        if (null == oldApiInfoVersion) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS); //判断版本是否存在
        }
        if (CommonStatus.ENABLE == oldApiInfoVersion.getCurrentVersion()) {
            throw new BusinessException(ExceptionInfo.ALREADY_IN_USE);//判断版本是否正在使用中
        }
        //版本号
        String versionId = oldApiInfoVersion.getVersionId();
        //运行环境：线上or预发
        Integer env = oldApiInfoVersion.getEnv();
        //apiId
        Integer apiId = oldApiInfoVersion.getApiId();

        //更新apiInfo表
        ApiInfoWithBLOBs apiInfo = this.findById(apiId);
        if (null == apiInfo || CommonStatus.DISENABLE == apiInfo.getStatus()) {//判断apiInfo是否存在
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        //判断该API是否已发布相应环境--2.0需求变化：未发布也能切换。
//        if ((CommonStatus.ONLINE == env && apiInfo.getPublishProductEnvStatus() == CommonStatus.DISENABLE) ||
//                (CommonStatus.ADVANCE == env && apiInfo.getPublishTestEnvStatus() == CommonStatus.DISENABLE)) {
//            throw new BusinessException(ExceptionInfo.API_IS_UNPUBLISHED);
//        }
        String oldVersionId = null;
        ApiInfoWithBLOBs apiInfoUpdate =new ApiInfoWithBLOBs();//用于更新apiInfo参数
        RequestParamsVersionsLatest requestParamsVersionsLatest;
        //更新apiInfo的publishProductEnvStatus，productEnvVersion，publishTestEnvStatus，testEnvVersion字段
        if (CommonStatus.ONLINE == env) {
            oldVersionId = apiInfo.getProductEnvVersion();
            apiInfo.setProductEnvVersion(versionId);
            apiInfo.setPublishProductEnvStatus(CommonStatus.ENABLE);
        } else {
//            oldVersionId = apiInfo.getTestEnvVersion();
//            apiInfo.setTestEnvVersion(versionId);
//            apiInfo.setPublishTestEnvStatus(CommonStatus.ENABLE);
            LOG.error("env参数错误");
        }
        apiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.updateVersionIdByApiId(apiInfo);
        //  更新apiInfoVersion和redis-httpPath
        if (null != oldVersionId && versionId !=null) {
            reswitchUpdateApiInfoVersion(oldVersionId,CommonStatus.DISENABLE);//修改oldVersionId版本的状态currentVersion=2，设置status=2表示不可用
            reswitchUpdateApiInfoVersion(versionId,CommonStatus.ENABLE);//修改versinonId版本的状态currentVersion=1，设置status=1表示可用

            //获取versionId版本的数据
            ApiInfoVersionsExample example = getExampleByVersionIdAndStatus(versionId,CommonStatus.ENABLE);
            List<ApiInfoVersionsWithBLOBs> apiInfoVersionsList= apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
            if (apiInfoVersionsList == null || apiInfoVersionsList.size() == 0){
                throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
            }
            ApiInfoVersionsWithBLOBs apiInfoVersions = apiInfoVersionsList.get(0);//获取versionid接口版本信息
//            apiInfoUpdate = CopyApiInfoVersions(apiInfoVersions,apiInfo.getApiGroupId());
            BeanCustomUtils.copyPropertiesIgnoreNull(apiInfoVersions, apiInfoUpdate);
            apiInfoUpdate.setApiGroupId(apiInfo.getApiGroupId());
            apiInfoUpdate.setStatus(CommonStatus.ENABLE);
            apiInfoUpdate.setUpdateTime(System.currentTimeMillis());
            apiInfoUpdate.setId(apiInfoVersions.getApiId());
            //更新apiInfo表（除publishProductEnvStatus，productEnvVersion，publishTestEnvStatus，testEnvVersion所有字段）
            apiInfoMapper.updatePartByPrimaryKeyWithBLOBs(apiInfoUpdate);
            //更新apiInfoVersionLatest表，表示切换后的版本默认是最新发布版本
            ApiInfoVersionsExample selectExample = getExampleByVersionIdAndStatus(versionId,CommonStatus.ENABLE);
            List<ApiInfoVersionsWithBLOBs> apiInfoVersionsWithBLOBsList = apiInfoVersionsMapper.selectByExampleWithBLOBs(selectExample);
            if (apiInfoVersionsWithBLOBsList == null || apiInfoVersionsWithBLOBsList.size()==0){
                throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
            }
            ApiInfoVersionsWithBLOBs apiInfoVersionsWithBLOBs=apiInfoVersionsWithBLOBsList.get(0);
            if(apiInfoVersionsLatestMapper.updateByPrimaryKeyWithBLOBs(apiInfoVersionsWithBLOBs) == 0){
                apiInfoVersionsLatestMapper.insert(apiInfoVersionsWithBLOBs);
            }

            //更新request_params
            RequestParamsVersionsExample oldRequestParamsVersionExample = getRequestParamsVersionExample(oldVersionId);
            List<RequestParamsVersions> oldRequestParamsList =  requestParamsVersionsMapper.selectRequestParamsIdByVersionId(oldRequestParamsVersionExample);//通过oldVersionId查找requestParamsId
            if(oldRequestParamsList ==null || oldRequestParamsList.size() == 0){
                throw new BusinessException(ExceptionInfo.NOT_NULL_REQUEST_VERSIONS_PARAMS);
            }
            for (RequestParamsVersions rpv:oldRequestParamsList){
                requestParamsMapper.updateStatus2DisableById(rpv.getRequestParamsId());//将requestParamsId对应记录的status置为2不可用
            }
            RequestParamsVersionsExample requestParamsVersionExample = getRequestParamsVersionExample(versionId);
        	List<RequestParamsVersions> requestParamsList =  requestParamsVersionsMapper.selectRequestParamsIdByVersionId(requestParamsVersionExample);//通过versionId查找requestParamsId
        	if(requestParamsList ==null || requestParamsList.size() == 0){
               throw new BusinessException(ExceptionInfo.NOT_NULL_REQUEST_VERSIONS_PARAMS);
           }
            requestParamsVersionsLatestMapper.deleteByPrimaryKey(apiId);
            List<RequestParamsVersionsLatest> list = new ArrayList<>();
            for (RequestParamsVersions rpv:requestParamsList){
                requestParamsMapper.updateStatus2EnableById(rpv.getRequestParamsId());//将requestParamsId对应记录的status置为1可用
                //更新requestParamsVersionsLatest表（根据apiId和versionId）
                requestParamsVersionsLatest = new RequestParamsVersionsLatest();
                BeanCustomUtils.copyPropertiesIgnoreNull(rpv, requestParamsVersionsLatest);
                requestParamsVersionsLatest.setApiId(apiInfo.getId());
                requestParamsVersionsLatest.setId(null);
                requestParamsVersionsLatestMapper.insert(requestParamsVersionsLatest);//更新失败，插入数据
                list.add(requestParamsVersionsLatest);
            }

            //更新backend_request_params
            BackendRequestParamsVersionsExample oldBackendRequestParamsVersionsExample = getBackendVersionExample(oldVersionId);//通过oldVersionId查找requestParamsId集合
            List<BackendRequestParamsVersions> oldBackendRequestParamsList =  backendVersionsMapper.selectByExample(oldBackendRequestParamsVersionsExample);
            if(oldBackendRequestParamsList ==null || oldBackendRequestParamsList.size() == 0){
                throw new BusinessException(ExceptionInfo.NOT_NULL_REQUEST_VERSIONS_PARAMS);
            }
            for (BackendRequestParamsVersions brpv:oldBackendRequestParamsList){//更新oldVersionId版本的status为2不可用
                backendMapper.updateStatus2DisableById(brpv.getBackendParamsId());
            }
            BackendRequestParamsVersionsExample backendRequestParamsVersionsExample = getBackendVersionExample(versionId);//通过versionId查找requestParamsId集合
            List<BackendRequestParamsVersions> backendRequestParamsList =  backendVersionsMapper.selectByExample(backendRequestParamsVersionsExample);
            if(backendRequestParamsList ==null || backendRequestParamsList.size() == 0){
                throw new BusinessException(ExceptionInfo.NOT_NULL_REQUEST_VERSIONS_PARAMS);
            }
            for (BackendRequestParamsVersions brpv:backendRequestParamsList){//更新versionId版本的status为1可用
                //先查询BackendParamsId对应在BackendRequestParams表中存在
                    backendMapper.updateStatus2EnableById(brpv.getBackendParamsId());
            }
            ApiInfoRedisMsg apiInfoRedisMsg = new ApiInfoRedisMsg();
            apiInfoRedisMsg.setApiInfoWithBLOBs(apiInfoVersionsWithBLOBs);
            apiInfoRedisMsg.setList(list);
            redisService.setToCaches("api:"+apiId,apiInfoRedisMsg);

            // wt 修改组合接口参数
            modifyCombinationParamService.updateCombInfWithPublish(apiId);
        }

    }

    private BackendRequestParamsVersionsExample getBackendVersionExample(String versionId) {
        BackendRequestParamsVersionsExample backendRequestParamsVersionsExample = new BackendRequestParamsVersionsExample();
        BackendRequestParamsVersionsExample.Criteria criteria = backendRequestParamsVersionsExample.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        return backendRequestParamsVersionsExample;
    }

    private RequestParamsVersionsExample getRequestParamsVersionExample(String versionId) {
        RequestParamsVersionsExample requestParamsVersionExample = new RequestParamsVersionsExample();
        RequestParamsVersionsExample.Criteria requestParamsVersionCriteria = requestParamsVersionExample.createCriteria();
        requestParamsVersionCriteria.andVersionIdEqualTo(versionId);
        return requestParamsVersionExample;
    }

    private ApiInfoVersionsExample getExampleByVersionIdAndStatus(String versionId, Integer status) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        criteria.andCurrentVersionEqualTo(status);
        return example;
    }

    private void reswitchUpdateApiInfoVersion(String versionId, int status) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        ApiInfoVersionsWithBLOBs temp = new ApiInfoVersionsWithBLOBs();
        temp.setCurrentVersion(status);
        apiInfoVersionsMapper.updateByExampleSelective(temp, example);
        if (status == CommonStatus.ENABLE){
            List<ApiInfoVersionsWithBLOBs> list  = apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
            if (list!=null && list.size()>0)
                redisService.setRedisHttpPath2Cache(list.get(0));
        }
    }


    /**
     * 版本表复制到编辑表--切换BackendRequestParams表信息
     *
     * @param apiId
     * @param versionId
     */
    private void copyToBackendParams(Integer apiId, String versionId, List<Integer> requestParamsIdList) {
        //删除旧数据
        this.updateBackendParamsStatus(apiId);
        //导入新数据
        List<BackendRequestParamsVersions> backendParamsVersionsList = this.findBackendParamsVersionsListByVersionId(versionId);
        if (null != backendParamsVersionsList && backendParamsVersionsList.size() > 0) {
            Integer i = 0;
            Iterator<BackendRequestParamsVersions> iterator = backendParamsVersionsList.iterator();
            BackendRequestParams backendParams = new BackendRequestParams();
            while (iterator.hasNext()) {
                BeanCustomUtils.copyPropertiesIgnoreNull(iterator.next(), backendParams);
                backendParams.setId(null);
                backendParams.setApiId(apiId);
                backendParams.setStatus(CommonStatus.ENABLE);
                Long createTime = System.currentTimeMillis();
                backendParams.setCreateTime(createTime);
                backendParams.setUpdateTime(createTime);

                if (null != requestParamsIdList && requestParamsIdList.size() > 0) {
                    //当后端参数的参数类型是变量时且i < len时，对requestParamsId进行赋值
                    if (i < requestParamsIdList.size() && CommonStatus.VARIABLE == backendParams.getParamsType()) {
                        backendParams.setRequestParamsId(requestParamsIdList.get(i++));
                    }
                }
                backendMapper.insert(backendParams);
            }
        }
    }

    /**
     * 【编辑表】-修改上游请求参数的状态
     *
     * @param apiId
     */
    private void updateBackendParamsStatus(Integer apiId) {
        BackendRequestParamsExample example = new BackendRequestParamsExample();
        BackendRequestParamsExample.Criteria criteria = example.createCriteria();

        criteria.andApiIdEqualTo(apiId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);

        BackendRequestParams backendParams = new BackendRequestParams();
        backendParams.setStatus(CommonStatus.DISENABLE);
        backendParams.setUpdateTime(System.currentTimeMillis());

        backendMapper.updateByExampleSelective(backendParams, example);
    }

    /**
     * 通过版本号查找对应的后端参数（上游请求参数表）
     *
     * @param versionId
     * @return
     */
    private List<BackendRequestParamsVersions> findBackendParamsVersionsListByVersionId(String versionId) {
        BackendRequestParamsVersionsExample example = new BackendRequestParamsVersionsExample();
        BackendRequestParamsVersionsExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);
        return backendVersionsMapper.selectByExample(example);
    }

    /**
     * 版本表复制到编辑表--切换requestParams表信息
     *
     * @param apiId
     * @param versionId
     */
    private List<Integer> copyToRequestParams(Integer apiId, String versionId) {
        //删除旧数据
        this.updateRequestParamsStatus(apiId);
        //导入新数据
        List<RequestParamsVersions> requestParamsVersionsList = this.findRequestParamsVersionsListByVersionId(versionId);
        List<Integer> requestParamsIdList = null;
        if (null != requestParamsVersionsList && requestParamsVersionsList.size() > 0) {
            requestParamsIdList = new ArrayList<Integer>();
            Iterator<RequestParamsVersions> iterator = requestParamsVersionsList.iterator();
            RequestParams requestParams = new RequestParams();
            while (iterator.hasNext()) {
                BeanCustomUtils.copyPropertiesIgnoreNull(iterator.next(), requestParams);
                requestParams.setId(null);
                requestParams.setApiId(apiId);
                requestParams.setStatus(CommonStatus.ENABLE);
                Long createTime = System.currentTimeMillis();
                requestParams.setCreateTime(createTime);
                requestParams.setUpdateTime(createTime);
                requestParamsMapper.insert(requestParams);
                Integer requestParamsId = requestParams.getId();
                requestParamsIdList.add(requestParamsId);
            }
        }
        return requestParamsIdList;
    }

    /**
     * 【编辑表】-修改下游请求参数的状态
     *
     * @param apiId
     */
    private void updateRequestParamsStatus(Integer apiId) {
        RequestParamsExample example = new RequestParamsExample();
        RequestParamsExample.Criteria criteria = example.createCriteria();

        criteria.andApiIdEqualTo(apiId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);

        RequestParams requestParams = new RequestParams();
        requestParams.setStatus(CommonStatus.DISENABLE);
        requestParams.setUpdateTime(System.currentTimeMillis());

        requestParamsMapper.updateByExampleSelective(requestParams, example);
    }

    /**
     * 通过版本号查找对应的入参参数（下游请求参数表）
     *
     * @param versionId
     * @return
     */
    private List<RequestParamsVersions> findRequestParamsVersionsListByVersionId(String versionId) {
        RequestParamsVersionsExample example = new RequestParamsVersionsExample();
        RequestParamsVersionsExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);
        example.setOrderByClause("paramsLocation,id asc");
        return requestParamsVersionsMapper.selectByExample(example);
    }


    private List<CallBackParam> findCallBackParamListByApiId(Integer apiId){
        return callBackParamMapper.findByApiId(apiId);
    }

    private List<List<String>> convertList(List<CallBackParam> callBackParamList){
        if (null == callBackParamList || callBackParamList.size()<=0){
            return Collections.emptyList();
        }
        List<List<String>> finalList = new ArrayList<>();
        callBackParamList.forEach(item -> {
            List<String> tmp = new ArrayList<>();
            tmp.add(item.getId()+"");
            tmp.add(item.getParamName());
            tmp.add(item.getDescription());
            tmp.add(item.getParamType());
            tmp.add(item.getRemark());
            tmp.add(item.getPosition());
            tmp.add(item.getParentId()+"");
            finalList.add(tmp);
        });
        return finalList;
    }

    /**
     * 重切换--切换apiInfo表信息
     *
     * @param apiInfoVersion
     */
    private ApiInfoVersionsWithBLOBs copyToApiInfo(ApiInfoVersionsWithBLOBs apiInfoVersion) {
        //获取锁定的版本表apiInfoVersions
        apiInfoVersion = this.findApiInfoVersionsByIdAndGroupId(apiInfoVersion.getId(), apiInfoVersion.getApiGroupId());
        if (null == apiInfoVersion) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
        }
        Integer apiId = apiInfoVersion.getApiId();
        //获取锁定的编辑表apiInfo
        ApiInfoWithBLOBs apiInfo = this.findById(apiId);
        if (null == apiInfo) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        Long createTime = apiInfo.getCreateTime();
        //把apiInfoVersions内容copy到apiInfo里
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfoVersion, apiInfo);
        apiInfo.setId(apiId);
        apiInfo.setCreateTime(createTime);
        apiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfo.setStatus(CommonStatus.ENABLE);

        apiInfoMapper.updateByPrimaryKeyWithBLOBs(apiInfo);
        return apiInfoVersion;
    }

    /**
     * 根据id和分组id确定api版本
     *
     * @param id
     * @param apiGroupId
     * @return
     */
    private ApiInfoVersionsWithBLOBs findApiInfoVersionsByIdAndGroupId(Integer id, Integer apiGroupId) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andApiGroupIdEqualTo(apiGroupId);
        List<ApiInfoVersionsWithBLOBs> apiInfoVersionsList = apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        if (null == apiInfoVersionsList || apiInfoVersionsList.size() == 0) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
        }
        return apiInfoVersionsList.get(0);
    }

    /**
     * 根据apiInfoVersions版本表id返回信息
     *
     * @param id
     * @return
     */
    private ApiInfoVersionsWithBLOBs findApiInfoVersionsById(Integer id) {
        if (null == id) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
        }
        return apiInfoVersionsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SystemErrorCode> systemErrorCodeList() {
        SystemErrorCodeExample example = new SystemErrorCodeExample();
        return systemErrorCodeMapper.selectByExample(example);
    }

    @Override
    public PageInfo<ApiExpand> apiInfoList(ApiInfoQuery apiInfoQuery) {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        //参数校验
        if (null == apiInfoQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null != apiInfoQuery.getApiName()) {
            String apiName = apiInfoQuery.getApiName().replace("_", "\\_");
            criteria.andApiNameLike(StringUtils.wrap(apiName, "%"));
        }
        if (null != apiInfoQuery.getApiGroupId()) {
            criteria.andApiGroupIdEqualTo(apiInfoQuery.getApiGroupId());
        }
        Integer env = apiInfoQuery.getEnv();
        if (null != env) {
            if (CommonStatus.ONLINE == env) {
                criteria.andPublishProductEnvStatusEqualTo(CommonStatus.ENABLE);
            }
//            else if (CommonStatus.ADVANCE == env) {
//                criteria.andPublishTestEnvStatusEqualTo(CommonStatus.ENABLE);
//            }
            else {
                throw new DataValidationException(StringUtils.join("env", ExceptionInfo.ERROR));
            }
        }
        //status默认为可用
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        //设置按创建时间倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiExpandPage = PageHelper.startPage(apiInfoQuery.getPageNum(), apiInfoQuery.getPageSize());
        apiInfoMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiExpandPage.toPageInfo();
        pageInfo.setList(this.packageGroupNameAndStrategyName(apiExpandPage.getResult()));
        return pageInfo;
    }

    /**
     * 封装分组名称（编辑表）
     *
     * @param apiInfoList
     * @return
     */
    private List<ApiExpand> packageGroupNameAndStrategyName(List<ApiInfoWithBLOBs> apiInfoList) {
        if (null == apiInfoList || apiInfoList.size() == 0) {
            return Collections.emptyList();
        }
        List<ApiExpand> apiExpandList = new ArrayList<>();
        Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
        while (iterator.hasNext()) {
            apiExpandList.add(this.packageGroupNameAndStrategyName(iterator.next()));
        }
        return apiExpandList;
    }

    /**
     * 封装分组名称（编辑表）
     *
     * @param apiInfo
     * @return
     */
    private ApiExpand packageGroupNameAndStrategyName(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            return null;
        }
        ApiExpand apiExpand = new ApiExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfo, apiExpand);
        if (null != apiInfo.getApiGroupId()) {
            apiExpand.setGroupName(apiGroupService.findById(apiInfo.getApiGroupId()).getGroupName());
        }
        if (null != apiInfo.getLimitStrategyuuid()) {
            apiExpand.setStrategyName(strategyAuthService.findStrategyByStrategyuuidAndStatusIsEnable(apiInfo.getLimitStrategyuuid()).getName());
        }
        return apiExpand;
    }

    @Override
    public PageInfo<ApiVersionExpand> apiInfoVersionList(ApiInfoVersionQuery apiInfoVersionQuery) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        //参数校验
        if (null == apiInfoVersionQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        //发现API-正在使用的线上环境的版本api
        if (null == apiInfoVersionQuery.getApiId()) {
            //发布环境在线上
            criteria.andEnvEqualTo(CommonStatus.ONLINE);
            //正在使用
            criteria.andCurrentVersionEqualTo(CommonStatus.ENABLE);
            //apiName条件查询
            if (null != apiInfoVersionQuery.getApiName()) {
                String apiName = apiInfoVersionQuery.getApiName().replace("_", "\\_");
                criteria.andApiNameLike(StringUtils.wrap(apiName, "%"));
            }
        }
        //适用于某API的发布历史查询
        else {
            criteria.andApiIdEqualTo(apiInfoVersionQuery.getApiId());
        }
        //少传或漏传参数
//        else {
//            throw new DataValidationException(ExceptionInfo.PARAMS_ERROR);
//        }
        //设置创建时间按倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiVersionExpandPage = PageHelper.startPage(apiInfoVersionQuery.getPageNum(), apiInfoVersionQuery.getPageSize());
        apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiVersionExpandPage.toPageInfo();
        pageInfo.setList(this.packageGroupNameVersion(apiVersionExpandPage.getResult()));
        return pageInfo;
    }

    /**
     *  返回apiInfoVersionLatest列表（包含条件查询）
     * @param apiInfoVersionQuery
     * @return
     */
    @Override
    public PageInfo<ApiVersionExpand> apiInfoVersionLatestList(ApiInfoVersionQuery apiInfoVersionQuery) {
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        //参数校验
        if (null == apiInfoVersionQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        //发现API-正在使用最新发布版本api
        if (null == apiInfoVersionQuery.getApiId()) {
            //最新发布表中存的版本为预发下的，env= 2
//            criteria.andEnvEqualTo(CommonStatus.ADVANCE);
            criteria.andEnvEqualTo(CommonStatus.ONLINE);
            //正在使用
            criteria.andCurrentVersionEqualTo(CommonStatus.ENABLE);
            //apiName条件查询
            if (null != apiInfoVersionQuery.getApiName()) {
                String apiName = apiInfoVersionQuery.getApiName().replace("_", "\\_");
                criteria.andApiNameLike(StringUtils.wrap(apiName, "%"));
            }
        } else {//适用于某API的发布历史查询
            criteria.andApiIdEqualTo(apiInfoVersionQuery.getApiId());
        }
        //设置创建时间按倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiVersionExpandPage = PageHelper.startPage(apiInfoVersionQuery.getPageNum(), apiInfoVersionQuery.getPageSize());
//        apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        apiInfoVersionsLatestMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiVersionExpandPage.toPageInfo();
        pageInfo.setList(this.packageGroupNameVersion(apiVersionExpandPage.getResult()));
        return pageInfo;
    }
    /**
     * 包装分组名称（版本表）
     *
     * @param apiInfoVersionsList
     * @return
     */
    private List<ApiVersionExpand> packageGroupNameVersion(List<ApiInfoVersionsWithBLOBs> apiInfoVersionsList) {
        if (null == apiInfoVersionsList || apiInfoVersionsList.size() == 0) {
            return Collections.emptyList();
        }
        List<ApiVersionExpand> apiVersionExpandList = new ArrayList<>();
        Iterator<ApiInfoVersionsWithBLOBs> iterator = apiInfoVersionsList.iterator();
        while (iterator.hasNext()) {
            apiVersionExpandList.add(this.packageGroupNameVersion(iterator.next()));
        }
        return apiVersionExpandList;
    }
    /**
     * 包装分组名称（版本表）
     *
     * @parama　piInfoList
     * @return
     */
    private List<ApiExpand> packageGroupName(List<ApiInfoWithBLOBs> apiInfoList) {
        if (null == apiInfoList || apiInfoList.size() == 0) {
            return Collections.emptyList();
        }
        List<ApiExpand> apiExpandList = new ArrayList<>();
        Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
        while (iterator.hasNext()) {
            apiExpandList.add(this.packageGroupName(iterator.next()));
        }
        return apiExpandList;
    }
    /**
     * 包装分组名称（版本表）
     *
     * @param apiInfoVersions
     * @return
     */
    private ApiVersionExpand packageGroupNameVersion(ApiInfoVersionsWithBLOBs apiInfoVersions) {
        if (null == apiInfoVersions) {
            return null;
        }
        ApiVersionExpand apiVersionExpand = new ApiVersionExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfoVersions, apiVersionExpand);
        if (null != apiInfoVersions.getApiGroupId()) {
            apiVersionExpand.setGroupName(apiGroupService.findById(apiInfoVersions.getApiGroupId()).getGroupName());
        }
        return apiVersionExpand;
    }
    /**
     * 包装分组名称（版本表）
     *
     * @param apiInfo
     * @return
     */
    private ApiExpand packageGroupName(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            return null;
        }
        ApiExpand apiExpand = new ApiExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfo, apiExpand);
        if (null != apiInfo.getApiGroupId()) {
            apiExpand.setGroupName(apiGroupService.findById(apiInfo.getApiGroupId()).getGroupName());
        }
        return apiExpand;
    }
    @Override
    public Map debugging(IDInfo idInfo, List<RequestParamAndValue> headerList, List<RequestParamAndValue> queryList, List<RequestParamAndValue> bodyList) {
        //根据idInfo查询httpPath、httpMethod
        ApiInfoWithBLOBs apiInfo = this.findById(idInfo.getApiId());
        if (apiInfo.getStatus() == CommonStatus.DISENABLE) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        //List<ServiceInstance> instances = discoveryClient.getInstances(zuulServiceName);
        //String httpPath = StringUtils.join(RandomLB.getServer(instances), apiInfo.getHttpPath());
        String httpPath = "http://"+zuulServiceName+apiInfo.getHttpPath();
        //1：get方式    2：post方式
        Integer httpMethod = apiInfo.getHttpMethod();
        Map<String, String> headerMap = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        headerMap.put("env", env);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);


        if (null != headerList && headerList.size() > 0) {
            Iterator<RequestParamAndValue> iterator1 = headerList.iterator();
            RequestParamAndValue temp = null;
            while (iterator1.hasNext()) {
                temp = iterator1.next();
                headerMap.put(temp.getParamName(), temp.getParamValue());
            }
        }


        if (httpMethod == CommonStatus.GET) {
            StringBuilder builder = new StringBuilder();
            if (null != queryList && queryList.size() > 0) {
                Iterator<RequestParamAndValue> iterator2 = queryList.iterator();
                RequestParamAndValue temp = null;
                builder.append("?");
                while (iterator2.hasNext()) {
                    temp = iterator2.next();
                    builder.append(temp.getParamName()).append("=").append(temp.getParamValue()).append("&");
                }
            }
            String par="";
            if(builder.length()>0){
                par = builder.toString().substring(0, builder.length() - 1);
            }
            if (StringUtils.isEmpty(headerMap.get("appKey"))) headerMap.put("appKey", appKey);
            headerMap.forEach(headers::add);
            HttpEntity<Object> entity = new HttpEntity<>(null, headers);
            ResponseEntity<String> exchange = restTemplate.exchange(httpPath+par, HttpMethod.GET, entity, String.class);
            Map map = new HashMap();
            map.put("resultString", exchange.getBody());
            try {
                map.put("resultList", handleJsonList(json2TreeList(idInfo.getApiId(), exchange.getBody(), 0, 1, 1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        } else {
            headers.add(ZuulHeader.REQUEST_VERSION,Constant.REQUEST_VERSION_NOW);
            if (null != bodyList && bodyList.size() > 0) {
                Iterator<RequestParamAndValue> iterator3 = bodyList.iterator();
                RequestParamAndValue temp = null;
                while (iterator3.hasNext()) {
                    temp = iterator3.next();
                    if ("appKey".equals(temp.getParamName()))
                    	headerMap.put("appKey", temp.getParamValue());
                    paramMap.put(temp.getParamName(), temp.getParamValue());
                }
            }
            if (StringUtils.isEmpty(headerMap.get("appKey"))) headerMap.put("appKey", appKey);
            headerMap.forEach(headers::add);
            String body = null;
           /* CommonResult<RespCompanyKey> commonResult = microChargeService.selectKey(headerMap.get("appKey"));
            RespCompanyKey appKeyOb = commonResult.getData();
            if(appKeyOb==null||StringUtils.isBlank(appKeyOb.getPrivateKey())||StringUtils.isBlank(appKeyOb.getPublicKey())){
                Map map = new HashMap();
                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                json.put("msg", "appKey不存在");
                map.put("resultString", json.toJSONString());
                return map;
            }
            try {
                String publicKey1 = appKeyOb.getPublicKey();
                log.info("web请求publick:"+publicKey1);
                body = RSAUtil.encrypt(JSON.toJSONString(paramMap), publicKey1);
            } catch (Exception e) {
                log.error("请求端加密失败",e);
                throw new BusinessException("加密失败");
            }
            log.info("web请求加密串："+body);*/
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            String hashMap = restTemplate.postForObject(httpPath, entity, String.class, "");
            CommonResult commonResult1 = JSON.parseObject(hashMap,CommonResult.class);
            Object data = commonResult1.getData();
            if (ResultStatusEnum.SUCCESS.getCode() == commonResult1.getCode()) {
                String s = JSON.toJSONString(data, SerializerFeature.WriteMapNullValue);
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
                String resultSign = jsonObject.getString("sign");
                String resultBody = JSON.toJSONString(jsonObject.getJSONObject("body"), SerializerFeature.WriteMapNullValue);
                log.info("web请求返回body："+resultBody);
                if(!RSAUtil.checkSign(resultBody,resultSign,"")){
                    //throw new BusinessException("入口处获取返回值验签失败");
                    log.error("入口处获取返回值验签失败");
                    com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
                    json.put("msg", "zuul返回数据验签失败");
                    Map map = new HashMap();
                    map.put("resultString", json.toJSONString());
                    return map;
                }
            }
            Map map = new HashMap();

            map.put("resultString", hashMap);
            try {
                map.put("resultList", handleJsonList(json2TreeList(idInfo.getApiId(), hashMap, 0, 1, 1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
    }

    /**
     * 构建指标列表
     * @param paramList 原始列表
     * @return 含其他信息的指标列表
     */
    private List<CallBackParam> handleJsonList(List<CallBackParam> paramList){
        //构建原JSON架构
        for (CallBackParam callBackParam : paramList) {
            paramList.forEach(callBackParam1 -> {
                if (callBackParam.getId().equals(callBackParam1.getParentId())) {
                    callBackParam.getChildren().add(callBackParam1);
                }
            });
        }
        //删除多余元素
        List<CallBackParam> callBackParamsStructured = paramList.stream().filter(callBackParam -> callBackParam.getParentId() == 0).collect(Collectors.toList());

        //加工备注/码值等内容
        //1. 获取数据库的返回参数
        List<CallBackParam> callBackParamsDB = callBackParamMapper.findByApiId(callBackParamsStructured.get(0).getApiId());
        List<CallBackParam> callBackParamsDBStructured = handleCallBackParam(callBackParamsDB);
        //2. 比较节点,填充中文名称
        handleJsonNode(callBackParamsStructured, callBackParamsDBStructured);
        return callBackParamsStructured;
    }

    private void handleJsonNode(List<CallBackParam> sourceList, List<CallBackParam> dbList) {
        sourceList.forEach(callBackParam ->
                dbList.forEach(callBackParamDB -> {
                    if (callBackParam.equals(callBackParamDB)) {
                        callBackParam.setDescription(callBackParamDB.getDescription());
                        callBackParam.setRemark(callBackParamDB.getRemark());
                        if (CollectionUtils.isNotEmpty(callBackParam.getChildren()) && CollectionUtils.isNotEmpty(callBackParamDB.getChildren())) {
                            handleJsonNode(callBackParam.getChildren(), callBackParamDB.getChildren());
                        }
                    }
                })
        );
    }

    /**
     * json转化为list,每个节点为一个callbackParam对象,
     * @param apiId apiId
     * @param jsonString json 串
     * @param pid 父节点ID
     * @param id 节点ID
     * @param layer 层级
     * @return json串结构list
     * @throws Exception ex
     */
    private List<CallBackParam> json2TreeList(int apiId, String jsonString, int pid, int id, int layer) throws Exception {
        List<CallBackParam> list = new ArrayList<>();
        JSONObject json = new JSONObject(jsonString);
        Iterator keys = json.keys();
        int tmp = layer;
        while (keys.hasNext()) {
            CallBackParam callBackParam = new CallBackParam();
            String key = (String) keys.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                callBackParam.setParentId(pid);
                callBackParam.setId(id++);
                callBackParam.setParamName(key);
                callBackParam.setParamType("JSONObject");
                callBackParam.setApiId(apiId);
                callBackParam.setPosition(String.valueOf(tmp));

                list.add(callBackParam);
                list.addAll(this.json2TreeList(apiId, value.toString(), callBackParam.getId(), id, ++layer));
            } else if (value instanceof JSONArray) {
                callBackParam.setParentId(pid);
                callBackParam.setId(id++);
                callBackParam.setParamName(key);
                callBackParam.setParamType("JSONArray");
                callBackParam.setApiId(apiId);

                JSONArray jsonArray = (JSONArray) value;
                //数组为空
                if (jsonArray.length() <= 0) {
                    callBackParam.setValue("[]");
                    callBackParam.setPosition(String.valueOf(tmp));
                    list.add(callBackParam);
                    continue;
                } else if (jsonArray.get(0) instanceof JSONObject) {
                    callBackParam.setPosition(String.valueOf(tmp));
                    for (int i =0; i<jsonArray.length(); i++) {
                        list.addAll(this.json2TreeList(apiId, jsonArray.get(i).toString(), callBackParam.getId(), id, ++layer));
                        --layer;
                    }
                } else {//简单数组["aaa","bbb","ccc"]
                    callBackParam.setValue(value.toString());
                    callBackParam.setPosition(String.valueOf(tmp));
                }
                list.add(callBackParam);
            } else {
                callBackParam.setParentId(pid);
                callBackParam.setId(id++);
                callBackParam.setParamName(key);
                callBackParam.setValue(value.toString());
                callBackParam.setParamType("String");
                callBackParam.setApiId(apiId);
                callBackParam.setPosition(String.valueOf(tmp));

                list.add(callBackParam);
            }
        }

        return list;
    }

    @Override
    public String httpPathUnique(ApiInfoWithBLOBs apiInfo) {
        //参数校验
        ApiValidate.httpPathUniqueCheck(apiInfo);
        List<ApiInfoWithBLOBs> apiInfoList = this.findList();
        if (null == apiInfoList || apiInfoList.size() == 0) {
            return null;
        }
        ApiInfoWithBLOBs tempApiInfo;
        Iterator<ApiInfoWithBLOBs> iterator = apiInfoList.iterator();
        while (iterator.hasNext()) {
            tempApiInfo = iterator.next();
            if (tempApiInfo.getHttpPath().equals(apiInfo.getHttpPath())) {
                //这里用于修改api时，防止httpPath与自身比较
                if (null != apiInfo.getId() && apiInfo.getId().equals(tempApiInfo.getId())) {
                    return null;
                }
                return tempApiInfo.getApiName();
            }
        }
        return null;
    }

    @Override
    public void paramNameCheck(List<ParamNames> paramNameList) {
        Integer size = paramNameList.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (paramNameList.get(i).getParamName().equals(paramNameList.get(j).getParamName())) {
                    throw new BusinessException(ExceptionInfo.PARAMS_ARE_REPEAT);
                }
            }
        }
    }

    @Override
    public void copyToEdit(ApiInfoVersionsWithBLOBs apiInfoVersion) {
        //参数校验
        ApiValidate.apiIdCheck(apiInfoVersion);
        ApiValidate.apiGroupIdCheck(apiInfoVersion);

        //更新apiInfo表内容
        apiInfoVersion = this.copyToApiInfo(apiInfoVersion);
        String versionId = apiInfoVersion.getVersionId();
        Integer apiId = apiInfoVersion.getApiId();
        //更新入参（下游请求参数编辑表）
        List<Integer> requestParamsIdList = this.copyToRequestParams(apiId, versionId);
        //更新后端参数（上游请求参数编辑表）
        this.copyToBackendParams(apiId, versionId, requestParamsIdList);
    }


    public String downloadDocx(String versionId, Integer apiId) {
        ApiInfoVersionsWithBLOBs apiInfoVersion = this.findApiInfoVersionsByVersionId(versionId);
        if (null == apiInfoVersion || (!apiInfoVersion.getApiId().equals(apiId))) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO_VERSIONS);
        }
        //docx文档中的变量替换
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("${apiName}", apiInfoVersion.getApiName());
        paramMap.put("${url}", apiInfoVersion.getHttpPath());
        paramMap.put("${httpMethod}", Operator.httpMethodInteger2String(apiInfoVersion.getHttpMethod()));
        paramMap.put("${resultExample}", FormatJSON.format(apiInfoVersion.getCallBackSuccessExample()));
        paramMap.put("${resultFailExample}", FormatJSON.format(apiInfoVersion.getCallBackFailExample()));

        //入参列表
        List<RequestParamsVersions> requestParamList = this.findRequestParamsVersionsListByVersionId(versionId);
        List<RequestParamForDOCX> rpList = this.convertRequestParamVersion(requestParamList);
        List<List<String>> dataList = this.convert2StringList(rpList);

        //出参列表
        List<CallBackParam> responceParamList = this.findCallBackParamListByApiId(apiId);
        List<List<String>> responseParamList = this.convertList(responceParamList);

        try {
            WordUtils.buildAndUpload(paramMap, dataList,responseParamList ,null, null, "apiWord", apiInfoVersion.getApiName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiInfoVersion.getApiName();
    }

    public Map<String,Object> showDocx(String apiId,String versionId){
        ApiInfoVersionsWithBLOBs apiInfoVersion = this.findApiInfoVersionsByVersionId(versionId);
        List<CallBackParam> callBackParams = callBackParamMapper.findByApiId(Integer.valueOf(apiId));
        List<RequestParamsVersions> requestParamList = this.findRequestParamsVersionsListByVersionId(versionId);
        List<RequestParamForDOCX> requestParams = this.convertRequestParamVersion(requestParamList);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("uri",apiInfoVersion.getHttpPath());
        resultMap.put("apiName",apiInfoVersion.getApiName());
        resultMap.put("method",Operator.httpMethodInteger2String(apiInfoVersion.getHttpMethod()));
        resultMap.put("example",apiInfoVersion.getCallBackSuccessExample());
        resultMap.put("requestList",requestParams);
        resultMap.put("responseList",this.handleCallBackParam(callBackParams));
        return resultMap;
    }

    /**
     * 构建树结构
     * @param paramList 参数列表
     * @return 带结构的参数列表
     */
    private List<CallBackParam> handleCallBackParam(List<CallBackParam> paramList){
        if (CollectionUtils.isEmpty(paramList)) {
            return new ArrayList<>();
        }
        for (CallBackParam callBackParam : paramList) {
            paramList.forEach(callBackParam1 -> {
                if (callBackParam.getId().equals(callBackParam1.getParentId())) {
                    callBackParam.getChildren().add(callBackParam1);
                }
            });
        }
        return paramList.stream().filter(callBackParam -> callBackParam.getParentId() == 0).collect(Collectors.toList());
    }

    public List<CallBackParam> parseJson(String apiId,String json) throws Exception {
            // 解析json获得返回参数列表
            this.parse1(apiId, json, 0, 1);

            // 查询持久化到数据库的入参
            List<CallBackParam> tempList = callBackParamMapper.findByApiId(Integer.valueOf(apiId));

            // 组装成父子结构
            tempList = this.handleCallBackParam(tempList);
        return tempList;
    }

    /**
     * json解析
     * @param apiId apiId
     * @param jsonString json字符串
     * @return
     * @throws Exception
     */
    private void parse1(String apiId, String jsonString, int pid, int layer) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        Iterator keys = json.keys();
        int tmp = layer;
        while (keys.hasNext()) {
            CallBackParam callBackParam = new CallBackParam();
            String key = (String) keys.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                callBackParam.setParamName(key);
                callBackParam.setParamType("JSONObject");
                callBackParam.setParentId(pid);
                callBackParam.setApiId(Integer.valueOf(apiId));
                callBackParam.setPosition(String.valueOf(tmp));
                callBackParamMapper.insert(callBackParam);
                this.parse1(apiId, value.toString(), callBackParam.getId(), ++layer);
            } else if (value instanceof JSONArray) {
                callBackParam.setParamName(key);
                callBackParam.setParamType("JSONArray");
                callBackParam.setParentId(pid);
                callBackParam.setApiId(Integer.valueOf(apiId));

                JSONArray jsonArray = (JSONArray) value;
                if ((jsonArray).length() <= 0) {
                    callBackParam.setPosition(String.valueOf(tmp));
                    callBackParamMapper.insert(callBackParam);
                } else if (jsonArray.get(0) instanceof JSONObject) {
                    callBackParam.setPosition(String.valueOf(tmp));
                    callBackParamMapper.insert(callBackParam);
                    this.parse1(apiId, jsonArray.get(0).toString(), callBackParam.getId(), ++layer);
                } else {
                    callBackParam.setPosition(String.valueOf(tmp));
                    callBackParamMapper.insert(callBackParam);
                }
            } else {
                callBackParam.setParamName(key);
                callBackParam.setParentId(pid);
                callBackParam.setParamType("String");
                callBackParam.setApiId(Integer.valueOf(apiId));
                callBackParam.setPosition(String.valueOf(tmp));
                callBackParamMapper.insert(callBackParam);
            }
        }
    }

    /**
     * json解析
     * @param apiId apiId
     * @param jsonString json字符串
     * @param pos json层级
     * @param parentName 父节点名称
     * @return
     * @throws Exception
     */
    private List<CallBackParam> parse(String apiId, String jsonString, int pos, String parentName) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        Iterator keys = json.keys();
        List<CallBackParam> list = new ArrayList<>();
        /*int tmp = pos;
        while (keys.hasNext()) {
            CallBackParam callBackParam = new CallBackParam();
            String key = (String) keys.next();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                callBackParam.setParamName(key);
                callBackParam.setParamType("JSONObject");
                callBackParam.setPosition(tmp + "");
                callBackParam.setParentId(parentName);
                callBackParam.setApiId(Integer.valueOf(apiId));
                list.add(callBackParam);
                list.addAll(this.parse(apiId, value.toString(), ++pos, key));
            } else if (value instanceof JSONArray) {
                if (((JSONArray) value).length() <= 0) continue;
                callBackParam.setParamName(key);
                callBackParam.setParamType("JSONArray");
                callBackParam.setPosition(tmp + "");
                callBackParam.setParentId(parentName);
                callBackParam.setApiId(Integer.valueOf(apiId));
                list.add(callBackParam);
                list.addAll(this.parse(apiId, ((JSONArray) value).getString(0), ++pos, key));
            } else {
                callBackParam.setParamName(key);
                callBackParam.setPosition(tmp + "");
                callBackParam.setParentId(parentName);
                callBackParam.setParamType("String");
                callBackParam.setApiId(Integer.valueOf(apiId));
                list.add(callBackParam);
            }
        }*/
        return list;
    }

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public boolean editDocx(com.alibaba.fastjson.JSONObject data){
        try {
            com.alibaba.fastjson.JSONObject responseList = data.getJSONObject("responseList");
            CallBackParam callBackParam = responseList.toJavaObject(CallBackParam.class);
            callBackParam.setApiId(data.getInteger("apiId"));
            callBackParamMapper.updateByIdWithoutParentId(callBackParam);
        } catch (Exception e) {
            log.error("edit docx error...{}",e);
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public boolean deleteDocx(com.alibaba.fastjson.JSONObject data){
        try {
            if (StringUtils.isNotBlank(data.getString("apiId"))){
                String apiId = data.getString("apiId");
                callBackParamMapper.deleteByApiId(Integer.valueOf(apiId));
            }else if (StringUtils.isNotBlank(data.getString("id"))){
                String id = data.getString("id");
                callBackParamMapper.deleteById(Integer.valueOf(id));
            }
        } catch (Exception e) {
            log.error("delete docx error...");
            return false;
        }
        return true;
    }

    /**
     * 将list包装成List<List<String>>
     *
     * @param rpList
     * @return
     */
    private List<List<String>> convert2StringList(List<RequestParamForDOCX> rpList) {
        List<List<String>> finalList = new ArrayList<List<String>>();
        for (int i = 0; i < rpList.size(); i++) {
            List<String> list = new ArrayList<String>();
//            RequestParamForDOCX tmp = new RequestParamForDOCX();
            RequestParamForDOCX tmp;
            tmp = rpList.get(i);
            list.add(tmp.getParamName());
            list.add(tmp.getType());
            list.add(tmp.getParamsLocation());
            list.add(tmp.getParamMust());
            list.add(tmp.getParamsDescription());
            finalList.add(list);
        }
        return finalList;
    }

    /**
     * 包装请求参数用以做API的docx文档
     *
     * @param list
     * @return
     */
    private List<RequestParamForDOCX> convertRequestParamVersion(List<RequestParamsVersions> list) {
        if (null == list || list.size() == 0) {
            return Collections.emptyList();
        }
        List<RequestParamForDOCX> rpList = new ArrayList<>();
        RequestParamsVersions rp;
        Iterator<RequestParamsVersions> iterator = list.iterator();
        while (iterator.hasNext()) {
            rp = iterator.next();
            rpList.add(this.convertRequestParamVersion(rp));
        }
        return rpList;
    }

    /**
     * 包装请求参数用以做API的docx文档
     *
     * @param rp
     * @return
     */
    private RequestParamForDOCX convertRequestParamVersion(RequestParamsVersions rp) {
        if (null == rp) {
            return null;
        }
        RequestParamForDOCX rpForDOCX = new RequestParamForDOCX();
        BeanCustomUtils.copyPropertiesIgnoreNull(rp, rpForDOCX);
        rpForDOCX.setType(Operator.paramTypeInteger2String(rp.getParamsType()));
        rpForDOCX.setParamMust(Operator.paramMustInteger2String(rp.getParamsMust()));
        rpForDOCX.setParamsLocation(Operator.paramLocation(rp.getParamsLocation()));
        return rpForDOCX;
    }

    //接口2.0
    @Override
    @Transactional
    public void apibindingStrategy(Integer apiId, String uuid, Integer total) {
        if (StringUtils.isNotBlank(uuid)) {
            //验证绑定策略是否存在且状态为可用
            strategyAuthService.findStrategyByStrategyuuidAndStatusIsEnable(uuid);
        } else {
            uuid = null;
        }

        //绑定策略
        ApiInfoWithBLOBs apiInfo = this.findById(apiId);
        if (CommonStatus.DISENABLE == apiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        apiInfo.setLimitStrategyuuid(uuid);
        apiInfo.setLimitStrategyTotal(total);
        apiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.updateByPrimaryKeyWithBLOBs(apiInfo);

        //如果api有测试和生产版本则修改版本的绑定策略，使之立即生效
        ApiInfoVersionsWithBLOBs param = new ApiInfoVersionsWithBLOBs();

//        if (StringUtils.isNotBlank(apiInfo.getTestEnvVersion())) {
//            ApiInfoVersionsExample versionsTestExample = new ApiInfoVersionsExample();
//            ApiInfoVersionsExample.Criteria versionsTestCriteria = versionsTestExample.createCriteria();
//            versionsTestCriteria.andVersionIdEqualTo(apiInfo.getTestEnvVersion());
//            versionsTestCriteria.andEnvEqualTo(CommonStatus.ADVANCE);
//            List<ApiInfoVersionsWithBLOBs> apiVersionsTest = apiInfoVersionsMapper.selectByExampleWithBLOBs(versionsTestExample);
//            if (CollectionUtil.isNotEmpty(apiVersionsTest)) {
//                BeanUtils.copyProperties(apiVersionsTest.get(0), param);
//                param.setLimitStrategyuuid(uuid);
//                param.setLimitStrategyTotal(total);
//                apiInfoVersionsMapper.updateByExample(param, versionsTestExample);
//                //同步latest表和redis-api,redis-httpPath
//                updateApiLatestAndRedis(apiId,param);
//            }
//        }
        if (StringUtils.isNotBlank(apiInfo.getProductEnvVersion())) {
            ApiInfoVersionsExample versionsProductExample = new ApiInfoVersionsExample();
            ApiInfoVersionsExample.Criteria versionsProductCriteria = versionsProductExample.createCriteria();
            versionsProductCriteria.andVersionIdEqualTo(apiInfo.getProductEnvVersion());
            versionsProductCriteria.andEnvEqualTo(CommonStatus.ONLINE);
            List<ApiInfoVersionsWithBLOBs> apiVersionsTest = apiInfoVersionsMapper.selectByExampleWithBLOBs(versionsProductExample);
            if (CollectionUtil.isNotEmpty(apiVersionsTest)) {
                BeanUtils.copyProperties(apiVersionsTest.get(0), param);
                param.setLimitStrategyuuid(uuid);
                param.setLimitStrategyTotal(total);
                apiInfoVersionsMapper.updateByExample(param, versionsProductExample);
                //同步latest表和redis策略
                updateApiLatestAndRedis(apiId,param);
            }

        }
    }

    @Override
    public void setGroupIdToNull(Integer apiId) {
        ApiInfoWithBLOBs apiInfo = apiInfoMapper.selectByPrimaryKey(apiId);
        if (CommonStatus.DISENABLE == apiInfo.getStatus()) {
            throw new BusinessException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        apiInfo.setApiGroupId(null);
        apiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.updateByPrimaryKeyWithBLOBs(apiInfo);
    }

    @Override
    public PageInfo<ApiInfoVersionsWithBLOBs> findApiByStrategy(CurrentLimitStrategyQuery strategyQuery) {
        String uuid = strategyQuery.getUuid();
        ApiInfoVersionsExample example = new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andLimitStrategyuuidEqualTo(uuid);
        criteria.andCurrentVersionEqualTo(CommonStatus.ENABLE);
        //设置按创建时间倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiPage = PageHelper.startPage(strategyQuery.getPageNum(), strategyQuery.getPageSize());
        apiInfoVersionsMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiPage.toPageInfo();
        pageInfo.setList(apiPage.getResult());
        return pageInfo;
    }

    @Override
    public PageInfo<ApiExpand> findApiAndNoGroup(ApiInfoQuery apiInfoQuery) {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        Integer env = apiInfoQuery.getEnv();
        if (null != env) {
            if (CommonStatus.ONLINE == env) {
                criteria.andPublishProductEnvStatusEqualTo(CommonStatus.ENABLE);
            }
//            else if (CommonStatus.ADVANCE == env) {
//                criteria.andPublishTestEnvStatusEqualTo(CommonStatus.ENABLE);
//            }
            else {
                throw new DataValidationException(StringUtils.join("env", ExceptionInfo.ERROR));
            }
        }
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        criteria.andApiGroupIdIsNull();
        //设置按创建时间倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiExpandPage = PageHelper.startPage(apiInfoQuery.getPageNum(), apiInfoQuery.getPageSize());
        apiInfoMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiExpandPage.toPageInfo();
        pageInfo.setList(apiExpandPage.getResult());
        return pageInfo;
    }

    private List<ApiInfoWithBLOBs> getApiAndNoGroup(List<ApiInfoWithBLOBs> list) {
        ApiInfoWithBLOBs apiInfo;
        for (int i = 0; i < list.size(); i++) {
            apiInfo = list.get(i);
            if (apiInfo.getApiGroupId() != null) {
                list.remove(i);
                i = i - 1;
            }
        }
        return list;
    }

    @Override
    public boolean checkUniqueApiNameGlobal(ApiQuery apiQuery) {
        //查询条件为空，则返回false
        if (null == apiQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_QUERY);
        }
        String apiName = apiQuery.getApiName();
        Integer apiId = apiQuery.getApiId();
        if (StringUtils.isBlank(apiName)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APINAME);
        }
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        if (null != apiId) {
            criteria.andIdNotEqualTo(apiId);
        }

        criteria.andApiNameEqualTo(apiName);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> apiList = apiInfoMapper.selectByExampleWithBLOBs(example);
        return null == apiList || apiList.size() == 0;
    }

    /**
     * 保存三方结果的参数抽取与转换
     *
     * @param apiResultEtl
     */
    @Transactional
    @Override
    public void save(ApiResultEtl apiResultEtl) {
        //验证参数必填相关属性的逻辑正确性：即属性必填时，默认值应为null

        //设置状态和时间
        Long createTime = System.currentTimeMillis();
        apiResultEtl.setId(null);
        apiResultEtl.setStatus(CommonStatus.ENABLE);
        apiResultEtl.setCreateTime(createTime);
        apiResultEtl.setUpdateTime(createTime);
        apiResultEtlMapper.insert(apiResultEtl);
    }

    @Override
    public boolean hasNoneOfApi(Integer groupId) {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        criteria.andApiGroupIdEqualTo(groupId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> apiList = apiInfoMapper.selectByExampleWithBLOBs(example);
        return null == apiList || apiList.size() == 0;
    }

    @Override
    public void addExistedApi(Integer groupId, Integer apiId) {
        if (!apiGroupService.hasNoneOfLowerLevelGroup(groupId)) {
            throw new BusinessException(ExceptionInfo.THE_GROUP_HAS_LOWER_LEVEL_GROUP);
        }
        //添加到关系表
        this.addRelationOfApiAndGroup(apiId, groupId);
        //添加groupId到apiInfo表
        ApiInfoWithBLOBs apiInfo = this.findById(apiId);

        apiInfo.setApiGroupId(groupId);
        apiInfo.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.updateByPrimaryKey(apiInfo);
    }

    @Override
    public boolean checkStrategyUsed(String uuid) {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        criteria.andLimitStrategyuuidEqualTo(uuid);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> list = apiInfoMapper.selectByExampleWithBLOBs(example);
        return null == list || list.size() == 0;
    }

    @Override
    public PageInfo<ApiExpand> apiInfoListByGroup(ApiInfoQuery apiInfoQuery) {
        //参数校验
        if (null == apiInfoQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        //无分组的api列表查询
        Integer flagNoGroup = apiInfoQuery.getFlagNoGroup();
        if (null != flagNoGroup && flagNoGroup.equals(1)) {
            return this.findApiAndNoGroup(apiInfoQuery);
        }
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        if (null != apiInfoQuery.getApiName()) {
            String apiName = apiInfoQuery.getApiName().replace("_", "\\_");
            criteria.andApiNameLike(StringUtils.wrap(apiName, "%"));
        }
        if (null != apiInfoQuery.getApiId()) {
        	Integer apiId = apiInfoQuery.getApiId();
        	criteria.andIdEqualTo(apiId);
        }
        if (null != apiInfoQuery.getApiGroupId()) {
            Integer apiGroupId = apiInfoQuery.getApiGroupId();
            ApiGroupRelationExample relationExample = new ApiGroupRelationExample();
            ApiGroupRelationExample.Criteria relationCriteria = relationExample.createCriteria();
            relationCriteria.andPathLike(StringUtils.wrap(StringUtils.wrap(apiGroupId.toString(), "."), "%"));
            List<ApiGroupRelation> relationList = apiGroupRelationMapper.selectByExample(relationExample);
            if (null != relationList && relationList.size() > 0) {
                Iterator<ApiGroupRelation> relationIterator = relationList.iterator();
                ApiGroupRelation relation;
                List<Integer> apiIdList = new ArrayList<Integer>();
                while (relationIterator.hasNext()) {
                    relation = relationIterator.next();
                    apiIdList.add(relation.getApiId());
                }
                criteria.andIdIn(apiIdList);
            } else {//分组下无api结果返回
                Page apiExpandPage = PageHelper.startPage(apiInfoQuery.getPageNum(), apiInfoQuery.getPageSize());
                PageInfo pageInfo = apiExpandPage.toPageInfo();
                return pageInfo;
            }
        }
//        Integer env = apiInfoQuery.getEnv();
//        if (null != env) {
//            if (CommonStatus.ONLINE.equals(env)) {
//                LOG.info("test");
//                criteria.andPublishProductEnvStatusEqualTo(CommonStatus.ENABLE);
//            }
////            else if (CommonStatus.ADVANCE == env) {
////                criteria.andPublishTestEnvStatusEqualTo(CommonStatus.ENABLE);
////            }
//            else {
//                throw new DataValidationException(StringUtils.join("env", ExceptionInfo.ERROR));
//            }
//        }
        //status默认为可用
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        //设置按创建时间倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiExpandPage = PageHelper.startPage(apiInfoQuery.getPageNum(), apiInfoQuery.getPageSize());
        apiInfoMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiExpandPage.toPageInfo();
        pageInfo.setList(this.packageGroupNameAndStrategyName(apiExpandPage.getResult()));
        return pageInfo;
    }

    @Override
    public PageInfo<ApiExpand> apiInfoListByApiIdList(ApiIdListQuery apiIdListQuery,List<Integer> validApiIdList) {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(validApiIdList);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        example.setOrderByClause("id ASC");
        Page page = PageHelper.startPage(apiIdListQuery.getPageNum(), apiIdListQuery.getPageSize());
        apiInfoMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = page.toPageInfo();
//        pageInfo.setList(page.getResult());
        pageInfo.setList(this.packageGroupNameAndStrategyName(page.getResult()));
        return pageInfo;
    }

    @Override
    public boolean checkUniqueInterfaceHttpPath(ApiQuery apiQuery) {
        //查询条件为空，则返回false
        if (null == apiQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_QUERY);
        }

        String backEndPath = apiQuery.getBackEndPath();
        Integer apiId = apiQuery.getApiId();

        if (StringUtils.isBlank(backEndPath)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_HTTPPATH);
        }

        ApiInfoExample example1 = new ApiInfoExample();
        ApiInfoExample.Criteria criteria1 = example1.createCriteria();

        criteria1.andBackEndPathEqualTo(backEndPath);
        criteria1.andStatusEqualTo(CommonStatus.ENABLE);
        if (null != apiId) {
            criteria1.andIdNotEqualTo(apiId);
        }

        List<ApiInfoWithBLOBs> apiList = apiInfoMapper.selectByExampleWithBLOBs(example1);
        boolean partA = (null == apiList || apiList.size() == 0);

        ApiRateDistributeExample example2 = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andBackEndPathEqualTo(backEndPath);
        criteria2.andStatusEqualTo(CommonStatus.ENABLE);
        Integer disId = apiQuery.getDisId();
        if (null != disId) {
            criteria2.andIdNotEqualTo(disId);
        }

        List<ApiRateDistribute> subApiList = subApiInfoMapper.selectByExample(example2);
        boolean partB = (null == subApiList || subApiList.size() == 0);
        return (partA && partB);
    }

    @Override
    public String convertApiRiskIndex(IDInfo idInfo, List<RequestParamAndValue> headerList, List<RequestParamAndValue> queryList, List<RequestParamAndValue> bodyList,Integer apiId) throws IOException {

        Map map = debugging(idInfo, headerList, queryList, bodyList);
        log.info("response: {}" + map.toString());
        List<ApiRiskIndex> riskIndices = selectApiRiskIndexByApiId(apiId);
        Object dataField = map.get("resultString");
        log.info("dataField : {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataField));
        JsonNode root = objectMapper.readTree(map.get("resultString").toString());
        JsonNode dataNode = root.path(DATA);
        for (ApiRiskIndex riskIndex : riskIndices) {
            List<String> list = riskIndex.getIndexFields();
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i);
                if (DATA.equals(str)) {
                    continue;
                }
                if (dataNode.isObject() && i != list.size()) {
                    replaceContainerNode(dataNode, list.get(i), i , list, riskIndex);
                } else {
                    String key = list.get(i);
                    JsonNode node = dataNode.get(key);
                    if ( node != null) {
                        ((ObjectNode) dataNode).remove(key);
                        ((ObjectNode) dataNode).set(riskIndex.getRiskIndexName(), node);
                    }
                }

            }
        }
        return objectMapper.writeValueAsString(root);
    }

    @Override
    public String removeRedisKey(IDInfo idInfo, List<RequestParamAndValue> headerList, List<RequestParamAndValue> queryList, List<RequestParamAndValue> bodyList, Integer apiId) throws IOException {

        RequestParamAndValue requestParamAndValue = new RequestParamAndValue();
        requestParamAndValue.setParamName(API_CACHE);
        requestParamAndValue.setParamValue(API_CACHE);
        headerList.add(requestParamAndValue);
        Map map = debugging(idInfo, headerList, queryList, bodyList);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    }

    private static void replaceContainerNode(JsonNode jsonNode, String path, int i, List<String> list, ApiRiskIndex riskIndex) {
        JsonNode node = jsonNode.get(path);
        if (node == null)
            return;
        if (node.isValueNode()) {
            String key = list.get(i);
            ((ObjectNode) jsonNode).remove(key);
            ((ObjectNode) jsonNode).put(riskIndex.getRiskIndexName(), node.asText());
        } else if (node.isObject()) {
            i++;
            String key = list.get(i);
            JsonNode temptNode = node.get(key);
            if (temptNode == null)
                return;
            if (i < list.size() - 1 ) {
                replaceContainerNode(temptNode, list.get(i + 1), i + 1, list, riskIndex);
            } else {
                ((ObjectNode) node).remove(key);
                ((ObjectNode) node).set(riskIndex.getRiskIndexName(), temptNode);
            }
        } else if (node.isArray()) {
            String key = list.get(i);
            ArrayNode arrayNode = (ArrayNode) jsonNode.get(key);
            for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                JsonNode arrayNodeChild = it.next();
                if (arrayNodeChild.isValueNode()) {
                    ((ObjectNode) jsonNode).remove(key);
                    ((ObjectNode) jsonNode).set(riskIndex.getRiskIndexName(), arrayNode);
                } else if (arrayNodeChild.isObject()) {
                    String arrayKey = list.get(i + 1);
                    JsonNode value = arrayNodeChild.get(arrayKey);
                    ((ObjectNode) arrayNodeChild).remove(arrayKey);
                    ((ObjectNode) arrayNodeChild).set(riskIndex.getRiskIndexName(), value);
                }
            }

        }
    }

    /**
     * 批量发布获取有效的(数据库中有效)apiId
     * @param apiIdList
     * @return
     */
    @Override
    public String chooseValidApiID(String apiIdList) {
        Set<Integer> set = getApiIdList(apiIdList);
        if (set == null){
            log.info("请按照格式输入正确的apiId");
            return null;
        }
        StringBuilder validApiIdList = new StringBuilder();
        StringBuilder invalidApiIdList = new StringBuilder();
        for (Object apiId: set){
            ApiInfoWithBLOBs apiInfo = apiInfoMapper.selectByPrimaryKey(Integer.parseInt(apiId.toString()));//根据id查询接口是否存在
            if (null == apiInfo || CommonStatus.DISENABLE == apiInfo.getStatus()) {
                invalidApiIdList.append(apiId+" ");
            }else{
                validApiIdList.append(apiId+" ");
            }
        }
        if(StringUtils.isNotBlank(invalidApiIdList.toString())){
            log.info("不存在|不可用的apiId: {} ",invalidApiIdList.toString());
        }
        if(StringUtils.isBlank(validApiIdList.toString())){
            log.info("有效apiId为空！");
            return null;
        }
        return validApiIdList.toString();
    }
    /**
     * 批量发布获取有效的(数据库中有效)apiId
     * @param apiIdList
     * @return
     */
    @Override
    public List<Integer> chooseValidListApiID(String apiIdList) {
        Set<Integer> set = getApiIdList(apiIdList);
        List<Integer> validList =new ArrayList();
        if (set == null){
            log.info("请按照格式输入正确的apiId");
            return null;
        }
        StringBuilder validApiIdList = new StringBuilder();
        StringBuilder invalidApiIdList = new StringBuilder();
        for (Object apiId: set){
            ApiInfoWithBLOBs apiInfo = apiInfoMapper.selectByPrimaryKey(Integer.parseInt(apiId.toString()));//根据id查询接口是否存在
            if (null == apiInfo || CommonStatus.DISENABLE == apiInfo.getStatus()) {
                invalidApiIdList.append(apiId+" ");
            }else{
                validApiIdList.append(apiId+" ");
                validList.add(Integer.valueOf(apiId.toString()));
            }
        }
        if(StringUtils.isNotBlank(invalidApiIdList.toString())){
            log.info("不存在|不可用的apiId: {} ",invalidApiIdList.toString());
        }
        if (validList.size()==0)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        return validList;
    }
    @Override
    public Map autoTest(Integer apiId) throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("apiId", apiId);
        List<AutoTestResponse> results = apiInfoMapper.selectRequestParamsForAutoTest(map);
        List<RequestParamAndValue> headerList = new ArrayList<>();
        List<RequestParamAndValue> queryList = new ArrayList<>();
        List<RequestParamAndValue> bodyList = new ArrayList<>();
        IDInfo idInfo = new IDInfo();
        idInfo.setApiId(apiId);
        for (AutoTestResponse autoTest : results) {
            Integer location = autoTest.getParamsLocation();
            RequestParamAndValue paramAndValue = new RequestParamAndValue();
            paramAndValue.setParamName(autoTest.getParamName());
            paramAndValue.setParamValue(autoTest.getParamValue());
            if (1 == location) {
                headerList.add(paramAndValue);
            } else if(2 == location) {
                queryList.add(paramAndValue);
            } else if(3 == location) {
                bodyList.add(paramAndValue);
            }
        }
        List<RequestParamAndValue> copyOfHeaderList = new ArrayList<>();
        copyOfHeaderList.addAll(headerList);
        List<RequestParamAndValue> copyOfQueryList = new ArrayList<>();
        copyOfQueryList.addAll(queryList);
        List<RequestParamAndValue> copyOfBodyList = new ArrayList<>();
        copyOfBodyList.addAll(bodyList);
        String removeRedisKeyResult = removeRedisKey(idInfo, copyOfHeaderList, copyOfQueryList, copyOfBodyList, apiId);
        log.info("redis key: {}", removeRedisKeyResult);
        if (StringUtils.isNotBlank(removeRedisKeyResult)) {
            return debugging(idInfo, headerList, queryList, bodyList);
        }
        return debugging(idInfo, headerList, queryList, bodyList);
    }

    @Override
    public List<ApiInfoVersionLatestResponse> findApiInfoVersionsLatestForAutoTest(Integer groupId) {

        return apiInfoVersionsLatestMapper.selectApiInfoLatestForAutoTest(groupId);
    }

    @Override
    public int insertTestResult(String testResult, Integer apiId, Integer apiGroupId, String apiName) {
        AutoTestResult autoTestResult = new AutoTestResult();
        autoTestResult.setApiId(apiId);
        autoTestResult.setTestDate(new Date());
        autoTestResult.setTestResult(testResult);
        autoTestResult.setApiGroupId(apiGroupId);
        autoTestResult.setApiName(apiName);
        log.info("自动测试结果,apiId:{},result:{}", apiId, testResult);
        return autoTestMapper.insert(autoTestResult);
    }

    @Override
    public PageInfo<AutoTestResult> autoTestList(AutoTestQuery query) {

        Page<AutoTestResult> apiExpandPage = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        AutoTestExample example = new AutoTestExample();
        example.setApiId(query.getApiId());
        example.setApiGroupId(query.getApiGroupId());
        example.setStartDate(query.getStartDate());
        example.setEndDate(query.getEndDate());
        autoTestMapper.selectByExample(example);
        PageInfo<AutoTestResult> pageInfo = apiExpandPage.toPageInfo();
        pageInfo.setList(apiExpandPage.getResult());
        return pageInfo;
    }


    @Override
    public int configAutoTestOn(Integer apiId) {
        return apiInfoVersionsLatestMapper.changeAutoTestStatusAsYes(apiId);
    }

    @Override
    public int configAutoTestOff(Integer apiId) {
        return apiInfoVersionsLatestMapper.changeAutoTestStatusAsNo(apiId);

    }

    @Override
    public PageInfo<ApiInfoVersionLatestResponse> selectApiInfoLatestExample(ApiInfoLatestQuery query) {

        Page<ApiInfoVersionLatestResponse> apiExpandPage = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        ApiInfoLatestExample example = new ApiInfoLatestExample();
        example.setApiId(query.getApiId());
        example.setApiGroupId(query.getApiGroupId());
        example.setId(query.getId());
        example.setAutoTest(query.getAutoTest());
        example.setApiName(query.getApiName());
        apiInfoVersionsLatestMapper.selectApiInfoLatestByExample(example);
        PageInfo<ApiInfoVersionLatestResponse> pageInfo = apiExpandPage.toPageInfo();
        pageInfo.setList(apiExpandPage.getResult());
        return pageInfo;
    }


    /**
     * 批量发布获取apiIdList
     * @param apiIdList
     * @return
     */
    public Set getApiIdList(String apiIdList){
        String[] apiIdArray = apiIdList.split(" ");
        Set<Integer> set = new HashSet<>();
        for (int i=0;i<apiIdArray.length;i++){
            if(apiIdArray[i].contains("-")){//含有-，10-20
                int start = Integer.parseInt(apiIdArray[i].substring(0,apiIdArray[i].lastIndexOf("-")));
                int end = Integer.parseInt(apiIdArray[i].substring(apiIdArray[i].lastIndexOf("-")+1,apiIdArray[i].length()));
                if (start>=end){
                    log.info("start apiId 不小于 end apiId!");
                    continue;
                }else{
                    while(start<=end){
                        set.add(start++);
                    }
                }
            }else{
                set.add(Integer.parseInt(apiIdArray[i]));
            }
        }
        log.info("getApiList: {}",set);
        return set;
    }

    private void copy(List<RequestParamAndValue> dest, List<RequestParamAndValue> src) {
        int srcSize = src.size();
        ListIterator<RequestParamAndValue> di = dest.listIterator();
        ListIterator<RequestParamAndValue> si = src.listIterator();
        for (int i = 0; i < srcSize; i++) {
            di.next();
            di.set(si.next());
        }
    }

    /**
     * 同步latest表和redis策略
     * @param apiId
     * @param param
     */
    public void updateApiLatestAndRedis(Integer apiId,ApiInfoVersionsWithBLOBs param){
        //有发布版本，修改latest表策略
        apiInfoVersionsLatestMapper.updateByPrimaryKeyWithBLOBs(param);
        //更新redis接口缓存信息
        com.alibaba.fastjson.JSONObject jsonObject = redisService.get("api:"+apiId);
        if (jsonObject != null){
            ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
            apiInfoRedisMsg.setApiInfoWithBLOBs(param);
            redisService.setToCaches("api:"+apiId,apiInfoRedisMsg);
            log.info("绑定/更改限流策略，更新接口redis缓存,key = {}","api:"+apiId);
            //更新redis-httpPath缓存
            redisService.setRedisHttpPath2Cache(param);
        }else{
            log.info("绑定/更改限流策略，接口redis缓存,key = {}不存在","api:"+apiId);
        }
    }

    /**
     * 同步api_info中的jsonConfig到latest和redis
     * @return
     */
    @Override
    public List<Integer> synchroJsonConfig() {
        List<Integer> failId = new ArrayList<>();
        List<Integer> successId = new ArrayList<>();
        List<Integer> successRedisId = new ArrayList<>();

        ApiInfoVersionsExample example=new ApiInfoVersionsExample();
        ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdIsNotNull();//查询latest表所有记录
        List<ApiInfoVersionsWithBLOBs> list = apiInfoVersionsLatestMapper.selectByExampleWithBLOBs(example);
        log.info("latest表查到{}条记录",list.size());
        if (list!=null && list.size()>0){
            for (ApiInfoVersionsWithBLOBs latest:list){//遍历latest记录
                Integer apiId = latest.getApiId();
                ApiInfoWithBLOBs apiInfo = apiInfoMapper.selectByPrimaryKey(apiId);//api_info查询对应接口信息
                if (latest.getJsonConfig() == null && apiInfo !=null) {//查询到接口信息
                    latest.setJsonConfig(apiInfo.getJsonConfig());//将api_info的jsonConfig赋值给latest表对应接口
                    int result = apiInfoVersionsLatestMapper.updateByPrimaryKeyWithBLOBs(latest);//更新latest接口记录
                    successId.add(apiId);
                    if (result == 1){//更新成功，同步redis
                       com.alibaba.fastjson.JSONObject jsonObject = redisService.get("api:"+apiId);
                        if (jsonObject!=null){
                            ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
                            apiInfoRedisMsg.setApiInfoWithBLOBs(latest);
                        //存入redis缓存中
                            redisService.setToCaches("api:"+apiId,apiInfoRedisMsg);
                            successRedisId.add(apiId);
                        }else{
//                            log.info("redis不存在api:{},同步jsonconfig时失败,请重新发布接口",apiId);
                            failId.add(apiId);
                        }
                    }
                }
            }
        }
        log.info("successId={}",successId.size());
        log.info("successRedisId={}",successRedisId.size());
        log.info("failId={}",failId.size());
        return failId;
    }

    /**
     * 接口复制
     * @param apiExpand
     * @return
     */
    @Transactional
    @Override
    public Integer copy(ApiExpand apiExpand) {
        Integer apiId = apiExpand.getId();
        //根据接口id查询原始接口apiInfo信息
        ApiInfoWithBLOBs bloBs =  apiInfoMapper.selectByPrimaryKey(apiId);
        //校验接口唯一性，验证是否存在已经复制的接口(根据前端接口名称-路径-后端接口名称验证，编辑页面对这些参数做了唯一性校验)
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        criteria.andApiNameEqualTo(bloBs.getApiName());
        criteria.andInterfaceNameEqualTo(bloBs.getInterfaceName());
        criteria.andHttpPathEqualTo(bloBs.getHttpPath());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> list =  apiInfoMapper.selectByExample(example);
        if (list!=null && list.size()>1){
            throw new DataValidationException(ExceptionInfo.API_COPY_EXIST);//存在已经复制的接口
        }
        //接口处理-不存在未修改的复制接口，插入新接口（版本相关置为空,新复制接口无发布版本）
        bloBs.setPublishProductEnvStatus(CommonStatus.DISENABLE);
        bloBs.setPublishTestEnvStatus(CommonStatus.DISENABLE);
        bloBs.setProductEnvVersion(null);
        bloBs.setTestEnvVersion(null);
        bloBs.setId(null);
        bloBs.setCreateTime(System.currentTimeMillis());
        bloBs.setUpdateTime(System.currentTimeMillis());
        apiInfoMapper.insert(bloBs);
        //查询插入接口id（根据接口名称查询，按照数据库插入id自增原则，取较大的id即为新复制接口id）
        ApiInfoExample example1 = new ApiInfoExample();
        ApiInfoExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andApiNameEqualTo(bloBs.getApiName());
        criteria1.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> listNew =  apiInfoMapper.selectByExample(example1);
        Integer newApiId =null;
        if (listNew!=null && listNew.size()>1){
            newApiId = Math.max(listNew.get(0).getId(),listNew.get(1).getId());
        }
        //分组处理-获取多级分组信息
        Integer apiGroupId = bloBs.getApiGroupId();
        String path = null;
        if (newApiId != null) {
            path = this.getRelationPath(apiGroupId, newApiId);
        }
        String fullPathName = this.getFullPathName(apiGroupId);
        ApiGroupRelation apiGroupRelation = new ApiGroupRelation();
        apiGroupRelation.setId(null);
        apiGroupRelation.setApiId(newApiId);
        apiGroupRelation.setGroupId(apiGroupId);
        apiGroupRelation.setPath(path);
        apiGroupRelation.setFullPathName(fullPathName);
        apiGroupRelation.setCreateTime(System.currentTimeMillis());
        apiGroupRelationMapper.insert(apiGroupRelation);

        //前端参数处理-查询apiId对应前端参数，作为newApiId前端参数插入requestParams表中
        RequestParamsExample requestParamsExample = new RequestParamsExample();
        RequestParamsExample.Criteria requestParamsExampleCriteria = requestParamsExample.createCriteria();
        requestParamsExampleCriteria.andApiIdEqualTo(apiId);
        requestParamsExampleCriteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<RequestParams> paramsList = requestParamsMapper.selectByExample(requestParamsExample);
        Integer finalNewApiId = newApiId;
        paramsList.forEach(requestParams -> {
            requestParams.setId(null);
            requestParams.setApiId(finalNewApiId);
            requestParams.setCreateTime(System.currentTimeMillis());
            requestParams.setUpdateTime(System.currentTimeMillis());
            requestParamsMapper.insert(requestParams);
        });
        //后端参数处理-查询apiId对应的后端参数，作为newApiId后端参数插入backendRequestParams表中
        BackendRequestParamsExample backendRequestParamsExample = new BackendRequestParamsExample();
        BackendRequestParamsExample.Criteria backParamsCriteria = backendRequestParamsExample.createCriteria();
        backParamsCriteria.andApiIdEqualTo(apiId);
        backParamsCriteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<BackendRequestParams> backendRequestParamsList = backendMapper.selectByExample(backendRequestParamsExample);
        backendRequestParamsList.forEach(backendRequestParams -> {
            backendRequestParams.setId(null);
            backendRequestParams.setApiId(finalNewApiId);
            backendRequestParams.setUpdateTime(System.currentTimeMillis());
            backendRequestParams.setCreateTime(System.currentTimeMillis());
            backendMapper.insert(backendRequestParams);
        });
        //前后端参数对应关系建立
        BackendRequestParamsExample newBackendRequestParamsExample = new BackendRequestParamsExample();
        BackendRequestParamsExample.Criteria newBbackParamsCriteria = newBackendRequestParamsExample.createCriteria();
        newBbackParamsCriteria.andApiIdEqualTo(finalNewApiId);
        newBbackParamsCriteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<BackendRequestParams> newBackendRequestParamsList = backendMapper.selectByExample(newBackendRequestParamsExample);
        newBackendRequestParamsList.forEach(backendRequestParams -> {
            //获取新插入参数前端id
            Integer oldRequestParamsId = backendRequestParams.getRequestParamsId();
            if (null != oldRequestParamsId){
                //获取前端id对应参数名称
                String paramName = requestParamsMapper.selectByPrimaryKey(oldRequestParamsId).getParamName();
                //根据参数名称和新接口apiId获取新接口id，用于更新后端参数表中前端id，建立前后端参数对应关系(在复制过程中，通过paramName查询建立关系)
                RequestParamsExample paramsExample = new RequestParamsExample();
                RequestParamsExample.Criteria paramsExampleCriteria = paramsExample.createCriteria();
                paramsExampleCriteria.andApiIdEqualTo(finalNewApiId);
                paramsExampleCriteria.andParamNameEqualTo(paramName);
                paramsExampleCriteria.andStatusEqualTo(CommonStatus.ENABLE);
                Integer newRequestParamsId =  requestParamsMapper.selectByExample(paramsExample).get(0).getId();
                //更新后端参数对应前端id，建立前后端参数连接
                backendRequestParams.setRequestParamsId(newRequestParamsId);
                backendRequestParams.setUpdateTime(System.currentTimeMillis());
                backendMapper.updateByPrimaryKey(backendRequestParams);
            }
        });
        return newApiId;
    }

    /**
     * 接口导出
     * @param apiId
     * @return
     */
    @Override
    public Integer apiExport(Integer apiId,String filePath) {
        FileOutputStream outFile = null;
        LOG.info("文件路径:{}",filePath);
        try {
            OutputFileUtil.createFile(filePath);
            outFile = new FileOutputStream(filePath, true);
            //sql语句
            StringBuilder sb = new StringBuilder();

            //查询apiInfo
            ApiInfoWithBLOBs bloBs = apiInfoMapper.selectByPrimaryKey(apiId);

            //根据分组名称查询分组id
            ApiGroupExample exampleGroup = new ApiGroupExample();
            ApiGroupExample.Criteria criteriaGroup = exampleGroup.createCriteria();
            criteriaGroup.andIdEqualTo(bloBs.getApiGroupId());
            criteriaGroup.andStatusEqualTo(CommonStatus.ENABLE);
            List<ApiGroup> groupList = apiGroupMapper.selectByExample(exampleGroup);
            String gName = groupList.get(0).getGroupName();
            sb.append("select id from api_group where groupName='").append(gName).append("' and status=1 into @apiGroupId;\n");

            //insert api
            bloBs.setPublishTestEnvStatus(CommonStatus.DISENABLE);
            bloBs.setPublishProductEnvStatus(CommonStatus.DISENABLE);
            bloBs.setProductEnvVersion(null);
            bloBs.setTestEnvVersion(null);
            String insertApi = InsertSqlUtil.creatInsert("api_info",bloBs);
            sb.append(insertApi).append("\n");
            sb.append("select id from api_info where apiName=\'"+bloBs.getApiName()+"\' and status=1 into @result;\n");

            //查询分组
            ApiGroupRelation apiGroupRelation = apiGroupRelationMapper.selectByApiId(apiId);
            String path = apiGroupRelation.getPath();
            int index = path.indexOf(".",3);
            path=path.substring(0,index+1);
            apiGroupRelation.setPath("CONCAT('"+path+"',@apiGroupId,'.',@result)");//CONCAT('.9.10.128.',@result)
            String groupRelation = InsertSqlUtil.creatInsert("api_group_relation",apiGroupRelation);
            sb.append(groupRelation).append("\n");
//            //查询前端参数
            RequestParamsExample example = new RequestParamsExample();
            RequestParamsExample.Criteria criteria = example.createCriteria();
            criteria.andApiIdEqualTo(apiId);
            criteria.andStatusEqualTo(CommonStatus.ENABLE);
            List<RequestParams> requestParamsList= requestParamsMapper.selectByExample(example);
            requestParamsList.forEach(requestParams -> {
                String param = InsertSqlUtil.creatInsert("request_params",requestParams);
                sb.append(param).append("\n");
                String selectParamId = "select id from request_params where apiId=@result and paramName='"+requestParams.getParamName()+"' and status=1 into @paramId;";
                sb.append(selectParamId).append("\n");
                //查询后端
                BackendRequestParams backendParams = backendMapper.selectByRequestParamsId(requestParams.getId());
                String backParam = InsertSqlUtil.creatInsert("backend_request_params",backendParams);
                sb.append(backParam).append("\n");

            });
//            //查询后端参数
            BackendRequestParamsExample exampleBack = new BackendRequestParamsExample();
            BackendRequestParamsExample.Criteria criteriaBack = exampleBack.createCriteria();
            criteriaBack.andApiIdEqualTo(apiId);
            criteriaBack.andStatusEqualTo(CommonStatus.ENABLE);
            List<BackendRequestParams> backParamList = backendMapper.selectByExample(exampleBack);
            backParamList.forEach(backendRequestParams -> {
                if (null == backendRequestParams.getRequestParamsId()){
                    String backParam = InsertSqlUtil.creatInsert("backend_request_params",backendRequestParams);
                    sb.append(backParam).append("\n");
                }
            });
            //分组名
            String groupName = "";
            ApiGroup apiGroup = apiGroupMapper.selectByPrimaryKey(apiGroupRelation.getGroupId());
            if (apiGroup != null)
                groupName = apiGroup.getGroupName();
            //接口名
            String apiName = bloBs.getApiName();
            String memo = "-- 分组:" + groupName + " 接口:" + apiName + "\n";
            LOG.info(memo);
            String str = memo + sb.toString().replaceAll("\'@result\'","@result")
                    .replaceAll("'CONCAT","CONCAT").replaceAll("@result\\)'","@result)")
                    .replaceAll("'@paramId'","@paramId").replaceAll("'@apiGroupId'","@apiGroupId");
            outFile.write(str.getBytes(StandardCharsets.UTF_8));
            //后续文件操作
        } catch (Exception e) {
            LOG.error("API导出异常",e.getMessage());
        } finally {
            try {
                if( outFile != null) outFile.close();
            } catch (IOException e) {
                LOG.error("API导出输出流关闭异常",e.getMessage());
            }
        }
        return apiId;
    }

    /**
     * 组合接口列表
     * @param apiInfoQuery
     * @return
     */
    @Override
    public PageInfo<ApiExpand> combinationApiList(ApiInfoQuery apiInfoQuery) {
        //参数校验
        if (null == apiInfoQuery) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        //无分组的api列表查询
        Integer flagNoGroup = apiInfoQuery.getFlagNoGroup();
        if (null != flagNoGroup && flagNoGroup.equals(1)) {
            return this.findApiAndNoGroup(apiInfoQuery);
        }
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        if (null != apiInfoQuery.getApiName()) {
            String apiName = apiInfoQuery.getApiName().replace("_", "\\_");
            criteria.andApiNameLike(StringUtils.wrap(apiName, "%"));
        }
        if (null != apiInfoQuery.getApiId()) {
            Integer apiId = apiInfoQuery.getApiId();
            criteria.andIdEqualTo(apiId);
        }
        if (null != apiInfoQuery.getApiGroupId()) {
            Integer apiGroupId = apiInfoQuery.getApiGroupId();
            ApiGroupRelationExample relationExample = new ApiGroupRelationExample();
            ApiGroupRelationExample.Criteria relationCriteria = relationExample.createCriteria();
            relationCriteria.andPathLike(StringUtils.wrap(StringUtils.wrap(apiGroupId.toString(), "."), "%"));
            List<ApiGroupRelation> relationList = apiGroupRelationMapper.selectByExample(relationExample);
            if (null != relationList && relationList.size() > 0) {
                Iterator<ApiGroupRelation> relationIterator = relationList.iterator();
                ApiGroupRelation relation;
                List<Integer> apiIdList = new ArrayList<Integer>();
                while (relationIterator.hasNext()) {
                    relation = relationIterator.next();
                    apiIdList.add(relation.getApiId());
                }
                criteria.andIdIn(apiIdList);
            } else {//分组下无api结果返回
                Page apiExpandPage = PageHelper.startPage(apiInfoQuery.getPageNum(), apiInfoQuery.getPageSize());
                PageInfo pageInfo = apiExpandPage.toPageInfo();
                return pageInfo;
            }
        }
        Integer env = apiInfoQuery.getEnv();
        if (null != env) {
            if (CommonStatus.ONLINE == env) {
                criteria.andPublishProductEnvStatusEqualTo(CommonStatus.ENABLE);
            }
//            else if (CommonStatus.ADVANCE == env) {
//                criteria.andPublishTestEnvStatusEqualTo(CommonStatus.ENABLE);
//            }
            else {
                throw new DataValidationException(StringUtils.join("env", ExceptionInfo.ERROR));
            }
        }
        //status默认为可用
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        //组合接口
        criteria.andJsonConfigLike("%"+ Constant.COMBINATION_TYPE+"%");
        criteria.andJsonConfigLike("%"+Constant.COMBINATION_VALUE+"%");
        //设置按创建时间倒序排序
        example.setOrderByClause("createTime DESC");
        Page apiExpandPage = PageHelper.startPage(apiInfoQuery.getPageNum(), apiInfoQuery.getPageSize());
        apiInfoMapper.selectByExampleWithBLOBs(example);
        PageInfo pageInfo = apiExpandPage.toPageInfo();
        pageInfo.setList(this.packageGroupNameAndStrategyName(apiExpandPage.getResult()));
        return pageInfo;
    }

    @Override
    public boolean createProContract(List<Integer> apiIdList, String appKey) {
        com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
        List<Integer> failList = new ArrayList<>();
        for (Integer apiId : apiIdList) {
            //获取接口名称
            ApiInfoWithBLOBs bloBs = findById(apiId);
            object.put("apiId",apiId);
            object.put("appKey",appKey);
            object.put("userId","admin");
            object.put("apiName",bloBs.getApiName());
            /*ProContractResponse response = authManager.publicProContract(object);
            LOG.info("apiId={},code={},message={}",apiId,response.getCode(),response.getMsg());
            if (!"200".equals(response.getCode().toString())){
                failList.add(apiId);
            }*/
        }
        if (failList.size()>0){
            throw new DataValidationException(failList+"创建失败");
        }
        return true;
    }

    /**
     * 判断接口是否处于下线状态
     *
     * @param apiId
     * @return
     */
    @Override
    public boolean isOffline(Integer apiId) {
        ApiInfoExample apiInfoExample = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = apiInfoExample.createCriteria();
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        criteria.andIdEqualTo(apiId);
        criteria.andPublishProductEnvStatusEqualTo(CommonStatus.ONLINE);
        List<ApiInfoWithBLOBs> list = apiInfoMapper.selectByExampleWithBLOBs(apiInfoExample);
        //查询到已存在发布版本（未下线）
        if (null != list && list.size() > 0)
            return false;
        LOG.info("apiId={}已下线", apiId);
        return true;
    }
}
