package com.imjcker.manager.manage.validator;

import java.util.Iterator;
import java.util.List;

import com.imjcker.manager.manage.po.*;
import com.imjcker.manager.manage.vo.ApiExpand;
import com.imjcker.manager.manage.vo.ApiVersionStrategy;
import com.imjcker.manager.manage.vo.IDInfo;
import com.imjcker.manager.manage.vo.RequestParamAndValue;
import com.lemon.common.exception.ExceptionInfo;
import com.imjcker.manager.manage.po.*;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.exception.vo.BusinessException;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.util.JsonValidator;
import com.lemon.common.util.Validator;
import com.lemon.common.vo.CommonStatus;

/**
 * @author Lemon.kiana
 * @version 1.0
 *          2017年7月13日 上午9:29:55
 * @Title ApiValidate
 * @Description 对API的一些校验
 * @Copyright Copyright © 2017, Lemon, All Rights Reserved.
 */
public class ApiValidate {
    /**
     * 对apiInfo的基本信息进行校验
     *
     * @param apiInfo
     */
    public static void apiCheck(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_EXIST_APIINFO);
        }
        String apiName = apiInfo.getApiName();
        //Integer apiGroupId = apiInfo.getApiGroupId();
        //正则校验
        String httpPath = apiInfo.getHttpPath();
        Integer httpMethod = apiInfo.getHttpMethod();
        String apiDescription = apiInfo.getApiDescription();
        //正则校验
        String backEndAddress = apiInfo.getBackEndAddress();
        //正则校验
//        String backEndAddressB = apiInfo.getBackEndAddressB();
        //正则校验
        String backEndPath = apiInfo.getBackEndPath();
        Integer backEndHttpMethod = apiInfo.getBackEndHttpMethod();
        Integer backEndTimeout = apiInfo.getBackEndTimeout();
        Integer isMock = apiInfo.getIsMock();
        //正则校验
        String mockData = apiInfo.getMockData();
        Integer callBackType = apiInfo.getCallBackType();
        //正则校验
        String callBackSuccessExample = apiInfo.getCallBackSuccessExample();
        //正则校验
        String callBackFailExample = apiInfo.getCallBackFailExample();
        Integer saveMongoDB = apiInfo.getSaveMongoDB();
        //正则校验
        String mongodbURI = apiInfo.getMongodbURI();
        String collectionName = apiInfo.getMongodbCollectionName();
        String dbName = apiInfo.getMongodbDBName();
        /*String monggodbDBName = apiInfo.getMongodbDBName();
        String mongodbCollectionName = apiInfo.getMongodbCollectionName();*/
        Integer saveMQ = apiInfo.getSaveMQ();
        String mqAddress = apiInfo.getMqAddress();
        String mqUserName = apiInfo.getMqUserName();
        String mqPassword = apiInfo.getMqPasswd();
        String mqTopicName = apiInfo.getMqTopicName();
        //接口2.0------------------------下面----------------------------------
        Integer weight = apiInfo.getWeight();
        Integer charge = apiInfo.getCharge();
        String interfaceName = apiInfo.getInterfaceName();
        String responseTransParam = apiInfo.getResponseTransParam();
        String configTransParam = apiInfo.getResponseConfigJson();

        Integer cacheUnit = apiInfo.getCacheUnit();
        Integer cacheNo = apiInfo.getCacheNo();
        //接口2.0------------------------上面----------------------------------

       /* if (null == apiGroupId) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
        }*/
        if (StringUtils.isBlank(apiName)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APINAME);
        }
        if (apiName.length() < 4 || apiName.length() > 50) {
            throw new DataValidationException(ExceptionInfo.API_NAME_LENGTH_ERROR);
        }
        if (StringUtils.isBlank(apiDescription)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_API_DESCRIPTION);
        }
        if (apiDescription.length() < 1 || apiDescription.length() > 2000) {
            throw new DataValidationException(ExceptionInfo.API_DESCRIPTION_LENGTH_ERROR);
        }
        if (null == saveMongoDB) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SAVEMONGODB);
        }
        if (CommonStatus.ENABLE == saveMongoDB && (StringUtils.isBlank(mongodbURI) || StringUtils.isBlank(collectionName) || StringUtils.isBlank(dbName))) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MONGODB);
        }
        if (CommonStatus.DISENABLE == saveMongoDB && (StringUtils.isNotBlank(mongodbURI) || StringUtils.isNotBlank(collectionName) || StringUtils.isNotBlank(dbName))) {
            throw new BusinessException(ExceptionInfo.SETTING_MONGODB_ERROR);
        }
        /*if (null == mongodbURI ) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_MONGODBURI);
		}*/
        if (null == cacheNo && null != cacheUnit && (cacheUnit.equals(1) || cacheUnit.equals(2))) {
            throw new BusinessException(ExceptionInfo.NOT_NULL_CACHENO);
        }
        if (null == saveMQ) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SAVEMQ);
        }
        if (CommonStatus.ENABLE == saveMQ && (StringUtils.isBlank(mqAddress) || StringUtils.isBlank(mqTopicName))) {
            throw new BusinessException(ExceptionInfo.FILL_IN_MQ);
        }
        if (CommonStatus.DISENABLE == saveMQ && (StringUtils.isNotBlank(mqAddress) || StringUtils.isNotBlank(mqUserName) || StringUtils.isNotBlank(mqPassword) || StringUtils.isNotBlank(mqTopicName))) {
            throw new BusinessException(ExceptionInfo.SETTING_MQ_ERROR);
        }
        //httpPath
        if (StringUtils.isBlank(httpPath)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_HTTPPATH);
        }
        if (!Validator.subUrlCheck(httpPath)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_HTTPPATH);
        }

        if (null == httpMethod) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_HTTPMETHOD);
        }

        //接口2.0----------------下面
        if (StringUtils.isBlank(interfaceName)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_INTERFACE_NAME);
        }
        if (null == weight) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_WEIGHT);
        }
        if (null == charge) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_CHARGE);
        }
        if (CommonStatus.ENABLE != charge && CommonStatus.DISENABLE != charge) {
            throw new DataValidationException(StringUtils.join("是否计费", ExceptionInfo.ERROR));
        }
        //接口2.0----------------上面
        if (StringUtils.isBlank(backEndAddress)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_ADDRESS);
        }
//        if (StringUtils.isBlank(backEndAddressB)) {
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_ADDRESS);
//        }
//		if (!Validator.isAddressUrl(backEndAddress)) {
//			throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_BACKEND_ADDRESS);
//		}

        //backEndPath
        if (!isSocketProtocol(apiInfo)) {
            if (StringUtils.isBlank(backEndPath)) {
                throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_HTTPPATH);
            }
            if (!Validator.subUrlCheck(backEndPath)) {
                throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_BACKEND_HTTPPATH);
            }
            if (null == backEndHttpMethod) {
                throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_HTTPMETHOD);
            }
            if (backEndHttpMethod != CommonStatus.GET && backEndHttpMethod != CommonStatus.POST) {
                throw new DataValidationException(ExceptionInfo.BACKEND_HTTP_METHOD_ERROR);
            }
        }
        if (null == backEndTimeout) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_TIMEOUT);
        }
        if (backEndTimeout < 0 || backEndTimeout > 30000) {
            throw new DataValidationException(ExceptionInfo.BACKEND_TIMEOUT_ERROR);
        }

        if (null == isMock) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ISMOCK);
        }
        if (CommonStatus.ENABLE == isMock && StringUtils.isBlank(mockData)) {
            throw new DataValidationException(ExceptionInfo.FILL_IN_MOCK_DATA);
        }

        if (StringUtils.isNotBlank(mockData) && !JsonValidator.validate(mockData)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_MOCKDATA_ERROR);
        }

        if (null == callBackType) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_CALLBACKTYPE);
        }
        if (callBackType != 1) {
            throw new DataValidationException(ExceptionInfo.CALLBACKTYPE_ERROR);
        }
        if (StringUtils.isBlank(callBackSuccessExample)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUCCESS_EXAMPLE);
        }
        if (!JsonValidator.validate(callBackSuccessExample)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_SUCCESS_EXAMPLE_ERROR);
        }

        if (StringUtils.isNotBlank(callBackFailExample) && !JsonValidator.validate(callBackFailExample)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_FAIL_EXAMPLE_ERROR);
        }
        //responseTransParam
        if (StringUtils.isNotBlank(responseTransParam)) {
            if (!JsonValidator.validate(responseTransParam)) {
                throw new DataValidationException(ExceptionInfo.FORMAT_RESPONSE_TRANS_PARAN_ERROR);
            }
            responseConfigJsonValidation(responseTransParam);
        }

        if (StringUtils.isNotBlank(configTransParam) && !JsonValidator.validate(configTransParam)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_RESPONSE_TRANS_PARAN_ERROR);
        }


    }

    public static void callBackCheck(ApiInfoWithBLOBs apiInfo) {
        Integer callBackType = apiInfo.getCallBackType();
        //正则校验
        String callBackSuccessExample = apiInfo.getCallBackSuccessExample();
        //正则校验
        String callBackFailExample = apiInfo.getCallBackFailExample();
        if (null == callBackType) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_CALLBACKTYPE);
        }
        if (StringUtils.isBlank(callBackSuccessExample)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_SUCCESS_EXAMPLE);
        }
        if (!JsonValidator.validate(callBackSuccessExample)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_SUCCESS_EXAMPLE_ERROR);
        }
        if (StringUtils.isNotBlank(callBackFailExample) && !JsonValidator.validate(callBackFailExample)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_FAIL_EXAMPLE_ERROR);
        }
    }

    public static void apiExpandCheck(ApiExpand apiExpand) {
        Integer apiId = apiExpand.getId();
        Integer env = apiExpand.getEnv();
        String pubDescription = apiExpand.getPubDescription();
        if (null == apiId) {
            throw new DataValidationException(StringUtils.join("apiId", ExceptionInfo.NOT_NULL));
        }
        if (null == env) {
            throw new DataValidationException(StringUtils.join("env", ExceptionInfo.NOT_NULL));
        }
//        if (env != CommonStatus.ONLINE && env != CommonStatus.ADVANCE) {
        if (env != CommonStatus.ONLINE) {
            throw new DataValidationException(StringUtils.join("env", ExceptionInfo.ERROR));
        }

        if (StringUtils.isBlank(pubDescription)) {
            throw new DataValidationException(StringUtils.join("pubDescription", ExceptionInfo.NOT_NULL));
        }
    }

    /**
     * 对下游的请求参数进行校验
     *
     * @param requestParamsList
     */
    public static void requestParamsCheck(List<RequestParams> requestParamsList) {
        RequestParams requestParams;
        Iterator<RequestParams> iterator = requestParamsList.iterator();
        while (iterator.hasNext()) {
            requestParams = iterator.next();
            requesetParamsCheck(requestParams);
        }
    }

    /**
     * 对单条下游请求参数进行校验
     *
     * @param requestParams
     */
    public static void requesetParamsCheck(RequestParams requestParams) {
        String paramName = requestParams.getParamName();
        Integer paramsType = requestParams.getParamsType();
        Integer paramsLocation = requestParams.getParamsLocation();
        Integer paramsMust = requestParams.getParamsMust();
        Integer minLength = requestParams.getMinLength();
        Integer maxLength = requestParams.getMaxLength();
        String regularExpress = requestParams.getRegularExpress();

        //paramName
        if (StringUtils.isBlank(paramName)) {
            throw new DataValidationException(StringUtils.join("paramName", ExceptionInfo.NOT_NULL));
        }
        //paramsType
        if (null == paramsType) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.NOT_NULL));
        }
        if (paramsType < 1 || paramsType > 6) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.ERROR));
        }
        //paramsLocation
        if (null == paramsLocation) {
            throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.NOT_NULL));
        }
        if (paramsLocation < 1 || paramsLocation > 3) {
            throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.ERROR));
        }
        //paramsMust
        if (null == paramsMust) {
            throw new DataValidationException(StringUtils.join("paramsMust", ExceptionInfo.NOT_NULL));
        }
        if (paramsMust < 1 || paramsMust > 2) {
            throw new DataValidationException(StringUtils.join("paramsMust", ExceptionInfo.ERROR));
        }
        if (CommonStatus.PARAMTYPE_STRING == paramsType) {
            //minLength
            if (null == minLength) {
                throw new DataValidationException(StringUtils.join("minLength", ExceptionInfo.NOT_NULL));
            }
            //maxLength
            if (null == maxLength) {
                throw new DataValidationException(StringUtils.join("maxLength", ExceptionInfo.NOT_NULL));
            }
            //regularExpress
            /*if (StringUtils.isBlank(regularExpress)) {
                throw new DataValidationException(StringUtils.join("regularExpress",ExceptionInfo.NOT_NULL));
			}*/
        }


    }

    /**
     * 对发给上游的参数进行校验
     *
     * @param backendList
     */
    public static void backendRequestParamsCheck(List<BackendRequestParams> backendList) {
        BackendRequestParams backendRequestParams;
        Iterator<BackendRequestParams> iterator = backendList.iterator();
        while (iterator.hasNext()) {
            backendRequestParams = iterator.next();
            backendRequestParamsCheck(backendRequestParams);
        }
    }

    /**
     * 对单条发送给上游的请求参数进行校验
     *
     * @param backendRequestParams
     */
    public static void backendRequestParamsCheck(BackendRequestParams backendRequestParams) {
        Integer paramsType = backendRequestParams.getParamsType();
        String paramName = backendRequestParams.getParamName();
        Integer paramsLocation = backendRequestParams.getParamsLocation();
        //paramsType
        if (null == paramsType) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.NOT_NULL));
        }
        if (paramsType != CommonStatus.VARIABLE && paramsType != CommonStatus.CONSTANT && paramsType != CommonStatus.JSON && paramsType != CommonStatus.XML) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.ERROR));
        }
        if (paramsType == CommonStatus.VARIABLE || paramsType == CommonStatus.CONSTANT) {
            //paramName
            if (StringUtils.isBlank(paramName)) {
                throw new DataValidationException(StringUtils.join("paramName", ExceptionInfo.NOT_NULL));
            }
            //paramsLocation
            if (null == paramsLocation) {
                throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.NOT_NULL));
            }
        }
        if (paramsLocation != CommonStatus.HEADER && paramsLocation != CommonStatus.QUERY && paramsLocation != CommonStatus.BODY) {
            throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.ERROR));
        }
    }

    /**
     * 对返回码进行校验
     *
     * @param resultSettingsList
     */
    public static void resultSettingsCheck(List<Apiresultsettings> resultSettingsList) {
        Apiresultsettings apiresultsettings;
        Iterator<Apiresultsettings> iterator = resultSettingsList.iterator();
        while (iterator.hasNext()) {
            apiresultsettings = iterator.next();
            resultSettingsCheck(apiresultsettings);
        }
    }

    /**
     * 对单条返回码进行校验(适用于新增返回码、修改返回码除去ID的校验)
     *
     * @param apiresultsettings
     */
    public static void erroeCodeAndLookupInfoCheck(Apiresultsettings apiresultsettings) {
        Integer errorCode = apiresultsettings.getErrorCode();
        String lookupInfo = apiresultsettings.getLookupInfo();
        //errorCode
        if (null == errorCode) {
            throw new DataValidationException(StringUtils.join("errorCode", ExceptionInfo.NOT_NULL));
        }
        //lookupInfo
        if (StringUtils.isBlank(lookupInfo)) {
            throw new DataValidationException(StringUtils.join("lookupInfo", ExceptionInfo.NOT_NULL));
        }
    }

    /**
     * 对单条返回码进行校验(适用于新增返回码、修改返回码除去ID的校验)
     *
     * @param apiresultsettings
     */
    public static void resultSettingsCheck(Apiresultsettings apiresultsettings) {
        Integer errorCode = apiresultsettings.getErrorCode();
        String errorMsg = apiresultsettings.getErrorMsg();
        String lookupInfo = apiresultsettings.getLookupInfo();
        //errorCode
        if (null == errorCode) {
            throw new DataValidationException(StringUtils.join("errorCode", ExceptionInfo.NOT_NULL));
        }
        //errorMsg
        if (null == errorMsg) {
            throw new DataValidationException(StringUtils.join("errorMsg", ExceptionInfo.NOT_NULL));
        }
        //lookupInfo
        if (StringUtils.isBlank(lookupInfo)) {
            throw new DataValidationException(StringUtils.join("lookupInfo", ExceptionInfo.NOT_NULL));
        }
    }

    /**
     * api下线的参数校验
     *
     * @param apiExpand
     */
    public static void offlineCheck(ApiExpand apiExpand) {
        if (null == apiExpand) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiExpand.getId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
        }
//        if (null == apiExpand.getApiGroupId()) {
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
//        }
        if (null == apiExpand.getEnv()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_OFFLINE_OPERATION);
        }
//        if (CommonStatus.ONLINE != apiExpand.getEnv() && CommonStatus.ADVANCE != apiExpand.getEnv()) {
        if (CommonStatus.ONLINE != apiExpand.getEnv()) {
            throw new DataValidationException(ExceptionInfo.OFFLINE_ENV_ERROR);
        }
    }

    /**
     * httpPath加域名唯一性校验
     *
     * @param apiInfo
     */
    public static void domainHttpPathUniqueCheck(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiInfo.getApiGroupId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
        }
        if (StringUtils.isBlank(apiInfo.getHttpPath())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_HTTPPATH);
        }
    }

    /**
     * httpPath唯一性校验
     *
     * @param apiInfo
     */
    public static void httpPathUniqueCheck(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (StringUtils.isBlank(apiInfo.getHttpPath())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_HTTPPATH);
        }
        if (!Validator.subUrlCheck(apiInfo.getHttpPath())) {
            throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_HTTPPATH);
        }
    }

    public static void apiIdCheck(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiInfo.getId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
        }
    }

    public static void apiGroupIdCheck(ApiInfoWithBLOBs apiInfo) {
        if (null == apiInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiInfo.getApiGroupId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
        }
    }

    public static void apiIdCheck(ApiInfoVersionsWithBLOBs apiInfoVersion) {
        if (null == apiInfoVersion) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiInfoVersion.getId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_ID);
        }
    }

    public static void apiGroupIdCheck(ApiInfoVersionsWithBLOBs apiInfoVersion) {
        if (null == apiInfoVersion) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAMS);
        }
        if (null == apiInfoVersion.getApiGroupId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
        }
    }

    /**
     * 用于【调试API】参数校验
     *
     * @param requestParamAndValueList
     */
    public static void requestParamAndValueCheck(List<RequestParamAndValue> requestParamAndValueList) {
        RequestParamAndValue requestParamAndValue;
        Iterator<RequestParamAndValue> iterator = requestParamAndValueList.iterator();
        while (iterator.hasNext()) {
            requestParamAndValue = iterator.next();
            requestParamAndValueCheck(requestParamAndValue);
        }
    }

    /**
     * 用于【调试API】参数校验
     *
     * @param requestParamAndValue
     */
    public static void requestParamAndValueCheck(RequestParamAndValue requestParamAndValue) {
        if (null == requestParamAndValue) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_REQUEST_PARAM_AND_VALUE);
        }
        if (StringUtils.isBlank(requestParamAndValue.getParamName())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAM_NAME);
        }
        if (StringUtils.isBlank(requestParamAndValue.getParamValue())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARAM_VALUE);
        }
    }

    /**
     * 对apiId和apiGroupId参数进行校验
     *
     * @param idInfo
     */
    public static void idInfoCheck(IDInfo idInfo) {
        if (null == idInfo) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_IDINFO);
        }
        if (null == idInfo.getApiId()) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIID);
        }
//        if (null == idInfo.getApiGroupId()) {
//            throw new DataValidationException(ExceptionInfo.NOT_NULL_APIGROUPID);
//        }

    }

    /**
     * APIINFO参数类型检验--针对Integer类型
     *
     * @param jsonObject
     * @param objectName 解析对象名称
     */
    public static void apiInfoTypeCheck(JSONObject jsonObject, String objectName) {
        String backEndTimeout = jsonObject.getJSONObject(objectName).getString("backEndTimeout");
        if (StringUtils.isNotBlank(backEndTimeout) && !Validator.isInteger(backEndTimeout)) {
            throw new DataValidationException(StringUtils.join(ExceptionInfo.BACKEND_TIMEOUT_MUST_BE_INTEGER, "不能是", backEndTimeout));
        }
        //接口2.0------------------------------------下面---------------------------------------------
        String weight = jsonObject.getJSONObject(objectName).getString("weight");
        if (StringUtils.isNotBlank(weight) && !Validator.isInteger(weight)) {
            throw new DataValidationException(StringUtils.join(ExceptionInfo.WEIGHT_MUST_BE_INTEGER, "不能是", weight));
        }
        //接口2.0------------------------------------上面---------------------------------------------
    }

    /**
     * 下游请求参数类型检验-最大最小长度
     *
     * @param jsonArray1
     */
    public static void requestParamTypeCheck(JSONArray jsonArray1) {
        for (int i = 0; i < jsonArray1.size(); i++) {
            String minLength = jsonArray1.getJSONObject(i).getString("minLength");
            String maxLength = jsonArray1.getJSONObject(i).getString("maxLength");
            if (StringUtils.isNotBlank(minLength) && !Validator.isInteger(minLength)) {
                throw new DataValidationException(StringUtils.join(ExceptionInfo.MINLENGTH_MUST_BE_INTEGER, "不能是", minLength));
            }
            if (StringUtils.isNotBlank(maxLength) && !Validator.isInteger(maxLength)) {
                throw new DataValidationException(StringUtils.join(ExceptionInfo.MAXLENGTH_MUST_BE_INTEGER, "不能是", maxLength));
            }
        }
    }

    /**
     * 自定义错误返回码参数类型检验-erroeCode
     *
     * @param jsonArray3
     */
    public static void resultSettingTypeCheck(JSONArray jsonArray3) {
        for (int i = 0; i < jsonArray3.size(); i++) {
            String errorCode = jsonArray3.getJSONObject(i).getString("errorCode");
            if (StringUtils.isBlank(errorCode) && !Validator.isInteger(errorCode)) {
                throw new DataValidationException(StringUtils.join(ExceptionInfo.ERRORCODE_MUST_BE_INTEGER, "不能是", errorCode));
            }
        }
    }

    /**
     * 【参数验证】-已发布api绑定限流策略
     *
     * @param apiVersionStrategy
     * @Version 2.0
     */
    public static void apiStrategyCheck(ApiVersionStrategy apiVersionStrategy) {
        if (null == apiVersionStrategy) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_API_VERSION_STRATEGY);
        }
        String versionId = apiVersionStrategy.getVersionId();
//		String limitStrategyuuid = apiVersionStrategy.getLimitStrategyuuid();
        if (StringUtils.isBlank(versionId)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_VERSIONID);
        }
//		if (StringUtils.isBlank(limitStrategyuuid)) {
//			throw new DataValidationException(ExceptionInfo.NOT_NULL_LIMIT_STRATEGY_UUID);
//		}
    }

    /**
     * 【参数校验】-子上游APIInfo
     *
     * @param subApiInfo
     * @Version 2.0
     */
    public static void subApiCheck(ApiRateDistributeWithBLOBs subApiInfo) {
        Integer apiId = subApiInfo.getApiId();
        String interfaceName = subApiInfo.getInterfaceName();
        //正则校验
        String backEndAddress = subApiInfo.getBackEndAddress();
        //正则校验
        String backEndAddressB = subApiInfo.getBackEndAddressB();
        //正则校验
        String backEndPath = subApiInfo.getBackEndPath();
        //范围校验
        Integer backEndHttpMethod = subApiInfo.getBackEndHttpMethod();
        //范围校验
        Integer backEndTimeout = subApiInfo.getBackEndTimeout();
        //范围校验
        Integer callBackType = subApiInfo.getCallBackType();
        Integer weight = subApiInfo.getWeight();
        String responseTransParam = subApiInfo.getResponseTransParam();
        String responseConfigJson = subApiInfo.getResponseConfigJson();
        if (apiId == null) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARENT_ID);
        }

        if (StringUtils.isBlank(interfaceName)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_PARENT_NAME);
        }
        if (StringUtils.isBlank(backEndAddress)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_ADDRESS);
        }
        /*if (!Validator.isAddressUrl(backEndAddress)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_BACKEND_ADDRESS);
        }*/
        //backEndPath
        if (StringUtils.isBlank(backEndPath)) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_HTTPPATH);
        }
        if (!Validator.subUrlCheck(backEndPath)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_BACKEND_HTTPPATH);
        }

        if (null == backEndHttpMethod) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_HTTPMETHOD);
        }
        if (backEndHttpMethod != CommonStatus.GET && backEndHttpMethod != CommonStatus.POST) {
            throw new DataValidationException(ExceptionInfo.BACKEND_HTTP_METHOD_ERROR);
        }

        if (null == backEndTimeout) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_BACKEND_TIMEOUT);
        }
        if (backEndTimeout < 0 || backEndTimeout > 30000) {
            throw new DataValidationException(ExceptionInfo.BACKEND_TIMEOUT_ERROR);
        }
        if (null == weight) {
            throw new DataValidationException(ExceptionInfo.WEIGHT_MUST_BE_INTEGER);
        }
        if (weight < 0 || weight > 10) {
            throw new DataValidationException(ExceptionInfo.WEIGHT_MUST_BETWEEN);
        }

        if (StringUtils.isNotBlank(responseTransParam) && !JsonValidator.validate(responseTransParam)) {
            if (!JsonValidator.validate(responseTransParam)) {
                throw new DataValidationException(ExceptionInfo.FORMAT_RESPONSE_TRANS_PARAN_ERROR);
            }
            responseConfigJsonValidation(responseTransParam);
        }

        if (StringUtils.isNotBlank(responseConfigJson) && !JsonValidator.validate(responseConfigJson)) {
            throw new DataValidationException(ExceptionInfo.FORMAT_RESPONSE_TRANS_PARAN_ERROR);
        }

    }

    /**
     * subApi参数验证
     *
     * @param subParamsList
     * @Version 2.0
     */
    public static void subApiParamsCheck(List<BackendDistributeParams> subParamsList) {
        BackendDistributeParams subParam;
        Iterator<BackendDistributeParams> iterator = subParamsList.iterator();
        while (iterator.hasNext()) {
            subParam = iterator.next();
            subApiParamsCheck(subParam);
        }
    }

    /**
     * subApi参数验证
     *
     * @param subParam
     * @Version 2.0
     */
    private static void subApiParamsCheck(BackendDistributeParams subParam) {
        Integer requestParamsId = subParam.getRequestParamsId();
        Integer paramsType = subParam.getParamsType();
        String paramName = subParam.getParamName();
        String paramValue = subParam.getParamValue();
        Integer paramsLocation = subParam.getParamsLocation();
        //paramsType
        if (null == paramsType) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.NOT_NULL));
        }
        if (paramsType != CommonStatus.VARIABLE && paramsType != CommonStatus.CONSTANT && paramsType != CommonStatus.JSON && paramsType != CommonStatus.XML) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.ERROR));
        }
        //参数类型为常量、json、xml时，value不能为空
        if ((CommonStatus.CONSTANT == paramsType || CommonStatus.JSON == paramsType || CommonStatus.XML == paramsType) && StringUtils.isBlank(paramValue)) {
            throw new BusinessException(StringUtils.join("上游paramValue", ExceptionInfo.NOT_NULL));
        }
        //参数类型为变量时，requestParamsId不能为空
        if (CommonStatus.VARIABLE == paramsType && null == requestParamsId) {
            throw new BusinessException(StringUtils.join("上游变量参数对应的下游参数Id", ExceptionInfo.NOT_NULL));
        }
        //paramName
        if (StringUtils.isBlank(paramName)) {
            throw new DataValidationException(StringUtils.join("paramName", ExceptionInfo.NOT_NULL));
        }
        //paramsLocation
        if (null == paramsLocation) {
            throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.NOT_NULL));
        }
        if (paramsLocation != CommonStatus.HEADER && paramsLocation != CommonStatus.QUERY && paramsLocation != CommonStatus.BODY) {
            throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.ERROR));
        }
    }

    /**
     * ApiResultEtl 参数验证
     *
     * @param apiResultEtl
     * @Version 2.0
     */
    public static void apiResultEtlcheck(ApiResultEtl apiResultEtl) {
        String eKey = apiResultEtl.geteKey();
        String tKey = apiResultEtl.gettKey();
        String tValue = apiResultEtl.gettValue();
        String description = apiResultEtl.getDescription();

        if (StringUtils.isBlank(eKey)) {
            throw new DataValidationException(StringUtils.join("抽取key", ExceptionInfo.NOT_NULL));
        }

        if (StringUtils.isBlank(tKey)) {
            throw new DataValidationException(StringUtils.join("转换", ExceptionInfo.NOT_NULL));
        }

        if (StringUtils.isBlank(description)) {
            throw new DataValidationException(StringUtils.join("描述", ExceptionInfo.NOT_NULL));
        }

        if (StringUtils.isNotBlank(tValue)) {

            if (!JsonValidator.validate(tValue)) {
                throw new DataValidationException(ExceptionInfo.FORMAT_ERROR_TVALUE);
            }
        }

    }

    /**
     * ApiResultEtl 参数验证
     *
     * @param subApiResultEtlList
     * @Version 2.0
     */
    public static void apiResultEtlcheckList(List<ApiResultEtl> subApiResultEtlList) {
        for (ApiResultEtl apiResultEtl : subApiResultEtlList) {
            apiResultEtlcheck(apiResultEtl);
        }
    }


    /**
     * 对发给上游的参数进行校验
     *
     * @param backendList
     */
    public static void backendDistributeParamsCheckList(List<BackendDistributeParams> backendList) {

        for (BackendDistributeParams backendDistributeParams : backendList) {
            backendRequestParamsCheck(backendDistributeParams);
        }

    }

    /**
     * 对单条发送给上游的请求参数进行校验
     *
     * @param backendDistributeParams
     */
    public static void backendRequestParamsCheck(BackendDistributeParams backendDistributeParams) {
        Integer paramsType = backendDistributeParams.getParamsType();
        Integer paramsLocation = backendDistributeParams.getParamsLocation();
        //paramsType
        if (null == paramsType) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.NOT_NULL));
        }
        if (paramsType != CommonStatus.VARIABLE && paramsType != CommonStatus.CONSTANT && paramsType != CommonStatus.JSON && paramsType != CommonStatus.XML) {
            throw new DataValidationException(StringUtils.join("paramsType", ExceptionInfo.ERROR));
        }
        if (paramsLocation != CommonStatus.HEADER && paramsLocation != CommonStatus.QUERY && paramsLocation != CommonStatus.BODY) {
            throw new DataValidationException(StringUtils.join("paramsLocation", ExceptionInfo.ERROR));
        }
    }


    /**
     * 【参数校验】-字典信息的校验
     *
     * @param
     * @Version 2.0
     */
    public static void checkDictionary(Dictionary dictionary) {
        if (dictionary.getTypeId() == null) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_TYPE_ID);
        }

        if (StringUtils.isBlank(dictionary.getDictName())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_NAME);
        }
        if (StringUtils.isBlank(dictionary.getDictValue())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_VALUE);
        }
    }

    /**
     * 【参数校验】-字典类型的校验
     *
     * @param
     * @Version 2.0
     */
    public static void checkDictionaryType(DictionaryType dictionaryType) {
        if (StringUtils.isBlank(dictionaryType.getName())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_TYPE_NAME);
        }
    }

    /**
     * 【参数校验】-字典码值的校验
     *
     * @param
     * @Version 2.0
     */
    public static void checkDictionaryItem(DictionaryItem dictionaryItem) {
        if (dictionaryItem.getDictionaryId() == null) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_ID);
        }

        if (StringUtils.isBlank(dictionaryItem.getDictItemName())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_ITEM_NAME);
        }
        if (StringUtils.isBlank(dictionaryItem.getDictItemValue())) {
            throw new DataValidationException(ExceptionInfo.NOT_NULL_DICTIONARY_ITEM_VALUE);
        }
    }

    public static void responseConfigJsonValidation(String responseConfigJson) {
        JSONArray configJsons;
        try {
            configJsons = JSONArray.parseArray(responseConfigJson);
        } catch (Exception e) {
            throw new DataValidationException(ExceptionInfo.RESPONSE_CONFIG_PARAN_ERROR);
        }
        int errorCodeNum = 0;
        for (Object config : configJsons) {
            JSONObject configJson = (JSONObject) config;
            if ("errorCode".equals(configJson.getString("transKey"))) {
                if (Boolean.parseBoolean(configJson.getString("isData"))) {
                    throw new DataValidationException(String.format(ExceptionInfo.ERROR_CODE_EXTRACT, configJson.getString("key")));
                }
                errorCodeNum++;
            }
        }
        if (errorCodeNum == 0) {
            throw new DataValidationException(ExceptionInfo.ERROR_CODE_MISS);
        }
        if (errorCodeNum > 1) {
            throw new DataValidationException(ExceptionInfo.ERROR_CODE_MORE);
        }

    }

    public static boolean isSocketProtocol(ApiInfoWithBLOBs apiInfo) {
        if (apiInfo == null)
            return false;
        return 3 == apiInfo.getBackendProtocolType();
    }
}
