package com.knowhub.dao;

import com.knowhub.entity.Document;
import java.util.List;

public interface DocumentDAO {
    List<Document> findAll();
    Document findById(Integer id);
    boolean insert(Document doc);
    boolean delete(Integer id);
    boolean update(Document doc);
}
