package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.common.Result;
import com.zhiyou.knowledge.dto.LoginRequest;
import com.zhiyou.knowledge.entity.User;
import com.zhiyou.knowledge.service.UserService;
import com.zhiyou.knowledge.utils.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录接口（接收JSON参数）
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        // 获取前端传递的用户名和密码
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 调用service层验证登录
        User user = userService.login(username, password);
        if (user == null) {
            // 登录失败，返回500+错误信息
            return Result.error("用户名或密码错误");
        }

        // 登录成功，生成JWT token并返回
        String token = jwtUtil.generateToken(user.getId()); // 把 user 换成 user.getId()
        return Result.success(token);
    }
}