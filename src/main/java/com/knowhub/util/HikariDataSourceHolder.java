package com.knowhub.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * HikariCP 单例 DataSource。
 * 用 double-checked locking 保证线程安全；配置错误时降级到 Mock（返回 null），Service 层不会崩。
 */
public class HikariDataSourceHolder {

    private static volatile HikariDataSource INSTANCE;

    /**
     * 懒初始化。只有 DaoFactory 用 JDBC 实现时才会触发。
     */
    public static DataSource get() {
        if (INSTANCE == null) {
            synchronized (HikariDataSourceHolder.class) {
                if (INSTANCE == null) {
                    try {
                        HikariConfig cfg = new HikariConfig();
                        cfg.setDriverClassName(DbConfig.getDriver());
                        cfg.setJdbcUrl(DbConfig.getUrl());
                        cfg.setUsername(DbConfig.getUsername());
                        cfg.setPassword(DbConfig.getPassword());
                        cfg.setMinimumIdle(DbConfig.getMinIdle());
                        cfg.setMaximumPoolSize(DbConfig.getMaxPoolSize());
                        cfg.setConnectionTimeout(DbConfig.getTimeout());
                        cfg.setAutoCommit(true);
                        // 连接健康检查
                        cfg.setConnectionTestQuery("SELECT 1");
                        INSTANCE = new HikariDataSource(cfg);
                        System.out.println("[HikariCP] DataSource 初始化完成：" + DbConfig.getUrl());
                    } catch (Exception e) {
                        System.err.println("[HikariCP] 初始化失败，降级为 Mock: " + e.getMessage());
                        INSTANCE = null;
                    }
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 拿一个连接。用完必须 close()（会归还连接池）。
     */
    public static Connection getConnection() throws SQLException {
        DataSource ds = get();
        if (ds == null) throw new SQLException("HikariCP DataSource 未初始化");
        return ds.getConnection();
    }

    /** 应用关闭时调用。 */
    public static void shutdown() {
        if (INSTANCE != null) {
            try { INSTANCE.close(); } catch (Exception ignored) {}
            INSTANCE = null;
        }
    }
}
