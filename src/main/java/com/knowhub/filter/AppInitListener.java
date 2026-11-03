package com.knowhub.filter;

import com.knowhub.util.HikariDataSourceHolder;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * 应用生命周期监听器：
 * - 启动时：如果是 JDBC 模式，预热 HikariCP
 * - 关闭时：关闭连接池
 */
@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("===== KnowHub 应用启动 =====");
        if (com.knowhub.util.DbConfig.isJdbcMode()) {
            System.out.println("[AppInit] dao.impl=jdbc，预热连接池...");
            HikariDataSourceHolder.get();
        } else {
            System.out.println("[AppInit] dao.impl=mock，使用内存模拟 DAO");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HikariDataSourceHolder.shutdown();
        System.out.println("===== KnowHub 应用关闭 =====");
    }
}
