package com.knowhub.dao;

import com.knowhub.entity.User;
import com.knowhub.util.JdbcTemplate;

import java.util.List;

/** UserDAO 的 JDBC 实现（users 表）。 */
public class JdbcUserDAO implements UserDAO {

    private static User map(java.sql.ResultSet rs) throws java.sql.SQLException {
        User u = new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("nickname"),
                rs.getString("role"));
        u.setStatus(rs.getString("status"));
        u.setApiKey(rs.getString("api_key"));
        u.setAiBaseUrl(rs.getString("ai_base_url"));
        u.setAiModel(rs.getString("ai_model"));
        return u;
    }

    private static final String COLS = "id, username, password, nickname, role, status, api_key, ai_base_url, ai_model";

    @Override
    public User findByUsername(String username) {
        if (username == null) return null;
        return JdbcTemplate.queryOne(
                "SELECT " + COLS + " FROM users WHERE username = ?",
                JdbcUserDAO::map, username);
    }

    @Override
    public User findById(Integer id) {
        if (id == null) return null;
        return JdbcTemplate.queryOne(
                "SELECT " + COLS + " FROM users WHERE id = ?",
                JdbcUserDAO::map, id);
    }

    @Override
    public List<User> findAll() {
        return JdbcTemplate.queryList(
                "SELECT " + COLS + " FROM users ORDER BY id ASC",
                JdbcUserDAO::map);
    }

    @Override
    public boolean insert(User user) {
        if (user == null) return false;
        int key = JdbcTemplate.insertAndReturnKey(
                "INSERT INTO users(username, password, nickname, role, status) VALUES (?, ?, ?, ?, ?)",
                user.getUsername(), user.getPassword(), user.getNickname(),
                user.getRole() == null ? "user" : user.getRole(),
                user.getStatus() == null ? "pending" : user.getStatus());
        user.setId(key);
        return key > 0;
    }

    @Override
    public boolean updateStatus(Integer id, String status) {
        if (id == null || status == null) return false;
        return JdbcTemplate.update(
                "UPDATE users SET status = ? WHERE id = ?", status, id) > 0;
    }

    @Override
    public boolean updateAiConfig(Integer id, String apiKey, String baseUrl, String model) {
        if (id == null) return false;
        return JdbcTemplate.update(
                "UPDATE users SET api_key = ?, ai_base_url = ?, ai_model = ? WHERE id = ?",
                apiKey, baseUrl, model, id) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        return JdbcTemplate.update("DELETE FROM users WHERE id = ?", id) > 0;
    }
}
