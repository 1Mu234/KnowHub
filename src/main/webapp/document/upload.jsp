<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="menu" value="upload" scope="request"/>
<c:set var="pageTitle" value="文档上传" scope="request"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="card" style="max-width:680px">
    <h3>📤 上传新文档</h3>
    <div class="msg">${msg}</div>
    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        <div class="form-item">
            <label>文档标题</label>
            <input type="text" name="title" placeholder="不填则默认使用文件名">
        </div>
        <div class="form-item">
            <label>所属分类</label>
            <select name="categoryId" required>
                <option value="">请选择分类</option>
                <c:forEach items="${categoryList}" var="c">
                    <option value="${c.id}">${c.name}</option>
                </c:forEach>
            </select>
            <c:if test="${empty categoryList}">
                <div class="hint">⚠️ 还没有分类，请先到「分类管理」新增</div>
            </c:if>
        </div>
        <div class="form-item">
            <label>选择文件</label>
            <input type="file" name="file" accept=".pdf,.docx" required>
            <div class="hint">仅支持 PDF / DOCX 格式，单文件最大 20MB；上传后自动提取文本并建立搜索索引</div>
        </div>
        <button class="btn btn-primary" type="submit" style="padding:10px 28px">开始上传</button>
    </form>
</div>

<%@ include file="/WEB-INF/jspf/foot.jspf" %>
