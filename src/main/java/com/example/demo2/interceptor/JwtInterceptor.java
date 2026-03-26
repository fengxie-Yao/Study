package com.example.demo2.interceptor;

import com.example.demo2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        String uri = request.getRequestURI();
        System.out.println("🚨🚨🚨 拦截器被触发！请求路径是: " + uri); // 👈 重点看这个
        String authHeader = request.getHeader("Authorization");

        // 1️⃣ 没有 token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        try {
            // 2️⃣ 解析 token
            String token = authHeader.substring(7);
            String username = JwtUtil.parseToken(token);

            // 👉 可以把用户信息存起来（后面用）
            request.setAttribute("username", username);

            return true;

        } catch (Exception e) {
            // 3️⃣ token 错误
            response.setStatus(401);
            return false;
        }
    }
}