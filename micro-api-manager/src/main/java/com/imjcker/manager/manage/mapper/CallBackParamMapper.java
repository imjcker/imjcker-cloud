package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.CallBackParam;
import com.imjcker.manager.manage.po.CallBackParamExample;
import java.util.List;

import org.apache.ibatis.annotations.*;

public interface CallBackParamMapper {
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "apiId",column = "api_id"),
            @Result(property = "paramName",column = "param_name"),
            @Result(property = "paramType",column = "param_type"),
            @Result(property = "description",column = "description"),
            @Result(property = "position",column = "position"),
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "remark",column = "remark")
    })
    @Select("select * from call_back_param where api_id = #{apiId}")
    @Options(useCache = false,flushCache = Options.FlushCachePolicy.TRUE)
    List<CallBackParam> findByApiId(Integer apiId);

    @Delete("delete from call_back_param where id = #{id}")
    void deleteById(Integer id);

    @Delete("delete from call_back_param where api_id = #{apiId}")
    void deleteByApiId(Integer apiId);

    @Update("update call_back_param set param_name = #{paramName},description=#{description},remark=#{remark},parent_id=#{parentId} where id = #{id}")
    void updateById(CallBackParam callBackParam);

    @Update("update call_back_param set param_name = #{paramName},description=#{description},remark=#{remark} where id = #{id}")
    void updateByIdWithoutParentId(CallBackParam callBackParam);


    long countByExample(CallBackParamExample example);

    int deleteByExample(CallBackParamExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CallBackParam record);

    int insertSelective(CallBackParam record);

    List<CallBackParam> selectByExample(CallBackParamExample example);

    CallBackParam selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CallBackParam record, @Param("example") CallBackParamExample example);

    int updateByExample(@Param("record") CallBackParam record, @Param("example") CallBackParamExample example);

    int updateByPrimaryKeySelective(CallBackParam record);

    int updateByPrimaryKey(CallBackParam record);
}
