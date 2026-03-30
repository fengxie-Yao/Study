package com.example.demo2.dto;

import lombok.Data;

@Data
public class UserQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String username;
    private String nickname;
    private String role; // 角色名称过滤
}
