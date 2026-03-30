package com.example.demo2.controller;

import com.example.demo2.common.Result;
import com.example.demo2.dto.*;
import com.example.demo2.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterRequest request) {

        userService.register(request.getUsername(), request.getPassword());
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        return Result.success(response);
    }

    @GetMapping("/list")
    public List<UserDTO> list() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        System.out.println("当前用户：" + username);
        return userService.getAllUsers();
    }
    @GetMapping
    public Result<Page<UserDTO>> listUsers(@ModelAttribute UserQuery query) {
        return Result.success(userService.getUsers(query));
    }
    @PostMapping
    public Result<String> createUser(@RequestBody @Validated UserSaveRequest request) {
        userService.saveUser(request);
        return Result.success("创建成功");
    }
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody @Validated UserSaveRequest request) {
        request.setId(id); // 确保 ID 一致
        userService.saveUser(request);
        return Result.success("更新成功");
    }
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success("删除成功");
    }

    // 获取所有角色列表用于下拉框 (可选)
    @GetMapping("/roles")
    public Result<List<String>> getAllRoles() {
        // 调用 service 获取所有角色名
        return Result.success(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
    }

}