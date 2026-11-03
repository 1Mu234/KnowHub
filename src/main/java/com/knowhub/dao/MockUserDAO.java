package com.knowhub.dao;

import com.knowhub.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** UserDAO 的内存 Mock 实现。 */
public class MockUserDAO implements UserDAO {

    private static final List<User> dataList = new ArrayList<>();
    private static final AtomicInteger ID_GEN = new AtomicInteger(3);

    static {
        dataList.add(new User(1, "admin", "123456", "超级管理员", "admin"));
        dataList.add(new User(2, "user",  "123456", "普通用户",   "user"));
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) return null;
        return dataList.stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst().orElse(null);
    }

    @Override
    public User findById(Integer id) {
        if (id == null) return null;
        return dataList.stream()
                .filter(u -> id.equals(u.getId()))
                .findFirst().orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(dataList);
    }

    @Override
    public boolean insert(User user) {
        if (user == null) return false;
        if (user.getId() == null) user.setId(ID_GEN.getAndIncrement());
        dataList.add(user);
        return true;
    }

    @Override
    public boolean updateStatus(Integer id, String status) {
        User u = findById(id);
        if (u == null) return false;
        u.setStatus(status);
        return true;
    }

    @Override
    public boolean updateAiConfig(Integer id, String apiKey, String baseUrl, String model) {
        User u = findById(id);
        if (u == null) return false;
        u.setApiKey(apiKey);
        u.setAiBaseUrl(baseUrl);
        u.setAiModel(model);
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        return dataList.removeIf(u -> id.equals(u.getId()));
    }
}
