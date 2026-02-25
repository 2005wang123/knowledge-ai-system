package com.zhiyou.knowledge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置（解决前端5173端口访问后端8080端口的跨域问题）
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有接口生效
                .allowedOriginPatterns("*") // 允许所有来源（开发环境用，生产环境可指定前端地址）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .allowCredentials(true) // 允许携带Cookie
                .maxAge(3600) // 预检请求的有效期，单位秒
                .allowedHeaders("*"); // 允许所有请求头
    }
}