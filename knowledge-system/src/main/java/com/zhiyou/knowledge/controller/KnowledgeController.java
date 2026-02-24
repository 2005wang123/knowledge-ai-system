package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.service.KnowledgeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * AI问答控制器
 */
@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Resource
    private KnowledgeService knowledgeService;

    /**
     * 生成AI回答（前端调用的接口）
     */
    @PostMapping("/generate-answer")
    public String generateAnswer(@RequestBody Map<String, Object> params) {
        try {
            String question = (String) params.get("question");
            Long docId = Long.parseLong(params.get("docId").toString());

            return knowledgeService.generateAnswer(question, docId);
        } catch (Exception e) {
            e.printStackTrace();
            return "问答失败：" + e.getMessage();
        }
    }
}