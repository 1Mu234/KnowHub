<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>注册 - KnowHub</title>
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
        .error{color:#ef4444;font-size:13px;margin-bottom:14px;min-height:18px;text-align:center}
        .link{margin-top:18px;font-size:13px;color:#64748b;text-align:center}
        .link a{color:#4f46e5;text-decoration:none}
    </style>
</head>
<body>
    <div class="card">
        <div class="logo">📚</div>
        <h1>注册账号</h1>
        <div class="sub">注册后需管理员审批，方可使用完整功能</div>
        <div class="error">${error}</div>
        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-item">
                <label>账号</label>
                <input type="text" name="username" value="${username}" placeholder="至少 3 个字符" required autofocus>
            </div>
            <div class="form-item">
                <label>昵称</label>
                <input type="text" name="nickname" value="${nickname}" placeholder="不填则与账号相同">
            </div>
            <div class="form-item">
                <label>密码</label>
                <input type="password" name="password" placeholder="请输入密码" required>
            </div>
            <button type="submit">注 册</button>
        </form>
        <div class="link">已有账号？<a href="${pageContext.request.contextPath}/login.jsp">返回登录</a></div>
    </div>
</body>
</html>
