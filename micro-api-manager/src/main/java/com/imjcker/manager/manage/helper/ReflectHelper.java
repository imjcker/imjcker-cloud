package com.imjcker.manager.manage.helper;


import com.imjcker.manager.manage.annotation.BusinessId;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectHelper {

	private static Field getBusinessIdField(Class<?> entity) {
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			BusinessId businessId = field.getAnnotation(BusinessId.class);
			if (businessId != null) {
				return field;
			}
		}
		return null;
	}

	private static Field getAnnotationField(Class<?> entity, Class<? extends Annotation> annotationClass) {
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(annotationClass) != null) {
				return field;
			}
		}
		return null;
	}


	public static String getTableName(Object object) {
		return getTableName(object.getClass());
	}

	public static String getTableName(Class<?> entity) {
		Table table = entity.getAnnotation(Table.class);
		if (table != null)
			return table.name();
		throw new RuntimeException("POJO @Table Annotation undefine, failed to get tableName!");
	}

	public static String getSelectConditions(Object object) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Field> columnList = getColumnList(object);
		int i = 0;
		for (Field field : columnList) {
			Column column = field.getAnnotation(Column.class);
			JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (column != null && !column.name().equals("")) {
				Object conditionObject = getReadValue(object, field.getName());
				if (conditionObject!=null) {
					if (i++ != 0)
						sb.append(" AND ");
					sb.append(column.name()).append("='").append(conditionObject).append("'");
				}
			} else if (joinColumn != null && !joinColumn.name().equals("")) {
				Long conditionObject = getBusinessId(object);
				if (conditionObject!=null) {
					if (i++ != 0)
						sb.append(" AND ");
					sb.append(joinColumn.name()).append("='").append(conditionObject).append("'");
				}
			} else {
				Object conditionObject = getReadValue(object, field.getName());
				if (conditionObject!=null) {
					if (i++ != 0)
						sb.append(" AND ");
					sb.append(field.getName()).append("='").append(conditionObject).append("'");
				}
			}
        }
        return sb.toString();
	}

	public static List<Field> getColumnList(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		List<Field> columnList = new ArrayList<Field>(fields.length);
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(JoinColumn.class))
				columnList.add(field);
		}
		return columnList;
	}

	public static Long getBusinessId(Object object) throws Exception {
		Field field = getBusinessIdField(object.getClass());
		if (field == null)
			throw new RuntimeException("@BusinessId Annotation undefine!");
		PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object.getClass());
		Method method = pd.getReadMethod();
		Object value = method.invoke(object);
		// 支持从属性对象中获取BusinessId
		if (value instanceof Long)
			return (Long) value;
		else
			return getBusinessId(value);
	}
	/**对于BusinessId是Integer的实体*/
	public static Integer getIntBusinessId(Object object) throws Exception {
		Field field = getBusinessIdField(object.getClass());
		if (field == null)
			throw new RuntimeException("@BusinessId Annotation undefine!");
		PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object.getClass());
		Method method = pd.getReadMethod();
		Object value = method.invoke(object);
		// 支持从属性对象中获取BusinessId
		if (value instanceof Integer)
			return (Integer) value;
		else
			return 0;
	}
	public static String getInsertColumnNames(Object object) {
		StringBuilder sb = new StringBuilder();
		List<Field> columnList = getColumnList(object);
		int i = 0;
		for (Field field : columnList) {
            if (i++ != 0)
                sb.append(',');
            Column column = field.getAnnotation(Column.class);
    		JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (column != null && !column.name().equals(""))
            	sb.append(column.name());
            else if (joinColumn != null && !joinColumn.name().equals(""))
            	sb.append(joinColumn.name());
    		else
    			sb.append(field.getName());
        }
        return sb.toString();
    }

	public static String getInsertColumnValues(Object object) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Field> columnList = getColumnList(object);
		int i = 0;
		for (Field field : columnList) {
            if (i++ != 0)
                sb.append(',');
            if (field.isAnnotationPresent(JoinColumn.class)) {
            	// 查询该属性对象的BusinessId，不递归查询
            	Field businessId = getBusinessIdField(field.getType());
            	if (businessId == null)
            		throw new RuntimeException("@BusinessId Annotation undefine to @JoinColumn!");
            	sb.append("#{").append(field.getName()).append(".").append(businessId.getName()).append('}');
            } else {
            	sb.append("#{").append(field.getName()).append('}');
            }
        }
        return sb.toString();
    }



	private static Object getReadValue(Object object, String fieldName) throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor(fieldName, object.getClass());
		Method method = pd.getReadMethod();
		return method.invoke(object);
	}

	private static Object setWriteValue(Object object, String fieldName, Object args) throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor(fieldName, object.getClass());
		Method method = pd.getWriteMethod();
		return method.invoke(object, args);
	}


	/**update test*/
	public static String getUpdateConditions(Object object) throws Exception {
		StringBuilder sb = new StringBuilder();
		List<Field> columnList = getColumnList(object);
		int i = 0;
		for (Field field : columnList) {
			Column column = field.getAnnotation(Column.class);
			if (column != null && !column.name().equals("")) {
				Object conditionObject = getReadValue(object, field.getName());
				if (conditionObject!=null) {
					if (i++ != 0)
					sb.append(column.name()).append("='").append(conditionObject).append("',");
				}
			}
			else {
				Object conditionObject = getReadValue(object, field.getName());
				if (conditionObject!=null) {
					/**id进来的时候 序号是为0的 所以就不进入方法*/
					if (i++ != 0)
					sb.append(field.getName()).append("='").append(conditionObject).append("',");
				}
			}
		}
		return sb.toString().substring(0,sb.length()-1);
	}
}
