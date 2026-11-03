package com.knowhub.entity;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String role;    // admin / user
    private String status;  // pending（待审批）/ approved（已审批）
    private String apiKey;       // 用户自定义大模型 API Key（可空，空则用全局）
    private String aiBaseUrl;    // 用户自定义接口地址（可空）
    private String aiModel;      // 用户自定义模型名（可空）

    public User() {}
    public User(Integer id, String username, String password, String nickname, String role) {
        this.id = id; this.username = username; this.password = password;
        this.nickname = nickname; this.role = role;
        this.status = "approved";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getAiBaseUrl() { return aiBaseUrl; }
    public void setAiBaseUrl(String aiBaseUrl) { this.aiBaseUrl = aiBaseUrl; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String aiModel) { this.aiModel = aiModel; }

    /** 是否可以使用完整功能（查看源文档、上传） */
    public boolean isFullAccess() {
        return "admin".equals(role) || "approved".equals(status);
    }
}
