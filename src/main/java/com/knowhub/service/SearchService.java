package com.knowhub.service;

import com.knowhub.dao.DocumentDAO;
import com.knowhub.dao.QaRecordDAO;
import com.knowhub.entity.Document;
import com.knowhub.entity.QaRecord;
import com.knowhub.util.AiApiUtil;
import com.knowhub.util.EsUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 检索业务：ES 不可用时退化为内存 mock 搜索。 */
public class SearchService {

    private final DocumentDAO documentDAO;

    public SearchService(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    /**
     * 根据关键词召回相关文档。先查 ES，ES 无结果时再做一次内存 contains 兜底。
     */
    public List<Document> search(String keyword, int size) {
        List<Document> result = new ArrayList<>();
        if (keyword == null || keyword.isBlank()) return result;

        // 1. 尝试 ES
        List<Integer> ids = EsUtil.search(keyword, size);
        if (!ids.isEmpty()) {
            for (Integer id : ids) {
                Document d = documentDAO.findById(id);
                if (d != null) result.add(d);
            }
        }

        // 2. 兜底：内存 contains
        if (result.isEmpty()) {
            String lower = keyword.toLowerCase();
            for (Document d : documentDAO.findAll()) {
                if ((d.getTitle() != null && d.getTitle().toLowerCase().contains(lower))
                        || (d.getContent() != null && d.getContent().toLowerCase().contains(lower))) {
                    result.add(d);
                    if (result.size() >= size) break;
                }
            }
        }
        return result;
    }
}
