package com.zhiyou.knowledge.controller;

import com.zhiyou.knowledge.dto.AiAskRequest;
import com.zhiyou.knowledge.entity.Result;
import com.zhiyou.knowledge.service.AIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    // 构造函数注入，消除警告，避免字段注入的坑
    private final AIService aiService;

    // Spring 4.3+ 可省略@Autowired
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    // AI问答接口
    @PostMapping("/ask")
    public Result<String> ask(@RequestBody AiAskRequest request) {
        String answer = aiService.ask(request.getDocumentId(), request.getQuestion());
        return Result.success(answer);
    }
}