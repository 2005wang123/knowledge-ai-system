package com.zhiyou.knowledge.dto;

import lombok.Data;

/**
 * 登录请求参数DTO
 */
@Data
public class LoginRequest {
    // 用户名
    private String username;
    // 密码
    private String password;
}