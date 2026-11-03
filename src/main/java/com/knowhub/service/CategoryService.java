package com.knowhub.service;

import com.knowhub.dao.CategoryDAO;
import com.knowhub.entity.Category;

import java.util.List;

/** 分类业务层。 */
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> list() {
        return categoryDAO.findAll();
    }

    public boolean add(String name, String description) {
        if (name == null || name.isBlank()) return false;
        if (categoryDAO.existsByName(name)) return false; // 不允许重名
        return categoryDAO.insert(new Category(null, name.trim(), description == null ? "" : description.trim()));
    }

    public boolean update(Integer id, String name, String description) {
        if (id == null || name == null || name.isBlank()) return false;
        Category c = categoryDAO.findById(id);
        if (c == null) return false;
        // 检查重名（排除自己）
        Category dup = categoryDAO.findAll().stream()
                .filter(x -> name.trim().equals(x.getName()) && !id.equals(x.getId()))
                .findFirst().orElse(null);
        if (dup != null) return false;
        c.setName(name.trim());
        c.setDescription(description == null ? "" : description.trim());
        return categoryDAO.update(c);
    }

    public boolean delete(Integer id) {
        return id != null && categoryDAO.delete(id);
    }

    public Category getById(Integer id) {
        return categoryDAO.findById(id);
    }
}
