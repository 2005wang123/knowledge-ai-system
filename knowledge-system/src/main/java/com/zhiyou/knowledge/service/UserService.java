package com.zhiyou.knowledge.service;

import com.zhiyou.knowledge.dao.UserDao;
import com.zhiyou.knowledge.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户服务层（无第三方依赖）
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户登录（手动MD5加密）
     */
    public User login(String username, String password) {
        // 1. 手动实现MD5加密（替代Hutool）
        String encryptPwd = md5Encrypt(password);
        // 2. 查询用户
        User user = userDao.findByUsername(username);
        // 3. 验证密码
        if (user != null && encryptPwd.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * 用户注册
     */
    public boolean register(User user) {
        // 1. 检查用户名是否存在
        if (userDao.existsByUsername(user.getUsername())) {
            return false;
        }
        // 2. 加密密码
        user.setPassword(md5Encrypt(user.getPassword()));
        // 3. 设置默认值
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname("用户");
        }
        user.setRole(0); // 普通用户
        user.setStatus(1); // 正常
        // 4. 保存
        return userDao.save(user);
    }

    /**
     * 根据ID查询用户
     */
    public User findById(Long id) {
        return userDao.findById(id);
    }

    /**
     * 手动实现MD5加密（替代Hutool的MD5工具）
     */
    private String md5Encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
}