package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiResultEtl;
import com.imjcker.manager.manage.po.ApiResultEtlExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiResultEtlMapper {
    int countByExample(ApiResultEtlExample example);

    int deleteByExample(ApiResultEtlExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiResultEtl record);

    int insertSelective(ApiResultEtl record);

    List<ApiResultEtl> selectByExample(ApiResultEtlExample example);

    ApiResultEtl selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiResultEtl record, @Param("example") ApiResultEtlExample example);

    int updateByExample(@Param("record") ApiResultEtl record, @Param("example") ApiResultEtlExample example);

    int updateByPrimaryKeySelective(ApiResultEtl record);

    int updateByPrimaryKey(ApiResultEtl record);
}
