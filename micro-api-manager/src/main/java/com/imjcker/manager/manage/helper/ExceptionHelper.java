package com.imjcker.manager.manage.helper;

import com.lemon.common.vo.CommonResult;
import com.lemon.common.vo.ResultStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.BadSqlGrammarException;

public class ExceptionHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHelper.class);

	/**
	 *
	 * @param e 捕获异常
	 */
	public static CommonResult handleException(Throwable e, String path, int timeout) {
		String errorMessage = e.getMessage();
		 if (e instanceof IllegalStateException) {
			LOG.error("请求参数不合规:{}", errorMessage, e);
            return new CommonResult(ResultStatusEnum.PARAM_NULL,null);
         }else if(e instanceof BadSqlGrammarException){
			 LOG.error("更新参数不合规:{}", errorMessage, e);
			 return new CommonResult(ResultStatusEnum.PARAM_ERROR,null);
		 }
		LOG.error(errorMessage, e);
        return new CommonResult(ResultStatusEnum.ERROR,null);
    }

}
