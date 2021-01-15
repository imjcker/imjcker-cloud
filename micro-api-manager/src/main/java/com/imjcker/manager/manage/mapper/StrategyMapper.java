package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.po.Strategy;
import com.imjcker.manager.manage.po.query.StrategyQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {
    @Select({"<script>",
            "select count(1) from current_limit_strategy ",
            "WHERE",
            "<if test='query.name != null'>",
            "name like CONCAT('%','${query.name}','%') AND",
            "</if>",
            "status =1 ",
            "</script>"})
    public int queryCount(@Param("query") StrategyQuery query);

    @Select({"<script>",
            "select * from current_limit_strategy ",
            "WHERE",
            "<if test='query.name != null'>",
            "name like CONCAT('%','${query.name}','%')  AND",
            "</if>",
            "status =1  ORDER BY createTime desc",
            " limit #{query.startIndex}, #{query.pageSize}",
            "</script>"})
    public List<Strategy> queryByPage(@Param("query") StrategyQuery query);
    @Select("select uuid from current_limit_strategy where id =#{strategyId}")
    public String queryStrategyUid(@Param("strategyId")Integer strategyId);
    @Update("update current_limit_strategy set unit = #{entity.unit}, no = #{entity.no},updateTime=#{entity.updateTime} ,"
            + "name = #{entity.name}"
            + "where id = #{entity.id}")
    void updateStrategy(@Param("entity")Strategy entity);
    @Select({"<script>",
            "select count(1) from current_limit_strategy ",
            "<where>",
            "status =1",
            "and name =#{name}",
            "</where>",
            "</script>"})
    int countName(@Param("name")String name);
}
