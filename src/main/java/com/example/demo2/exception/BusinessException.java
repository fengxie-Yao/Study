package com.example.demo2.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code; // 自定义错误码，例如 400, 409 等

    public BusinessException(String message) {
        super(message);
        this.code = 400; // 默认客户端错误
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
