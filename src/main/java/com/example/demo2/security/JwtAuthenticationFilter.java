package com.example.demo2.security;

import com.example.demo2.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
@Component
public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // 1. 获取 Header 中的 Token
        String token = resolveToken((HttpServletRequest) request);

        // 2. 如果有 Token 且有效，则设置认证信息
        if (StringUtils.hasText(token)) {
            try {
                // ⭐ 关键：调用新的 getAuthentication 方法，它会带回角色信息
                Authentication authentication = jwtUtil.getAuthentication(token);
                if (authentication != null) {
                    // 将认证信息存入 SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token 无效或过期，清除上下文，让后续的 EntryPoint 处理 401
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}