package com.imjcker.manager.manage.provider;


import com.imjcker.manager.manage.helper.ReflectHelper;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class BaseSqlProvider<T> {

	public String insert(T record) throws Exception{
		BEGIN();

		INSERT_INTO(ReflectHelper.getTableName(record));

		VALUES(ReflectHelper.getInsertColumnNames(record), ReflectHelper.getInsertColumnValues(record));
		return SQL();
	}

	public String replace(T record) throws Exception{
		String sql = insert(record);
		return sql.replaceFirst("INSERT", "REPLACE");
	}

	public String select(T record) throws Exception{
		BEGIN();

		SELECT("*");
		FROM(ReflectHelper.getTableName(record));
		String conditions = ReflectHelper.getSelectConditions(record);
		if (!"".equals(conditions))
			WHERE(conditions+ " and status=1 ");
		return SQL();
	}


	public String update(T record) throws Exception {
		BEGIN();

		UPDATE(ReflectHelper.getTableName(record));
		SET(ReflectHelper.getUpdateConditions(record));
		WHERE("id = "+ReflectHelper.getIntBusinessId(record));
		return SQL();
	}
	/**逻辑删除*/
	public String delete(T record) throws Exception {
		Long currentTime=System.currentTimeMillis();
		BEGIN();
		UPDATE(ReflectHelper.getTableName(record));
		SET("status =2 ,updateTime="+currentTime);
		WHERE("id = "+ReflectHelper.getIntBusinessId(record));
		return SQL();
	}
}
