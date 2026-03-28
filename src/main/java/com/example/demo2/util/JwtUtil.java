package com.example.demo2.util;

import com.example.demo2.entity.Role;
import com.example.demo2.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long EXPIRATION = 1000 * 60 * 60; // 1小时

    // 生成 token
    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles()
                .stream()
                .map(Role::getName)
                .toList());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }
    // 解析tokens
    public Authentication getAuthentication(String token) {
        // 1. 解析 Token 获取 Claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 2. 获取用户名 (Subject)
        String username = claims.getSubject();
        if (username == null) {
            return null;
        }

        // 3. ⭐ 关键：提取 roles 列表
        // 从 Claims 中获取 "roles" 字段，它是一个 List<?> (可能是 ["ADMIN", "USER"])
        List<?> rolesList = claims.get("roles", List.class);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (rolesList != null) {
            for (Object roleObj : rolesList) {
                String roleName = roleObj.toString();
                // ⚠️ 重要：Spring Security 默认要求角色必须以 "ROLE_" 开头
                // 如果你在生成 Token 时存的是 "ADMIN"，这里必须拼成 "ROLE_ADMIN"
                if (!roleName.startsWith("ROLE_")) {
                    roleName = "ROLE_" + roleName;
                }
                authorities.add(new SimpleGrantedAuthority(roleName));
            }
        }

        // 4. 构建并返回 Authentication 对象
        // 参数：principal(用户名), credentials(密码，这里不需要因为已经验证过 token 了), authorities(权限列表)
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }


    // 解析 token
    public static String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}