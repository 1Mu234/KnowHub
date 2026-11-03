package com.knowhub.dao;

import com.knowhub.entity.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** CategoryDAO 的内存 Mock 实现。 */
public class MockCategoryDAO implements CategoryDAO {

    private static final List<Category> dataList = new ArrayList<>();
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    static {
        // 预置几个分类，方便演示
        dataList.add(new Category(ID_GEN.getAndIncrement(), "技术文档", "技术类知识文档"));
        dataList.add(new Category(ID_GEN.getAndIncrement(), "产品手册", "产品使用说明"));
        dataList.add(new Category(ID_GEN.getAndIncrement(), "规章制度", "公司内部规范"));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(dataList);
    }

    @Override
    public Category findById(Integer id) {
        if (id == null) return null;
        return dataList.stream()
                .filter(c -> id.equals(c.getId()))
                .findFirst().orElse(null);
    }

    @Override
    public boolean insert(Category category) {
        if (category == null) return false;
        if (category.getId() == null) {
            category.setId(ID_GEN.getAndIncrement());
        }
        dataList.add(category);
        return true;
    }

    @Override
    public boolean update(Category category) {
        if (category == null || category.getId() == null) return false;
        for (int i = 0; i < dataList.size(); i++) {
            if (category.getId().equals(dataList.get(i).getId())) {
                dataList.set(i, category);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        return dataList.removeIf(c -> id.equals(c.getId()));
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null) return false;
        return dataList.stream().anyMatch(c -> name.equals(c.getName()));
    }
}
