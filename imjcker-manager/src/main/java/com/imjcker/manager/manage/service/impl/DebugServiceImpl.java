package com.imjcker.manager.manage.service.impl;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.mapper.RequestParamsVersionsLatestMapper;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.vo.DebugApiQuery;
import com.imjcker.manager.manage.vo.DebugParams;
import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.CommonStatus;
import com.lemon.common.vo.ResultStatusEnum;
import com.imjcker.manager.manage.mapper.ApiInfoVersionsLatestMapper;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imjcker.manager.manage.service.DebugService;

@Service
public class DebugServiceImpl implements DebugService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DebugServiceImpl.class);
	@Autowired
	private ApiInfoVersionsLatestMapper apiInfoVersionsLatestMapper;

	@Autowired
	private RequestParamsVersionsLatestMapper requestParamsVersionsLatestMapper;

	@Autowired
	private RedisService redisService;

	@Override
	public Map<String, Object> getApiDebugInfo(DebugApiQuery query) {
		ApiInfoVersionsWithBLOBs apiInfoVersions = new ApiInfoVersionsWithBLOBs();
		List<RequestParamsVersionsLatest> paramslist = new ArrayList<>();
		JSONObject jsonObject = redisService.get("api:"+query.getApiId());
		if (jsonObject!=null){
			ApiInfoRedisMsg apiInfoRedisMsg = jsonObject.toJavaObject(ApiInfoRedisMsg.class);
			apiInfoVersions = apiInfoRedisMsg.getApiInfoWithBLOBs();
			paramslist = apiInfoRedisMsg.getList();
			LOGGER.info("redis中存在apiId={}接口参数信息,参数个数={}",query.getApiId(),apiInfoRedisMsg.getList().size());
		}else{
			LOGGER.info("redis中不存在apiId={},查询数据库",query.getApiId());
			ApiInfoVersionsExample example = new ApiInfoVersionsExample();
			ApiInfoVersionsExample.Criteria criteria = example.createCriteria();
			criteria.andApiIdEqualTo(query.getApiId());
			criteria.andEnvEqualTo(CommonStatus.ONLINE);
			criteria.andCurrentVersionEqualTo(1);
			//根据apiid、env、当前版本为最新版本查询api信息
			List<ApiInfoVersionsWithBLOBs> list = apiInfoVersionsLatestMapper.selectByExampleWithBLOBs(example);
			if (list.size() == 0) {
				return null;
			}
			apiInfoVersions = list.get(0);
			String versionId = apiInfoVersions.getVersionId();
			RequestParamsVersionsExample example2 = new RequestParamsVersionsExample();
			RequestParamsVersionsExample.Criteria criteria2 = example2.createCriteria();
			criteria2.andVersionIdEqualTo(versionId);
			//根据版本号查询参数
			paramslist = requestParamsVersionsLatestMapper.selectByExample(example2);

			//	查询数据库后存到redis
			ApiInfoRedisMsg apiInfoRedisMsg = new ApiInfoRedisMsg();
			apiInfoRedisMsg.setApiInfoWithBLOBs(apiInfoVersions);
			apiInfoRedisMsg.setList(paramslist);
			redisService.setToCaches("api:"+query.getApiId(),apiInfoRedisMsg);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("method", apiInfoVersions.getHttpMethod() == 1 ? "GET" : "POST");
		map.put("uri", apiInfoVersions.getHttpPath());
		map.put("headers", "");
		map.put("querys", "");
		map.put("bodys", "");
		if (paramslist.size() == 0) {
			return map;
		}
		List<DebugParams> headers = new ArrayList<>();
		List<DebugParams> querys = new ArrayList<>();
		List<DebugParams> bodys = new ArrayList<>();

		for (RequestParamsVersionsLatest entity : paramslist) {
			DebugParams debugParams = new DebugParams();
			Integer location = entity.getParamsLocation();
			String paramsDescription = entity.getParamsDescription();
			debugParams.setParamName(entity.getParamName());
			debugParams.setParamsLocation(location);
			debugParams.setParamsType(entity.getParamsType());
			debugParams.setParamMust(entity.getParamsMust());
			debugParams.setParamDescription(StringUtils.isBlank(paramsDescription)?"该字段未添加描述":paramsDescription);
			debugParams.setModel("");
			debugParams.setParamsDefaultValue(entity.getParamsDefaultValue());
			if (location == 1) {
				headers.add(debugParams);
			} else if (location == 2) {
				querys.add(debugParams);
			}else {
				bodys.add(debugParams);
			}
		}
		map.put("headers", headers);
		map.put("querys", querys);
		map.put("bodys", bodys);
		// 为风控调用接口信息的缓存,key: debugInfo + apiId
		redisService.setToCaches("debugInfo:" + query.getApiId(), new CommonResult(ResultStatusEnum.SUCCESS, map));
		return map;
	}
}
