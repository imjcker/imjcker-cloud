package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.DictionaryItem;
import com.imjcker.manager.manage.po.DictionaryItemExample;
import com.imjcker.manager.manage.po.DictionaryItemWithQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictionaryItemMapper {
    int countByExample(DictionaryItemExample example);

    int deleteByExample(DictionaryItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DictionaryItem record);

    int insertSelective(DictionaryItem record);

    List<DictionaryItem> selectByExample(DictionaryItemExample example);

    List<DictionaryItemWithQuery> selectConfuse(Map param);

    DictionaryItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DictionaryItem record, @Param("example") DictionaryItemExample example);

    int updateByExample(@Param("record") DictionaryItem record, @Param("example") DictionaryItemExample example);

    int updateByPrimaryKeySelective(DictionaryItem record);



    int updateByPrimaryKey(DictionaryItem record);
}
