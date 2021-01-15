package com.imjcker.manager.manage.mapper;


import com.imjcker.manager.manage.po.Dictionary;
import com.imjcker.manager.manage.po.DictionaryExample;
import com.imjcker.manager.manage.po.DictionaryWithQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DictionaryMapper {
    int countByExample(DictionaryExample example);

    int deleteByExample(DictionaryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Dictionary record);

    int insertSelective(Dictionary record);

    List<Dictionary> selectByExample(DictionaryExample example);

    List<DictionaryWithQuery> selectConfuse(Map m);

    Dictionary selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Dictionary record, @Param("example") DictionaryExample example);

    int updateByExample(@Param("record") Dictionary record, @Param("example") DictionaryExample example);

    int updateByPrimaryKeySelective(Dictionary record);

    int updateByPrimaryKey(Dictionary record);
}
