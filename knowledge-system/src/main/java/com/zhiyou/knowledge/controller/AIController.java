package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.common.Result;
import com.zhiyou.knowledge.dto.AiAskRequest;
import com.zhiyou.knowledge.service.AIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * AI问答控制器
 */
@RestController
public class AIController {

    @Resource
    private AIService aiService;

    /**
     * AI问答接口
     */
    @PostMapping("/ai/ask")
    public Result<String> ask(@RequestBody AiAskRequest request) {
        String answer = aiService.ask(request.getDocumentId(), request.getQuestion());
        return Result.success(answer);
    }
}