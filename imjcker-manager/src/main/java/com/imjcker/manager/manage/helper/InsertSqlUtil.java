package com.imjcker.manager.manage.helper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class InsertSqlUtil {
    /**
     * 根据实体转换成sql语句
     */
    public static String creatInsert(String name,Object entity){
        String sql = "insert into "+name+" ";
        String column = ""; // 列
        String c_values = ""; // 列值
        List<Map<String, Object>> list = getFiledsInfo(entity);
        for (int i = 0; i < list.size(); i++) {
            //約定id在數據庫自動生成
            if (list.get(i).get("f_name").toString() == "id")
                i++;
            if (list.get(i).get("f_value") != null ) {//&& StringUtils.isNotBlank(list.get(i).get("f_value").toString())
                // System.out.println("属性数据类型：" + list.get(i).get("f_type"));
                String tempColumn = list.get(i).get("f_name").toString();
                if ("apiId".equals(tempColumn)){
                    column += tempColumn+",";
                    c_values += "'" + "@result" + "',";
                } else if("requestParamsId".equals(tempColumn)){
                    column += tempColumn+",";
                    c_values += "'" + "@paramId" + "',";
                }else if("apiGroupId".equals(tempColumn) || "groupId".equals(tempColumn)){
                    column += tempColumn+",";
                    c_values += "'" + "@apiGroupId" + "',";
                }else{
                    column += tempColumn + ",";
                    c_values += "'" + list.get(i).get("f_value") + "',";
                }
            }
        }
        sql += "(" + column.substring(0, column.length() - 1) + ") values ("
                + c_values.substring(0, c_values.length() - 1) + ");";
        return sql;
    }

    /**
     * 根据属性名获取属性值
     * */
    protected static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 类名(obj_name)获取属性类型(f_type)，属性名(f_name)，属性值(f_value)的map组成的list
     * */
    @SuppressWarnings("unused")
    protected static List getFiledsInfo(Object o) {
        List<Map> list = new ArrayList();
        List<Field> listField = new ArrayList<>();
        Class tempClass=o.getClass();
        while(tempClass!=null){
            listField.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass=tempClass.getSuperclass();
        }
        listField.forEach(field -> {
            Map<String, Object> infoMap;
            infoMap = new HashMap<String, Object>();
            infoMap.put("obj_name", o.getClass());
            infoMap.put("f_type", field.getType().toString());
            infoMap.put("f_name", field.getName());
            infoMap.put("f_value", getFieldValueByName(field.getName(), o));
            list.add(infoMap);
        });
        return list;
    }
}
