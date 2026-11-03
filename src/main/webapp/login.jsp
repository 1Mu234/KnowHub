<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%
    if (session.getAttribute("loginUser") != null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>登录 - KnowHub</title>
    <style>
        *{box-sizing:border-box;margin:0;padding:0}
        body{font-family:"Segoe UI","PingFang SC","Microsoft YaHei",sans-serif;min-height:100vh;display:flex;align-items:center;justify-content:center;
             background:linear-gradient(135deg,#4f46e5 0%,#7c3aed 50%,#2563eb 100%)}
        .card{background:#fff;padding:44px 40px;border-radius:16px;box-shadow:0 20px 60px rgba(0,0,0,.25);width:380px}
        .logo{text-align:center;margin-bottom:8px;font-size:32px}
        h1{text-align:center;color:#1e293b;font-size:22px;margin-bottom:4px}
        .sub{text-align:center;color:#94a3b8;font-size:13px;margin-bottom:26px}
        .form-item{margin-bottom:16px}
        label{display:block;margin-bottom:6px;font-size:13px;font-weight:500;color:#475569}
        input{width:100%;padding:11px 14px;border:1px solid #e2e8f0;border-radius:8px;font-size:14px;transition:.15s}
        input:focus{outline:none;border-color:#4f46e5;box-shadow:0 0 0 3px rgba(79,70,229,.12)}
        button{width:100%;padding:12px;background:#4f46e5;border:0;color:#fff;border-radius:8px;font-size:15px;font-weight:600;cursor:pointer;transition:.15s;margin-top:6px}
        button:hover{background:#4338ca}
        .msg{color:#ef4444;font-size:13px;margin-bottom:14px;min-height:18px;text-align:center}
        .hint{margin-top:20px;font-size:12px;color:#94a3b8;text-align:center}
    </style>
</head>
<body>
    <div class="card">
        <div class="logo">📚</div>
        <h1>KnowHub</h1>
        <div class="sub">智能知识库管理系统</div>
        <div class="msg">${msg}</div>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-item">
                <label>账号</label>
                <input type="text" name="username" value="${username}" placeholder="请输入账号" required autofocus>
            </div>
            <div class="form-item">
                <label>密码</label>
                <input type="password" name="password" placeholder="请输入密码" required>
            </div>
            <button type="submit">登 录</button>
        </form>
        <div class="hint">演示账号：admin / 123456 &nbsp;|&nbsp; user / 123456</div>
        <div class="hint" style="margin-top:10px">没有账号？<a href="${pageContext.request.contextPath}/register" style="color:#4f46e5;text-decoration:none">立即注册</a></div>
    </div>
</body>
</html>
