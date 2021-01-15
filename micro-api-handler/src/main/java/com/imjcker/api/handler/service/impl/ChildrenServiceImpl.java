package com.imjcker.api.handler.service.impl;

import java.util.List;

import com.imjcker.api.handler.mapper.RequestParamsVersionsMapper;
import com.imjcker.api.handler.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imjcker.api.handler.mapper.ApiRateDistributeMapper;
import com.imjcker.api.handler.mapper.BackendDistributeParamsMapper;
import com.imjcker.api.handler.mapper.RequestParamsVersionsMapper;
import com.imjcker.api.handler.service.ChildrenService;

@Service
public class ChildrenServiceImpl implements ChildrenService{

	@Autowired
	private ApiRateDistributeMapper apiRateDistributeMapper;

	@Autowired
	private RequestParamsVersionsMapper requestParamsVersionsMapper;

	@Autowired
	private BackendDistributeParamsMapper backendDistributeParamsMapper;

	@Override
	public ApiRateDistributeWithBLOBs childApi(String uniqueUuid) {
		ApiRateDistributeExample example = new ApiRateDistributeExample();
		ApiRateDistributeExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		criteria.andUniqueUuidEqualTo(uniqueUuid);
		List<ApiRateDistributeWithBLOBs> list = apiRateDistributeMapper.selectByExampleWithBLOBs(example);
		return list != null && list.size() != 0 ? list.get(0) : null;
	}

	@Override
	public List<RequestParamsVersions> requestParams(String versionId) {
		RequestParamsVersionsExample example = new RequestParamsVersionsExample();
		RequestParamsVersionsExample.Criteria criteria = example.createCriteria();
		criteria.andVersionIdEqualTo(versionId);
		return requestParamsVersionsMapper.selectByExample(example);
	}

	@Override
	public List<BackendDistributeParams> childParams(Integer disId) {
		BackendDistributeParamsExample example = new BackendDistributeParamsExample();
		BackendDistributeParamsExample.Criteria criteria = example.createCriteria();
		criteria.andDisIdEqualTo(disId);
		criteria.andStatusEqualTo(1);
		return backendDistributeParamsMapper.selectByExample(example);
	}

}
