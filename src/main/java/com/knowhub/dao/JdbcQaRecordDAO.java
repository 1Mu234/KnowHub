package com.knowhub.dao;

import com.knowhub.entity.QaRecord;
import com.knowhub.util.JdbcTemplate;

import java.util.List;

/** QaRecordDAO 的 JDBC 实现（qa_records 表）。 */
public class JdbcQaRecordDAO implements QaRecordDAO {

    private static QaRecord map(java.sql.ResultSet rs) throws java.sql.SQLException {
        QaRecord r = new QaRecord();
        r.setId(rs.getInt("id"));
        r.setUserId((Integer) rs.getObject("user_id"));
        r.setQuestion(rs.getString("question"));
        r.setAnswer(rs.getString("answer"));
        r.setReferences(rs.getString("doc_references"));
        r.setCreateTime(JdbcTemplate.getLdt(rs, "create_time"));
        return r;
    }

    @Override
    public List<QaRecord> findAll() {
        return JdbcTemplate.queryList(
                "SELECT id, user_id, question, answer, doc_references, create_time" +
                " FROM qa_records ORDER BY create_time DESC",
                JdbcQaRecordDAO::map);
    }

    @Override
    public List<QaRecord> findByUserId(Integer userId) {
        if (userId == null) return findAll();
        return JdbcTemplate.queryList(
                "SELECT id, user_id, question, answer, doc_references, create_time" +
                " FROM qa_records WHERE user_id = ? ORDER BY create_time DESC",
                JdbcQaRecordDAO::map, userId);
    }

    @Override
    public QaRecord findById(Integer id) {
        if (id == null) return null;
        return JdbcTemplate.queryOne(
                "SELECT id, user_id, question, answer, doc_references, create_time" +
                " FROM qa_records WHERE id = ?",
                JdbcQaRecordDAO::map, id);
    }

    @Override
    public boolean insert(QaRecord record) {
        if (record == null) return false;
        if (record.getId() == null) {
            int key = JdbcTemplate.insertAndReturnKey(
                    "INSERT INTO qa_records(user_id, question, answer, doc_references, create_time)" +
                    " VALUES (?, ?, ?, ?, ?)",
                    record.getUserId(), record.getQuestion(), record.getAnswer(),
                    record.getReferences(), record.getCreateTime());
            record.setId(key);
            return key > 0;
        }
        return JdbcTemplate.update(
                "INSERT INTO qa_records(id, user_id, question, answer, doc_references, create_time)" +
                " VALUES (?, ?, ?, ?, ?, ?)",
                record.getId(), record.getUserId(), record.getQuestion(), record.getAnswer(),
                record.getReferences(), record.getCreateTime()) > 0;
    }
}
