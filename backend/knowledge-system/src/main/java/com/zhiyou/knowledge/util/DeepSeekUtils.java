package com.zhiyou.knowledge.util;

import com.alibaba.fastjson2.JSON;
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
    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.base-url}")
    private String baseUrl;

    @Value("${deepseek.chat-model}")
    private String chatModel;

    @Value("${deepseek.timeout}")
    private int timeout;

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
     */
    public String generateAnswer(String question, String docContent) throws Exception {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("问题不能为空");
        }

        // 构建提示词，让AI基于文档内容回答
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个文档问答助手，必须严格基于以下文档内容回答用户的问题，禁止编造文档里没有的信息。\n\n");
        prompt.append("【文档内容】：\n").append(docContent).append("\n\n");
        prompt.append("【用户问题】：").append(question).append("\n\n");
        prompt.append("要求：回答简洁、准确，仅基于提供的文档内容，不要编造信息。");

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", chatModel);
        JSONArray messages = new JSONArray();
        messages.add(JSON.parseObject("{\"role\":\"user\",\"content\":\"" + prompt.toString() + "\"}"));
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

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception("AI问答失败：" + response.message());
        }

        // 解析响应
        String responseBody = response.body().string();
        JSONObject result = JSON.parseObject(responseBody);
        JSONArray choices = result.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new Exception("AI问答失败：无回答内容");
        }

        return choices.getJSONObject(0).getJSONObject("message").getString("content");
    }
}