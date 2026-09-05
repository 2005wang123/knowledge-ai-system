package com.zhiyou.knowledge.util;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek API 工具类（AI问答）
 */
@Component
public class DeepSeekUtils {
    private static final Logger log = LoggerFactory.getLogger(DeepSeekUtils.class);
    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.base-url}")
    private String baseUrl;

    @Value("${deepseek.chat-model}")
    private String chatModel;

    @Value("${deepseek.timeout}")
    private int timeout;

    /** 文档内容最大字符数，超出部分截断，防止超出模型上下文窗口 */
    @Value("${deepseek.max-content-length:30000}")
    private int maxContentLength;

    private OkHttpClient client;

    @PostConstruct
    public void init() {
        client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 基于文档内容生成AI回答
     * @param question 用户问题
     * @param docContent 文档完整内容
     * @return AI回答
     * @throws Exception 异常
     */
    public String generateAnswer(String question, String docContent) throws Exception {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("问题不能为空");
        }

        // 防止文档内容过长导致超出模型上下文窗口：超出部分截断
        if (docContent != null && docContent.length() > maxContentLength) {
            log.warn("文档内容过长({}字符)，已截断至{}字符", docContent.length(), maxContentLength);
            docContent = docContent.substring(0, maxContentLength) + "\n……（文档内容过长，已截断）";
        }

        // 构建提示词，让AI基于文档内容回答
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个文档问答助手，必须严格基于以下文档内容回答用户的问题，禁止编造信息。\n");
        prompt.append("如果答案来自表格，请输出【严格的标准 Markdown 表格】，格式必须为：\n");
        prompt.append("| 列1 | 列2 | 列3 |\n");
        prompt.append("| --- | --- | --- |\n");
        prompt.append("| 数据1 | 数据2 | 数据3 |\n");
        prompt.append("要求：\n");
        prompt.append("1. 必须包含表头行、分隔线行、数据行，缺一不可；\n");
        prompt.append("2. 列数与原表格完全一致，不要合并或拆分单元格；\n");
        prompt.append("3. 不要加粗任何文字，所有内容样式统一；\n");
        prompt.append("4. 不要用空格对齐，严格用 | 分隔列。\n\n");
        prompt.append("【文档内容】：\n").append(docContent).append("\n\n");
        prompt.append("【用户问题】：").append(question).append("\n\n");
        prompt.append("回答要求：仅输出答案，不要多余解释，必须严格按上面的 Markdown 表格格式输出。");

        // ✅ 修复核心：用 JSONObject 安全构建消息，自动转义特殊字符
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", chatModel);

        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt.toString()); // 自动转义 " \n \ 等特殊字符
        messages.add(userMessage);

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        // 发送请求
        Request request = new Request.Builder()
                .url(baseUrl + "/chat/completions")
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        requestBody.toString()
                ))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() == null ? "" : response.body().string();
            if (!response.isSuccessful()) {
                // 记录模型服务返回的具体错误（如 401 密钥无效、400 上下文超长、429 限流等），便于排查
                log.error("DeepSeek API 调用失败, HTTP {}: {}", response.code(), extractErrorMessage(responseBody));
                throw new Exception("AI问答失败：模型服务返回错误(HTTP " + response.code() + ")");
            }

            // 解析响应
            JSONObject result = JSON.parseObject(responseBody);
            JSONArray choices = result.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) {
                throw new Exception("AI问答失败：无回答内容");
            }

            return choices.getJSONObject(0).getJSONObject("message").getString("content");
        }
    }

    /**
     * 从 DeepSeek 错误响应中提取可读的错误信息
     */
    private String extractErrorMessage(String responseBody) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return "响应体为空";
        }
        try {
            JSONObject obj = JSON.parseObject(responseBody);
            JSONObject error = obj.getJSONObject("error");
            if (error != null && error.getString("message") != null) {
                return error.getString("message");
            }
            return responseBody;
        } catch (Exception e) {
            return responseBody.length() > 500 ? responseBody.substring(0, 500) : responseBody;
        }
    }
}
