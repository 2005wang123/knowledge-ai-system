package com.zhiyou.knowledge.dao;

import com.zhiyou.knowledge.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问层（原生JDBC）
 */
@Repository // 必须加@Repository，让Spring扫描到并创建Bean
public class UserDao {

    // 注入JdbcTemplate（SpringBoot自动配置，无需手动创建）
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据用户名查询用户
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ? AND status = 1";
        // 修复：BeanPropertyRowMapper需要指定泛型，且参数顺序正确
        List<User> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(User.class), // 泛型<User>不能少
                username // 占位符?对应的参数
        );
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * 检查用户名是否存在
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        // 修复：queryForObject的参数类型匹配
        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class, // 返回值类型
                username
        );
        return count != null && count > 0;
    }

    /**
     * 保存用户（注册）
     */
    public boolean save(User user) {
        String sql = "INSERT INTO user (username, password, nickname, role, status) VALUES (?, ?, ?, ?, ?)";
        // 修复：update方法的参数顺序（sql + 所有占位符参数）
        int rows = jdbcTemplate.update(
                sql,
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getRole(),
                user.getStatus()
        );
        return rows > 0; // 影响行数>0表示保存成功
    }

    /**
     * 根据ID查询用户
     */
    public User findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        List<User> users = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(User.class),
                id
        );
        return users.isEmpty() ? null : users.get(0);
    }
}