package com.knowhub.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取 db.properties 的配置类。
 * 第一次调用时从 classpath 加载；找不到配置文件就用默认值（Mock 模式）。
 */
public class DbConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DbConfig.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                PROPS.load(in);
            } else {
                System.err.println("[DbConfig] 没找到 db.properties，全部使用默认值（Mock 模式）");
            }
        } catch (IOException e) {
            System.err.println("[DbConfig] 读取 db.properties 失败: " + e.getMessage());
        }
    }

    public static String get(String key, String def) {
        String v = PROPS.getProperty(key);
        return (v == null || v.isBlank()) ? def : v.trim();
    }

    /** dao.impl = "jdbc" 还是 "mock"。 */
    public static String getImpl() { return get("dao.impl", "mock"); }

    public static String getUrl()          { return get("db.url", ""); }
    public static String getDriver()       { return get("db.driver", ""); }
    public static String getUsername()     { return get("db.username", ""); }
    public static String getPassword()     { return get("db.password", ""); }
    public static int    getMinIdle()      { return Integer.parseInt(get("db.pool.minIdle", "2")); }
    public static int    getMaxPoolSize()  { return Integer.parseInt(get("db.pool.maxPoolSize", "10")); }
    public static long   getTimeout()      { return Long.parseLong(get("db.pool.connectionTimeout", "30000")); }

    public static boolean isJdbcMode() { return "jdbc".equalsIgnoreCase(getImpl()); }
}
