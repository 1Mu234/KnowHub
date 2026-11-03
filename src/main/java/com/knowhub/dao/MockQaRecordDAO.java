package com.knowhub.dao;

import com.knowhub.entity.QaRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** QaRecordDAO 的内存 Mock 实现。 */
public class MockQaRecordDAO implements QaRecordDAO {

    private static final List<QaRecord> dataList = new ArrayList<>();
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    @Override
    public List<QaRecord> findAll() {
        return new ArrayList<>(dataList);
    }

    @Override
    public List<QaRecord> findByUserId(Integer userId) {
        if (userId == null) return findAll();
        List<QaRecord> result = new ArrayList<>();
        for (QaRecord r : dataList) {
            if (userId.equals(r.getUserId())) result.add(r);
        }
        return result;
    }

    @Override
    public QaRecord findById(Integer id) {
        if (id == null) return null;
        return dataList.stream()
                .filter(r -> id.equals(r.getId()))
                .findFirst().orElse(null);
    }

    @Override
    public boolean insert(QaRecord record) {
        if (record == null) return false;
        if (record.getId() == null) {
            record.setId(ID_GEN.getAndIncrement());
        }
        dataList.add(record);
        return true;
    }
}
