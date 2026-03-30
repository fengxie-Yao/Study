package com.example.demo2.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserSaveRequest {
    private Long id; // null 表示新增，有值表示修改
    private String username;
    private String password; // 仅新增或修改密码时必填
    private String nickname;
    private String email;
    private Boolean enabled;
    private List<String> roles; // 前端传递角色名列表
}
