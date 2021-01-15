package com.imjcker.manager.manage.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.ApiInfoLatestQuery;
import com.imjcker.manager.manage.po.query.AutoTestQuery;
import com.imjcker.manager.manage.vo.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.vo.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**

 * @Title ApiService
 * @Description api服务接口
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年7月17日 上午10:34:25
 */
/**

 * @Title
 * @Description
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 1.0
 * 2017年8月7日 下午5:58:13
 */
public interface ApiService {

	/**
	 * 保存新建api的相关信息
	 *
	 * @param apiInfo
	 * @param requestParamsList
	 * @param backendList
	 * @return Integer api_info的ID
	 */
	Integer save(ApiInfoWithBLOBs apiInfo, List<RequestParams> requestParamsList,
                 List<BackendRequestParams> backendList);

	/**
	 * 同步发布信息到redis中
	 */
	String synchroLatest2Redis();
	/**
	 * 查询redis中接口参数信息
	 */
	String queryRedis(String pattern);
	/**
	 * 查询redis中接口参数信息
	 */
	String countRedis(String pattern);
	/**
	 * 删除redis中接口参数信息
	 */
	void deleteRedis(String pattern);
	/**
	 * 保存到API_INFO表
	 *
	 * @param apiInfo
	 * @return Integer
	 */
	Integer save(ApiInfoWithBLOBs apiInfo);

	/**
	 * 验证组内api名称唯一性：true：唯一；false：不唯一
	 *
	 * @param apiQuery
	 * @return boolean
	 */
	boolean checkUnique(ApiQuery apiQuery);

	/**
	 * 保存下游请求参数
	 *
	 * @param requestParams
	 * @return
	 */
	Integer save(RequestParams requestParams);

	/**
	 * 保存上游请求参数
	 *
	 * @param backendParams
	 */
	void save(BackendRequestParams backendParams);

	/**
	 * 发布版本
	 *
	 * @param apiExpand
	 * @return Integer
	 */
	Integer publish(ApiExpand apiExpand);

	/**
	 * 更新api编辑库表
	 *
	 * @param apiInfo
	 * @param requestParamsList
	 * @param backendList
	 */
	void update(ApiInfoWithBLOBs apiInfo, List<RequestParams> requestParamsList, List<BackendRequestParams> backendList);


	/**
	 * api下线功能
	 *
	 * @param apiExpand
	 * @return
	 */
	void offline(ApiExpand apiExpand);

	/**
	 * 通过apiId查询所有api信息（四张）
	 *
	 * @param apiInfo
	 * @return
	 */
	Map<String, Object> findAllByApiId(ApiInfoWithBLOBs apiInfo);

	/**
	 * 逻辑删除api信息
	 *
	 * @param apiInfo
	 * @return
	 */
	String delete(ApiInfoWithBLOBs apiInfo);

	/**
	 * 通过apiGroupId查找分组下的api
	 *
	 * @param id
	 * @return
	 */
	List<ApiInfoWithBLOBs> findByGroupIdAndStatus(Integer id);

	/**
	 * 通过apiId删除api与app的授权关系
	 *
	 * @param apiId
	 */
	void deleteAutorizationByApiId(Integer apiId);

	/**
	 * 切换版本到当前编辑库
	 *
	 * @param apiInfoVersion
	 */
	void reswitchVersion(ApiInfoVersionsWithBLOBs apiInfoVersion);

	/**
	 * 返回系统错误码列表
	 *
	 * @return
	 */
	List<SystemErrorCode> systemErrorCodeList();

	/**
	 * 返回apiInfo列表（包含条件查询）
	 *
	 * @param apiInfoQuery
	 * @return
	 */
	PageInfo<ApiExpand> apiInfoList(ApiInfoQuery apiInfoQuery);

	/**
	 * 返回apiInfoVersion列表（包含条件查询）
	 *
	 * @param apiInfoVersionQuery
	 * @return
	 */
	PageInfo<ApiVersionExpand> apiInfoVersionList(ApiInfoVersionQuery apiInfoVersionQuery);
	/**
	 * 返回apiInfoVersionLatest列表（包含条件查询）
	 *
	 * @param apiInfoVersionQuery
	 * @return
	 */
	PageInfo<ApiVersionExpand> apiInfoVersionLatestList(ApiInfoVersionQuery apiInfoVersionQuery);

	/**
	 * 查询apiInfoVersion（版本表）信息（四张表）
	 *
	 * @param apiInfoVersion
	 * @return
	 */
	Map<String, Object> findApiInfoVersion(ApiInfoVersionsWithBLOBs apiInfoVersion);

	/**
	 * 调试API
	 *
	 * @param bodyList
	 * @param queryList
	 * @param bodyList
	 * @return
	 */
	Map debugging(IDInfo idInfo, List<RequestParamAndValue> headerList, List<RequestParamAndValue> queryList, List<RequestParamAndValue> bodyList);

	/**
	 * httpPath全局唯一性验证
	 *
	 * @param apiInfo
	 * @return
	 */
	String httpPathUnique(ApiInfoWithBLOBs apiInfo);

	/**
	 * API内参数名检查唯一
	 *
	 * @param paramNameList
	 * @return
	 */
	void paramNameCheck(List<ParamNames> paramNameList);

	/**
	 * 复制历史版本的内容到当前编辑区
	 *
	 * @param apiInfoVersion
	 */
	void copyToEdit(ApiInfoVersionsWithBLOBs apiInfoVersion);

	/**
	 * 下载api的DOCX文档
	 *
	 * @param versionId
	 * @param apiId
	 * @return
	 * @Version 2.0
	 */
	String downloadDocx(String versionId, Integer apiId);

	Map<String,Object> showDocx(String apiId,String versionId);

	List<CallBackParam> parseJson(String apiId,String json) throws Exception;

//	boolean saveDocx(JSONObject jsonObject);

	boolean editDocx(JSONObject jsonObject);

	boolean deleteDocx(JSONObject jsonObject);

	/**
	 * 验证上游接口名称唯一性
	 *
	 * @param apiQuery
	 * @return
	 * @Version 2.0
	 */
	boolean checkUniqueInterfaceName(ApiQuery apiQuery);

	/**
	 * 为已发布api绑定限流策略
	 *
	 * @param
	 * @Version 2.0
	 */
	void apibindingStrategy(Integer apiId, String uuid, Integer total);

	/**
	 * 通过ID查找apiInfo
	 *
	 * @param id
	 * @return
	 */
	ApiInfoWithBLOBs findById(Integer id);

	/**
	 * 解绑分组和API，apiGroupId设为空
	 *
	 * @param apiId
	 * @Version 2.0
	 */
	void setGroupIdToNull(Integer apiId);

	/**
	 * 物理删除api与分组的关系【关系表】
	 *
	 * @param groupId,apiId,pathId
	 * @Version 2.0
	 */
	void physicallyDeleteRelation(Integer groupId, Integer apiId, String pathId);

	/**
	 * 通过UUID查找绑定该策略的上游API
	 *
	 * @param strategyQuery
	 * @return
	 * @Version 2.0
	 */
	PageInfo<ApiInfoVersionsWithBLOBs> findApiByStrategy(CurrentLimitStrategyQuery strategyQuery);

	/**
	 * 查找无分组API
	 *
	 * @param apiInfoQuery
	 * @return
	 * @Version 2.0
	 */
	PageInfo<ApiExpand> findApiAndNoGroup(ApiInfoQuery apiInfoQuery);

	/**
	 * 全局检查APIname
	 *
	 * @param apiQuery
	 * @return
	 * @Version 2.0
	 */
	boolean checkUniqueApiNameGlobal(ApiQuery apiQuery);

	/**
	 * 保存三方结果的参数抽取与转换
	 *
	 * @param apiResultEtl
	 * @return Integer
	 */
	void save(ApiResultEtl apiResultEtl);

	/**
	 * 该分组下是否直接有API
	 *
	 * @param groupId
	 * @return
	 * @Version 2.0
	 */
	boolean hasNoneOfApi(Integer groupId);

	/**
	 * 分组增加已存在API
	 *
	 * @param groupId
	 * @param apiId
	 * @return
	 * @Version 2.0
	 */
	void addExistedApi(Integer groupId, Integer apiId);

	/**
	 * 检查策略是否已绑定API
	 *
	 * @param uuid
	 * @return
	 * @Version 2.0
	 */
	boolean checkStrategyUsed(String uuid);

	/**
	 * 接口2.0通过分组展示api列表
	 *
	 * @param apiInfoQuery
	 * @return
	 * @Version 2.0
	 */
	PageInfo<ApiExpand> apiInfoListByGroup(ApiInfoQuery apiInfoQuery);

	PageInfo<ApiExpand> apiInfoListByApiIdList(ApiIdListQuery apiIdListQuery,List<Integer> validApiIdList);

	/**
	 * 全局检验上游接口（服务）httpPath唯一性
	 *
	 * @param apiQuery
	 * @return
	 * @Version 2.0
	 */
	boolean checkUniqueInterfaceHttpPath(ApiQuery apiQuery);

	/**
	 * 获取api分组全路径
	 *
	 * @param groupId
	 * @return
	 * @Version 2.0
	 */
	String getFullPathName(Integer groupId);

	/**
	 * 获取指定API的参数信息
	 *
	 * @param apiInfo
	 * @return
	 * @Version 2.0
	 */
	Map<String, Object> getApiParams(ApiInfoWithBLOBs apiInfo);

	/**
	 * 保存指定API的所有参数
	 *
	 * @param apiInfo
	 * @param requestParamsList
	 * @param backendList
	 * @Version 2.0
	 */
	void updateApiParams(ApiInfoWithBLOBs apiInfo, List<RequestParams> requestParamsList, List<BackendDistributeParams> backendList);

	/**
	 * 获取服务器的ip和port
	 *
	 * @return
	 */
	String getServerIpAndPort();

	int updateApiResponseTransParam(Integer apiId, String transJson);

	String convertApiRiskIndex(IDInfo idInfo, List<RequestParamAndValue> headerList, List<RequestParamAndValue> queryList,
							   List<RequestParamAndValue> bodyList, Integer apiId) throws IOException;

	String removeRedisKey(IDInfo idInfo, List<RequestParamAndValue> headerList, List<RequestParamAndValue> queryList,
						  List<RequestParamAndValue> bodyList, Integer apiId) throws IOException;


	Integer  findApiInfoVersionsLatestById(Integer id);

	Integer findRequestParamsVersionsLatestById(Integer requestParamsId);

	String  chooseValidApiID(String apiIdList);

	List  chooseValidListApiID(String apiIdList);

	Map autoTest(Integer apiId) throws IOException;

	List<ApiInfoVersionLatestResponse> findApiInfoVersionsLatestForAutoTest(Integer groupId);

	int insertTestResult(String testResult, Integer apiId, Integer apiGroupId, String apiName);

	PageInfo<AutoTestResult> autoTestList(AutoTestQuery query);

	int configAutoTestOn(Integer apiId);

	int configAutoTestOff(Integer apiId);

	PageInfo<ApiInfoVersionLatestResponse> selectApiInfoLatestExample(ApiInfoLatestQuery query);

	List<Integer> synchroJsonConfig();


    Integer copy(ApiExpand apiExpand);

    Integer apiExport(Integer apiId,String path);

    PageInfo<ApiExpand> combinationApiList(ApiInfoQuery apiInfoQuery);

    boolean createProContract(List<Integer> apiIdList, String appKey);

	boolean isOffline(Integer apiId);
}
