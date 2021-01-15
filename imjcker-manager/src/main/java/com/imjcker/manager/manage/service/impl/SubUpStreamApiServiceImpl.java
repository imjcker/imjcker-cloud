package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.ApiInfoWithSubApiQuery;
import com.imjcker.manager.manage.po.query.SubApiWeightQuery;
import com.imjcker.manager.manage.vo.ApiExpand;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.util.ShortUuidUtil;
import com.lemon.common.util.collections.CollectionUtil;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.RedisService;
import com.imjcker.manager.manage.service.SubUpStreamApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lemon.kiana
 * @version 2.0
 *          2017年9月20日 上午10:23:25
 * @Title SubUpStreamApiServiceImpl
 * @Description 子上游API服务实现层
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
@Transactional
@Service
public class SubUpStreamApiServiceImpl implements SubUpStreamApiService {


    @Autowired

    BackendDistributeParamsMapper backendDistributeParamsMapper;

    @Autowired
    ApiresultsettingsMapper resultSettingMapper;

    @Autowired
    ApiResultEtlMapper apiResultEtlMapper;

    @Autowired
    ApiRateDistributeMapper apiRateDistributeMapper;

    @Autowired
    ApiInfoMapper apiInfoMapper;

    @Autowired
    RequestParamsMapper requestParamsMapper;

    @Autowired
    ApiGroupRelationMapper apiGroupRelationMapper;

    @Autowired
    private RedisService redisService;

    @Override
    @Transactional
    public void updateApiName(ApiRateDistribute apiRateDistribute) {


        if (apiRateDistribute.getId() == null && apiRateDistribute.getApiId() != null) {
            if (apiInfoMapper.selectByPrimaryKey(apiRateDistribute.getApiId()) == null)
                throw new BusinessException(ExceptionInfo.ERROR_API_INFO);

            ApiInfoWithBLOBs apiInfo = new ApiInfoWithBLOBs();
            apiInfo.setInterfaceName(apiRateDistribute.getInterfaceName());
            ApiInfoExample apiInfoExample = new ApiInfoExample();
            ApiInfoExample.Criteria criteria = apiInfoExample.createCriteria();
            criteria.andIdEqualTo(apiRateDistribute.getApiId());
            apiInfoMapper.updateByExampleSelective(apiInfo, apiInfoExample);
        } else if (apiRateDistribute.getId() != null && apiRateDistribute.getApiId() != null) {

            if (apiRateDistributeMapper.selectByPrimaryKey(apiRateDistribute.getId()) == null)
                throw new BusinessException(ExceptionInfo.NOT_EXIST_SUB_API_NAME);

            ApiRateDistributeWithBLOBs updateSubApi = new ApiRateDistributeWithBLOBs();
            updateSubApi.setId(apiRateDistribute.getId());
            updateSubApi.setInterfaceName(apiRateDistribute.getInterfaceName());
            apiRateDistributeMapper.updateByPrimaryKeySelective(updateSubApi);
        } else {
            throw new BusinessException(ExceptionInfo.ERROR_API_INFO);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> get(ApiRateDistributeWithBLOBs apiRateDistribute) {
        Map<String, Object> map = new HashMap<>();
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(apiRateDistribute.getApiId());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiInfoWithBLOBs> apiInfoList = apiInfoMapper.selectByExampleWithBLOBs(example);
        if (CollectionUtil.isEmpty(apiInfoList) || apiInfoList.size() > 1) {
            throw new BusinessException(ExceptionInfo.ERROR_API_INFO);
        }
        ApiExpand apiExpand = new ApiExpand();
        BeanCustomUtils.copyPropertiesIgnoreNull(apiInfoList.get(0), apiExpand);

        ApiGroupRelationExample apiGroupRelationExample = new ApiGroupRelationExample();
        ApiGroupRelationExample.Criteria apiGroupRelationExampleCriteria = apiGroupRelationExample.createCriteria();
        apiGroupRelationExampleCriteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        List<ApiGroupRelation> apiGroupRelationList = apiGroupRelationMapper.selectByExample(apiGroupRelationExample);
        if (apiGroupRelationList.size() > 1) {
            throw new BusinessException(ExceptionInfo.ERROR_API_GROUP_INFO);
        }
        apiExpand.setApiGroupId(CollectionUtil.isEmpty(apiGroupRelationList) ? null : apiGroupRelationList.get(0).getGroupId());
        apiExpand.setGroupName(CollectionUtil.isEmpty(apiGroupRelationList) ? null : apiGroupRelationList.get(0).getFullPathName());
        map.put("apiInfo", apiExpand);

        //查找request_params
        RequestParamsExample requestParamsExample = new RequestParamsExample();
        RequestParamsExample.Criteria requestParamsExampleCriteria = requestParamsExample.createCriteria();
        requestParamsExampleCriteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        requestParamsExampleCriteria.andStatusEqualTo(CommonStatus.ENABLE);
        map.put("requestParamsList", requestParamsMapper.selectByExample(requestParamsExample));
        //查找api_rate_distribute
        ApiRateDistributeExample apiRateDistributeExample = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria cri = apiRateDistributeExample.createCriteria();
        cri.andApiIdEqualTo(apiRateDistribute.getApiId());
        cri.andIdEqualTo(apiRateDistribute.getId());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiRateDistributeWithBLOBs> apiRateDistributeWithBLOBs = apiRateDistributeMapper.selectByExampleWithBLOBs(apiRateDistributeExample);
        if (CollectionUtil.isEmpty(apiRateDistributeWithBLOBs) || apiRateDistributeWithBLOBs.size() > 1)
            throw new BusinessException(ExceptionInfo.ERROR_API_GROUP_INFO);
        map.put("subThirdApiInfo", apiRateDistributeWithBLOBs.get(0));

        //查找BackendDistributeParams
        BackendDistributeParamsExample backendDistributeParamsExample = new BackendDistributeParamsExample();
        BackendDistributeParamsExample.Criteria backendDistributeParamsExampleCriteria = backendDistributeParamsExample.createCriteria();
        if (apiRateDistribute.getApiId() != null)
            backendDistributeParamsExampleCriteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        backendDistributeParamsExampleCriteria.andDisIdEqualTo(apiRateDistribute.getId());
        backendDistributeParamsExampleCriteria.andStatusEqualTo(CommonStatus.ENABLE);
        map.put("subThirdRequestInfo", backendDistributeParamsMapper.selectByExample(backendDistributeParamsExample));
        return map;
    }

    @Transactional
    @Override
    public int save(ApiRateDistributeWithBLOBs subThirdApiInfo,
                    List<BackendDistributeParams> subThirdRequestInfo) {

        long currentTimeMillis = System.currentTimeMillis();

        boolean isEdit = false;
        if(subThirdApiInfo.getId() != null){
            isEdit = true;
        }

        if (subThirdApiInfo.getId() == null) {
            if (!checkSubApiUnique(subThirdApiInfo))
                throw new BusinessException(ExceptionInfo.EXIST_SUB_API_NAME);
            subThirdApiInfo.setStatus(CommonStatus.ENABLE);
            subThirdApiInfo.setUniqueUuid(ShortUuidUtil.generateShortUuid());
            subThirdApiInfo.setUpdateTime(currentTimeMillis);
            subThirdApiInfo.setCreateTime(currentTimeMillis);
            apiRateDistributeMapper.insert(subThirdApiInfo);
        } else {
            if (apiRateDistributeMapper.selectByPrimaryKey(subThirdApiInfo.getId()) == null)
                throw new BusinessException(ExceptionInfo.NOT_EXIST_SUB_API_NAME);

            ApiRateDistributeWithBLOBs updateApiRateDistribute = apiRateDistributeMapper.selectByPrimaryKey(subThirdApiInfo.getId());
            updateApiRateDistribute.setResponseConfigJson(subThirdApiInfo.getResponseConfigJson());
            updateApiRateDistribute.setResponseTransParam(subThirdApiInfo.getResponseTransParam());
            updateApiRateDistribute.setWeight(subThirdApiInfo.getWeight());
            updateApiRateDistribute.setBackEndAddress(subThirdApiInfo.getBackEndAddress());
            updateApiRateDistribute.setBackEndAddress(subThirdApiInfo.getBackEndAddressB());
            updateApiRateDistribute.setBackEndHttpMethod(subThirdApiInfo.getBackEndHttpMethod());
            updateApiRateDistribute.setBackendProtocolType(subThirdApiInfo.getBackendProtocolType());
            updateApiRateDistribute.setBackEndPath(subThirdApiInfo.getBackEndPath());
            updateApiRateDistribute.setBackEndTimeout(subThirdApiInfo.getBackEndTimeout());
            updateApiRateDistribute.setInterfaceName(subThirdApiInfo.getInterfaceName());
            updateApiRateDistribute.setCallBackType(subThirdApiInfo.getCallBackType());
            updateApiRateDistribute.setUpdateTime(currentTimeMillis);
            apiRateDistributeMapper.updateByPrimaryKeyWithBLOBs(updateApiRateDistribute);
        }
        if (isEdit) {
            //如果子接口id为空 则为更新 逻辑删除子接口对应的后端请求参数 再插入
            delBackendDistributeParams(subThirdApiInfo);
        }
        //保存后端子接口的请求参数 每次操作其实都是插入
        for (BackendDistributeParams backendDistributeParams : subThirdRequestInfo) {
            backendDistributeParams.setId(null);
            backendDistributeParams.setStatus(CommonStatus.ENABLE);
            backendDistributeParams.setApiId(subThirdApiInfo.getApiId());
            backendDistributeParams.setDisId(subThirdApiInfo.getId());
            backendDistributeParams.setCreateTime(currentTimeMillis);
            backendDistributeParams.setUpdateTime(currentTimeMillis);
            backendDistributeParamsMapper.insert(backendDistributeParams);

        }
        Integer apiId = subThirdApiInfo.getId();
        String key = getRateDistributeKey(apiId);
        ApiRateDistribute apiRate = new ApiRateDistribute();
        apiRate.setApiId(apiId);
        apiRate.setInterfaceName(subThirdApiInfo.getInterfaceName());
        apiRate.setWeight(subThirdApiInfo.getWeight());
        apiRate.setBackEndAddress(subThirdApiInfo.getBackEndAddress());
        apiRate.setBackEndAddressB(subThirdApiInfo.getBackEndAddressB());
        apiRate.setUniqueUuid(subThirdApiInfo.getUniqueUuid());
        List<ApiRateDistribute> apiRates = new ArrayList<>();
        apiRates.add(apiRate);
        redisService.setToCaches(key, apiRates);
        return apiId;
    }

    @Transactional
    @Override
    public void del(ApiRateDistribute apiRateDistribute) {
        delApiRateDistribute(apiRateDistribute);
        delBackendDistributeParams(apiRateDistribute);
        delApiresultsettings(apiRateDistribute);
        delResultEtl(apiRateDistribute);
        redisService.delete(getRateDistributeKey(apiRateDistribute.getApiId()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApiRateDistributeWithBLOBs> querySubApiListByApiId(Integer apiId) {
        List<ApiRateDistributeWithBLOBs> subs = new ArrayList<>();
        ApiInfoWithBLOBs ai = apiInfoMapper.selectByPrimaryKey(apiId);
        if (ai != null) {
            ApiRateDistributeExample apiRateDistributeExample = new ApiRateDistributeExample();
            ApiRateDistributeExample.Criteria cri = apiRateDistributeExample.createCriteria();
            cri.andApiIdEqualTo(apiId);
            cri.andStatusEqualTo(CommonStatus.ENABLE);

            ApiRateDistributeWithBLOBs sub = new ApiRateDistributeWithBLOBs();
            sub.setApiId(ai.getId());
            sub.setWeight(ai.getWeight());
            sub.setInterfaceName(ai.getInterfaceName());
            subs.add(sub);
            subs.addAll(1, apiRateDistributeMapper.selectByExampleWithBLOBs(apiRateDistributeExample));
        }
        return subs;
    }


    @Transactional(readOnly = true)
    @Override
    public PageInfo<ApiInfoWithSubApi> query(ApiInfoWithSubApiQuery query) {
        ApiInfoExample example = new ApiInfoExample();
        ApiInfoExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(query.getApiName()))
            criteria.andApiNameLike(StringUtils.wrap(query.getApiName(), "%"));
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        example.setOrderByClause("a.createTime DESC");
        Page apiInfoPage = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        apiInfoMapper.selectByExampleWithSubApi(example);
        List<ApiInfoWithSubApi> list = apiInfoPage.getResult();
        for (ApiInfoWithSubApi ai : list) {
            ApiRateDistributeExample apiRateDistributeExample = new ApiRateDistributeExample();
            apiRateDistributeExample.setOrderByClause("createTime DESC");
            ApiRateDistributeExample.Criteria cri = apiRateDistributeExample.createCriteria();
            cri.andApiIdEqualTo(ai.getId());
            cri.andStatusEqualTo(CommonStatus.ENABLE);
            List<ApiRateDistribute> subs = new ArrayList<>();
            ApiRateDistribute sub = new ApiRateDistribute();
            sub.setApiId(ai.getId());
            sub.setWeight(ai.getWeight());
            sub.setInterfaceName(ai.getInterfaceName());
            subs.add(sub);
            subs.addAll(1, apiRateDistributeMapper.selectByExample(apiRateDistributeExample));
            ai.setSubApis(subs);
            //格式化group name
            String originalFullPathName = ai.getApiGroupName();
            if (StringUtils.isNotBlank(originalFullPathName)) {
            	originalFullPathName = originalFullPathName.substring(1, originalFullPathName.length()-1);
            }
            ai.setApiGroupName(org.apache.commons.lang.StringUtils.contains(originalFullPathName,".") ? org.apache.commons.lang.StringUtils.substringAfterLast(originalFullPathName,".") : originalFullPathName);
        }
        PageInfo pageInfo = apiInfoPage.toPageInfo();
        pageInfo.setList(list);
        return pageInfo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubApiWeightQuery> getWeightConfigs(ApiRateDistribute apiRateDistribute) {
        List<SubApiWeightQuery> weightConfigs = new ArrayList<SubApiWeightQuery>();
        //获取主接口对应的后端api权重
        ApiInfoWithBLOBs orign = apiInfoMapper.selectByPrimaryKey(apiRateDistribute.getApiId());
        weightConfigs.add(SubApiWeightQuery.of(orign.getId(), null, orign.getWeight(), orign.getInterfaceName()));

        //根据主api接口查询子接口权重信息
        ApiRateDistributeExample example = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria criteria = example.createCriteria();
        criteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiRateDistribute> subApi = apiRateDistributeMapper.selectByExample(example);

        for (ApiRateDistribute sub : subApi) {
            weightConfigs.add(SubApiWeightQuery.of(sub.getApiId(), sub.getId(), sub.getWeight(), sub.getInterfaceName()));
        }

        return weightConfigs;
    }

    @Transactional
    @Override
    public List<SubApiWeightQuery> updateWeightConfigs(List<SubApiWeightQuery> list) {
        long currentTimeMillis = System.currentTimeMillis();
        for (SubApiWeightQuery subApiWeightQuery : list) {
            if (subApiWeightQuery.getWeight() < 0 || subApiWeightQuery.getWeight() > 10) {
                throw new DataValidationException(ExceptionInfo.WEIGHT_MUST_BETWEEN);
            }

            if (subApiWeightQuery.getSubId() == null) {
                ApiInfoExample example = new ApiInfoExample();
                ApiInfoExample.Criteria criteria = example.createCriteria();
                criteria.andIdEqualTo(subApiWeightQuery.getApiId());
                ApiInfoWithBLOBs subParam = new ApiInfoWithBLOBs();
                subParam.setWeight(subApiWeightQuery.getWeight());
                subParam.setUpdateTime(currentTimeMillis);
                apiInfoMapper.updateByExampleSelective(subParam, example);
            } else {
                ApiRateDistributeExample example = new ApiRateDistributeExample();
                ApiRateDistributeExample.Criteria criteria = example.createCriteria();
                criteria.andIdEqualTo(subApiWeightQuery.getSubId());
                criteria.andApiIdEqualTo(subApiWeightQuery.getApiId());
                ApiRateDistributeWithBLOBs subParam = new ApiRateDistributeWithBLOBs();

                subParam.setWeight(subApiWeightQuery.getWeight());
                subParam.setUpdateTime(currentTimeMillis);
                if (apiRateDistributeMapper.selectByPrimaryKey(subApiWeightQuery.getSubId()) == null)
                    throw new BusinessException(ExceptionInfo.NOT_EXIST_SUB_API_NAME);

                apiRateDistributeMapper.updateByExampleSelective(subParam, example);
            }
        }

        return list;
    }


    private void delApiRateDistribute(ApiRateDistribute apiRateDistribute) {
        //逻辑删除api子接口
        long currentTimeMillis = System.currentTimeMillis();
        ApiRateDistributeExample example = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(apiRateDistribute.getId());
        criteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        ApiRateDistributeWithBLOBs subParam = new ApiRateDistributeWithBLOBs();
        subParam.setStatus(CommonStatus.DISENABLE);
        subParam.setUpdateTime(currentTimeMillis);
        apiRateDistributeMapper.updateByExampleSelective(subParam, example);
    }


    private void delResultEtl(ApiRateDistribute apiRateDistribute) {
        long currentTimeMillis = System.currentTimeMillis();
        ApiResultEtlExample example = new ApiResultEtlExample();
        ApiResultEtlExample.Criteria criteria = example.createCriteria();
        criteria.andDisIdEqualTo(apiRateDistribute.getId());
        criteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        ApiResultEtl subParam = new ApiResultEtl();
        subParam.setStatus(CommonStatus.DISENABLE);
        subParam.setUpdateTime(currentTimeMillis);
        apiResultEtlMapper.updateByExampleSelective(subParam, example);
    }


    private void delApiresultsettings(ApiRateDistribute apiRateDistribute) {
        long currentTimeMillis = System.currentTimeMillis();
        ApiresultsettingsExample example = new ApiresultsettingsExample();
        ApiresultsettingsExample.Criteria criteria = example.createCriteria();
        criteria.andDisIdEqualTo(apiRateDistribute.getId());
        criteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        Apiresultsettings subParam = new Apiresultsettings();
        subParam.setStatus(CommonStatus.DISENABLE);
        subParam.setUpdateTime(currentTimeMillis);
        resultSettingMapper.updateByExampleSelective(subParam, example);
    }


    private void delBackendDistributeParams(ApiRateDistribute apiRateDistribute) {
        long currentTimeMillis = System.currentTimeMillis();
        BackendDistributeParamsExample example = new BackendDistributeParamsExample();
        BackendDistributeParamsExample.Criteria criteria = example.createCriteria();
        criteria.andDisIdEqualTo(apiRateDistribute.getId());
        criteria.andApiIdEqualTo(apiRateDistribute.getApiId());
        BackendDistributeParams subParam = new BackendDistributeParams();
        subParam.setStatus(CommonStatus.DISENABLE);
        subParam.setUpdateTime(currentTimeMillis);
        backendDistributeParamsMapper.updateByExampleSelective(subParam, example);
    }


    private boolean checkSubApiUnique(ApiRateDistribute apiRateDistribute) {
        ApiRateDistributeExample apiRateDistributeExample = new ApiRateDistributeExample();
        ApiRateDistributeExample.Criteria criteria = apiRateDistributeExample.createCriteria();
        criteria.andInterfaceNameEqualTo(apiRateDistribute.getInterfaceName());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<ApiRateDistribute> exist = apiRateDistributeMapper.selectByExample(apiRateDistributeExample);
        return CollectionUtil.isEmpty(exist);
    }

	@Override
	public void saveBackendParams(BackendDistributeParams bDParam) {
		//验证当参数类型为常量时，参数值不为null
        Integer paramsType = bDParam.getParamsType();
        String paramValue = bDParam.getParamValue();
        if (CommonStatus.CONSTANT == paramsType && StringUtils.isBlank(paramValue)) {
            throw new BusinessException(StringUtils.join("参数类型为常量，paramValue", ExceptionInfo.NOT_NULL));
        }
        Long createTime = System.currentTimeMillis();
        bDParam.setId(null);
        bDParam.setCreateTime(createTime);
        bDParam.setUpdateTime(createTime);
        bDParam.setStatus(CommonStatus.ENABLE);
        backendDistributeParamsMapper.insert(bDParam);
	}

	private String getRateDistributeKey(Integer apiId) {
        return "api:ratedistribute:" + apiId;
    }

}
