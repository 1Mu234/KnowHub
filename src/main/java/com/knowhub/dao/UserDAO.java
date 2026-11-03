package com.knowhub.dao;

import com.knowhub.entity.User;
import java.util.List;

public interface UserDAO {
    User findByUsername(String username);
    User findById(Integer id);
    List<User> findAll();
    boolean insert(User user);
    boolean updateStatus(Integer id, String status);
    /** 保存用户自定义的大模型配置（key/baseUrl/model 均可空，空表示用全局默认）。 */
    boolean updateAiConfig(Integer id, String apiKey, String baseUrl, String model);
    boolean delete(Integer id);
}
