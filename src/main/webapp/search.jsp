<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="search" scope="request"/>
<c:set var="pageTitle" value="知识库检索" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card">
    <form action="${pageContext.request.contextPath}/search" method="get" style="display:flex;gap:10px">
        <input type="text" name="keyword" value="${keyword}" placeholder="输入关键词，例如：请假制度、产品说明…" autofocus style="flex:1">
        <button class="btn btn-primary" type="submit" style="padding:9px 26px">🔍 搜索</button>
    </form>
</div>

<c:if test="${keyword != null && !keyword.isBlank()}">
<div class="card">
    <div style="font-size:13px;color:var(--muted);margin-bottom:12px">
        关键词「<b style="color:var(--primary)">${keyword}</b>」共命中 ${results.size()} 条结果
    </div>
    <c:choose>
        <c:when test="${empty results}">
            <div class="empty">😕 没有找到相关内容，换个关键词试试？</div>
        </c:when>
        <c:otherwise>
            <c:forEach items="${results}" var="d">
                <div style="border-bottom:1px solid var(--border);padding:14px 0">
                    <div style="margin-bottom:6px">
                        <b style="font-size:15px">${d.title}</b>
                        <span style="color:var(--muted);font-size:12px;margin-left:8px">#${d.id}</span>
                    </div>
                    <div style="font-size:12px;color:var(--muted);margin-bottom:6px">
                        <span class="badge">${d.categoryName}</span> &nbsp;${d.contentType} &nbsp;·&nbsp; ${d.uploadTime}
                    </div>
                    <div style="font-size:13px;color:#475569;line-height:1.75">
                        <c:choose>
                            <c:when test="${snippets[d.id] != null && !snippets[d.id].isEmpty()}">
                                <c:out value="${snippets[d.id]}" escapeXml="false"/>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${d.content != null && d.content.length() > 200}">${d.content.substring(0, 200)}…</c:when>
                                    <c:otherwise>${d.content}</c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
</c:if>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
