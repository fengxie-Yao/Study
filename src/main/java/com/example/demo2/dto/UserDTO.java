package com.example.demo2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private Boolean enabled;
    private List<String> roles; // 只返回角色名列表，如 ["ROLE_ADMIN"]
    private LocalDateTime createTime;

    public UserDTO(Long id, String username) {
    }
}