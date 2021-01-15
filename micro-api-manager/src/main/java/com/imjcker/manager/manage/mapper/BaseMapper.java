package com.imjcker.manager.manage.mapper;

import com.imjcker.manager.manage.provider.BaseSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BaseMapper<T>{

	@InsertProvider(type= BaseSqlProvider.class,method="insert")
	public void insert(T record);

	@InsertProvider(type=BaseSqlProvider.class,method="replace")
	public void replace(T record);

	@SelectProvider(type=BaseSqlProvider.class,method="select")
	public T select(T record);

	@SelectProvider(type=BaseSqlProvider.class,method="select")
	public List<T> selectList(T record);

	@UpdateProvider(type=BaseSqlProvider.class,method="update")
	public void update(T record);

	@UpdateProvider(type=BaseSqlProvider.class,method="delete")
	public void delete(T record);

}
