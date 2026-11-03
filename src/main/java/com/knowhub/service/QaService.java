package com.knowhub.service;

import com.knowhub.dao.QaRecordDAO;
import com.knowhub.entity.Document;
import com.knowhub.entity.QaRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** 智能问答业务：召回 → 拼 prompt → 调 AI → 落库。 */
public class QaService {

    private final QaRecordDAO qaRecordDAO;
    private final SearchService searchService;

    public QaService(QaRecordDAO qaRecordDAO, SearchService searchService) {
        this.qaRecordDAO = qaRecordDAO;
        this.searchService = searchService;
    }

    /**
     * 完整问答流程：返回 QaRecord（已带 answer 和 references）。
     * @param apiKey  用户自定义 Key（可空）
     * @param baseUrl 用户自定义接口地址（可空）
     * @param model   用户自定义模型名（可空）
     */
    public QaRecord ask(Integer userId, String question, String apiKey, String baseUrl, String model) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("问题不能为空");
        }

        // 1. ES 召回
        List<Document> refs = searchService.search(question, 5);

        // 2. 拼 prompt
        StringBuilder context = new StringBuilder();
        for (Document d : refs) {
            context.append("【来源：").append(d.getTitle()).append("】\n");
            String snippet = d.getContent() == null ? "" : d.getContent();
            if (snippet.length() > 1000) snippet = snippet.substring(0, 1000);
            context.append(snippet).append("\n\n");
        }

        String system = "你是 KnowHub 知识库助手，请严格基于给定的参考资料回答问题。" +
                "如果参考资料里没有答案，请明确说「知识库中未找到相关内容」，不要瞎编。";
        String user = "参考资料：\n" + context + "\n\n用户问题：" + question;

        // 3. 调大模型（优先用用户自己的配置，空则用全局 ai.properties）
        String answer = com.knowhub.util.AiApiUtil.chat(system, user, apiKey, baseUrl, model);

        // 4. 落库
        QaRecord record = new QaRecord();
        record.setUserId(userId);
        record.setQuestion(question);
        record.setAnswer(answer);
        record.setReferences(refs.stream()
                .map(d -> d.getId() + ":" + d.getTitle())
                .collect(Collectors.joining(",")));
        record.setCreateTime(LocalDateTime.now());
        qaRecordDAO.insert(record);

        return record;
    }

    public List<QaRecord> history(Integer userId) {
        return qaRecordDAO.findByUserId(userId);
    }
}
