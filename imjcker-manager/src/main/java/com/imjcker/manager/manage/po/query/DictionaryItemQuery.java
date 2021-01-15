package com.imjcker.manager.manage.po.query;

import com.lemon.common.vo.BaseQuery;

/**
 * .
 * User: lxl
 * Date: 2017/10/13
 * Time: 17:54
 * Description:
 */
public class DictionaryItemQuery extends BaseQuery {

    private String dictItemName;

    private String dictItemValue;

    private Integer dictionaryId;

    public String getDictItemName() {
        return dictItemName;
    }

    public void setDictItemName(String dictItemName) {
        this.dictItemName = dictItemName;
    }

    public String getDictItemValue() {
        return dictItemValue;
    }

    public void setDictItemValue(String dictItemValue) {
        this.dictItemValue = dictItemValue;
    }

    public Integer getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Integer dictionaryId) {
        this.dictionaryId = dictionaryId;
    }
}
