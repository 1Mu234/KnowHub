package com.knowhub.dao;

import com.knowhub.entity.Category;
import com.knowhub.util.JdbcTemplate;

import java.util.List;

/** CategoryDAO 的 JDBC 实现（categories 表）。 */
public class JdbcCategoryDAO implements CategoryDAO {

    private static Category map(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }

    @Override
    public List<Category> findAll() {
        return JdbcTemplate.queryList(
                "SELECT id, name, description FROM categories ORDER BY id ASC",
                JdbcCategoryDAO::map);
    }

    @Override
    public Category findById(Integer id) {
        if (id == null) return null;
        return JdbcTemplate.queryOne(
                "SELECT id, name, description FROM categories WHERE id = ?",
                JdbcCategoryDAO::map, id);
    }

    @Override
    public boolean insert(Category category) {
        if (category == null) return false;
        if (category.getId() == null) {
            int key = JdbcTemplate.insertAndReturnKey(
                    "INSERT INTO categories(name, description) VALUES (?, ?)",
                    category.getName(), category.getDescription());
            category.setId(key);
            return key > 0;
        }
        return JdbcTemplate.update(
                "INSERT INTO categories(id, name, description) VALUES (?, ?, ?)",
                category.getId(), category.getName(), category.getDescription()) > 0;
    }

    @Override
    public boolean update(Category category) {
        if (category == null || category.getId() == null) return false;
        return JdbcTemplate.update(
                "UPDATE categories SET name=?, description=? WHERE id=?",
                category.getName(), category.getDescription(), category.getId()) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        return JdbcTemplate.update("DELETE FROM categories WHERE id = ?", id) > 0;
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null) return false;
        Integer cnt = JdbcTemplate.queryOne(
                "SELECT COUNT(*) FROM categories WHERE name = ?",
                rs -> rs.getInt(1), name);
        return cnt != null && cnt > 0;
    }
}
