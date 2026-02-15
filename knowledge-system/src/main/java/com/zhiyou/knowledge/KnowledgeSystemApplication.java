package com.zhiyou.knowledge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SpringBootApplication
@MapperScan("com.zhiyou.knowledge.mapper") // 扫描MyBatis Mapper接口
public class KnowledgeSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeSystemApplication.class, args);
    }
}