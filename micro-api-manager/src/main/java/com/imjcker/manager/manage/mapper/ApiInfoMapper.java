package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.model.ApiRiskIndex;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.vo.AutoTestResponse;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ApiInfoMapper {
    int countByExample(ApiInfoExample example);

    int deleteByExample(ApiInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ApiInfoWithBLOBs record);

    int insertSelective(ApiInfoWithBLOBs record);

    List<ApiInfoWithBLOBs> selectByExampleWithBLOBs(ApiInfoExample example);

    List<ApiInfoWithSubApi> selectByExampleWithSubApi(ApiInfoExample example);

    List<ApiInfoWithBLOBs> selectByExample(ApiInfoExample example);

    ApiInfoWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ApiInfoWithBLOBs record, @Param("example") ApiInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") ApiInfoWithBLOBs record, @Param("example") ApiInfoExample example);

    int updateByExample(@Param("record") ApiInfo record, @Param("example") ApiInfoExample example);

    int updateByPrimaryKeySelective(ApiInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ApiInfoWithBLOBs record);

    int updateByPrimaryKey(ApiInfo record);

    List<ApiInfoWithDictionary> selectByExampleWithDictionary(ApiInfoExample example);

    int updateApiTransParamById(HashMap<String, Object> map);

    List<ApiRiskIndex> selectApiRiskIndexByApiId(Map<String, Object> params);

    List<AutoTestResponse> selectRequestParamsForAutoTest(Map<String, Object> params);

    int updatePartByPrimaryKeyWithBLOBs(ApiInfoWithBLOBs record);//更新除publishProductEnvStatus，productEnvVersion，publishTestEnvStatus，testEnvVersion所有字段

    int updateVersionIdByApiId(ApiInfoWithBLOBs record);
}
