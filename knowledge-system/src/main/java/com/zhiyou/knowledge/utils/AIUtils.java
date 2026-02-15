package com.zhiyou.knowledge.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class AIUtils {
    // 替换成你的通义千问API Key
    private static final String API_KEY = "sk-7277422d53494216be810160ce2463dc";
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    public static String getAnswer(String docContent, String question) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // 2. 构建请求体（严格按照通义千问API格式）
        String prompt = "请严格基于以下文档内容回答用户问题，不要使用外部知识：\n" +
                "【文档内容】：" + docContent + "\n" +
                "【用户问题】：" + question;

        JSONObject body = new JSONObject();
        body.put("model", "qwen-turbo");
        JSONObject input = new JSONObject();
        input.put("messages", new JSONArray() {{
            add(new JSONObject() {{
                put("role", "user");
                put("content", prompt);
            }});
        }});
        body.put("input", input);

        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);

        try {
            // 3. 发送POST请求
            ResponseEntity<String> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // 4. 解析返回结果
            JSONObject result = JSON.parseObject(response.getBody());
            return result.getJSONObject("output").getString("text").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "AI回答失败：" + e.getMessage();
        }
    }
}