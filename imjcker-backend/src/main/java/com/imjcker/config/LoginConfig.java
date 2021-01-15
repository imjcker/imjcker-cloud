package com.imjcker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableJpaRepositories(basePackages = {"com.imjcker.repository"})
public class LoginConfig extends WebMvcConfigurerAdapter {
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/article/save*", "/article/delete*")
                //排除不需要验证登录用户操作权限的请求
                .excludePathPatterns("/oauth/github/callback*");
    }
}
