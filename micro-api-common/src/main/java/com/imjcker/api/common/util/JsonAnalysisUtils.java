package com.imjcker.api.common.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.vo.JsonLinearData;
import com.imjcker.api.common.vo.JsonDataTypeEnum;

/**

 * @Title JSON的解析工具类
 * @Description 从根节点到叶子节点的key值数据,需要保证输入的json格式正确，为了保证解析结果的可用性，所以数据不能为NULL，否则解析不到节点数据
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 * @author Lemon.kiana
 * @version 2.0
 * 2017年11月9日 下午4:00:07
 */
public class JsonAnalysisUtils {
	private JsonAnalysisUtils() {
	}

	/**
	 * 检查object的类型
	 * @param object
	 * @return
	 * @Version 2.0
	 */
	public static JsonDataTypeEnum getType(Object object){
	     if (object instanceof JSONObject) {
	    	 return JsonDataTypeEnum.JSONObject;
	     } else if (object instanceof JSONArray) {
	    	 return JsonDataTypeEnum.JSONArray;
	     } else if (object instanceof String) {
	    	 return JsonDataTypeEnum.String;
	     } else if (object instanceof Short) {
	    	 return JsonDataTypeEnum.Short;
	     } else if (object instanceof Integer) {
	    	 return JsonDataTypeEnum.Integer;
	     } else if (object instanceof Long) {
	    	 return JsonDataTypeEnum.Long;
	     } else if (object instanceof Float) {
	    	 return JsonDataTypeEnum.Float;
	     } else if (object instanceof Double) {
	    	 return JsonDataTypeEnum.Double;
	     } else {
	    	 return JsonDataTypeEnum.Boolean;
	     }
	}



//	public static void main (String[] args) {
		//String s1 = "{    \"errorCode\": 200,    \"errorMsg\": \"请求成功!\",    \"uid\": \"20161114130244584UdhReOQKYylmBXD\",    \"data\": {        \"hits\": \"0\",        \"message\": \"未命中\"    }}";
		//String s2 = "{    \"errorCode\": 200,    \"errorMsg\": \"请求成功!\",    \"uid\": \"20161114130244584UdhReOQKYylmBXD\",    \"data\": {        \"hits\": \"0\",        \"message\": \"未命中\",		\"list\": [		  {			\"id\": 58,			\"companyName\": \"11\",			\"description\": \"111\",			\"status\": 1,			\"createTime\": 1509524084180,			\"updateTime\": 1509524084180,			\"companyTagNum\": 1		  },		  {			\"id\": 57,			\"companyName\": \"接口管理测试系统\",			\"description\": \"接口管理测试系统\",			\"status\": 1,			\"createTime\": 1508912839740,			\"updateTime\": 1508917020153,			\"companyTagNum\": 1		  }    ]    }}";
		//String s = "{    \"errorCode\": 200,    \"errorMsg\": \"请求成功\",    \"uid\": \"20160811101840779sEVSljbyBCjNiAC\",    \"data\": {        \"result\": {            \"content\": [                {                    \"cell_phone\": \"18691179115\",                    \"id_type\": 1,                    \"baidu_account\": \"\",                    \"discredit_records\": {                        \"netloan\": 1,                        \"tax\": 1,                        \"court\": 1                    },                    \"real_name\": \"吴燕\",                    \"imei\": \"\",                    \"gender\": 1,                    \"id_no\": \"610628197707020043\"                }            ]        }    }}";
		//String ss = "{    \"errorCode\": 200,    \"errorMsg\": \"请求成功\",    \"uid\": \"20160811101840779sEVSljbyBCjNiAC\",    \"data\": {        \"result\": {            \"content\": [                {                    \"cell_phone\": \"18691179115\",                    \"id_type\": 1,                    \"baidu_account\": \"\",                    \"discredit_records\": {                        \"netloan\": 1,                        \"tax\": 1,                        \"court\": 1                    },                    \"real_name\": \"吴燕\",                    \"imei\": \"\",                    \"gender\": 1,                    \"id_no\": \"610628197707020043\"                }            ],			\"ceshi\":[				{					\"one\":1,					\"two\":2,					\"three\":3				}			]        }    }}";
//		String json = "{\"appKey\":\"puhuikuaixin\",\"orderId\":\"66D81F390A0104691EE42C0831722031\",\"apply_relation_information\":{\"contact_number_relate_contact_person_num_3m_details\":{\"13333333333\":[\"123\",\"13423\"]},\"phone_relate_name_num_3m_details\":[\"123\"],\"time\":1998}}";
//		String json = "{\"appKey\":\"puhuikuaixin\",\"orderId\":\"66D81F390A0104691EE42C0831722031\",\"apply_relation_information\":{\"contact_number_relate_contact_person_num_3m_details\":{\"13333333333\":[{\"hehea\":\"123\"},{\"heheb\":\"13423\"}]},\"phone_relate_name_num_3m_details\":[{\"hehec\":\"123\"}],\"time\":1998}}";
//		JSONObject jsonStr = JSONObject.parseObject(json);
//		Integer apiId = 12;
//		String apiName = "测试接口";
//		List<JsonLinearData> list = new ArrayList<JsonLinearData>();
//		StringBuffer dataKey = new StringBuffer();
//		getDataList(jsonStr,apiId,apiName,list,dataKey);
//		System.out.println("结束了");
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(i + 1 + " ,dataKey: " + list.get(i).getDataKey());
//			System.out.println(i + 1 + " ,apiId: " + list.get(i).getApiId() + " ,dataKey: " + list.get(i).getDataKey() + " ,dataType: " + list.get(i).getDataType());
//		}
//	}


	public static void getDataList(Object object, Integer apiId, String apiName, List<JsonLinearData> list, StringBuffer dataKey) {
		//判断类型分别进行处理
		if ((!getType(object).equals(JsonDataTypeEnum.JSONObject)) && (!getType(object).equals(JsonDataTypeEnum.JSONArray))) { //value
			//加入list
			dataKey.deleteCharAt(dataKey.length()-1);
			list.add(getJsonLinearData(dataKey,apiId,apiName,getType(object)));
			Integer end = dataKey.lastIndexOf(".");
			if (!end.equals(-1)) {
				String str = dataKey.substring(0, end + 1);
				dataKey.delete(0, dataKey.length()).append(str);
			} else {
				dataKey.delete(0, dataKey.length());
			}
		} else if (getType(object).equals(JsonDataTypeEnum.JSONObject)) { //JSONObject
			//转换成JSONObject
			JSONObject jsonObject = JSONObject.parseObject(object.toString());
			//获取下级节点keys
			Set<Entry<String,Object>>jsonEntry = jsonObject.entrySet();
			Iterator<Entry<String, Object>> ite = jsonEntry.iterator();
			Entry<String, Object> data;
			while (ite.hasNext()) {
				data = ite.next();
				dataKey.append(data.getKey()).append(".");
				Object value = data.getValue();
				getDataList(value,apiId,apiName,list,dataKey);
			}
			if (dataKey.toString().endsWith(".")) {
				dataKey.deleteCharAt(dataKey.length() - 1);
				Integer end = dataKey.lastIndexOf(".");
				if (!end.equals(-1)) {
					String str = dataKey.substring(0, end + 1);
					dataKey.delete(0, dataKey.length()).append(str);
				} else {
					dataKey.delete(0, dataKey.length());
				}
			}
		} else if (getType(object).equals(JsonDataTypeEnum.JSONArray)) { //JSONArray
			JSONArray jsonArr = JSONArray.parseArray(object.toString());
			Iterator itArr = jsonArr.iterator();
			//while (itArr.hasNext()) {
				Object obj = itArr.next();
//				Object obj = (JSONObject)itArr.next();
				getDataList(obj,apiId,apiName,list,dataKey);
			//}
		}
	}


//	private static String getDataList(Object object, Integer apiId, String apiName, List<JsonLinearData> list,StringBuffer dataKey) {
//		//object为JSONObject对象
//		if (getType(object).equals(JsonDataTypeEnum.JSONObject)) {
//			//转换成JSONObject
//			JSONObject jsonObj = new JSONObject(object);
//			//获取下级节点keys
//			Iterator<String> dataIte = jsonObj.keys();
//			String singleKey = "";
//			while (dataIte.hasNext()) {
//				//获取单个key并存储
//				singleKey = dataIte.next();
//				dataKey.append(singleKey).append(".");
//
//				Object dataValue = jsonObj.get(singleKey);
//				//判断类型分别进行处理
//				if (0 == checkType(dataValue.toString())) { //value
//					dataKey.append(dataValue.toString());
//					//加入list
//					list.add(getJsonLinearData(dataKey,apiId,apiName,getType(dataValue)));
//				} else if (1 == checkType(dataValue.toString())) { //JSONObject
//					//转换成JSONObject
//					JSONObject jsonObject = new JSONObject(dataValue);
//					getDataList(dataValue,apiId,apiName,list,dataKey);
//					//获取下级节点keys
//					Iterator<String> ite = jsonObj.keys();
//					String key = "";
//					while (ite.hasNext()) {
//						key = ite.next();
//						dataKey.append(key).append(".");
//						Object value = jsonObj.get(key);
//						getDataList(value,apiId,apiName,list,dataKey);
//					}
//				} else if (2 == checkType(dataValue.toString())) { //JSONArray
//					JSONArray jsonArr = new JSONArray(dataValue);
//					Iterator itArr = jsonArr.iterator();
//					while (itArr.hasNext()) {
//						Object obj = (JSONObject)itArr.next();
//						getDataList(obj,apiId,apiName,list,dataKey);
//					}
//				}
//				getDataList(jsonObj.get(singleKey),apiId,apiName,list,dataKey);
//			}
//		} else if (getType(object).equals(JsonDataTypeEnum.JSONArray)) {
////			JSONArray jsonArr = object.getJSONArray(singleKey);
//		} else {
//
//		}
//
//		return null;
//	}


	/**
	 * 构造一条线性数据
	 * @param dataKey
	 * @param apiId
	 * @param apiName
	 * @return
	 * @Version 2.0
	 */
	private static JsonLinearData getJsonLinearData(StringBuffer dataKey, Integer apiId, String apiName,JsonDataTypeEnum dataType) {
		JsonLinearData data = new JsonLinearData();
		data.setApiId(apiId);
		data.setApiName(apiName);
		data.setDataKey(dataKey.toString());
		data.setDataType(dataType.toString());
		return data;
	}

}
