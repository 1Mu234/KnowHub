package com.knowhub.servlet;

import com.knowhub.entity.User;
import com.knowhub.service.UserService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** 用户自定义大模型 API 配置。 */
@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {

    private final UserService userService = new UserService(DaoFactory.getUserDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/settings.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String apiKey = req.getParameter("apiKey");
        String baseUrl = req.getParameter("baseUrl");
        String model = req.getParameter("model");
        String action = req.getParameter("action");

        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loginUser");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // 清空配置
        if ("clear".equals(action)) {
            userService.saveAiConfig(u.getId(), "", "", "");
            u.setApiKey(null);
            u.setAiBaseUrl(null);
            u.setAiModel(null);
            req.setAttribute("msg", "已清空自定义配置，将使用系统默认配置");
            req.getRequestDispatcher("/settings.jsp").forward(req, resp);
            return;
        }

        boolean ok = userService.saveAiConfig(u.getId(), apiKey, baseUrl, model);
        if (ok) {
            // 同步更新 session 中的用户对象，问答立即生效
            u.setApiKey(apiKey == null || apiKey.isBlank() ? null : apiKey.trim());
            u.setAiBaseUrl(baseUrl == null || baseUrl.isBlank() ? null : baseUrl.trim());
            u.setAiModel(model == null || model.isBlank() ? null : model.trim());
            req.setAttribute("msg", "配置已保存，问答功能将使用你的配置");
        } else {
            req.setAttribute("msg", "保存失败");
        }
        req.getRequestDispatcher("/settings.jsp").forward(req, resp);
    }
}
