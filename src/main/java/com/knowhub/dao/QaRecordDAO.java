package com.knowhub.dao;

import com.knowhub.entity.QaRecord;
import java.util.List;

public interface QaRecordDAO {
    List<QaRecord> findAll();
    List<QaRecord> findByUserId(Integer userId);
    QaRecord findById(Integer id);
    boolean insert(QaRecord record);
}
