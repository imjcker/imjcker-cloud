package com.imjcker.api.handler.service;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.handler.model.ChildEntity;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.model.ChildEntity;
import com.imjcker.api.handler.model.MainEntity;
import com.imjcker.api.handler.po.ApiInfoVersions;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface ClientService {
	/**
	 * @return 1, swdsfdfdf：表示主接口      2,swdsfdfdf：表示子接口
	 * @Title: 负载均衡获取上游接口
	 */
	String getHost(Integer apiId, String uniqueUuid, Integer weight) throws IOException;

	/**
	 * @Title: 获取主接口调用的Entity，加缓存
	 */
	MainEntity getMainEntity(String versionId);

	/**
	 * @Title: 拼装主接口的参数信息
	 */
	MainEntity.Params handleMainEntity(MainEntity mainEntity, HttpServletRequest request, Map<String,Object> accountMap,JSONObject paramJson);

	/**
	 * @Title: 主接口发送请求，返回信息
	 */
	String doRequest(MainEntity mainEntity, MainEntity.Params params, String appkey, String redisKey,String retryFlag,int retryCount, String type);

	/**
	 * @Title: 获取子接口调用的Entity，加缓存
	 */
	ChildEntity getChildEntity(String uniqueUuid, String versionId);

	/**
	 * @Title: 拼装子接口的参数信息
	 */
	ChildEntity.Params handleChildEntity(ChildEntity childEntity, HttpServletRequest request, Map<String,Object> accountMap,JSONObject paramJson);

	/**
	 * @Title: 子接口发送请求，返回信息
	 */
	String doRequest(ChildEntity childEntity, ChildEntity.Params params, String appkey, String redisKey,String retryFlag,int retryCount,String type);
	/**
	 * 删除缓存
	 */
	String removeRedisKey(ApiInfoVersions api, String params, String appKey);

	MainEntity.Params getRequestParams(HttpServletRequest request, MainEntity mainEntity, JSONObject paramJson);
}
