package com.knowhub.service;

import com.knowhub.dao.UserDAO;
import com.knowhub.entity.User;

import java.util.List;

/** 用户业务：登录 + 注册 + 审批。 */
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        User u = userDAO.findByUsername(username);
        if (u == null) return null;
        return password.equals(u.getPassword()) ? u : null;
    }

    /**
     * 注册：默认 role=user、status=pending（等管理员审批）。
     * @return null 表示成功；否则返回错误信息
     */
    public String register(String username, String password, String nickname) {
        if (username == null || username.isBlank()) return "账号不能为空";
        if (password == null || password.isBlank()) return "密码不能为空";
        if (username.length() < 3) return "账号至少 3 个字符";
        if (userDAO.findByUsername(username) != null) return "账号已被注册";
        User u = new User(null, username.trim(), password,
                (nickname == null || nickname.isBlank()) ? username.trim() : nickname.trim(),
                "user");
        u.setStatus("pending");
        return userDAO.insert(u) ? null : "注册失败";
    }

    public List<User> listAll() { return userDAO.findAll(); }

    public boolean approve(Integer id) { return userDAO.updateStatus(id, "approved"); }

    public boolean reject(Integer id) { return userDAO.updateStatus(id, "pending"); }

    /** 保存用户自定义的大模型配置（空字符串视为"清空"，用全局默认）。 */
    public boolean saveAiConfig(Integer id, String apiKey, String baseUrl, String model) {
        if (id == null) return false;
        String k = (apiKey == null || apiKey.isBlank()) ? null : apiKey.trim();
        String b = (baseUrl == null || baseUrl.isBlank()) ? null : baseUrl.trim();
        String m = (model == null || model.isBlank()) ? null : model.trim();
        return userDAO.updateAiConfig(id, k, b, m);
    }

    public boolean delete(Integer id) { return id != null && userDAO.delete(id); }
}
