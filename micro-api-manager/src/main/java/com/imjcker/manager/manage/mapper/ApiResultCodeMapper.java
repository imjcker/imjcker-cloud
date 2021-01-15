package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.ApiResultCode;
import com.imjcker.manager.manage.po.ApiResultCodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApiResultCodeMapper {
    long countByExample(ApiResultCodeExample example);

    int deleteByExample(ApiResultCodeExample example);

    int deleteByPrimaryKey(@Param("apiId") Integer apiId, @Param("paramId") Integer paramId);

    int insert(ApiResultCode record);

    int insertSelective(ApiResultCode record);

    List<ApiResultCode> selectByExample(ApiResultCodeExample example);

    ApiResultCode selectByPrimaryKey(@Param("apiId") Integer apiId, @Param("paramId") Integer paramId);

    int updateByExampleSelective(@Param("record") ApiResultCode record, @Param("example") ApiResultCodeExample example);

    int updateByExample(@Param("record") ApiResultCode record, @Param("example") ApiResultCodeExample example);

    int updateByPrimaryKeySelective(ApiResultCode record);

    int updateByPrimaryKey(ApiResultCode record);
}
