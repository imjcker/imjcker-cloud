package com.imjcker.manager.manage.po.query;

import com.lemon.common.vo.BaseQuery;

/**
 * .
 * User: lxl
 * Date: 2017/10/13
 * Time: 17:54
 * Description:
 */
public class DictionaryQuery extends BaseQuery {

    private Integer typeId;

    private String dictName;

    private String dictValue;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
}
