package com.zhiyou.knowledge.entity;

import lombok.Data;

/**
 * 用户实体类（对应数据库表）
 */
@Data // Lombok自动生成get/set方法
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private Integer role;
    private Integer status;
}