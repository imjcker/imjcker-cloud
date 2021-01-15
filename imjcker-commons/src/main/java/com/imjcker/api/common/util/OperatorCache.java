package com.imjcker.api.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接口缓存判断
 */
public class OperatorCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperatorCache.class);

    /**
     * @param @return 设定文件
     * @return Boolean    返回类型
     * @Title: 判断接口返回结果是否存缓存
     */
    public static Boolean resultIsToCache(String result, String type) {
        if (StringUtils.isBlank(result)) {
            LOGGER.debug("result为空，直接返回false不缓存");
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        switch (type) {
            case "E01":
                return isWjResultCache(jsonObject);//万界
            case "E02":
                return isTdResultCache(jsonObject);//同盾
            case "E03":
                return isYgResultCache(jsonObject);//意高
            case "E04":
                return isHouseManagerResultCache(jsonObject);//房管局
            case "E05":
                return isQxbResultCache(jsonObject);//启信宝
            case "E06":
                return isGpResultCache(jsonObject);//公平网
            case "E07":
                return isJdResultCache(jsonObject);//京东
            case "E08":
                return isDyResultCache(jsonObject);//德阳公积金
            case "E09":
                return isSichuanGuoshuiResultCache(jsonObject);//四川国税
            case "E10":
                return isGzgsResultCache(jsonObject);//贵州国税
            case "E11":
                return isBaiRongResultCache(jsonObject);//百融
            case "E12":
                return isZcxResultCache(jsonObject);//中诚信
            case "E13":
                return isGxdResultCache(jsonObject);//国信达
            case "E14":
                return isSnsbResultCache(jsonObject);//遂宁社保
            case "E15":
                return isBBDResultCache(jsonObject);//BBD烟商
            case "E15-company":
                return isBBDCompanyResultCache(jsonObject);//BBD企业信息
            case "E15-index":
                return isBBDIndexResultCache(jsonObject);//BBD指标平台接口
            case "E16":
                return isICBCResultCache(jsonObject);
            case "E18":
                return isDuXiaoManResultCache(jsonObject);//度小满
            case "E19":
                return isZLHJResultCache(jsonObject);//中联惠捷
            case "E20":
                return isPzhGjjResultCache(jsonObject);//攀枝花公积金
            case "E21":
                return isGyZfwResultCache(jsonObject);// 贵阳筑房网
            case "E22":
                return isYdrxResultCache(jsonObject);// 易得融信
            case "E23":
                return isJinBCResultCache(jsonObject);// 金保创
            case "E24":
                return isJuMaResultCache(jsonObject);// 驹马
            case "E26":
                return isNcGjjResultCache(jsonObject);//南充公积金
            case "E27":
                return yinshuiResultCoche(jsonObject);//E27银税
            case "E28":
                return yinlianResult(jsonObject); // 银联数据
            case "E29":
                return cheResultCode(jsonObject);//E29车300
            case "E30":
                return  isPencilResultCache(jsonObject);//铅笔头
            default:
                break;
        }
        return true;//内部数据true
    }

    /**
     * @Description : 铅笔头
     * @param jsonObject
     * @Return :java.lang.Boolen
     * @Date : 2020/3/30
     */
    private  static Boolean isPencilResultCache(JSONObject jsonObject){
        return  "0".equals(jsonObject.getString("status"));

    }

    /**
     * @Description : 银联数据
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2020/3/30 16:43
     */
    private static Boolean yinlianResult(JSONObject jsonObject) {
        JSONObject head = jsonObject.getJSONObject("head");
        return "E000".equals(head.getString("rtCode"));
    }

    private static Boolean isJuMaResultCache(JSONObject jsonObject) {
        return "0".equals(jsonObject.getString("code"));
    }

    /**
     * 南充公积金缓存判断
     * @param jsonObject
     * @return
     */
    private static Boolean isNcGjjResultCache(JSONObject jsonObject) {
        String success = jsonObject.getString("success");
        return StringUtils.isNotBlank(success) && "true".equals(success);
    }

    /**
     * @Description :攀枝花公积金缓存判断
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/10/28 8:49
     */
    private static Boolean isPzhGjjResultCache(JSONObject jsonObject) {
        JSONArray head = jsonObject.getJSONArray("head");
        JSONObject json = head.getJSONObject(0);
        return "000000".equals(json.getString("rescode"));
    }

    /**
     * @Description : 金保创
     * @param jsonObject
     * @Return : java.lang.Boolean
     * @Date : 2019/9/30 14:45
     */
    private static Boolean isJinBCResultCache(JSONObject jsonObject) {
        return "0".equals(jsonObject.getString("DATA"));
    }

    /**
     * 易得融信
     * @param jsonObject
     * @return
     */
    private static Boolean isYdrxResultCache(JSONObject jsonObject) {
        String msgcode = jsonObject.getString("status");
        return StringUtils.isNotBlank(msgcode) && "success".equals(msgcode);
    }

    /**
     * 贵阳筑房网
     * @param jsonObject
     * @return
     */
    private static Boolean isGyZfwResultCache(JSONObject jsonObject) {
        String msgcode = jsonObject.getString("msgcode");
        return StringUtils.isNotBlank(msgcode) && "1000".equals(msgcode);
    }

    /**
     * 遂宁社保缓存判断
     * @param jsonObject
     * @return
     */
    private static Boolean isSnsbResultCache(JSONObject jsonObject) {
        JSONObject result = jsonObject.getJSONObject("result");
        if (null == result)
            return false;
        String code = result.getString("code");
        return StringUtils.isNotBlank(code) && "2000".equals(code);
    }

    private static Boolean isICBCResultCache(JSONObject jsonObject) {
        String code = jsonObject.getString("returnCode");
        return !StringUtils.isBlank(code) && "0".equals(code);
    }

    /**
     * 百融
     * @param jsonObject
     * @return
     */
    private static Boolean isBaiRongResultCache(JSONObject jsonObject) {
        String result = jsonObject.getString("code");
        return StringUtils.isNotBlank(result) && ("600000".equals(result) || "00".equals(result));
    }

    /**
     * 中诚信
     * @param jsonObject
     * @return
     */
    private static Boolean isZcxResultCache(JSONObject jsonObject) {
        String result = jsonObject.getString("resCode");
        return !StringUtils.isBlank(result) && "0000".equals(result);
    }

    /**
     * 中联惠捷
     * @param jsonObject
     * @return
     */
    private static Boolean isZLHJResultCache(JSONObject jsonObject) {
        String result = jsonObject.getString("result");
        return !StringUtils.isBlank(result) && "Y".equals(result);
    }

    /*
        万界
     */
    private static Boolean isWjResultCache(JSONObject jsonObject) {
        String resultCode = jsonObject.getString("result");
        return !StringUtils.isBlank(resultCode) && "0".equals(resultCode);
    }

    /*
        同盾
     */
    private static Boolean isTdResultCache(JSONObject jsonObject) {
        String success = jsonObject.getString("success");

        return !StringUtils.isBlank(success) && ("true".equals(success) && jsonObject.get("reason_code") == null);
    }

    /*
        意高
     */
    private static Boolean isYgResultCache(JSONObject jsonObject) {
        String resMsg = jsonObject.getString("resMsg");
        return !StringUtils.isBlank(resMsg) && "请求成功".equals(resMsg);
    }

    /*
        房管局
     */
    private static Boolean isHouseManagerResultCache(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        return !StringUtils.isBlank(status) && !"fail".equals(status);
    }

    /*
     企信宝
    */
    private static Boolean isQxbResultCache(JSONObject jsonObject) {
        return true;
    }

    /*
        公平网
     */
    private static Boolean isGpResultCache(JSONObject jsonObject) {
        return true;
    }

    /*
        京东
     */
    private static Boolean isJdResultCache(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        return !StringUtils.isBlank(code) && "0000".equals(code);
    }

    /**
     * 四川国税
     * @param jsonObject
     * @return
     */
    private static Boolean isSichuanGuoshuiResultCache(JSONObject jsonObject) {
        if (jsonObject.getJSONObject("data") != null
                && jsonObject.getJSONObject("data").getJSONObject("UMSFX") != null
                && jsonObject.getJSONObject("data").getJSONObject("UMSFX").getJSONObject("RespHeader") != null) {
            String code = jsonObject.getJSONObject("data").getJSONObject("UMSFX").getJSONObject("RespHeader").getString("RespCode");
            return !StringUtils.isBlank(code) && "99999999".equals(code);
        } else return false;
    }
    /*
        国信达
     */
    private static Boolean isGxdResultCache(JSONObject jsonObject) {
        String code = jsonObject.getString("code");
        return !StringUtils.isBlank(code) && "0".equals(code);
    }

    /*
        德阳公积金
     */
    private static Boolean isDyResultCache(JSONObject jsonObject) {
        // ans_code==0 存缓存
        String head = jsonObject.getString("head");
        if (StringUtils.isNotBlank(head)) {
            String ans_code = jsonObject.getJSONObject("head").getString("ans_code");
            return !StringUtils.isBlank(ans_code) && ("0".equals(jsonObject.getJSONObject("head").get("ans_code").toString()));
        }
        return true;
    }

    /*
        贵州国税
     */
    private static Boolean isGzgsResultCache(JSONObject jsonObject) {
        String resultCode = jsonObject.getString("result");
        return !StringUtils.isBlank(resultCode) && "1".equals(resultCode);
    }


    /**
     * BBD烟商
     *
     * @param jsonObject
     * @return
     */
    private static Boolean isBBDResultCache(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        LOGGER.debug("status = {}", status);
        return !StringUtils.isBlank(status) && "200".equals(status);
    }

    /*
        BBD企业信息
     */
    private static Boolean isBBDCompanyResultCache(JSONObject jsonObject) {
        String errCode = jsonObject.getString("err_code");
        LOGGER.debug("err_Code = {}", errCode);
        return !StringUtils.isBlank(errCode) && "0".equals(errCode);
    }
    /*
        BBD指标平台接口
     */
    private static Boolean isBBDIndexResultCache(JSONObject jsonObject) {
        String status = jsonObject.getString("status");
        LOGGER.debug("status = {}", status);
        return !StringUtils.isBlank(status) && "200".equals(status);
    }
    /*
        度小满
     */
    private static Boolean isDuXiaoManResultCache(JSONObject jsonObject){
        String retCode = jsonObject.getString("retCode");
        LOGGER.debug("retCode = {}",retCode);
        return !StringUtils.isBlank(retCode) && "0".equals(retCode);
    }

    /**  E27 银税 */
    private static Boolean yinshuiResultCoche(JSONObject jsonObject){
        JSONObject service = jsonObject.getJSONObject("service");
        if(null == service){
            return false;
        }
        String rtnCode = service.getJSONObject("head").getString("rtnCode");
        return !StringUtils.isBlank(rtnCode) && "0000".equals(rtnCode);
    }

    /**  E29 车300 */
    private static Boolean cheResultCode(JSONObject jsonObject){
        String status = jsonObject.getString("status");
        return !StringUtils.isBlank(status) && "1".equals(status);
    }
}
