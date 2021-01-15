package com.imjcker.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imjcker.api.common.util.Operator;
import com.imjcker.api.common.util.RedisClusterUtils;
import com.imjcker.api.common.vo.ResponseBuilder;
import com.imjcker.api.common.vo.ResultStatusEnum;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.gateway.po.CurrentLimitStrategy;
import com.imjcker.gateway.po.RateLimitAppkey;
import com.imjcker.gateway.po.RateLimitAppkeyApi;
import com.imjcker.gateway.service.MessageCenterService;
import com.imjcker.gateway.service.RateLimitService;
import com.imjcker.gateway.service.ZuulProxyService;
import com.imjcker.gateway.util.AppkeyLimitStrategy;
import com.imjcker.gateway.util.Constant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yezhiyuan
 * @version V2.0
 * 限流策略Filter
 */
public class RateLimitFilter extends ZuulFilter {
    @Autowired
    private ZuulProxyService service;
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    private MessageCenterService messageCenterService;
    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        if (!(boolean) ctx.get("isSuccess")) {
            return null;
        }
        ctx.getResponse().setCharacterEncoding("UTF-8");
        String orderId = (String) request.getAttribute(ZuulHeader.PARAM_KEY_ORDER_ID);
        try {
            JedisCluster jedis = RedisClusterUtils.getJedis();
            Integer apiId = (Integer) request.getAttribute("apiId");
            String appKey = StringUtils.isBlank(request.getHeader("appKey")) ? request.getParameter("appKey") : request.getHeader("appKey");

            // appKey 限流
            RateLimitAppkey appKeyRateLimit = rateLimitService.getAppKey(appKey);//先从缓存获取,没有再从数据库获取
            if (appKeyRateLimit == null) {
                //平台没有查询到 RateLimitAppkey 说明是风控新增的,此处做新增数据操作.默认total为-1,表示不限制数量, 限流策略为空
                appKeyRateLimit = new RateLimitAppkey();
                appKeyRateLimit.setAppKey(appKey);
                appKeyRateLimit.setTotal(-1);
                rateLimitService.saveAppKey(appKeyRateLimit);
            }
            String appKeyStrategy_uuid = appKeyRateLimit.getStrategy();
            if (StringUtils.isNotEmpty(appKeyStrategy_uuid)) {
                CurrentLimitStrategy appKeyLimitStrategy = service.getLimitStrategy(appKeyStrategy_uuid);
                if (appKeyLimitStrategy != null) {
                    //1. 校验总量
                    jedis.incr(Constant.RL_APPKEY_TOTAL + appKey);//统计appKey调用+1
                    String total_times = jedis.get(Constant.RL_APPKEY_TOTAL + appKey);
                    if (appKeyRateLimit.getTotal() != -1 && appKeyRateLimit.getTotal() < Long.parseLong(total_times)) {
                        String sendMessage = appKeyStrategy_uuid + "_sendToCenter_total_" + appKey;
                        String messageChannel = appKeyStrategy_uuid + "_messageChannel_total_" + appKey;
                        if (!jedis.exists(sendMessage) || !"false".equals(jedis.get(sendMessage))) {
                            LOGGER.error("appKey 限流,发送总量警告");
                            this.sendWarnMsg(ctx, request, jedis, apiId, appKey, appKeyLimitStrategy, sendMessage, messageChannel, total_times, ResultStatusEnum.RATE_LIMIT_TOTAL_APPKEY);
                        }
                    }
                    //2. 校验频率
                    String appKey_times = this.setExpire(Constant.RL_APPKEY_RATE + appKey, Operator.timeToSecond(appKeyLimitStrategy.getUnit()));
                    if (appKey_times != null && Long.parseLong(appKey_times) > appKeyLimitStrategy.getNo()) {
                        String sendMessage = appKeyStrategy_uuid + "_sendToCenter_rate_" + appKey;
                        String messageChannel = appKeyStrategy_uuid + "_messageChannel_rate_" + appKey;
                        if (!jedis.exists(sendMessage) || !"false".equals(jedis.get(sendMessage))) {
                            LOGGER.error("appKey 限流, 发送频率警告");
                            this.sendWarnMsg(ctx, request, jedis, apiId, appKey, appKeyLimitStrategy, sendMessage, messageChannel, appKey_times, ResultStatusEnum.RATE_LIMIT_RATE_APPKEY);
                        }
                    }
                }
            }

            //api + appKey 限流校验
            RateLimitAppkeyApi appKeyApiRateLimit = rateLimitService.getAppKeyApi(appKey, apiId);
            if (appKeyApiRateLimit == null) {
                // 添加默认
                appKeyApiRateLimit = new RateLimitAppkeyApi();
                appKeyApiRateLimit.setAppKey(appKey);
                appKeyApiRateLimit.setApiId(apiId);
                appKeyApiRateLimit.setTotal(-1);
                rateLimitService.saveAppKeyApi(appKeyApiRateLimit);
            }
            String appKeyApiRateLimitStrategy_uuid = appKeyApiRateLimit.getStrategy();
            if (StringUtils.isNotEmpty(appKeyApiRateLimitStrategy_uuid)) {
                CurrentLimitStrategy appKeyLimitStrategy = service.getLimitStrategy(appKeyApiRateLimitStrategy_uuid);
                if (appKeyLimitStrategy != null) {
                    //1. 校验总量
                    jedis.incr(Constant.RL_APPKEY_API_TOTAL + appKey + apiId);//统计appKey+api调用+1
                    String total_times = jedis.get(Constant.RL_APPKEY_API_TOTAL + appKey + apiId);
                    if (appKeyApiRateLimit.getTotal() != -1 && appKeyApiRateLimit.getTotal() < Long.parseLong(total_times)) {
                        String sendMessage = appKeyApiRateLimitStrategy_uuid + "_sendToCenter_total_" + appKey + apiId;
                        String messageChannel = appKeyApiRateLimitStrategy_uuid + "_messageChannel_total_" + appKey + apiId;
                        if (!jedis.exists(sendMessage) || !"false".equals(jedis.get(sendMessage))) {
                            LOGGER.error("appKey+api 限流, 发送总量警告");
                            this.sendWarnMsg(ctx, request, jedis, apiId, appKey, appKeyLimitStrategy, sendMessage, messageChannel, total_times, ResultStatusEnum.RATE_LIMIT_TOTAL_APPKEY_API);
                        }
                    }
                    //2. 校验频率
                    String appKey_api_times = this.setExpire(Constant.RL_APPKEY_API_RATE + appKey + apiId, Operator.timeToSecond(appKeyLimitStrategy.getUnit()));
                    if (appKey_api_times != null && Long.parseLong(appKey_api_times) > appKeyLimitStrategy.getNo()) {
                        String sendMessage = appKeyApiRateLimitStrategy_uuid + "_sendToCenter_rate_" + appKey + apiId;
                        String messageChannel = appKeyApiRateLimitStrategy_uuid + "_messageChannel_rate_" + appKey + apiId;
                        if (!jedis.exists(sendMessage) || !"false".equals(jedis.get(sendMessage))) {
                            LOGGER.error("appKey+api 限流, 发送频率警告");
                            this.sendWarnMsg(ctx, request, jedis, apiId, appKey, appKeyLimitStrategy, sendMessage, messageChannel, appKey_api_times, ResultStatusEnum.RATE_LIMIT_RATE_APPKEY_API);
                        }
                    }
                }
            }

            //api限流
            String apiRateLimitStrategy_uuid = (String) request.getAttribute("api_limit_uuid");
            int limitStrategyTotal = (int) request.getAttribute("api_limit_total");
            if (StringUtils.isNotBlank(apiRateLimitStrategy_uuid)) {
                CurrentLimitStrategy strategy = service.getLimitStrategy(apiRateLimitStrategy_uuid);
                if (strategy != null) {
                    //总量限流
                    //1. 校验总量
                    jedis.incr(Constant.RL_API_TOTAL + apiId);//统计appKey+api调用+1
                    String total_times = jedis.get(Constant.RL_API_TOTAL + apiId);
                    if (limitStrategyTotal != -1 && limitStrategyTotal < Long.parseLong(total_times)) {
                        String sendMessage = apiRateLimitStrategy_uuid + "_sendToCenter_total_" + apiId;
                        String messageChannel = apiRateLimitStrategy_uuid + "_messageChannel_total_" + apiId;
                        if (!jedis.exists(sendMessage) || !"false".equals(jedis.get(sendMessage))) {
                            LOGGER.error("api 限流, 发送总量警告");
                            this.sendWarnMsg(ctx, request, jedis, apiId, appKey, strategy, sendMessage, messageChannel, total_times, ResultStatusEnum.RATE_LIMIT_TOTAL_APPKEY_API);
                        }
                    }

                    //原有 频率限流
                    String numStamp = StringUtils.join(apiRateLimitStrategy_uuid, "_num_", apiId);
                    String sendMessage = StringUtils.join(apiRateLimitStrategy_uuid, "_sendToCenter_" + apiId);
                    String messageChannel = StringUtils.join(apiRateLimitStrategy_uuid, "_messageChannel_", apiId);
                    Integer unit = Operator.timeToSecond(strategy.getUnit());//将单位分钟/小时/天转换为对应秒数
                    String times = setExpire(numStamp, unit);
                    //根据频率周期内的次数比较发送警告
                    LOGGER.debug("{}秒内的第{}次调用,剩余过期时间:{}秒", unit, times, jedis.ttl(numStamp));
                    if (times != null && Integer.parseInt(times) > strategy.getNo()) {
                        if (!jedis.exists(sendMessage) || !"false".equals(jedis.get(sendMessage))) {//初始默认为null，预警后为false,过期时间内不再发送告警。
                            this.sendWarnMsg(ctx, request, jedis, apiId, appKey, strategy, sendMessage, messageChannel, times, ResultStatusEnum.RATE_LIMIT_RATE_API);
                        }
                        LOGGER.debug("{}秒后微信可告警,{}秒后短信可告警", jedis.ttl(sendMessage), jedis.ttl(messageChannel));
//                        LOGGER.warn(LogPojo.getErrorLogMsg(ResultStatusEnum.API_RATE_LIMITED.getMessage(), null));
//                        ctx.setSendZuulResponse(false);
//                        request.setAttribute("errorCode", ResultStatusEnum.API_RATE_LIMITED.getCode());
//                        ctx.setResponseBody(JSON.toJSONString(new ErrorResult(ResultStatusEnum.API_RATE_LIMITED, orderId)));
//                        ctx.set("isSuccess", false);
                        return null;
                    }
                }
            }
            //appKey限流
            String appLimitStrategy = (String) request.getAttribute("app_limit_strategy");
            if (StringUtils.isNotBlank(appLimitStrategy)) {
                AppkeyLimitStrategy strategy = JSONObject.parseObject(appLimitStrategy, AppkeyLimitStrategy.class);
                if (strategy.getStatus_flag() == 1) {
                    String timeStamp = StringUtils.join(strategy.getUuid(), "_time_", appKey);
                    String numStamp = StringUtils.join(strategy.getUuid(), "_num_", appKey);
                    Integer unit = Operator.timeToSecond(strategy.getUnit());
                    //redis存的超时时间
                    String timeSave = jedis.get(timeStamp);
                    //如果不存在或者是不等于数据库设置值
                    if (!unit.toString().equals(timeSave)) {
                        //重新设置超时时间
                        jedis.set(timeStamp, unit.toString());
                        jedis.expire(numStamp, unit);
                    }
                    String numSave = jedis.get(numStamp);
                    if (numSave == null) {
                        jedis.set(numStamp, "0");
                        jedis.expire(numStamp, unit);
                    }
                    jedis.incr(numStamp);
                    if (Integer.parseInt(jedis.get(numStamp)) > strategy.getNo()) {
                        ctx.setSendZuulResponse(false);
                        request.setAttribute("errorCode", ResultStatusEnum.TFB_MAX_LIMIT_EXCEPTION.getCode());
                        ResponseBuilder responseBuilder = ResponseBuilder.builder()
                                .uid(orderId)
                                .errorCode(ResultStatusEnum.TFB_MAX_LIMIT_EXCEPTION.getCode())
                                .errorMsg(ResultStatusEnum.TFB_MAX_LIMIT_EXCEPTION.getMessage())
                                .build();
                        ctx.setResponseBody(JSON.toJSONString(responseBuilder));
                        ctx.set("isSuccess", false);
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("RateLimitFilter发生异常");
            ctx.setSendZuulResponse(false);
            request.setAttribute("errorCode", ResultStatusEnum.TFB_INTERNAL_ERROR.getCode());
            ResponseBuilder responseBuilder = ResponseBuilder.builder()
                    .uid(orderId)
                    .errorCode(ResultStatusEnum.TFB_INTERNAL_ERROR.getCode())
                    .errorMsg(e.getMessage())
                    .build();
            ctx.setResponseBody(JSON.toJSONString(responseBuilder));
            ctx.set("isSuccess", false);
            return null;
        }
        return null;
    }

    /**
     * 发送预警消息
     *
     * @param ctx            ctx
     * @param request        request请求
     * @param jedis          jedis
     * @param apiId          apiId
     * @param appKey         appKey
     * @param strategy       策略
     * @param sendMessage    消息内容
     * @param messageChannel 消息渠道
     * @param times          次数
     */
    private void sendWarnMsg(RequestContext ctx, HttpServletRequest request, JedisCluster jedis,
                             Integer apiId, String appKey, CurrentLimitStrategy strategy,
                             String sendMessage, String messageChannel, String times,
                             ResultStatusEnum resultStatusEnum) {
        // todo 后续开发公众号告警
        /*LOGGER.info("to send message");
        ApiInfoVersions entity = rateLimitService.getApiInfo(apiId, request);
        Map<String, String> zuulRequestHeaders = ctx.getZuulRequestHeaders();
        JSONObject content = new JSONObject();
        content.put("content", resultStatusEnum.getMessage());
        content.put("appKey", appKey);
        content.put("apiName", entity.getApiName());
        content.put("errorCode", resultStatusEnum.getCode() + "");
        content.put("uid", zuulRequestHeaders.get("zuul_requestuuid"));
        content.put("strategyName", strategy.getName());
        content.put("times", times);
        boolean sendToPhone = false;
        if (!jedis.exists(messageChannel)) {
            int phoneIntervalTime = messageCenterService.getPhoneIntervalTime(strategy.getUnit());
            jedis.set(messageChannel, "false");
            jedis.expire(messageChannel, phoneIntervalTime);
            if (jedis.ttl(messageChannel) == -1) {
                jedis.expire(messageChannel, phoneIntervalTime);
            }
            sendToPhone = true;
            LOGGER.info("phoneIntervalTime={}", phoneIntervalTime);
        }
        String result = messageCenterService.sendMessage(content, sendToPhone);
        JSONObject object = ExtractUtil.xml2Json(result);
        if ("0000".equals(object.getString("respCode"))) {
            jedis.set(sendMessage, "false");
            int wxIntervalTime = messageCenterService.getWxIntervalTime(strategy.getUnit());
            LOGGER.info("wxIntervalTime={}", wxIntervalTime);
            jedis.expire(sendMessage, wxIntervalTime);

            if (jedis.ttl(sendMessage) == -1) {
                jedis.expire(sendMessage, wxIntervalTime);
            }

        }*/
    }

    /**
     * 设置过期时间,返回调用次数
     *
     * @param key     调用次数key
     * @param timeout 过期时间
     * @return 调用次数
     */
    private String setExpire(String key, Integer timeout) {
        JedisCluster jedis = RedisClusterUtils.getJedis();
        jedis.incr(key);
        String times = jedis.get(key);
        if ("1".equals(times)) {//==1表示初始化，原来redis中不存在，默认永久有效，需要设置有效时间。
            LOGGER.debug("当前限流次数=1,初始化过期时间");
            jedis.expire(key, timeout);
        } else if (StringUtils.isEmpty(times)) {//redis中为空，重新初始化
            LOGGER.debug("当前限流次数=null,初始化过期时间");
            jedis.incr(key);
            jedis.expire(key, timeout);
            times = jedis.get(key);
        } else if (jedis.exists(key) && jedis.ttl(key) == -1) {
            LOGGER.debug("检测到未设置过期时间，设置过期时间");
            jedis.expire(key, timeout);
        }
        return times;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


}
