<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="menu" value="document" scope="request"/>
<c:set var="pageTitle" value="文档列表" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card">
    <div class="flex-bar">
        <h3 style="margin:0">📋 全部文档</h3>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/upload">+ 上传新文档</a>
    </div>
    <div class="msg">${msg}</div>
    <table>
        <thead>
            <tr>
                <th style="width:50px">ID</th><th>标题</th><th>原始文件名</th><th>分类</th>
                <th style="width:70px">类型</th><th style="width:90px">大小</th><th style="width:170px">上传时间</th><th style="width:90px">操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${documentList}" var="d">
            <tr>
                <td>${d.id}</td>
                <td><b>${d.title}</b></td>
                <td style="color:var(--muted)">${d.fileName}</td>
                <td><span class="badge">${d.categoryName}</span></td>
                <td>${d.contentType}</td>
                <td><fmt:formatNumber value="${d.fileSize / 1024}" pattern="0.0"/> KB</td>
                <td style="color:var(--muted)">${d.uploadTime}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/document" method="post"
                          onsubmit="return confirm('确认删除该文档？')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${d.id}">
                        <button class="btn btn-danger" type="submit">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty documentList}">
            <tr><td colspan="8" class="empty">还没有文档，点击右上角「上传新文档」开始</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
