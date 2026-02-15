package com.zhiyou.knowledge.dto;

import lombok.Data;

/**
 * AI问答请求参数
 */
@Data
public class AiAskRequest {
    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 用户问题
     */
    private String question;
}