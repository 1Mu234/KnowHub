<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="settings" scope="request"/>
<c:set var="pageTitle" value="API 设置" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card" style="max-width:680px">
    <h3>⚙️ 大模型 API 配置</h3>
    <div class="msg" style="${msg != null && msg.contains('失败') ? 'color:var(--danger)' : 'color:#15803d'}">${msg}</div>

    <p style="color:var(--muted);font-size:13px;margin-bottom:18px">
        在此填入你自己的大模型 API Key，问答功能将优先使用你的配置（消耗你自己的免费额度）。
        留空的字段将使用系统全局默认配置。
    </p>

    <form action="${pageContext.request.contextPath}/settings" method="post">
        <div class="form-item">
            <label>API Key <span style="color:var(--danger)">*</span></label>
            <input type="password" name="apiKey" value="${sessionScope.loginUser.apiKey}"
                   placeholder="sk-..." autocomplete="off">
            <div class="hint">去 <a href="https://cloud.siliconflow.cn" target="_blank">硅基流动</a> 免费注册申请</div>
        </div>
        <div class="form-item">
            <label>接口地址（可选）</label>
            <input type="text" name="baseUrl" value="${sessionScope.loginUser.aiBaseUrl}"
                   placeholder="https://api.siliconflow.cn/v1/chat/completions">
            <div class="hint">留空使用系统默认；支持任意 OpenAI 兼容接口</div>
        </div>
        <div class="form-item">
            <label>模型名（可选）</label>
            <input type="text" name="model" value="${sessionScope.loginUser.aiModel}"
                   placeholder="Qwen/Qwen2.5-7B-Instruct">
            <div class="hint">留空使用系统默认；可选：Qwen/Qwen2.5-7B-Instruct、deepseek-ai/DeepSeek-V3 等</div>
        </div>

        <button class="btn btn-primary" type="submit" style="padding:10px 28px">保存配置</button>
        <c:if test="${sessionScope.loginUser.apiKey != null}">
            <button class="btn btn-ghost" type="submit" name="action" value="clear"
                    onclick="return confirm('确认清空自定义配置？')">清空配置</button>
        </c:if>
    </form>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
