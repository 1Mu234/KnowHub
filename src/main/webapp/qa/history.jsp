<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="history" scope="request"/>
<c:set var="pageTitle" value="问答历史" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card">
    <h3>📜 我的问答历史</h3>
    <table>
        <thead>
            <tr><th style="width:170px">时间</th><th>问题</th><th>回答</th><th style="width:200px">参考文档</th></tr>
        </thead>
        <tbody>
        <c:forEach items="${recordList}" var="r">
            <tr>
                <td style="color:var(--muted)">${r.createTime}</td>
                <td><b>${r.question}</b></td>
                <td style="white-space:pre-wrap;color:#475569;max-width:380px">${r.answer}</td>
                <td style="color:var(--muted);font-size:12px">${r.references}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty recordList}">
            <tr><td colspan="4" class="empty">还没有问答记录，去「智能问答」提个问题吧</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
