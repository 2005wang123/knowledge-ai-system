package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.service.KnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // 添加日志记录器
    private static final Logger log = LoggerFactory.getLogger(KnowledgeController.class);

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
            // 将异常详情记录到服务器日志（供开发排查）
            log.error("生成AI回答时发生异常", e);
            // 返回通用错误提示，不包含敏感信息
            return "问答失败：服务器处理请求时出错，请稍后重试";
        }
    }
}
