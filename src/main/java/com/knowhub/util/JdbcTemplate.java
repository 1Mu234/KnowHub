package com.knowhub.util;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 极简 JDBC 模板：封装 try-with-resources，避免 DAO 里到处写样板代码。
 * 不依赖 Spring JDBC，保持轻量。
 */
public class JdbcTemplate {

    /** 单行映射器接口。 */
    @FunctionalInterface
    public interface RowMapper<T> { T mapRow(ResultSet rs) throws SQLException; }

    /** 查询多行。 */
    public static <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... params) {
        List<T> list = new ArrayList<>();
        try (Connection conn = HikariDataSourceHolder.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("[JdbcTemplate] queryList 失败: " + e.getMessage(), e);
        }
        return list;
    }

    /** 查询单行（没找到返回 null）。 */
    public static <T> T queryOne(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection conn = HikariDataSourceHolder.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper.mapRow(rs);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("[JdbcTemplate] queryOne 失败: " + e.getMessage(), e);
        }
    }

    /** 写操作（insert/update/delete），返回受影响行数。 */
    public static int update(String sql, Object... params) {
        try (Connection conn = HikariDataSourceHolder.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillParams(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("[JdbcTemplate] update 失败: " + e.getMessage(), e);
        }
    }

    /** 插入并返回自增主键。 */
    public static int insertAndReturnKey(String sql, Object... params) {
        try (Connection conn = HikariDataSourceHolder.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillParams(ps, params);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("[JdbcTemplate] insertAndReturnKey 失败: " + e.getMessage(), e);
        }
    }

    /** 统一填充参数，LocalDateTime 用 setObject，避免 Timestamp 转换。 */
    private static void fillParams(PreparedStatement ps, Object[] params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            int idx = i + 1;
            if (p == null) {
                ps.setNull(idx, Types.NULL);
            } else if (p instanceof LocalDateTime) {
                ps.setObject(idx, p);
            } else {
                ps.setObject(idx, p);
            }
        }
    }

    /** ResultSet → LocalDateTime 安全取法。 */
    public static LocalDateTime getLdt(ResultSet rs, String col) throws SQLException {
        return rs.getObject(col, LocalDateTime.class);
    }
}
