package com.imjcker.api.common.util;

import java.text.NumberFormat;
import java.util.Set;

import com.imjcker.api.common.vo.Constant;
import com.imjcker.api.common.vo.ZuulHeader;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;


/**
 * @author yezhiyuan
 * @version V1.0
 * @Title: 操作工具包
 * @Package com.lemon.common.util
 * @date 2017年3月27日 下午4:12:26
 */
public class Operator {
    /**
     * @return String    返回类型
     * @Title: 两个int数字求百分比，精确到百分位
     */
    public static String toPercent(Integer a, Integer b) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(2);
        return format.format(Double.parseDouble(a.toString()) / Double.parseDouble(b.toString()));
    }

    /**
     * @param @param time
     * @Title: 时间标记转换为秒
     */
    public static Integer timeToSecond(Integer timeFlag) {
        Integer result = 0;
        switch (timeFlag) {
            case 1:
                result = 60;
                break;//分钟
            case 2:
                result = 60 * 60;
                break;//小时
            case 3:
                result = 24 * 60 * 60;
                break;//天
            default:
                result = 1000;
                break;
        }
        return result;
    }

    public static String paramTypeInteger2String(Integer flag) {
        String result = "";
        switch (flag) {
            case 1:
                result = "String";
                break;
            case 2:
                result = "Integer";
                break;
            case 3:
                result = "Long";
                break;
            case 4:
                result = "Float";
                break;
            case 5:
                result = "Double";
                break;
            case 6:
                result = "Boolean";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public static String paramMustInteger2String(Integer flag) {
        String result = "";
        switch (flag) {
            case 1:
                result = "是";
                break;
            case 2:
                result = "否";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public static String httpMethodInteger2String(Integer flag) {
        String result = "";
        switch (flag) {
            case 1:
                result = "GET";
                break;
            case 2:
                result = "POST";
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    public static String paramLocation(Integer locationIndex){
        String location;
        switch (locationIndex){
            case 1:
                location = "header";
                break;
            case 2:
                location = "query";
                break;
            case 3:
                location = "body";
                break;
            default:
                location = "";
                break;
        }
        return location;
    }

    /**
     * @param @param env
     * @Title: 获取线上/测试环境标记
     */
    public static Integer getEnv(String env) {
        Integer envv = 0;
        if (StringUtils.isBlank(env)) {
            envv = Constant.ENV_ONLINE;
        } else {
            envv = "TEST".equals(env.toUpperCase()) ? Constant.ENV_TEST : Constant.ENV_ONLINE;
        }
        return envv;
    }

    /**
     * @param @param strings
     * @Title: 将string数组内的各值拼接，然后MD5
     */
    public static String MD5ByStringArray(String[] strings) {
        if (strings.length == 0 || strings == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return MD5Utils.MD5(builder.toString());
    }

    /**
     * @return String    返回类型
     * @Title: 按预定规则替换特定的字符串
     */
    public static String replace(String parent, String target, String replace) {
        if (parent == null) {
            return null;
        }
        if (StringUtils.isBlank(replace)) {
        	return parent.replace(StringUtils.join("${", target, "}"), "");
		}
        return parent.replace(StringUtils.join("${", target, "}"), replace);
    }

    /**
     * @return Integer    返回类型
     * @Title: 转换缓存时间为秒
     *
     * -1 : 没缓存
     * -2 : 不限时间，永久缓存
     * >=0 : 有缓存，并限时
     */
    public static Integer cacheTime(Integer cacheUnit, Integer cacheNo) {
        Integer time = 0;
        if (cacheUnit == null) return -1;
        switch (cacheUnit) {
            case 1:
                time = cacheNo * 60 * 60;
                break;//小时
            case 2:
                time = cacheNo * 24 * 60 * 60;
                break;//天
            case 3:
                time = -2;
                break;//不限
            default:
                break;
        }
        return time;
    }

    /**
     * @Title: 过滤header一些信息
     * @param @param key
     * @param @return    设定文件
     * @return Boolean    返回类型
     */
    public static Boolean headerOper(String key) {
    	if (!"content-type".equals(key)
				&& !"connection".equals(key) && !"Connection".equals(key)
				&& !"accept-language".equals(key) && !"Accept-Language".equals(key)
				&& !"host".equals(key) && !"Host".equals(key)
				&& !"accept".equals(key) && !"Accept".equals(key)
				&& !"origin".equals(key) && !"Origin".equals(key)
				&& !"user-agent".equals(key) && !"User-Agent".equals(key)
				&& !"cache-control".equals(key) && !"Cache-Control".equals(key)
				&& !"content-length".equals(key) && !"Content-Length".equals(key)
				&& !"postman-token".equals(key) && !"Postman-Token".equals(key)
				&& !"accept-encoding".equals(key) && !"Accept-Encoding".equals(key)
				&& !"x-forwarded-port".equals(key) && !"x-forwarded-host".equals(key)
				&& !"x-forwarded-proto".equals(key) && !"x-forwarded-for".equals(key)
				&& !"X-B3-Sampled".equals(key) && !"X-B3-TraceId".equals(key)
				&& !"Content-Type".equals(key) && !"X-Span-Name".equals(key)
				&& !"Cache-Control".equals(key) && !"X-B3-SpanId".equals(key)
				&& !"X-B3-ParentSpanId".equals(key)
				&& !"zuul_apiid".equals(key)
				&& !"zuul_versionid".equals(key)
				&& !"zuul_uniqueuuid".equals(key)
				&& !"zuul_requestuuid".equals(key)
				&& !ZuulHeader.PARAM_KEY_ORDER_ID.equals(key)
				&& !ZuulHeader.PARAM_KEY_API_VERSION_ID.equals(key)
				&& !ZuulHeader.PARAM_UID_CREATE_TIME.equals(key)
				&& !ZuulHeader.PARAM_API_ID.equals(key)
				&& !ZuulHeader.PARAM_WEIGTH.equals(key)
				&& !ZuulHeader.PARAM_UNIQUE_UUID.equals(key)) {
    		return true;
    	}
    	return false;
	}

    /**
     * @Title: 判断数据超市返回结果是否存缓存
     * @param @return    设定文件
     * @return Boolean    返回类型
     */
    public static Boolean resultIsToCache(String result) {
		try {
			//数据超市errorCode = 200存缓存
			JSONObject jsonObject = JSONObject.parseObject(result);
			Integer errorCode = jsonObject.getInteger("errorCode");
			if (errorCode != null) {
				if (!errorCode.equals(200)) {
					return false;
				}
			}
			//同盾对接天府银行，信贷保镖API
			Boolean success = jsonObject.getBoolean("success");
			if (success != null) {
				if (!(success == true && jsonObject.get("reason_code") == null)) {
					return false;
				}
			}
			//成都房管局对接天府银行
			String status = jsonObject.getString("status");
			if (status != null) {
				if ("fail".equals(status)) {
					return false;
				}
			}

			//意高对接天府银行
			String resMsg = jsonObject.getString("resMsg");
			if (resMsg != null) {
				if (!"请求成功".equals(resMsg)) {
					return false;
				}
			}
			//京东/国信达 对接天府银行
            String code = jsonObject.getString("code");
			if (StringUtils.isNotBlank(code)){
			    if (!jsonObject.containsKey("evaluateCode") && !"0000".equals(code)){
			        return false;
                }
                if (jsonObject.containsKey("evaluateCode") && !"0".equals(code)){
			        return false;
                }
            }
            //德阳公积金   ans_code==0 存缓存
            String head = jsonObject.getString("head");
			if (StringUtils.isNotBlank(head)){
                String ans_code = jsonObject.getJSONObject("head").getString("ans_code");
                if(StringUtils.isNotBlank(ans_code) && (!"0".equals(jsonObject.getJSONObject("head").get("ans_code").toString()))){
                    return false;
                }
            }

            //贵州国税 result=1需要缓存
            String resultCode = jsonObject.getString("result");
			if (StringUtils.isNotBlank(resultCode)){
			    if (!"1".equals(resultCode)){
			        return false;
                }
            }
            return true;
		} catch (Exception e) {
			return false;
		}
	}

    /**
     * 判断字符串的编码
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode[] = new String[]{
                "ISO-8859-1",
                "UTF-8",
                "GB2312",
                "GBK",
                "GB18030",
                "Big5",
                "Unicode",
                "ASCII"
        };
        for (int i = 0; i < encode.length; i++){
            try {
                if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {
                    return encode[i];
                }
            } catch (Exception ex) {
            }
        }
        return "unknown_type";
    }

    /**
     * @Title: redis通过通配符批量删除，key*
     * @return Boolean    true:移除成功
     */
    public static Boolean removeBatch(Jedis jedis, String keys) {
		try {
			Set<String> set = jedis.keys(keys);
	        for (String key : set) {
	            jedis.del(key);
	        }
	        return true;
		} catch (Exception e) {
			return false;
		}
	}
/*
    public static void main(String[] args) {
//        Long begin = System.currentTimeMillis();
//        String xml2 = "{'errorCode':${errorCode},'errorMsg':${errorMsg},'uid':'20160811101840779sEVSljbyBCjNiAC','data':{'errorCode':800,'errorMsg':'登录失败'}}";
//        xml2 = replace(xml2, "errorCode", "200");
//        xml2 = replace(xml2, "errorMsg", "请求成功");
//
//        System.out.println(xml2);
//
//        System.out.println(System.currentTimeMillis() - begin);
//
//        System.out.println(cacheTime(3, 1));

    	//数据超市errorCode = 200存缓存
//    	String result = "{\"uid\":\"20180208150328091byhsTEnVtTlXttb\",\"data\":{\"result\":\"1\",\"msg\":\"在网\"},\"errorCode\":300,\"errorMsg\":\"请求成功\"}";
    	//同盾对接天府银行，信贷保镖API
//    	String result1 = "{\"success\":true,\"id\":\"WF2017091310213218425646\",\"reason_code\":\"6001\"}";
//
//
//    	System.out.println(resultIsToCache(result1));

        String str = "gfsdfdsfdf";
        String encode = getEncoding(str);
        System.out.println(encode);

        str = "得分梵蒂冈的身份地方";
        encode = getEncoding(str);
        System.out.println(encode);
    }*/
}
