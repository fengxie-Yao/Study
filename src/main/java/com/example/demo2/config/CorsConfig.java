package com.example.demo2.config; // 确保包路径正确

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 拦截所有路径
                // 1. 明确指定前端地址 (开发环境)
                // 生产环境可以配置多个，或者读取配置文件
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")

                // 2. 明确允许的方法 (OPTIONS 必须包含，用于预检请求)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                // 3. 允许所有头部 (因为 JWT 在 Authorization 头部)
                .allowedHeaders("*")

                // 4. 允许携带凭证 (Cookie/Token)
                // 注意：如果使用了 allowCredentials(true)，allowedOrigins 就不能是 "*"
                .allowCredentials(true)

                // 5. 预检请求缓存时间 (秒)，减少 OPTIONS 请求频率
                .maxAge(3600);
    }
}