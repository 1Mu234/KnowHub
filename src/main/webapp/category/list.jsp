<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="category" scope="request"/>
<c:set var="pageTitle" value="分类管理" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="msg">${msg}</div>

<!-- 新增 -->
<div class="card">
    <h3>➕ 新增分类</h3>
    <form action="${pageContext.request.contextPath}/category" method="post"
          style="display:flex;gap:10px;flex-wrap:wrap;align-items:center">
        <input type="hidden" name="action" value="add">
        <input type="text" name="name" placeholder="分类名称" required style="width:200px">
        <input type="text" name="description" placeholder="描述（可选）" style="flex:1;min-width:220px">
        <button class="btn btn-primary" type="submit">新增</button>
    </form>
</div>

<!-- 编辑 -->
<c:if test="${cat != null}">
<div class="card" style="border-color:var(--primary)">
    <h3>✏️ 编辑分类 #${cat.id}</h3>
    <form action="${pageContext.request.contextPath}/category" method="post"
          style="display:flex;gap:10px;flex-wrap:wrap;align-items:center">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${cat.id}">
        <input type="text" name="name" value="${cat.name}" required style="width:200px">
        <input type="text" name="description" value="${cat.description}" style="flex:1;min-width:220px">
        <button class="btn btn-primary" type="submit">保存</button>
        <a class="btn btn-ghost" href="${pageContext.request.contextPath}/category">取消</a>
    </form>
</div>
</c:if>

<!-- 列表 -->
<div class="card">
    <h3>🗂️ 分类列表</h3>
    <table>
        <thead><tr><th style="width:60px">ID</th><th>名称</th><th>描述</th><th style="width:160px">操作</th></tr></thead>
        <tbody>
        <c:forEach items="${categoryList}" var="c">
            <tr>
                <td>${c.id}</td>
                <td><b>${c.name}</b></td>
                <td style="color:var(--muted)">${c.description}</td>
                <td>
                    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/category?action=edit&id=${c.id}">编辑</a>
                    <form style="display:inline" action="${pageContext.request.contextPath}/category" method="post"
                          onsubmit="return confirm('确认删除该分类？')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${c.id}">
                        <button class="btn btn-danger" type="submit">删除</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty categoryList}">
            <tr><td colspan="4" class="empty">暂无分类，先在上方新增一个</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
