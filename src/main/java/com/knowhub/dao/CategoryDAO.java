package com.knowhub.dao;

import com.knowhub.entity.Category;
import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();
    Category findById(Integer id);
    boolean insert(Category category);
    boolean update(Category category);
    boolean delete(Integer id);
    boolean existsByName(String name);
}
