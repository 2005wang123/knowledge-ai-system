package com.zhiyou.knowledge.service.impl;

import com.zhiyou.knowledge.entity.Document;
import com.zhiyou.knowledge.service.AIService;
import com.zhiyou.knowledge.service.DocumentService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class AIServiceImpl implements AIService {

    @Resource
    private DocumentService documentService;

    // 分别填入你的 AK 和 SK
    private static final String API_KEY = "bce-v3/ALTAK-kvUDJKDnwvLlHMF2qRXXj/2c55b088b18de9178ac88b4821bf7a4f634c230a";
    private static final String SECRET_KEY = "4e3e8c468bba46e099faf6d6831bcff8";

    // 生成 Basic Auth 头
    private String getBasicAuthHeader() {
        String auth = API_KEY + ":" + SECRET_KEY;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    // 调用 ERNIE 5.0
    private String callErnie5API(String prompt) throws Exception {
        String url = "https://qianfan.baidubce.com/v2/chat/completions";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", getBasicAuthHeader());
        conn.setDoOutput(true);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "ERNIE-5.0-Turbo");
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", prompt));
        requestBody.put("messages", messages);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray choices = jsonResponse.getJSONArray("choices");
        return choices.getJSONObject(0).getJSONObject("message").getString("content").trim();
    }

    @Override
    public String ask(Long documentId, String question) {
        try {
            Document document = documentService.getById(documentId);
            if (document == null) return "文档不存在";

            String docContent = new String(Files.readAllBytes(Paths.get(document.getFilePath())));
            if (docContent.length() > 2000) docContent = docContent.substring(0, 2000) + "...";

            String prompt = "严格基于以下文档回答问题，不要编造内容：\n" + docContent + "\n用户问题：" + question;
            return callErnie5API(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "AI回答失败: " + e.getMessage();
        }
    }
}