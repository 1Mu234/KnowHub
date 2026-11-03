package com.knowhub.entity;

import java.time.LocalDateTime;

public class QaRecord {
    private Integer id;
    private Integer userId;
    private String question;
    private String answer;
    private String references;    // 召回的文档 id / 标题，逗号分隔
    private LocalDateTime createTime;

    public QaRecord() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getReferences() { return references; }
    public void setReferences(String references) { this.references = references; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
