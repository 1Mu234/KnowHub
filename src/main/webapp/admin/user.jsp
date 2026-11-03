<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="admin" scope="request"/>
<c:set var="pageTitle" value="用户审批" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card">
    <h3>👥 用户管理</h3>
    <div class="msg">${msg}</div>
    <table>
        <thead>
            <tr>
                <th style="width:50px">ID</th><th>账号</th><th>昵称</th>
                <th style="width:90px">角色</th><th style="width:110px">状态</th><th style="width:220px">操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${userList}" var="u">
            <tr>
                <td>${u.id}</td>
                <td><b>${u.username}</b></td>
                <td>${u.nickname}</td>
                <td>
                    <c:choose>
                        <c:when test="${u.role == 'admin'}"><span class="badge" style="background:#fef3c7;color:#b45309">管理员</span></c:when>
                        <c:otherwise><span class="badge">用户</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${u.status == 'approved'}"><span class="badge" style="background:#dcfce7;color:#15803d">✓ 已审批</span></c:when>
                        <c:otherwise><span class="badge" style="background:#fee2e2;color:#b91c1c">⏳ 待审批</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${u.role != 'admin'}">
                        <c:if test="${u.status != 'approved'}">
                            <form style="display:inline" action="${pageContext.request.contextPath}/admin/user" method="post">
                                <input type="hidden" name="action" value="approve">
                                <input type="hidden" name="id" value="${u.id}">
                                <button class="btn btn-primary" type="submit">通过审批</button>
                            </form>
                        </c:if>
                        <c:if test="${u.status == 'approved'}">
                            <form style="display:inline" action="${pageContext.request.contextPath}/admin/user" method="post">
                                <input type="hidden" name="action" value="reject">
                                <input type="hidden" name="id" value="${u.id}">
                                <button class="btn btn-ghost" type="submit">收回权限</button>
                            </form>
                        </c:if>
                        <form style="display:inline" action="${pageContext.request.contextPath}/admin/user" method="post"
                              onsubmit="return confirm('确认删除该用户？')">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${u.id}">
                            <button class="btn btn-danger" type="submit">删除</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="hint">说明：待审批用户只能使用首页 / 检索 / 问答；审批通过后可查看和上传源文档。用户重新登录后权限生效。</div>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
