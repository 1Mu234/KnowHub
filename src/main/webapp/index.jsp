<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="home" scope="request"/>
<c:set var="pageTitle" value="首页" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<%
    // 一次性提示消息（Filter 拦截后带回）
    String flash = (String) session.getAttribute("flashMsg");
    if (flash != null) {
        session.removeAttribute("flashMsg");
        request.setAttribute("flashMsg", flash);
    }
%>
<c:if test="${flashMsg != null}">
    <div class="card" style="border-color:#fca5a5;background:#fef2f2;color:#b91c1c;font-size:13px">⚠️ ${flashMsg}</div>
</c:if>
<c:if test="${!sessionScope.loginUser.fullAccess}">
    <div class="card" style="border-color:#fde68a;background:#fffbeb;color:#92400e;font-size:13px">
        ⏳ 你的账号正在等待管理员审批。审批通过后可使用分类管理、文档上传、文档列表等完整功能。
    </div>
</c:if>
<div class="card">
    <h3>👋 欢迎回来，${sessionScope.loginUser.nickname}</h3>
    <p style="color:var(--muted)">这里是 KnowHub 智能知识库，支持文档上传、全文检索与 AI 智能问答。从左侧菜单进入各模块。</p>
</div>

<div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px">
    <div class="card" style="margin:0">
        <h3>📤 上传文档</h3>
        <p style="color:var(--muted);font-size:13px;margin-bottom:12px">支持 PDF / DOCX，自动提取文本并建立搜索索引</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/upload">去上传</a>
    </div>
    <div class="card" style="margin:0">
        <h3>🔍 知识检索</h3>
        <p style="color:var(--muted);font-size:13px;margin-bottom:12px">基于关键词的全文检索，秒级定位相关内容</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/search">去检索</a>
    </div>
    <div class="card" style="margin:0">
        <h3>🤖 智能问答</h3>
        <p style="color:var(--muted);font-size:13px;margin-bottom:12px">基于知识库内容的 AI 问答，回答有据可依</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/qa">去提问</a>
    </div>
</div>

<div class="card" style="margin-top:20px">
    <p class="hint" style="margin:0">💡 数据存储模式：由 db.properties 中 dao.impl 控制（jdbc=MySQL 持久化 / mock=内存模拟）</p>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
