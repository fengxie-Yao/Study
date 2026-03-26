package com.example.demo2.config;

import com.example.demo2.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")   // 拦截所有接口
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register"
                ); // 放行登录注册
    }
}