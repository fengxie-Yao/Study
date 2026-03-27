package com.example.demo2.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;      // 注册时可能需要指定角色
    private String email;     // 注册可能需要邮箱
    private String captcha;   // 注册通常需要验证码
}