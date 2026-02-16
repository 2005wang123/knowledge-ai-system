package com.zhiyou.knowledge.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * DeepSeek API调用客户端（Spring管理，可Autowired注入）
 */
@Component // 核心：让Spring接管这个类，才能在Service中注入
public class DeepSeekApiClient {
    // ========== 替换成你自己的DeepSeek API Key ==========
    private static final String DEEPSEEK_API_KEY = "sk-xxx"; //你的DeepSeek API Key
    // DeepSeek通用对话接口地址（固定，不用改）
    private static final String DEEPSEEK_CHAT_URL = "https://api.deepseek.com/v1/chat/completions";

    /**
     * 调用DeepSeek模型获取回答
     * @param prompt 拼接好的提示词（文档内容+用户问题）
     * @return AI回答/错误信息
     */
    public String callDeepSeek(String prompt) {
        try {
            // 1. 建立连接
            URL url = new URL(DEEPSEEK_CHAT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // 2. 设置请求头（认证+内容类型）
            conn.setRequestProperty("Authorization", "Bearer " + DEEPSEEK_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true); // 允许发送请求体

            // 3. 构造请求体（DeepSeek要求的格式）
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "deepseek-chat"); // 模型名，固定为deepseek-chat
            requestBody.put("temperature", 0.7); // 回答随机性，0-1之间
            requestBody.put("max_tokens", 2048); // 最大回答长度

            // 消息体：必须是[{"role":"user","content":"提示词"}]格式
            JSONArray messages = new JSONArray();
            JSONObject msg = new JSONObject();
            msg.put("role", "user");
            msg.put("content", prompt);
            messages.add(msg);
            requestBody.put("messages", messages);

            // 4. 发送请求
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // 5. 处理响应
            int responseCode = conn.getResponseCode();
            // 处理错误响应（比如401/402/500）
            if (responseCode != 200) {
                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder errorMsg = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorMsg.append(errorLine);
                }
                errorReader.close();
                return "DeepSeek调用失败（响应码：" + responseCode + "）：" + errorMsg;
            }

            // 处理成功响应
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder responseMsg = new StringBuilder();
            String responseLine;
            while ((responseLine = responseReader.readLine()) != null) {
                responseMsg.append(responseLine);
            }
            responseReader.close();

            // 6. 解析返回结果
            JSONObject resultJson = JSON.parseObject(responseMsg.toString());
            JSONArray choices = resultJson.getJSONArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                return message.getString("content"); // 返回AI的回答内容
            } else {
                return "DeepSeek未返回有效回答：" + responseMsg;
            }

        } catch (Exception e) {
            e.printStackTrace(); // 控制台打印异常，方便排查
            return "DeepSeek调用异常：" + e.getMessage();
        }
    }

    // ========== 测试方法（可选，直接运行验证API是否可用） ==========
    public static void main(String[] args) {
        DeepSeekApiClient client = new DeepSeekApiClient();
        String testPrompt = "基于以下文档回答：DeepSeek是深度求索推出的大模型，适用于知识库问答场景。\n问题：DeepSeek适合什么场景？";
        String answer = client.callDeepSeek(testPrompt);
        System.out.println("AI回答：" + answer);
    }
}