package com.knowhub.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取 ai.properties 的大模型配置。
 * 找不到配置文件时，默认走硅基流动（SiliconFlow）免费额度。
 */
public class AiConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AiConfig.class.getResourceAsStream("/ai.properties")) {
            if (in != null) {
                PROPS.load(in);
            } else {
                System.err.println("[AiConfig] 没找到 ai.properties，使用默认云端配置");
            }
        } catch (IOException e) {
            System.err.println("[AiConfig] 读取 ai.properties 失败: " + e.getMessage());
        }
    }

    public static String get(String key, String def) {
        String v = PROPS.getProperty(key);
        return (v == null || v.isBlank()) ? def : v.trim();
    }

    /** Chat Completions 接口地址。默认硅基流动。 */
    public static String getBaseUrl() {
        return get("ai.baseUrl", "https://api.siliconflow.cn/v1/chat/completions");
    }

    /** API Key。 */
    public static String getApiKey() {
        return get("ai.apiKey", "");
    }

    /** 模型名。 */
    public static String getModel() {
        return get("ai.model", "Qwen/Qwen2.5-7B-Instruct");
    }
}
