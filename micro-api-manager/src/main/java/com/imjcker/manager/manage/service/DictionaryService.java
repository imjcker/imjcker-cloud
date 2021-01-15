package com.imjcker.manager.manage.service;

import com.github.pagehelper.PageInfo;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.query.ApiInfoWithSubApiQuery;
import com.imjcker.manager.manage.po.query.DictionaryItemQuery;
import com.imjcker.manager.manage.po.query.DictionaryQuery;
import com.imjcker.manager.manage.po.query.DictionaryTypeQuery;
import com.imjcker.manager.manage.po.*;

/**
 * .
 * User: lxl
 * Date: 2017/10/12
 * Time: 14:20
 * Description:
 */
public interface DictionaryService {


    int saveOrUpateDictionaryType(DictionaryType dictionaryType);

    void delDictionaryType(DictionaryType dictionaryType);

    int saveOrUpateDictionary(Dictionary dictionary);

    void delDictionary(Dictionary dictionary);

    int saveOrUpateDictionaryItem(DictionaryItem dictionaryItem);

    void delDictionaryItem(DictionaryItem dictionaryItem);

    PageInfo<DictionaryItemWithQuery> queryDictionaryItem(DictionaryItemQuery query);

    PageInfo<DictionaryWithQuery> queryDictionary(DictionaryQuery query);

    PageInfo<DictionaryType> queryDictionaryType(DictionaryTypeQuery query);

    PageInfo<ApiInfoWithDictionary> queryApiDict(ApiInfoWithSubApiQuery query);
}
