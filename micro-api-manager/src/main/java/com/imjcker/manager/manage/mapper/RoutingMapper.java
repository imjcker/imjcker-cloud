package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.RoutingStrategy;
import com.imjcker.manager.manage.po.query.RoutingQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoutingMapper extends BaseMapper<RoutingStrategy> {

    @Select({"<script>",
            "select * from api_mapping_info ",
            "WHERE",
            "<if test='query.name != null'>",
            "name like CONCAT('%','${query.name}','%')  AND",
            "</if>",
            "enable =1  ORDER BY path desc",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<RoutingStrategy> queryByPage(@Param("query") RoutingQuery query);

    @Select({"<script>",
            "select count(1) from api_mapping_info ",
            "WHERE",
            "<if test='query.name != null'>",
            "name like CONCAT('%','${query.name}','%') AND",
            "</if>",
            "enable =1 ",
            "</script>"})
    public int queryCount(@Param("query") RoutingQuery query);

    @Select({"<script>",
            "select count(1) from api_mapping_info ",
            "<where>",
            "<if test='entity.id != null'>",
            "id != #{entity.id} AND",
            "</if>",
            "enable =1",
            "and name =#{entity.name}",
            "</where>",
            "</script>"})
    int countName(@Param("entity") RoutingStrategy entity);

    @Select({"<script>",
            "select count(1) from api_mapping_info ",
            "<where>",
            "<if test='entity.id != null'>",
            "id != #{entity.id} AND",
            "</if>",
            "enable =1",
            "and path =#{entity.path}",
            "</where>",
            "</script>"})
    int countPath(@Param("entity") RoutingStrategy entity);

    @Update("update api_mapping_info set path = #{entity.path}, aUrl = #{entity.aUrl},bUrl=#{entity.bUrl},name=#{entity.name} ,"
            + "description = #{entity.description}"
            + "where id = #{entity.id}")
    void updateRoutingStrategy(@Param("entity") RoutingStrategy entity);

    @Delete("delete from api_mapping_info where id = #{entity.id}"
    )
    void deleteRoutingStrategy(@Param("entity") RoutingStrategy entity);

}
