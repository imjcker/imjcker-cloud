package com.imjcker.api.handler.config;

import com.imjcker.api.handler.strategy.ApiStrategyFactory;
import com.imjcker.api.handler.strategy.ApiStrategyFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author thh  2019/7/23
 * @version 1.0.0
 **/
@Configuration
@ConditionalOnClass(ApiStrategyFactory.class)
public class ApiHandlerAutoConfigure {
}
