package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：捕获所有接口的异常，返回具体错误信息
 */
@RestControllerAdvice // 核心注解，作用于所有@RestController
public class GlobalExceptionHandler {

    // 捕获所有Exception类型的异常
    @ExceptionHandler(Exception.class)
    public Result<String> handleAllException(Exception e) {
        // 打印完整异常日志（IDEA控制台会显示）
        e.printStackTrace();
        // 返回具体错误信息给前端（Postman能看到）
        return Result.error("登录失败：" + e.getMessage());
    }
}