package com.zhiyou.knowledge.service;

/**
 * AI问答服务接口
 */
public interface AIService {
    /**
     * 根据文档内容回答问题
     * @param documentId 文档ID
     * @param question 用户问题
     * @return 回答内容
     */
    String ask(Long documentId, String question);
}