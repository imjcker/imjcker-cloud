package com.imjcker.api.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author WT
 * @Date 15:16 2019/8/21
 * @Version ThirdResultContainDataUtil v1.0
 * @Desicrption 判断第三方数据源是否返回含有正确数据
 */
public class ThirdResultContainDataUtil {

    public static Boolean containData(String result,String type) {
        if (StringUtils.isBlank(result)) {
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        switch (type) {
            case "E01":
                return wanjieResult(jsonObject);
            case "E02":
                return tongdunResult(jsonObject);
            case "E04":
                return chengduFangguanju(jsonObject);
            case "E05":
                return qixinbaoResult(jsonObject);
            case "E06":
                return gongpingwangResult(jsonObject);
            case "E07":
                return jingdongResult(jsonObject);
            case "E08":
                return deyanggongjijin(jsonObject);
            case "E10":
                return guizhouguoshui(jsonObject);
            case "E11":
                return bairongJinrong(jsonObject);
            case "E12":
                return zhongchengxin(jsonObject);
            case "E13":
                return guoxindaResult(jsonObject);
            case "E14":
                return suiningshebao(jsonObject);
            case "E15":
                return bbdYSResult(jsonObject);
            case "E15-company":
                return bddCompanyResult(jsonObject);
            case "E15-index":
                return bddIndexResult(jsonObject);
            case "E16":
                return icbcResult(jsonObject);
            case "E17":
                return sifashesuInfo(jsonObject);
            case "E18":
                return duxiaomanResult(jsonObject);
            case "E19":
                return zhonglianhuijie(jsonObject);
            case "E20":
                return panzhihuagjj(jsonObject);
            case "E21":
                return guiyangzhufangwang(jsonObject);
            case "E22":
                return yiDeRongXinResult(jsonObject);
            case "E23":
                return "0".equals(jsonObject.getString("DATA"));  // 金保创
            case "E27":
                return yinShuiResult(jsonObject);
            case "E28":
                return yinlianResult(jsonObject);
            case "E29":
                return cheResult(jsonObject);
            case "E30":
                return  isPencilResultCache(jsonObject);//铅笔头
            default:
                break;
        }
        return true;
    }
    /**
     * @Description : 铅笔头
     * @param jsonObject
     * @Return :java.lang.Boolen
     * @Date : 2020/3/30
     */
    private  static Boolean isPencilResultCache(JSONObject jsonObject){
        if (!"0".equals(jsonObject.getString("status"))) {
            return false;
        }
        String data=jsonObject.getString("data");
        if (StringUtils.isNotBlank(data)) {
            return data.startsWith("{") ? jsonObject.getJSONObject("data").size() > 0
                    : jsonObject.getJSONArray("data").size() > 0;
        }
        return false;
    }

    /**
     * @Description : 银联数据
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2020/3/30 16:45
     */
    private static Boolean yinlianResult(JSONObject jsonObject) {
        JSONObject head = jsonObject.getJSONObject("head");
        if (!"E000".equals(head.getString("rtCode"))) {
            return false;
        }
        JSONObject body = jsonObject.getJSONObject("body");
        return "E000".equals(body.getString("rtCode"));
    }

    /**
     * @Description : 贵阳筑房网
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 16:36
     */
    private static Boolean guiyangzhufangwang(JSONObject jsonObject) {
        String msgcode = jsonObject.getString("msgcode");
        return StringUtils.isNotBlank(msgcode) && "1000".equals(msgcode);
    }

    /**
     * @Description : 攀枝花公积金
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 16:30
     */
    private static Boolean panzhihuagjj(JSONObject jsonObject) {
        return deyanggongjijin(jsonObject);
    }

    /**
     * @Description : 中联惠捷
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 16:17
     */
    private static Boolean zhonglianhuijie(JSONObject jsonObject) {
        String result = jsonObject.getString("result");
        return StringUtils.isNotBlank(result) && "Y".equals(result);
    }

    /**
     * @Description : 度小满
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 16:12
     */
    private static Boolean duxiaomanResult(JSONObject jsonObject) {
        String retCode = jsonObject.getString("retCode");
        return StringUtils.isNotBlank(retCode) && "0".equals(retCode);
    }

    /**
     * @Description : 司法涉诉信息查询
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 15:36
     */
    private static Boolean sifashesuInfo(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        if (StringUtils.isNotBlank(code) && "0".equals(code)) {
            return "s".equals(jsonObject.getString("success"));
        }
        return false;
    }

    /**
     * @Description : ICBC 数据
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 15:19
     */
    private static Boolean icbcResult(JSONObject jsonObject) {
        String data = jsonObject.getString("data");
        if (StringUtils.isNotBlank(data)) {
            return data.startsWith("{") ? jsonObject.getJSONObject("data").size() > 0
                    : jsonObject.getJSONArray("data").size() > 0;
        }
        return false;
    }

    /**
     * @Description : BDD 指标平台
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 15:11
     */
    private static Boolean bddIndexResult(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        if (StringUtils.isNotBlank(code) && "200".equals(code)) {
            String result = jsonObject.getString("data");
            return StringUtils.isNotBlank(result) && jsonObject.getJSONObject("data").size() > 0;
        }
        return false;
    }

    /**
     * @Description : BDD 企业关联信息
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 15:08
     */
    private static Boolean bddCompanyResult(JSONObject jsonObject) {
        String errCode = jsonObject.getString("err_code");
        if (StringUtils.isNotBlank(errCode) && "0".equals(errCode)) {
            String results = jsonObject.getString("results");
            if (StringUtils.isNotBlank(results)) {
                return results.startsWith("{") ? jsonObject.getJSONObject("results").size() > 0
                        : jsonObject.getJSONArray("results").size() > 0;
            }
        }
        return false;
    }

    /**
     * @Description : BBD 烟商数据
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 14:58
     */
    private static Boolean bbdYSResult(JSONObject jsonObject) {
        return StringUtils.isNotBlank(jsonObject.getString("status")) && "200".equals(jsonObject.getString("status"));
    }

    /**
     * @param jsonObject
     * @Description : 遂宁社保
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 14:43
     */
    private static Boolean suiningshebao(JSONObject jsonObject) {
        JSONObject result = jsonObject.getJSONObject("result");
        if (result != null) {
            return StringUtils.isNotBlank(result.getString("code")) && "2000".equals(result.getString("code"));
        }
        return false;
    }

    /**
     * @Description : 国信达
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 14:13
     */
    private static Boolean guoxindaResult(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        if ("0".equals(code)) {
            String data = jsonObject.getString("data");
            if (StringUtils.isNotBlank(data)) {
                return data.startsWith("{") ? jsonObject.getJSONObject("data").size() > 0
                        : jsonObject.getJSONArray("data").size() > 0;
            }
        }
        return false;
    }

    /**
     * @Description : 中诚信
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 11:28
     */
    private static Boolean zhongchengxin(JSONObject jsonObject) {
        return "0000".equals(jsonObject.getString("resCode"));
    }

    /**
     * @Description : 百融金融
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 11:14
     */
    private static Boolean bairongJinrong(JSONObject jsonObject) {

        return "600000".equals(jsonObject.getString("code")) || "00".equals(jsonObject.getString("code"));
    }

    /**
     * @Description : 贵州国税
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 11:08
     */
    private static Boolean guizhouguoshui(JSONObject jsonObject) {
        return "1".equals(jsonObject.getString("result"));
    }

    /**
     * @Description : 德阳公积金
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 10:39
     */
    private static Boolean deyanggongjijin(JSONObject jsonObject) {
        JSONObject head = jsonObject.getJSONObject("head");
        return head != null && "0".equals(head.getString("ans_code"));
    }

    /**
     * @Description : 京东数据
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 10:21
     */
    private static Boolean jingdongResult(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        return "0000".equals(code) && jsonObject.getJSONObject("data") != null && !jsonObject.getJSONObject("data").isEmpty();
    }

    /**
     * @Description : 公平网
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 10:09
     */
    private static Boolean gongpingwangResult(JSONObject jsonObject) {
        return jsonObject.getBooleanValue("Status");
    }

    /**
     * @param jsonObject
     * @Description : 启信宝数据
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 10:02
     */
    private static Boolean qixinbaoResult(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        return "200".equals(status) && !jsonObject.getJSONObject("data").isEmpty();
    }

    /**
     * @Description : 成都房管局
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 9:43
     */
    private static Boolean chengduFangguanju(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        if ("success".equals(status)) {
            String result = jsonObject.getString("result");
            if (StringUtils.isNotBlank(result)) {
                return result.startsWith("{") ? jsonObject.getJSONObject("result").size() > 0
                        : jsonObject.getJSONArray("result").size() > 0;
            }
        }
        return false;
    }

    /**
     * @param jsonObject
     * @Description : 同盾正确数据返回判断
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 9:36
     */
    private static Boolean tongdunResult(JSONObject jsonObject) {
        String success = jsonObject.getString("success");
        return "true".equals(success) && StringUtils.isBlank(jsonObject.getString("reason_code"));
    }

    /**
     * @Description : 万界数据判断
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/8/30 9:19
     */
    private static Boolean wanjieResult(JSONObject jsonObject) {
        String result = jsonObject.getString("result");
        if (StringUtils.isNotBlank(result) && "0".equals(result)) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null && !data.isEmpty()) {
                String bak_code = data.getString("bak_code");
                return StringUtils.isNotBlank(bak_code) && "0".equals(bak_code);
            }
        }
        return false;
    }

    /**
     * 易得融信判断
     * @param jsonObject
     * @return
     */
    private static Boolean yiDeRongXinResult(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        if ("success".equals(status) || "000000".equals(status)) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (!data.isEmpty()) {
                return true;
            }
            JSONObject result = jsonObject.getJSONObject("result");
            return !result.isEmpty();
        }
        return false;
    }

    /**
     * @Description : E27
     * @param
     * @Return :
     * @Date : 2020/3/26 17:16
     */
    private static Boolean yinShuiResult(JSONObject jsonObject){
        JSONObject service = jsonObject.getJSONObject("service");
        if(null == service){
            return false;
        }
        String rtnCode = service.getJSONObject("head").getString("rtnCode");
        if(StringUtils.isBlank(rtnCode) || !"0000".equals(rtnCode)){
            return false;
        }
        JSONObject body = service.getJSONObject("body");
        if(null == body){
            return false;
        }
        String code = body.getString("code");
        return !StringUtils.isBlank(code) && "0000".equals(code);
    }

    /**  E29 车300 */
    private static Boolean cheResult(JSONObject jsonObject){
        String status = jsonObject.getString("status");
        if(StringUtils.isBlank(status) || !"1".equals(status)){
            return false;
        }
        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            if("uid".equals(key) || "status".equals(key)) {
                continue;
            }
            String value = jsonObject.getString(key);
            if (value.startsWith("{") || value.startsWith("[")) {
                if (value.length() > 2) return true;
            } else {
                if (value.length() > 0) return true;
            }
            /*try {
                JSONObject jsonSome = jsonObject.getJSONObject(key);
                Set<String> set = jsonSome.keySet();
                if (set.size() > 0) return true;
            } catch (Exception e) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(key);
                    if (jsonArray.size() > 0) return true;
                } catch (Exception e1) {
                    String dataStr = jsonObject.getString(key);
                    if (dataStr.length() > 0) return true;
                }
            }*/
        }
        return false;
    }

    public static void main(String[] args) {

        JSONObject json = new JSONObject();
        json.put("data", new JSONObject());
        System.out.println(json.getJSONObject("data").size() > 0);
    }
}
