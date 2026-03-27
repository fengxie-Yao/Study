package com.example.demo2.handler; // 记得改为你实际的包名

import com.example.demo2.common.Result;
import com.example.demo2.exception.BusinessException; // 引入你的自定义异常
import com.example.demo2.exception.LoginFailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 1. @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// 表示这是一个全局控制器增强类，且返回值直接序列化为 JSON
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理注册时用户名存在的异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 返回特定的错误码 (如 400) 和消息
        return Result.fail(e.getCode(), e.getMessage());
    }
    /**
     * 处理登陆时用户名密码错误的异常
     */
    @ExceptionHandler(LoginFailException.class)
    public Result<Void> handleLoginFailException(LoginFailException e) {
        // 返回 401 状态码和具体的错误信息
        return Result.fail(e.getCode(), e.getMessage());
    }
    /**
     * 处理登陆时用户不存在的问题
     */
    @ExceptionHandler(Exception.class) // 或者 RuntimeException.class
    public Result<Void> handleGeneralException(Exception e) {
        return Result.fail(500, e.getMessage());
    }
}