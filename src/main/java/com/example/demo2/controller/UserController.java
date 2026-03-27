package com.example.demo2.controller;

import com.example.demo2.common.Result;
import com.example.demo2.dto.LoginRequest;
import com.example.demo2.dto.LoginResponse;
import com.example.demo2.dto.RegisterRequest;
import com.example.demo2.dto.UserDTO;
import com.example.demo2.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Result<?> register(@RequestBody RegisterRequest request) {

        userService.register(request.getUsername(), request.getPassword());
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        return Result.success(response);
    }

//    @GetMapping("/list")
//    public List<UserDTO> list(HttpServletRequest request) {
//        String username = (String) request.getAttribute("username");
//        System.out.println("当前用户：" + username);
//        return userService.getAllUsers();
//    }
    @GetMapping("/list")
    public List<UserDTO> list() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        System.out.println("当前用户：" + username);
        return userService.getAllUsers();
    }
}