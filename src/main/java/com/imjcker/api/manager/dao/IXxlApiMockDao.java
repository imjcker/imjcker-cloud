package com.imjcker.api.manager.dao;

import com.imjcker.api.manager.core.model.XxlApiMock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by imjcker on 17/4/1.
 */
@Mapper
public interface IXxlApiMockDao {

    public int add(XxlApiMock xxlApiMock);
    public int update(XxlApiMock xxlApiMock);
    public int delete(@Param("id") int id);

    public List<XxlApiMock> loadAll(@Param("documentId") int documentId);
    public XxlApiMock load(@Param("id") int id);
    public XxlApiMock loadByUuid(@Param("uuid") String uuid);

}
