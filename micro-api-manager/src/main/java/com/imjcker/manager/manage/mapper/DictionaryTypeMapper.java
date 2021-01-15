package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.DictionaryType;
import com.imjcker.manager.manage.po.DictionaryTypeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictionaryTypeMapper {
    int countByExample(DictionaryTypeExample example);

    int deleteByExample(DictionaryTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DictionaryType record);

    int insertSelective(DictionaryType record);

    List<DictionaryType> selectByExample(DictionaryTypeExample example);

    DictionaryType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DictionaryType record, @Param("example") DictionaryTypeExample example);

    int updateByExample(@Param("record") DictionaryType record, @Param("example") DictionaryTypeExample example);

    int updateByPrimaryKeySelective(DictionaryType record);

    int updateByPrimaryKey(DictionaryType record);
}
