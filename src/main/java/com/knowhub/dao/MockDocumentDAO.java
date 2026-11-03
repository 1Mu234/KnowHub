package com.knowhub.dao;

import com.knowhub.entity.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** DocumentDAO 的内存 Mock 实现。 */
public class MockDocumentDAO implements DocumentDAO {

    private static final List<Document> dataList = new ArrayList<>();
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    @Override
    public List<Document> findAll() {
        return new ArrayList<>(dataList);
    }

    @Override
    public Document findById(Integer id) {
        if (id == null) return null;
        return dataList.stream()
                .filter(d -> id.equals(d.getId()))
                .findFirst().orElse(null);
    }

    @Override
    public boolean insert(Document doc) {
        if (doc == null) return false;
        if (doc.getId() == null) {
            doc.setId(ID_GEN.getAndIncrement());
        }
        dataList.add(doc);
        return true;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        return dataList.removeIf(d -> id.equals(d.getId()));
    }

    @Override
    public boolean update(Document doc) {
        if (doc == null || doc.getId() == null) return false;
        for (int i = 0; i < dataList.size(); i++) {
            if (doc.getId().equals(dataList.get(i).getId())) {
                dataList.set(i, doc);
                return true;
            }
        }
        return false;
    }
}
