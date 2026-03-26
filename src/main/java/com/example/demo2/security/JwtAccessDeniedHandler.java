package com.example.demo2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("message", "无权限访问");
        result.put("data", null);

        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}