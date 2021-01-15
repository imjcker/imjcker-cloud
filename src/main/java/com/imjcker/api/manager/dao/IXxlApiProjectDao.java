package com.imjcker.api.manager.dao;

import com.imjcker.api.manager.core.model.XxlApiProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by imjcker on 17/3/30.
 */
@Mapper
public interface IXxlApiProjectDao {

    public int add(XxlApiProject xxlApiProject);
    public int update(XxlApiProject xxlApiProject);
    public int delete(@Param("id") int id);

    public XxlApiProject load(@Param("id") int id);
    public List<XxlApiProject> pageList(@Param("offset") int offset,
                                        @Param("pagesize") int pagesize,
                                        @Param("name") String name,
                                        @Param("bizId") int bizId);
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("name") String name,
                             @Param("bizId") int bizId);

}
