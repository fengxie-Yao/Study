package com.example.demo2.exception;

import lombok.Getter;

/**
 * 专门用于处理登录失败的异常
 */
@Getter
public class LoginFailException extends RuntimeException {
    private final int code;

    public LoginFailException(String message) {
        super(message);
        this.code = 401; // 401 Unauthorized
    }

    public LoginFailException(int code, String message) {
        super(message);
        this.code = code;
    }
}