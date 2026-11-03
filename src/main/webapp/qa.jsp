<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="qa" scope="request"/>
<c:set var="pageTitle" value="智能问答" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card">
    <h3>🤖 向知识库提问</h3>
    <div class="msg">${error}</div>
    <form action="${pageContext.request.contextPath}/qa" method="post">
        <textarea name="question" placeholder="输入你的问题，例如：公司的请假流程是怎样的？" required
                  style="min-height:90px;resize:vertical">${question}</textarea>
        <button class="btn btn-primary" type="submit" style="margin-top:10px;padding:10px 26px">发送</button>
    </form>
</div>

<c:if test="${latestAnswer != null}">
<div class="card">
    <div style="background:#f1f5f9;border-radius:8px;padding:12px 16px;margin-bottom:12px">
        <b>🙋 我的问题：</b>${latestAnswer.question}
    </div>
    <div style="background:var(--primary-light);border-radius:8px;padding:14px 16px;white-space:pre-wrap">
        <b>🤖 AI 回答：</b>${latestAnswer.answer}
    </div>
    <c:if test="${latestAnswer.references != null && !latestAnswer.references.isBlank()}">
        <div class="hint" style="margin-top:10px">📎 参考文档：${latestAnswer.references}</div>
    </c:if>
</div>
</c:if>

<div class="card">
    <div class="flex-bar">
        <h3 style="margin:0">💬 近期问答</h3>
        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/qa?view=history">查看全部历史</a>
    </div>
    <c:choose>
        <c:when test="${empty history}">
            <div class="empty">还没有问答记录</div>
        </c:when>
        <c:otherwise>
            <ul style="padding-left:18px;font-size:13px;color:#475569">
                <c:forEach items="${history}" var="h" end="9">
                    <li style="margin-bottom:6px">${h.createTime} — ${h.question}</li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
