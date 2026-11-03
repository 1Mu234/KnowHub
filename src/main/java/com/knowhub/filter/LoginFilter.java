package com.knowhub.filter;

import com.knowhub.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * 登录 + 权限拦截器。
 * <p>规则：
 * <ul>
 *   <li>未登录 → 跳登录页（白名单除外）</li>
 *   <li>/admin/** → 仅 admin 角色</li>
 *   <li>/document、/upload（源文档相关）→ admin 或已审批用户</li>
 *   <li>待审批用户：只能用首页 / 检索 / 问答</li>
 * </ul>
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    private static final String[] EXCLUDES = {
            "/login", "/login.jsp", "/register", "/register.jsp",
            "/static/", "/assets/", "/css/", "/js/", "/images/", "/favicon.ico"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String ctx = req.getContextPath();
        String path = uri.startsWith(ctx) ? uri.substring(ctx.length()) : uri;

        for (String ex : EXCLUDES) {
            if (path.startsWith(ex)) {
                chain.doFilter(request, response);
                return;
            }
        }
        if (path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        Object obj = session == null ? null : session.getAttribute("loginUser");
        if (obj == null) {
            resp.sendRedirect(ctx + "/login.jsp");
            return;
        }
        User user = (User) obj;

        // 管理员专属
        if (path.startsWith("/admin") && !"admin".equals(user.getRole())) {
            deny(req, resp, "无权限：该功能仅管理员可用");
            return;
        }

        // 源文档相关：admin 或已审批用户
        boolean docRelated = path.startsWith("/document") || path.startsWith("/upload");
        if (docRelated && !user.isFullAccess()) {
            deny(req, resp, "账号未审批：暂无权限查看 / 上传源文档，请联系管理员审批");
            return;
        }

        chain.doFilter(request, response);
    }

    private void deny(HttpServletRequest req, HttpServletResponse resp, String msg) throws IOException {
        // 带回提示信息跳首页（session 存一次性消息）
        req.getSession().setAttribute("flashMsg", msg);
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
