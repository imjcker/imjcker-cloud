package com.imjcker.api.handler.exception.handler;

import javax.servlet.http.HttpServletRequest;

import com.imjcker.api.common.vo.LogPojo;
import com.imjcker.api.common.vo.ZuulHeader;
import com.imjcker.api.handler.model.AsyncModel;
import com.imjcker.api.handler.plugin.charge.RabbitMQSender;
import com.imjcker.api.handler.util.AsyncCollectionUtil;
import com.imjcker.api.handler.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 全局异常处理器
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private RabbitMQSender chargeMQSender;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult defaultHandler(HttpServletRequest req, Exception ex){
		String orderId = req.getHeader(ZuulHeader.PARAM_KEY_ORDER_ID);

		Integer isCharge = AsyncCollectionUtil.getCharge();
		if (isCharge == 1) {
			//抛全局异常设置下游调用失败
			AsyncModel async = AsyncCollectionUtil.getAsyncModel();
			async.setTargetStatus(2);
			//发送计费MQ
			chargeMQSender.send(async);
		}
		JsonResult result = new JsonResult(orderId,ex.toString());
		LOGGER.error(LogPojo.getErrorLogMsg("未知异常", result, ex));
		return result;
    }


}
