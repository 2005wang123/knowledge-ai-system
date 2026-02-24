package com.zhiyou.knowledge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目启动类（纯文档上传功能，无向量）
 */
@SpringBootApplication
@MapperScan("com.zhiyou.knowledge.mapper") // 扫描Mapper接口
@EnableTransactionManagement // 开启事务管理
public class KnowledgeAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeAiApplication.class, args);
        System.out.println("===== 文档上传系统启动成功 =====");
        System.out.println("===== 接口地址：http://localhost:8080/api/document =====");
    }
}