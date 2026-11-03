package com.knowhub.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * 大模型 API 封装。
 * <p>配置从 ai.properties 读取（AiConfig）。默认对接 Ollama（本地、免费、无需 Key）。
 * 也可改成硅基流动、DeepSeek 等任意 OpenAI 兼容接口。
 * <p>不可用时返回兜底文案。
 */
public class AiApiUtil {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    /** 调用大模型，返回回答文本。使用全局配置（ai.properties）。 */
    public static String chat(String systemPrompt, String userPrompt) {
        return chat(systemPrompt, userPrompt, null, null, null);
    }

    /**
     * 调用大模型，返回回答文本。
     * @param userApiKey  用户自定义 Key（可空，空则用 AiConfig 全局配置）
     * @param userBaseUrl 用户自定义接口地址（可空）
     * @param userModel   用户自定义模型名（可空）
     */
    public static String chat(String systemPrompt, String userPrompt,
                              String userApiKey, String userBaseUrl, String userModel) {
        String baseUrl = (userBaseUrl != null && !userBaseUrl.isBlank()) ? userBaseUrl : AiConfig.getBaseUrl();
        String apiKey  = (userApiKey  != null && !userApiKey.isBlank())  ? userApiKey  : AiConfig.getApiKey();
        String model   = (userModel   != null && !userModel.isBlank())   ? userModel   : AiConfig.getModel();

        JsonObject payload = new JsonObject();
        payload.addProperty("model", model);
        payload.addProperty("temperature", 0.7);

        JsonArray messages = new JsonArray();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            JsonObject sys = new JsonObject();
            sys.addProperty("role", "system");
            sys.addProperty("content", systemPrompt);
            messages.add(sys);
        }
        JsonObject usr = new JsonObject();
        usr.addProperty("role", "user");
        usr.addProperty("content", userPrompt);
        messages.add(usr);
        payload.add("messages", messages);

        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtil.toJson(payload)));

        if (apiKey != null && !apiKey.isBlank()) {
            reqBuilder.header("Authorization", "Bearer " + apiKey);
        }

        try {
            HttpResponse<String> resp = CLIENT.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                JsonObject root = JsonUtil.parse(resp.body());
                JsonArray choices = root.getAsJsonArray("choices");
                if (choices.size() > 0) {
                    JsonObject msg = choices.get(0).getAsJsonObject()
                            .getAsJsonObject("message");
                    return msg.get("content").getAsString();
                }
            }
            return "大模型返回异常，HTTP " + resp.statusCode() + ": " + resp.body();
        } catch (Exception e) {
            System.err.println("[AiApiUtil] 调用失败: " + e.getMessage());
            return "（兜底回答）大模型服务暂不可用，请稍后再试。问题是：" + userPrompt;
        }
    }
}
