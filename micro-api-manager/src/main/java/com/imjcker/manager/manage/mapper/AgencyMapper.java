package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.Agency;
import com.imjcker.manager.manage.po.query.AgencyQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface AgencyMapper extends BaseMapper<Agency> {
    @Select({"<script>",
            "select * from agency_api_account ",
            "WHERE",
            "<if test='query.apiName != null'>",
            "apiName like CONCAT('%','${query.apiName}','%')  AND",
            "</if>",
            "<if test='query.appKey != null'>",
            "appKey like CONCAT('%','${query.appKey}','%')  AND",
            "</if>",
            "<if test='query.apiGroupName != null'>",
            "apiGroupName like CONCAT('%','${query.apiGroupName}','%')  AND",
            "</if>",
            "<if test='query.sourceFlag != null'>",
            "sourceFlag = #{query.sourceFlag} AND",
            "</if>",
            "status =1  ORDER BY id",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<Agency> queryByPage(@Param("query") AgencyQuery query);

    @Delete("update  agency_api_account set status=2 where id = #{entity.id} and status=1"
    )
    void deleteAgency(@Param("entity") Agency entity);

    @Select({"<script>",
            "select count(1) from agency_api_account ",
            "WHERE",
            "<if test='query.apiName != null'>",
            "apiName like CONCAT('%','${query.apiName}','%')  AND",
            "</if>",
            "<if test='query.appKey != null'>",
            "appKey like CONCAT('%','${query.appKey}','%')  AND",
            "</if>",
            "<if test='query.apiGroupName != null'>",
            "apiGroupName like CONCAT('%','${query.apiGroupName}','%')  AND",
            "</if>",
            "<if test='query.sourceFlag != null'>",
            "sourceFlag = #{query.sourceFlag} AND",
            "</if>",
            "status =1 ",
            "</script>"})
    public int queryCount(@Param("query") AgencyQuery query);

    @Select({"<script>",
            "select * from agency_api_account ",
            "WHERE",
            "<if test='entity.sourceFlag != null'>",
            "sourceFlag = #{entity.sourceFlag} AND",
            "</if>",
            "<if test='entity.apiGroupId != null'>",
            "apiGroupId = #{entity.apiGroupId} AND",
            "</if>",
            "<if test='entity.apiName != null'>",
            "apiName = #{entity.apiName}  AND",
            "</if>",
            "<if test='entity.appKey != null'>",
            "appKey = #{entity.appKey}  AND",
            "</if>",
            "status =1",
            "</script>"})
    public List<Agency> selectAgency(@Param("entity") Agency entity);

    @Select({"<script>",
            "select * from agency_api_account ",
            "WHERE",
            "appKey = #{appKey} AND",
            "apiGroupId = #{groupId} AND",
            "status =1  ORDER BY apiId",
            "</script>"})
    List<Agency> queryByAppKeyAndgroup(@Param("appKey") String appKey, @Param("groupId") Integer groupId);

    @Select({"<script>",
            "select * from agency_api_account ",
            "WHERE",
            "appKey = #{entity.appKey} AND",
            "apiGroupId = #{entity.apiGroupId} AND",
            "sourceFlag != #{entity.sourceFlag} AND",
            "status =1",
            "</script>"})
    List<Agency> selectBySourceAndGroup(@Param("entity") Agency entity);
}
