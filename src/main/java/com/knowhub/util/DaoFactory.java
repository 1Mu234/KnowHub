package com.knowhub.util;

import com.knowhub.dao.*;

/**
 * DAO 工厂：根据 db.properties 里的 dao.impl 自动返回 Mock 或 JDBC 实现。
 * <p>
 * 切换数据库只用改一行配置，Servlet / Service 代码零改动。
 */
public class DaoFactory {

    private static final boolean JDBC_MODE = DbConfig.isJdbcMode();

    public static UserDAO getUserDAO() {
        return JDBC_MODE ? new JdbcUserDAO() : new MockUserDAO();
    }

    public static CategoryDAO getCategoryDAO() {
        return JDBC_MODE ? new JdbcCategoryDAO() : new MockCategoryDAO();
    }

    public static DocumentDAO getDocumentDAO() {
        return JDBC_MODE ? new JdbcDocumentDAO() : new MockDocumentDAO();
    }

    public static QaRecordDAO getQaRecordDAO() {
        return JDBC_MODE ? new JdbcQaRecordDAO() : new MockQaRecordDAO();
    }

    public static boolean isJdbcMode() { return JDBC_MODE; }
}
