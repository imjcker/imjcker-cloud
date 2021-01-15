package com.imjcker.manager.manage.service.impl;

import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.lemon.common.util.BeanCustomUtils;
import com.lemon.common.util.collections.CollectionUtil;
import com.lemon.common.vo.CommonStatus;
import com.lemon.common.vo.Constant;
import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.ApiService;
import com.imjcker.manager.manage.service.ModifyCombinationParamService;
import com.imjcker.manager.manage.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author WT
 * @Date 9:01 2019/8/23
 * @Version ModifyCombinationParamServiceImpl v1.0
 * @Desicrption  根据子接口的改变修改组合接口的参数信息
 */
@Service
public class ModifyCombinationParamServiceImpl
        implements ModifyCombinationParamService {

    private static final Logger logger = LoggerFactory.getLogger(ModifyCombinationParamServiceImpl.class);

    @Autowired
    private ModifyCombinationParamMapper modifyCombinationParamMapper;

    @Autowired
    private ApiService apiService;

    @Autowired
    private RequestParamsMapper requestParamsMapper;

    @Autowired
    private RequestParamsVersionsMapper requestParamsVersionsMapper;

    @Autowired
    private RequestParamsVersionsLatestMapper requestParamsVersionsLatestMapper;

    @Autowired
    private BackendRequestParamsVersionsMapper backendRequestParamsVersionsMapper;

    @Autowired
    private BackendRequestParamsMapper backendMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据apiId 的发布,修改组合接口请求参数
     * @param apiId
     * @return
     */
    @Override
    @Transactional
    public void updateCombInfWithPublish(Integer apiId) {
        // 获取apiId关联的所有组合接口
        List<ApiCombination> combinationList = modifyCombinationParamMapper.findCombInfByApiId(apiId);
        if (CollectionUtil.isNotEmpty(combinationList)) {
            for (ApiCombination apiCombination : combinationList) {
                Integer status = apiCombination.getStatus();
                if (3 == status) {
                    // 此为下线接口重新发布
                    modifyCombinationParamMapper.updateCombinationStatus(apiCombination.getCombinationId(), apiId, CommonStatus.ENABLE);
                }
                this.updateCombinationParams(apiCombination.getCombinationId());
            }
        }
    }

    @Override
    @Transactional
    public void updateCombInfWithStatus(Integer apiId, Integer status) {
        List<ApiCombination> combinationList = modifyCombinationParamMapper.findCombInfByApiId(apiId);
        if (CollectionUtil.isNotEmpty(combinationList)) {
            for (ApiCombination apiCombination : combinationList) {
                modifyCombinationParamMapper.updateCombinationStatus(apiCombination.getCombinationId(), apiId, status);
                this.updateCombinationParams(apiCombination.getCombinationId());
            }
        }
    }

    /**
     * 清除api相关缓存
     * @param apiId
     */
    private void cleanApiRedis(Integer apiId,String versionId) {
        logger.info("清除缓存");
        redisService.delete("api:" + apiId);
        String httpPath = modifyCombinationParamMapper.findHttpPathWithApiId(apiId);
        String redisKey1 = "api:env1" + ":httpPath:" + httpPath;
        String redisKey2 = "api:env2" + ":httpPath:" + httpPath;
        redisService.delete(redisKey1);
        redisService.delete(redisKey2);
        String combInfokey = "api:combination:" + apiId;
        redisService.delete(combInfokey);
        String versionKey = Constant.REDIS_KEY_PRE + "versionid:" + versionId;
        redisService.delete(versionKey);
    }

    /**
     * 根据组合接口apiId修改前后端参数
     * @param combinationId
     */
    @Override
    @Transactional
    public void updateCombinationParams(Integer combinationId) {
        logger.info("开始修改组合接口参数,组合接口id: {}", combinationId);
        // 查出组合接口所有的关联接口
        List<ApiCombination> list = modifyCombinationParamMapper.findCombInfByCid(combinationId);
        if (CollectionUtil.isEmpty(list)) {
            // 没有关联的子接口,或者已经完全删除
            return;
        }
        logger.info("组合接口的子接口个数: {}", list.size());
        // 查出组合接口的versionId
        String cVersionId = modifyCombinationParamMapper.findVersionIdByApiId(combinationId);
        logger.info("组合接口versionId: {}", cVersionId);
        // 删除组合接口版本表的请求参数
        modifyCombinationParamMapper.deleteCombInfVersionsParams("request_params_versions",cVersionId);
        modifyCombinationParamMapper.deleteCombInfRequestVersionLatest(cVersionId, combinationId);
        // 将组合接口的前端请求参数置为不可用
        modifyCombinationParamMapper.updateCombInfParamsStatus("request_params",combinationId, CommonStatus.DISENABLE);

        // 删除组合接口版本后端请求参数
        modifyCombinationParamMapper.deleteCombInfVersionsParams("backend_request_params_versions", cVersionId);
        // 将组合接口的后端请求参数置为不可用
        modifyCombinationParamMapper.updateCombInfParamsStatus("backend_request_params", combinationId, CommonStatus.DISENABLE);


        List<RequestParamsVersions> allRequestParams = new ArrayList<>();
        for (ApiCombination combination : list) {
            Integer apiId = combination.getApiId();
            String sVersionId = modifyCombinationParamMapper.findVersionIdByApiId(apiId);
            // 查询前端参数
            List<RequestParamsVersions> requestParamsVersions = modifyCombinationParamMapper.findVersionsParamsByVersionId(sVersionId);
            // 校验前端参数重名情况, header 中appKey 除外
            if (CollectionUtil.isNotEmpty(allRequestParams)) {
                // 如果header中已存在appKey, 排除header中appKey
                boolean existHeader = allRequestParams.stream()
                        .anyMatch(paramVersion ->
                                "appKey".equals(paramVersion.getParamName())
                                        && paramVersion.getParamsLocation() == 1);
                List<RequestParamsVersions> paramsVersions;
                if (existHeader) {
                    paramsVersions = requestParamsVersions.stream()
                            .filter(paramVersion ->
                                    !"appKey".equals(paramVersion.getParamName()) &&
                                            1 != paramVersion.getParamsLocation())
                            .collect(toList());
                }else {
                    paramsVersions = requestParamsVersions;
                }
                // 过滤参数名,参数位置都相同的参数
                List<RequestParamsVersions> versionsList = paramsVersions.stream().filter(param -> {
                    for (RequestParamsVersions allRequestParam : allRequestParams) {
                        if (param.getParamName().equals(allRequestParam.getParamName())
                                && param.getParamsLocation().equals(allRequestParam.getParamsLocation())) {
                            return false;
                        }
                    }
                    return true;
                }).collect(toList());
                allRequestParams.addAll(versionsList);
            }else {
                allRequestParams.addAll(requestParamsVersions);
            }
        }

        logger.info("组合接口前端参数个数: {}", allRequestParams.size());

        // 插入组合接口前后端参数
        if (CollectionUtil.isNotEmpty(allRequestParams)) {
            for (RequestParamsVersions paramVersion : allRequestParams) {
                RequestParams params = new RequestParams();
                params.setApiId(combinationId);
                params.setParamName(paramVersion.getParamName());
                params.setParamsType(paramVersion.getParamsType());
                params.setParamsLocation(paramVersion.getParamsLocation());
                params.setParamsMust(paramVersion.getParamsMust());
                params.setParamsDefaultValue(paramVersion.getParamsDefaultValue());
                params.setParamsExample(paramVersion.getParamsExample());
                params.setParamsDescription(paramVersion.getParamsDescription());
                params.setMinLength(paramVersion.getMinLength());
                params.setMaxLength(paramVersion.getMaxLength());
                params.setRegularExpress(paramVersion.getRegularExpress());
                params.setStatus(CommonStatus.ENABLE);
                params.setCreateTime(System.currentTimeMillis());
                params.setUpdateTime(System.currentTimeMillis());

                // 插入前端参数
                Integer requestParamId = apiService.save(params);

                logger.debug("插入的前端参数Id: {}", requestParamId);

                // 构建后端参数
                BackendRequestParams backendRequestParams = new BackendRequestParams();
                backendRequestParams.setApiId(combinationId);
                backendRequestParams.setRequestParamsId(requestParamId);
                backendRequestParams.setParamName(paramVersion.getParamName());
                backendRequestParams.setParamsType(paramVersion.getParamsType());
                backendRequestParams.setParamsLocation(paramVersion.getParamsLocation());
                backendRequestParams.setParamValue(paramVersion.getParamsDefaultValue());
                backendRequestParams.setParamDescription(paramVersion.getParamsDescription());
                backendRequestParams.setStatus(CommonStatus.ENABLE);
                backendRequestParams.setCreateTime(System.currentTimeMillis());
                backendRequestParams.setUpdateTime(System.currentTimeMillis());
                // 插入后端参数
                apiService.save(backendRequestParams);
            }
            // 插入requestParams版本表
            this.publishRequestParams(cVersionId, combinationId);
            // 插入backendParams版本表
            this.publishBackendRequest(cVersionId,combinationId);
        }

        // 清除组合接口有关缓存
        this.cleanApiRedis(combinationId,cVersionId);
    }

    /**
     * 发布下游输入参数到对应版本表
     *
     * @param versionId
     * @param apiId
     */
    private void publishRequestParams(String versionId, Integer apiId) {
        logger.info("插入前端参数版本表");
        //根据apiId从数据库取出下游请求参数列表
        List<RequestParams> requestParamsList = this.findRequestParamsByApiId(apiId);
        //若该api下参数列表不为空则复制到对应的版本表
        if (null != requestParamsList && requestParamsList.size() > 0) {
            logger.info("前端参数个数: {}", requestParamsList.size());
            RequestParams params;
            RequestParamsVersions requestParamsVersions;
            RequestParamsVersionsLatest requestParamsVersionsLatest;
            Iterator<RequestParams> iterator = requestParamsList.iterator();
            while (iterator.hasNext()) {
                params = iterator.next();
                requestParamsVersions = new RequestParamsVersions();
                BeanCustomUtils.copyPropertiesIgnoreNull(params, requestParamsVersions);
                requestParamsVersions.setId(null);
                requestParamsVersions.setVersionId(versionId);
                requestParamsVersions.setRequestParamsId(params.getId());
                requestParamsVersions.setCreateTime(System.currentTimeMillis());
                //插入最新版本请求参数信息
                requestParamsVersionsMapper.insert(requestParamsVersions);
                //kjy --插入最新版本请求参数信息
                requestParamsVersionsLatest = new RequestParamsVersionsLatest();
                BeanCustomUtils.copyPropertiesIgnoreNull(params, requestParamsVersionsLatest);
                requestParamsVersionsLatest.setId(null);
                requestParamsVersionsLatest.setVersionId(versionId);
                requestParamsVersionsLatest.setRequestParamsId(params.getId());
                requestParamsVersionsLatest.setCreateTime(System.currentTimeMillis());
                requestParamsVersionsLatest.setApiId(apiId);
                //把最新版本接口参数信息放在request_params_version_latest表中；
                requestParamsVersionsLatestMapper.insert(requestParamsVersionsLatest);
            }
        }
    }

    private void publishBackendRequest(String versionId, Integer apiId) {
        //根据apiId从数据库取出上游请求参数列表
        List<BackendRequestParams> backendList = this.getBackendParamsByApiIdAndStatus(apiId);
        //若该api下参数列表不为空则复制到对应的版本表
        if (null != backendList && backendList.size() > 0) {
            logger.info("后端参数版本表参数个数: {}", backendList.size());
            BackendRequestParamsVersions backendParamsVersions;
            for (BackendRequestParams backendRequestParams : backendList) {
                backendParamsVersions = new BackendRequestParamsVersions();
                BeanCustomUtils.copyPropertiesIgnoreNull(backendRequestParams, backendParamsVersions);
                backendParamsVersions.setId(null);
                backendParamsVersions.setVersionId(versionId);
                //版本切换，在backend_request_params_versions表增加backendParamsId字段与backend_request_params表中的id关联
                backendParamsVersions.setBackendParamsId(backendRequestParams.getId());
                backendParamsVersions.setCreateTime(System.currentTimeMillis());

                backendRequestParamsVersionsMapper.insert(backendParamsVersions);
            }
        }
    }

    private List<BackendRequestParams> getBackendParamsByApiIdAndStatus(Integer apiId) {
        BackendRequestParamsExample emp = new BackendRequestParamsExample();
        BackendRequestParamsExample.Criteria crt = emp.createCriteria();
        crt.andApiIdEqualTo(apiId);
        crt.andStatusEqualTo(CommonStatus.ENABLE);
        return backendMapper.selectByExample(emp);
    }

    private List<RequestParams> findRequestParamsByApiId(Integer apiId) {
        RequestParamsExample emp = new RequestParamsExample();
        RequestParamsExample.Criteria crt = emp.createCriteria();
        crt.andApiIdEqualTo(apiId);
        crt.andStatusEqualTo(CommonStatus.ENABLE);
        return requestParamsMapper.selectByExample(emp);
    }

}
