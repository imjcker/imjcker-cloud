package com.imjcker.api.common.exception;

/**
 * @Title ExceptionInfo
 * @Description 异常信息
 */
public interface ExceptionInfo {
	//数据校验
	String NOT_NULL_APIGROUPNAME = "api分组名称不能为空";
	String NOT_NULL_GROUPDESCRIPTION = "api分组描述不能为空";
	String NOT_NULL_APINAME = "api名称不能为空";
	String NOT_NULL_APIGROUPID = "分组不能为空";
	String NOT_NULL_ID = "ID不能为空";
	String NOT_NULL_PARAMS = "参数不能为空";
	String NOT_NULL_QUERY = "查询Query不能为空";
	String NOT_NULL_HTTPPATH = "请求Path不能为空";
	String NOT_NULL_BACKEND_HTTPPATH = "后端请求Path不能为空";
	String NOT_NULL_HTTPMETHOD = "httpMethod不能为空";
	String NOT_NULL_BACKEND_ADDRESS = "后端服务地址不能为空";
	String NOT_NULL_BACKEND_HTTPMETHOD = "后端HttpMethod不能为空";
	String NOT_NULL_BACKEND_TIMEOUT = "后端超时不能为空";
	String NOT_NULL_ISMOCK = "Mock不能为空";
	String NOT_NULL_CALLBACKTYPE = "callBackType不能为空";
	String NOT_NULL_SUCCESS_EXAMPLE = "返回结果示例不能为空";
	String NOT_NULL_FAIL_EXAMPLE = "callBackFailExample不能为空";
	String NOT_NULL_SAVEMONGODB = "存储MongoDB不能为空";
	String NOT_NULL_SAVEMQ = "发送MQ不能为空";
	String NOT_NULL = "不能为空";
	String NOT_NULL_APIID = "用于查找apiInfo的ID值为空";
	String NOT_NULL_API_RESULTSETTINGS_ID = "用于查找apiResultSettings的ID值为空";
	String NOT_NULL_REQUEST_PARAMS_ID = "用于查找requestParams的ID值为空";
	String NOT_NULL_BACKEND_PARAMS_ID = "用于查找backendRequestParams的ID值为空";
	String NOT_NULL_BACKEND_PARAM_VALUE = "后端常量参数的值不能为空";
	String NOT_NULL_OFFLINE_OPERATION = "下线操作不能为空";
	String NOT_NULL_APIGROUP = "api分组不能为空";
	String NOT_NULL_APIGROUPUUID = "api分组UUID不能为空";
	String NOT_NULL_MONGODBURI = "mongodbURI不能为空";
	String NOT_NULL_REQUEST_PARAM_AND_VALUE = "调试API传入的参数信息不能为空";
	String NOT_NULL_PARAM_NAME = "调试API传入的参数名称不能为空";
	String NOT_NULL_PARAM_VALUE = "调试API传入的参数值不能为空";
	String NOT_NULL_IDINFO = "调试API传入的APIID和API分组ID不能为空";
	String NOT_NULL_API_DESCRIPTION = "API描述不能为空";
	String NOT_NULL_PUBDESCRIPTION = "切换版本备注不能为空";
	String NOT_NULL_RESULTSETTINGSLIST = "错误码不能为空";
	//接口2.0---------------下面
	String NOT_NULL_WEIGHT = "权重不能为空";
	String NOT_NULL_INTERFACE_NAME = "上游接口名称不能为空";
	String NOT_NULL_CHARGE = "是否计费必须选择";
	String NOT_NULL_API_VERSION_STRATEGY = "绑定策略的参数不能为空";
	String NOT_NULL_VERSIONID = "版本号不能为空";
	String NOT_NULL_LIMIT_STRATEGY_UUID = "限流策略UUID不能为空";
	String NOT_CURRENT_VERSION = "不是当前版本";
	String NOT_NULL_SUBAPI_ID = "子上游APIID不能为空";
	String NOT_NULL_CACHENO = "缓存时间不能为空";
	//接口2.0---------------上面
	String API_EXIST="API已存在";
	String API_NAME_LENGTH_ERROR = "api名称长度不符合规范，应在4-50字符之间";
	String API_COPY_EXIST = "存在复制接口";
	String BACKEND_TIMEOUT_ERROR = "后端超时时间应在1-30000毫秒以内";
	String API_DESCRIPTION_LENGTH_ERROR = "api描述长度不符合规范，应在1-2000字符之间";
	String OFFLINE_ENV_ERROR = "api下线参数错误，应为1：线上、2预发（测试）";
	String PARAMS_ERROR = "参数错误";
	String ERROR = "不符合规范";
	String WEIGHT_EXIST = "该权重已存在";

	String FORMAT_ERROR_APIGROUPNAME = "api分组名称格式错误";
	String FORMAT_ERROR_GROUPDESCRIPTION = "api分组描述格式错误";
//	String FORMAT_ERROR_BACKEND_ADDRESS = "后端服务地址格式错误";
	String FORMAT_ERROR_HTTPPATH = "请求Path格式错误";
	String FORMAT_ERROR_BACKEND_HTTPPATH = "后端请求Path格式错误";
	String FORMAT_MOCKDATA_ERROR = "Mock返回结果格式错误";
	String FORMAT_SUCCESS_EXAMPLE_ERROR = "返回结果示例格式错误";
	String FORMAT_FAIL_EXAMPLE_ERROR = "失败返回结果示例格式错误";
	String FORMAT_MONGODBURI_ERROR = "mongodbURI格式错误";
	String BACKEND_HTTP_METHOD_ERROR = "后端请求方式类型错误";
	String CALLBACKTYPE_ERROR = "后端返回类型类型错误";
	String FORMAT_RESPONSE_TRANS_PARAN_ERROR = "json格式不合法";
	String LENGTH_ERROR_STRATEGY_NAME = "策略名称应为4-15字符";
	String FORMAT_ERROR_TVALUE = "转换值的JSON格式错误";


	String ID_IS_NOT_INTEGER = "ID不是Integer类型";
	String BACKEND_TIMEOUT_MUST_BE_INTEGER = "后端服务超时必须为数字";
	String MINLENGTH_MUST_BE_INTEGER = "最小长度必须为数字";
	String MAXLENGTH_MUST_BE_INTEGER = "最大长度必须为数字";
	String ERRORCODE_MUST_BE_INTEGER = "错误码必须为数字";
	//接口2.0------------------------------下面-------------------------------
	String WEIGHT_MUST_BE_INTEGER = "权重不能为空且必须为数字";
	String WEIGHT_MUST_BETWEEN = "权重必须为0到10之间";

	//接口2.0------------------------------上面-------------------------------
	//业务
	String EXIST_APIGROUPNAME = "API分组名称已存在";
	String EXIST_APINAME = "API名称已存在";
	//接口2.0------------------------------下面-------------------------------
	String EXIST_INTERFACE_NAME = "上游接口名称已存在";
	String THREE_LEVEL_CANT_BUILD_GROUP = "三级分组下不能再建分组";
	//接口2.0------------------------------上面-------------------------------

	String NOT_EXIST_ID = "id不存在";
	String NOT_EXIST_GROUP = "api分组不存在";
	String NOT_EXIST_APIINFO = "这样的api不存在";
	String NOT_EXIST_RESULTSETTINGS = "这样的自定义错误返回码数据不存在";
	String NOT_EXIST_REQUEST_PARAMS = "这样的下游请求参数数据不存在";
	String NOT_EXIST_BACKEND_REQUEST_PARAMS = "这样的上游请求参数数据不存在";
	String NOT_EXIST_APIINFO_VERSIONS = "这样的apiInfo版本不存在";
	String NOT_EXIST_SUBAPI = "这样的子上游API不存在";
	//接口2.0------------------------------下面-------------------------------
	String NOT_EXIST_LIMIT_STRATEGY = "这样的限流策略不存在";
	String NOT_EXSIT_API_GROUP = "这样的分组不存在";
	String THE_GROUP_HAS_LOWER_LEVEL_GROUP = "该分组下已存在分组，不能再添加api";
	//接口2.0------------------------------上面-------------------------------

	String OPERATION_IS_INVALID = "操作无效";
	String SETTING_MOCK_ERROR = "Mock返回结果设置错误";
	String FILL_IN_MOCK_DATA = "请填写Mock返回结果";
	String FILL_IN_MONGODB= "请输入mongoDB相关参数";
	String SETTING_MONGODB_ERROR = "mongoDB相关参数设置错误";
	String FILL_IN_MQ = "请输入MQ相关参数";
	String SETTING_MQ_ERROR = "MQ相关参数设置错误";
	String PARAMS_MUST_ERROR = "下游请求参数参数必填相关属性错误";

	String NOT_NULL_STATUS_RESULTSETTINGS = "自定义错误返回码的status不存在";
	String NOT_NULL_STATUS_REQUEST_PARAMS = "requestParams的status不存在";
	String NOT_NULL_REQUEST_VERSIONS_PARAMS = "requestParamsVersion中不存在对应versionId的参数";
	String NOT_NULL_STATUS_BACKEND_REQUEST = "后端服务请求参数的status不能为空";

	String SQL_DB_EXCEPTION = "SQL数据库操作异常";

	String URL_IS_REPEAT = "前后端请求路径相同";
	String IS_REPEAT = "与当前路径重复";
	String ERROR_CODE_IS_NOT_REPEAT = "错误码不允许重复";
	String LOOKUPINFO_IS_NOT_REPEAT = "寻值不允许重复";
	String PARAMS_ARE_REPEAT = "参数不能重复";
	String ERROR_CODE_CAN_NOT_REPEAT = "错误码不能重复";
	String LOOKUPINFO_CAN_NOT_REPEAT = "寻值不能重复";
	String ERRORCODE_IS_THE_SAME_AS_SYSTEM_ERRORCODE = "自定义错误码不能与系统级错误码重复";

	String HTTP_METHOD_PARAM_ERROR = "请求方式为GET时，参数不能在Body,请求方式为POST时，参数不能在Query";

	String NOT_THE_ONLY_ONE_ERRORCODE_LOOKUPINFO = "自定义错误返回码的错误码或寻值不唯一";

	String DELETE_API_GROUP_FAILED = "删除api分组失败，该分组下存在运行中的API";
	String DELETE_API_FAILED = "删除api失败，该api正在运行中！";

	String ALREADY_IN_USE = "正在使用中";
	String ALREADY_BINDING = "该API已经绑定了分组";
	String API_IS_UNPUBLISHED = "该API未发布，不能进行此操作";
	String THIS_STRATEGY_ALREADY_BINDING = "该策略已经绑定了API，请先解除绑定再删除";
	String THIS_GROUP_ALREADY_HAVE_API = "该分组下已经有API，不能再创建下级";


	//接口2.0------------------------------子接口信息-------------------------------
	String NOT_NULL_SUB_API_ID = "子ID不能为空";
	String NOT_NULL_PARENT_ID = "父级API.ID不能为空";
	String NOT_NULL_PARENT_NAME = "父级API名称不能为空";
	String NOT_NULL_SUB_THIRD_API_INFO = "下游子API基本信息不能为空";
	String NOT_NULL_SUB_THIRD_REQUEST_INFO = "下游子API请求不能为空";
	String NOT_NULL_SUB_THIRD_RESULT_INFO = "下游子API返回信息不能为空";
	String NOT_DEL_SUB_API_ID = "下游主接口对应的子接口不允许删除";
	String NOT_EXIST_SUB_API_NAME = "该子接口不存在";
	String EXIST_SUB_API_NAME = "子接口名字存在";
	String ERROR_API_INFO = "API数据异常,请联系管理员";
	String ERROR_API_GROUP_INFO = "API数据分组异常,请联系管理员";
	String NUM_OF_API_PARAM_ERROR = "API配置参数出现异常,请联系管理员";


	//接口2.0------------------------------码值类信息-------------------------------
	String EXIST_DICTIONARY_TYPE_NAME = "码值类型名称不能重复";
	String EXIST_DICTIONARY_NAME = "描述不能重复";
	String EXIST_DICTIONARY_VALUE = "码值不能重复";
	String EXIST_DICTIONARY_ITEM_VALUE = "字典条目码值不能重复";
	String EXIST_DICTIONARY_ITEM_NAME = "字典条目名称不能重复";
	String NOT_NULL_DICTIONARY_ID= "码值ID不能为空";
	String NOT_NULL_DICTIONARY_TYPE_ID= "码值类型ID不能为空";
	String
			NOT_NULL_DICTIONARY_ITEM_ID= "码值ID不能为空";

	String NOT_NULL_DICTIONARY_INFO= "字典信息不能为空";
	String NOT_NULL_DICTIONARY_TYPE_INFO= "字典类型信息不能为空";
	String NOT_NULL_DICTIONARY_ITEM_INFO= "字典码值信息不能为空";

	String NOT_NULL_DICTIONARY_NAME = "码值不能为空";
	String NOT_NULL_DICTIONARY_VALUE= "码值描述不能为空";
	String NOT_NULL_DICTIONARY_TYPE_NAME= "码值类型不能为空";

	String NOT_NULL_DICTIONARY_ITEM_VALUE= "字典码值名称不能为空";
	String NOT_NULL_DICTIONARY_ITEM_NAME= "字典码值不能为空";


	//接口2.0------------------------------返回报文json配置-------------------------------
	String ERROR_CODE_MISS = "缺少转换key值errorCode";
	String ERROR_CODE_EXTRACT = "字段%s配置转换key值为errorCode,此字段不能配置抽取";
	String ERROR_CODE_MORE = "配置多个转换key值为errorCode";
	String RESPONSE_CONFIG_PARAN_ERROR = "返回报文字段配置json格式不合法";

	//------------------------------组合接口---------------------------
	String OVER_WEIGHT="权重总和超过10，请重新检查配置";
	String EXIST= "已存在";
	String ERROR_VALUE="参数错误";


	/**-----------------------------------------计费---------------------------------------**/
	String CHARGE_UNCONTROLLABLE_COST= "无法控制成本，请更换计费规则";
	String CHARGE_DEFICIT="即将亏损:";
	String CHARGE_OVER_MAXNUM="亏损，超过最大调用量条数:";
	String CHARGE_SOURCE_NOT_EXIT="上游接口未绑定规则";
	String CHARGE_MODE_NOT_EXIT="不允许的计费模式";
	String CHARGE_NOT_EXIT="规则不存在";


}
