package com.zhiyou.knowledge.service.impl;

import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.mapper.DocumentMapper;
import com.zhiyou.knowledge.service.KnowledgeService;
import com.zhiyou.knowledge.util.DeepSeekUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AI问答服务实现类
 */
@Service
public class KnowledgeServiceImpl implements KnowledgeService {

    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private DeepSeekUtils deepSeekUtils;

    @Override
    public String generateAnswer(String question, Long docId) throws Exception {
        // 1. 根据文档ID查询文档内容
        Document document = documentMapper.selectById(docId);
        if (document == null) {
            throw new RuntimeException("文档不存在");
        }

        String docContent = document.getContent();
        if (docContent == null || docContent.trim().isEmpty()) {
            throw new RuntimeException("文档内容为空，无法进行问答");
        }

        // 2. 调用DeepSeek API生成回答
        return deepSeekUtils.generateAnswer(question, docContent);
    }
}