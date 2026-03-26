package com.example.demo2.controller;

import com.example.demo2.common.Result;
import com.example.demo2.dto.UserDTO;
import com.example.demo2.service.UserService;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {
        userService.register(username, password);
        return "注册成功";
    }

    @PostMapping("/login")
    public Result<?> login(@RequestParam String username,
                           @RequestParam String password) {

        String token = userService.login(username, password);

        if (token != null) {
            return Result.success(token);
        }

        return Result.fail("用户名或密码错误");
    }

    @GetMapping("/list")
    public List<UserDTO> list(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        System.out.println("当前用户：" + username);
        return userService.getAllUsers();
    }
}