//package com.imjcker.manager.config;
//
//import org.springframework.context.annotation.Configuration;
///**
// * @Author WT
// * @Date 14:58 2019/11/21
// * @Version SecuritySecureConfig v1.0
// * @Desicrption
// */
//@Configuration
//public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        super.configure(http);
//        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();
//        http.logout().logoutUrl("/logout");
//        http.csrf().disable();
//
//        // 设置不需要保护的资源/路径
//        http.authorizeRequests()
//                .antMatchers("/login.html", "/**/*.css", "/img/**", "/third-party/**", "/info", "/health")
//                .permitAll();
//
//        http.authorizeRequests().antMatchers("/**").authenticated();
//
//        http.httpBasic();
//    }
//}
