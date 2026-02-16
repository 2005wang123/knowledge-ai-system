package com.zhiyou.knowledge; // 最顶层包，能扫描所有子包

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 自动扫描当前包及子包的所有@Component/@Service/@Controller
@MapperScan("com.zhiyou.knowledge.mapper") // 扫描Mapper接口
public class KnowledgeSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeSystemApplication.class, args);
    }
}