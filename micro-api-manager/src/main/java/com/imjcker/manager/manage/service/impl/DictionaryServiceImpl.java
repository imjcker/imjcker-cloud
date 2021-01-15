package com.imjcker.manager.manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.ApiInfoWithSubApiQuery;
import com.imjcker.manager.manage.po.query.DictionaryItemQuery;
import com.imjcker.manager.manage.po.query.DictionaryQuery;
import com.imjcker.manager.manage.po.query.DictionaryTypeQuery;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.util.collections.CollectionUtil;
import com.lemon.common.vo.CommonStatus;
import com.imjcker.manager.manage.mapper.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * User: lxl
 * Date: 2017/10/13
 * Time: 10:31
 * Description:
 */
@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    DictionaryItemMapper dictionaryItemMapper;

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Autowired
    DictionaryTypeMapper dictionaryTypeMapper;

    @Autowired
    ApiInfoMapper apiInfoMapper;

    @Autowired
    ApiRateDistributeMapper apiRateDistributeMapper;

    @Override
    @Transactional
    public int saveOrUpateDictionaryType(DictionaryType dictionaryType) {
        long currentTimeMillis = System.currentTimeMillis();
        if (dictionaryType.getId() == null) {
            if (!CollectionUtil.isEmpty(checkDictionaryTypeUnique(dictionaryType)))
                throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_TYPE_NAME);
            dictionaryType.setStatus(CommonStatus.ENABLE);
            dictionaryType.setUpdateTime(currentTimeMillis);
            dictionaryType.setCreateTime(currentTimeMillis);
            dictionaryTypeMapper.insert(dictionaryType);
        } else {
            List<DictionaryType> exise = checkDictionaryTypeUnique(dictionaryType);
            if (!CollectionUtil.isEmpty(exise)) {
                for (DictionaryType d : exise) {
                    if (d.getName().equals(dictionaryType.getName()) && !d.getId().equals(dictionaryType.getId()))
                        throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_TYPE_NAME);
                }
            }

            dictionaryType.setUpdateTime(currentTimeMillis);
            dictionaryTypeMapper.updateByPrimaryKeySelective(dictionaryType);
        }
        return dictionaryType.getId();
    }

    @Override
    @Transactional
    public void delDictionaryType(DictionaryType dictionaryType) {
        long currentTimeMillis = System.currentTimeMillis();
        DictionaryExample dictionaryQueryExample = new DictionaryExample();
        DictionaryExample.Criteria dictionaryQueryCriteria = dictionaryQueryExample.createCriteria();
        dictionaryQueryCriteria.andStatusEqualTo(CommonStatus.ENABLE);
        dictionaryQueryCriteria.andTypeIdEqualTo(dictionaryType.getId());
        List<Dictionary> existDictionary = dictionaryMapper.selectByExample(dictionaryQueryExample);
        for(Dictionary del:existDictionary){
            delDictionary(del);
        }

        DictionaryTypeExample example = new DictionaryTypeExample();
        DictionaryTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(dictionaryType.getId());
        DictionaryType update = new DictionaryType();
        update.setStatus(CommonStatus.DISENABLE);
        update.setUpdateTime(currentTimeMillis);
        dictionaryTypeMapper.updateByExampleSelective(update, example);
    }


    @Override
    @Transactional
    public int saveOrUpateDictionary(Dictionary dictionary) {
        long currentTimeMillis = System.currentTimeMillis();
        if (dictionary.getId() == null) {
            if (!CollectionUtil.isEmpty(checkDictionaryName(dictionary)))
                throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_NAME);
            if (!CollectionUtil.isEmpty(checkDictionaryValue(dictionary)))
                throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_VALUE);
            dictionary.setStatus(CommonStatus.ENABLE);
            dictionary.setUpdateTime(currentTimeMillis);
            dictionary.setCreateTime(currentTimeMillis);
            dictionaryMapper.insert(dictionary);
        } else {
            List<Dictionary> exise = checkDictionaryName(dictionary);
            if (!CollectionUtil.isEmpty(exise)) {
                for (Dictionary d : exise) {
                    if (d.getDictName().equals(dictionary.getDictName()) && !d.getId().equals(dictionary.getId()))
                        throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_NAME);
                }
            }

            exise = checkDictionaryValue(dictionary);
            if (!CollectionUtil.isEmpty(exise)) {
                for (Dictionary d : exise) {
                    if (d.getDictValue().equals(dictionary.getDictName()) && !d.getId().equals(dictionary.getId()))
                        throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_VALUE);
                }
            }

            dictionary.setUpdateTime(currentTimeMillis);
            dictionaryMapper.updateByPrimaryKeySelective(dictionary);
        }
        return dictionary.getId();
    }

    @Override
    @Transactional
    public void delDictionary(Dictionary dictionary) {
        long currentTimeMillis = System.currentTimeMillis();
        //删除Dictionary Item
        DictionaryItemExample dictionaryItemExample = new DictionaryItemExample();
        DictionaryItemExample.Criteria dictionaryItemCriteria = dictionaryItemExample.createCriteria();
        dictionaryItemCriteria.andDictionaryIdEqualTo(dictionary.getId());
        DictionaryItem dictionaryItemUpdate = new DictionaryItem();
        dictionaryItemUpdate.setStatus(CommonStatus.DISENABLE);
        dictionaryItemUpdate.setUpdateTime(currentTimeMillis);
        dictionaryItemMapper.updateByExampleSelective(dictionaryItemUpdate, dictionaryItemExample);
        //删除Dictionary
        DictionaryExample example = new DictionaryExample();
        DictionaryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(dictionary.getId());
        Dictionary update = new Dictionary();
        update.setStatus(CommonStatus.DISENABLE);
        update.setUpdateTime(currentTimeMillis);
        dictionaryMapper.updateByExampleSelective(update, example);

    }

    @Override
    @Transactional
    public int saveOrUpateDictionaryItem(DictionaryItem dictionaryItem) {
        long currentTimeMillis = System.currentTimeMillis();
        if (dictionaryItem.getId() == null) {
            if (!CollectionUtil.isEmpty(checkDictionaryItemValue(dictionaryItem)))
                throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_ITEM_VALUE);
            if (!CollectionUtil.isEmpty(checkDictionaryItemName(dictionaryItem)))
                throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_ITEM_NAME);
            dictionaryItem.setStatus(CommonStatus.ENABLE);
            dictionaryItem.setUpdateTime(currentTimeMillis);
            dictionaryItem.setCreateTime(currentTimeMillis);
            dictionaryItemMapper.insert(dictionaryItem);
        } else {
            List<DictionaryItem> exise = checkDictionaryItemValue(dictionaryItem);
            if (!CollectionUtil.isEmpty(exise)) {
                for (DictionaryItem d : exise) {
                    if (d.getDictItemValue().equals(dictionaryItem.getDictItemValue()) && !d.getId().equals(dictionaryItem.getId()))
                        throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_ITEM_VALUE);
                }
            }

            exise = checkDictionaryItemName(dictionaryItem);
            if (!CollectionUtil.isEmpty(exise)) {
                for (DictionaryItem d : exise) {
                    if (d.getDictItemName().equals(dictionaryItem.getDictItemName()) && !d.getId().equals(dictionaryItem.getId()))
                        throw new BusinessException(ExceptionInfo.EXIST_DICTIONARY_ITEM_NAME);
                }
            }


            dictionaryItem.setUpdateTime(currentTimeMillis);
            dictionaryItemMapper.updateByPrimaryKeySelective(dictionaryItem);
        }
        return dictionaryItem.getId();
    }


    @Override
    @Transactional
    public void delDictionaryItem(DictionaryItem dictionaryItem) {
        long currentTimeMillis = System.currentTimeMillis();
        DictionaryItemExample example = new DictionaryItemExample();
        DictionaryItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(dictionaryItem.getId());
        DictionaryItem update = new DictionaryItem();
        update.setStatus(CommonStatus.DISENABLE);
        update.setUpdateTime(currentTimeMillis);
        dictionaryItemMapper.updateByExampleSelective(update, example);
    }


    @Override
    @Transactional(readOnly = true)
    public PageInfo<DictionaryItemWithQuery> queryDictionaryItem(DictionaryItemQuery query) {
        Map param = new HashMap();
        DictionaryItemExample example = new DictionaryItemExample();
        DictionaryItemExample.Criteria criteria = example.createCriteria();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getDictItemName()))
            param.put("dictItemName", org.apache.commons.lang3.StringUtils.wrap(query.getDictItemName(), "%"));


        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getDictItemValue()))
            param.put("dictItemValue", org.apache.commons.lang3.StringUtils.wrap(query.getDictItemValue(), "%"));

        if (query.getDictionaryId() != null)
            param.put("dictionaryId", query.getDictionaryId());

        Page dictionaryItemPages = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        dictionaryItemMapper.selectConfuse(param);

        return dictionaryItemPages.toPageInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<DictionaryWithQuery> queryDictionary(DictionaryQuery query) {
        Map param = new HashMap();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getDictName()))
            param.put("dickName", org.apache.commons.lang3.StringUtils.wrap(query.getDictName(), "%"));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getDictValue()))
            param.put("dictValue", org.apache.commons.lang3.StringUtils.wrap(query.getDictValue(), "%"));
        if (query.getTypeId() != null)
            param.put("typeId", query.getTypeId());
        Page dictionaryPages = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        dictionaryMapper.selectConfuse(param);

        return dictionaryPages.toPageInfo();
    }

    private List<DictionaryType> checkDictionaryTypeUnique(DictionaryType dictionaryType) {
        DictionaryTypeExample dictionaryTypeExample = new DictionaryTypeExample();
        DictionaryTypeExample.Criteria criteria = dictionaryTypeExample.createCriteria();
        criteria.andNameEqualTo(StringUtils.trim(dictionaryType.getName()));
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<DictionaryType> exist = dictionaryTypeMapper.selectByExample(dictionaryTypeExample);
        return exist;
    }

    private List<Dictionary> checkDictionaryName(Dictionary dictionary) {
        DictionaryExample dictionaryExample = new DictionaryExample();
        DictionaryExample.Criteria criteria = dictionaryExample.createCriteria();
        criteria.andDictNameEqualTo(StringUtils.trim(dictionary.getDictName()));
        criteria.andTypeIdEqualTo(dictionary.getTypeId());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<Dictionary> exist = dictionaryMapper.selectByExample(dictionaryExample);
        return exist;
    }

    private List<Dictionary> checkDictionaryValue(Dictionary dictionary) {
        DictionaryExample dictionaryExample = new DictionaryExample();
        DictionaryExample.Criteria criteria = dictionaryExample.createCriteria();
        criteria.andDictValueEqualTo(StringUtils.trim(dictionary.getDictValue()));
        criteria.andTypeIdEqualTo(dictionary.getTypeId());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<Dictionary> exist = dictionaryMapper.selectByExample(dictionaryExample);
        return exist;
    }

    private List<DictionaryItem> checkDictionaryItemValue(DictionaryItem dictionaryItem) {
        DictionaryItemExample dictionaryItemExample = new DictionaryItemExample();
        DictionaryItemExample.Criteria criteria = dictionaryItemExample.createCriteria();
        criteria.andDictionaryIdEqualTo(dictionaryItem.getDictionaryId());
        criteria.andDictItemValueEqualTo(dictionaryItem.getDictItemValue());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<DictionaryItem> exist = dictionaryItemMapper.selectByExample(dictionaryItemExample);
        return exist;
    }

    private List<DictionaryItem> checkDictionaryItemName(DictionaryItem dictionaryItem) {
        DictionaryItemExample dictionaryItemExample = new DictionaryItemExample();
        DictionaryItemExample.Criteria criteria = dictionaryItemExample.createCriteria();
        criteria.andDictionaryIdEqualTo(dictionaryItem.getDictionaryId());
        criteria.andDictItemNameEqualTo(dictionaryItem.getDictItemValue());
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        List<DictionaryItem> exist = dictionaryItemMapper.selectByExample(dictionaryItemExample);
        return exist;
    }

    @Override
    public PageInfo<DictionaryType> queryDictionaryType(DictionaryTypeQuery query) {
        DictionaryTypeExample dictionaryTypeExample = new DictionaryTypeExample();
        dictionaryTypeExample.setOrderByClause("createTime DESC");
        DictionaryTypeExample.Criteria criteria = dictionaryTypeExample.createCriteria();
        if(StringUtils.isNotBlank(query.getName()))
            criteria.andNameLike(org.apache.commons.lang3.StringUtils.wrap(query.getName(), "%"));
        criteria.andStatusEqualTo(CommonStatus.ENABLE);

        Page dictionaryTypePages = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        dictionaryTypeMapper.selectByExample(dictionaryTypeExample);
        return dictionaryTypePages.toPageInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<ApiInfoWithDictionary> queryApiDict(ApiInfoWithSubApiQuery query) {
        ApiInfoExample example = new ApiInfoExample();
        example.setOrderByClause("createTime DESC");
        ApiInfoExample.Criteria criteria = example.createCriteria();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getApiName()))
            criteria.andApiNameLike(org.apache.commons.lang3.StringUtils.wrap(query.getApiName(), "%"));
        criteria.andStatusEqualTo(CommonStatus.ENABLE);
        Page apiInfoPage = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        apiInfoMapper.selectByExampleWithDictionary(example);
        List<ApiInfoWithDictionary> list = apiInfoPage.getResult();
        for (ApiInfoWithDictionary ai : list) {
            ApiRateDistributeExample apiRateDistributeExample = new ApiRateDistributeExample();
            ApiRateDistributeExample.Criteria cri = apiRateDistributeExample.createCriteria();
            cri.andApiIdEqualTo(ai.getId());
            cri.andStatusEqualTo(CommonStatus.ENABLE);
            List<ApiRateDistributeWithDictionary> subs = new ArrayList<>();
            ApiRateDistributeWithDictionary sub = new ApiRateDistributeWithDictionary();
            sub.setResponseTransParam(ai.getResponseTransParam());
            sub.setInterfaceName(ai.getInterfaceName());
            subs.add(sub);
            subs.addAll(1, apiRateDistributeMapper.selectByExampleWithDictionary(apiRateDistributeExample));
            ai.setSubApis(subs);
        }
        PageInfo pageInfo = apiInfoPage.toPageInfo();
        pageInfo.setList(list);
        return pageInfo;
    }
}
