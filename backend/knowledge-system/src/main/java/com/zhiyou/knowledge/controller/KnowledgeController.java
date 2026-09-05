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
            Object questionObj = params.get("question");
            Object docIdObj = params.get("docId");

            // 参数校验：给用户明确的错误提示
            if (questionObj == null || questionObj.toString().trim().isEmpty()) {
                return "问答失败：请输入问题";
            }
            if (docIdObj == null || docIdObj.toString().trim().isEmpty()) {
                return "问答失败：缺少文档ID";
            }

            Long docId;
            try {
                docId = Long.parseLong(docIdObj.toString());
            } catch (NumberFormatException e) {
                log.warn("问答接口 docId 格式错误: {}", docIdObj);
                return "问答失败：文档ID格式不正确";
            }

            return knowledgeService.generateAnswer(questionObj.toString().trim(), docId);
        } catch (IllegalArgumentException e) {
            // 业务校验类错误：日志保留详细原因，对外返回通用提示，避免泄露内部实现细节
            log.warn("问答业务校验失败: {}", e.getMessage());
            return "问答失败：请求参数或业务状态不合法";
        } catch (Exception e) {
            // 其它异常（如 DeepSeek 调用失败）：记录详细日志，返回通用提示
            log.error("生成AI回答时发生异常", e);
            return "问答失败：服务器处理请求时出错，请稍后重试";
        }
    }
}
