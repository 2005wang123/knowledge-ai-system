package com.zhiyou.knowledge.service;

/**
 * AI问答服务接口
 */
public interface KnowledgeService {
    /**
     * 基于文档ID生成AI回答
     */
    String generateAnswer(String question, Long docId) throws Exception;
}