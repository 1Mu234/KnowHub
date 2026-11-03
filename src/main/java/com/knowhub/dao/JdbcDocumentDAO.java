package com.knowhub.dao;

import com.knowhub.entity.Document;
import com.knowhub.util.JdbcTemplate;

import java.util.List;

/** DocumentDAO 的 JDBC 实现（documents 表）。 */
public class JdbcDocumentDAO implements DocumentDAO {

    private static Document map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Document d = new Document();
        d.setId(rs.getInt("id"));
        d.setTitle(rs.getString("title"));
        d.setFileName(rs.getString("file_name"));
        d.setFilePath(rs.getString("file_path"));
        d.setContentType(rs.getString("content_type"));
        d.setFileSize(rs.getLong("file_size"));
        d.setCategoryId((Integer) rs.getObject("category_id"));
        d.setContent(rs.getString("content"));
        d.setUploadTime(JdbcTemplate.getLdt(rs, "upload_time"));
        return d;
    }

    @Override
    public List<Document> findAll() {
        return JdbcTemplate.queryList(
                "SELECT id, title, file_name, file_path, content_type, file_size," +
                "       category_id, content, upload_time FROM documents ORDER BY upload_time DESC",
                JdbcDocumentDAO::map);
    }

    @Override
    public Document findById(Integer id) {
        if (id == null) return null;
        return JdbcTemplate.queryOne(
                "SELECT id, title, file_name, file_path, content_type, file_size," +
                "       category_id, content, upload_time FROM documents WHERE id = ?",
                JdbcDocumentDAO::map, id);
    }

    @Override
    public boolean insert(Document doc) {
        if (doc == null) return false;
        if (doc.getId() == null) {
            int key = JdbcTemplate.insertAndReturnKey(
                    "INSERT INTO documents(title, file_name, file_path, content_type, file_size," +
                    "                        category_id, content, upload_time)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    doc.getTitle(), doc.getFileName(), doc.getFilePath(),
                    doc.getContentType(), doc.getFileSize(), doc.getCategoryId(),
                    doc.getContent(), doc.getUploadTime());
            doc.setId(key);
            return key > 0;
        }
        return JdbcTemplate.update(
                "INSERT INTO documents(id, title, file_name, file_path, content_type, file_size," +
                "                       category_id, content, upload_time)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                doc.getId(), doc.getTitle(), doc.getFileName(), doc.getFilePath(),
                doc.getContentType(), doc.getFileSize(), doc.getCategoryId(),
                doc.getContent(), doc.getUploadTime()) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        return JdbcTemplate.update("DELETE FROM documents WHERE id = ?", id) > 0;
    }

    @Override
    public boolean update(Document doc) {
        if (doc == null || doc.getId() == null) return false;
        return JdbcTemplate.update(
                "UPDATE documents SET title=?, file_name=?, file_path=?, content_type=?," +
                "                    file_size=?, category_id=?, content=?, upload_time=?" +
                " WHERE id=?",
                doc.getTitle(), doc.getFileName(), doc.getFilePath(),
                doc.getContentType(), doc.getFileSize(), doc.getCategoryId(),
                doc.getContent(), doc.getUploadTime(), doc.getId()) > 0;
    }
}
