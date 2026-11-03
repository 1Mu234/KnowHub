package com.knowhub.servlet;

import com.knowhub.service.UserService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 注册（对外开放，注册后为 pending 状态，需管理员审批）。 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService(DaoFactory.getUserDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        String err = userService.register(username, password, nickname);
        if (err == null) {
            req.setAttribute("msg", "注册成功，请等待管理员审批后使用完整功能");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", err);
            req.setAttribute("username", username);
            req.setAttribute("nickname", nickname);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
