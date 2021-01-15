package com.imjcker.manager.manage.service.impl;

import com.imjcker.manager.manage.po.ApiCombination;
import com.imjcker.manager.manage.po.ApiCombinationExample;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.ApiCombinationMapper;
import com.imjcker.manager.manage.service.ApiCombinationService;
import com.imjcker.manager.manage.service.ModifyCombinationParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ApiCombinationServiceImpl implements ApiCombinationService {

    @Autowired
    ApiCombinationMapper apiCombinationMapper;

    @Autowired
    private ModifyCombinationParamService modifyCombinationParamService;

    @Override
    public  List<ApiCombination> selectById(Integer combinationId) {
        ApiCombinationExample example = new ApiCombinationExample();
        ApiCombinationExample.Criteria criteria = example.createCriteria();
        criteria.andCombinationIdEqualTo(combinationId);
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        example.setOrderByClause("weight DESC,score DESC");
        List<ApiCombination> list = apiCombinationMapper.selectByExampleWithBLOBs(example);
        return list;
    }

    @Override
    @Transactional
    public Integer insert(ApiCombination apiCombination) {
        List<ApiCombination> combinationList = this.selectById(apiCombination.getCombinationId());

        if (null != combinationList && combinationList.size() > 0) {
            // 校验是否添加
            if (combinationList.stream().anyMatch(combinf ->
                    Objects.equals(combinf.getApiId(),apiCombination.getApiId()))) {
                throw new DataValidationException(ExceptionInfo.API_EXIST);
            }
            // 校验同一组合接口下的子接口权重不能一样
//            if (combinationList.stream().anyMatch(comb ->
//                    Objects.equals(comb.getWeight(), apiCombination.getWeight()))) {
//                throw new DataValidationException(ExceptionInfo.WEIGHT_EXIST);
//            }
        }
        apiCombination.setStatus(CommonStatus.ENABLE);
        apiCombination.setCreateTime(System.currentTimeMillis());
        apiCombination.setUpdateTime(System.currentTimeMillis());
        Integer id = apiCombinationMapper.insert(apiCombination);

        // wt 修改参数
        modifyCombinationParamService.updateCombinationParams(apiCombination.getCombinationId());
        return id;
    }

    @Override
    @Transactional
    public Integer update(ApiCombination apiCombination) {
        Integer id = apiCombination.getId();
        if (null == id)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
        ApiCombination combination = apiCombinationMapper.selectByPrimaryKey(id);
        // 校验权重
//        List<ApiCombination> combinationList = this.selectById(combination.getCombinationId());
//        if (null != combinationList && combinationList.size() > 0) {
//            if (combinationList.stream().anyMatch(comb ->
//                    Objects.equals(comb.getWeight(), apiCombination.getWeight()))) {
//                throw new DataValidationException(ExceptionInfo.WEIGHT_EXIST);
//            }
//        }
        // 修改
        apiCombination.setStatus(CommonStatus.ENABLE);
        apiCombination.setUpdateTime(System.currentTimeMillis());
        int update = apiCombinationMapper.updateByPrimaryKeySelective(apiCombination);

        // wt 修改参数
        modifyCombinationParamService.updateCombinationParams(combination.getCombinationId());
        return update;
    }
    @Override
    @Transactional
    public Integer delete(ApiCombination apiCombination) {
        Integer id = apiCombination.getId();
        if (null == id)
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
        apiCombination.setStatus(CommonStatus.DISENABLE);
        Integer delId = apiCombinationMapper.updateByPrimaryKeySelective(apiCombination);

        // wt 修改参数
        ApiCombination combination = apiCombinationMapper.selectByPrimaryKey(id);
        modifyCombinationParamService.updateCombinationParams(combination.getCombinationId());
        return delId;
    }
}
