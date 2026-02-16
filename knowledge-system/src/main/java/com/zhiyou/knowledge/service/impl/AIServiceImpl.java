package com.zhiyou.knowledge.service.impl;

import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.mapper.DocumentMapper;
import com.zhiyou.knowledge.service.AIService;
import com.zhiyou.knowledge.utils.DeepSeekApiClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

// 核心：必须加 @Service 注解，Spring才会创建这个Bean
@Service
public class AIServiceImpl implements AIService {

    // 构造函数注入/字段注入都可以，这里用@Resource更简洁
    @Resource
    private DocumentMapper documentMapper;
    @Resource
    private DeepSeekApiClient deepSeekApiClient;

    @Override
    public String ask(Long documentId, String question) {
        // 1. 查询文档内容
        Document document = documentMapper.selectById(documentId);
        if (document == null || document.getContent() == null) {
            return "未找到该文档或文档内容为空";
        }
        String docContent = document.getContent();

        // 2. 构建提示词（适配Java 8，不用文本块）
        String prompt = "你是一个专业的知识库问答助手，仅能基于提供的文档内容回答问题，不编造任何未提及的信息。\n" +
                "文档内容：\n" + docContent + "\n\n" +
                "用户问题：" + question + "\n\n" +
                "回答要求：\n" +
                "1. 严格基于文档内容回答，不要添加额外信息；\n" +
                "2. 如果文档中没有相关内容，直接回复「文档中未找到相关答案」；\n" +
                "3. 回答简洁明了，逻辑清晰。";

        // 3. 调用DeepSeek API
        return deepSeekApiClient.callDeepSeek(prompt);
    }
}