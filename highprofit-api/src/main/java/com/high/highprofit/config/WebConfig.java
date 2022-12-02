package com.high.highprofit.config;

import com.high.highprofit.interceptor.LoginInterceptor;
import com.high.highprofit.interceptor.ResetLoginStateInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web相关配置
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;

    private final ResetLoginStateInterceptor resetLoginStateInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor, ResetLoginStateInterceptor resetLoginStateInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.resetLoginStateInterceptor = resetLoginStateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器添加顺序与拦截顺序一致
        String[] loginPath = {
                "/user/verify",
                "/account/**",
                "/income/**",
                "/recharge/**",
                "/bidInfo/record",
        };

        registry.addInterceptor(loginInterceptor).addPathPatterns(loginPath);

        registry.addInterceptor(resetLoginStateInterceptor).addPathPatterns("/**");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
