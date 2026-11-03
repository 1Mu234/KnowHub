package com.knowhub.servlet;

import com.knowhub.entity.User;
import com.knowhub.service.UserService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 用户审批管理（仅管理员，/admin/** 由 LoginFilter 拦截保护）。 */
@WebServlet("/admin/user")
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService(DaoFactory.getUserDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("userList", userService.listAll());
        req.getRequestDispatcher("/admin/user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Integer id = null;
        if (idStr != null) {
            try { id = Integer.parseInt(idStr); } catch (NumberFormatException ignored) {}
        }

        User current = (User) req.getSession().getAttribute("loginUser");
        String msg = null;
        if (id == null) {
            msg = "参数错误";
        } else if ("approve".equals(action)) {
            if (!userService.approve(id)) msg = "审批失败";
        } else if ("reject".equals(action)) {
            if (!userService.reject(id)) msg = "操作失败";
        } else if ("delete".equals(action)) {
            if (id.equals(current.getId())) {
                msg = "不能删除自己";
            } else if (!userService.delete(id)) {
                msg = "删除失败";
            }
        }

        if (msg != null) req.setAttribute("msg", msg);
        req.setAttribute("userList", userService.listAll());
        req.getRequestDispatcher("/admin/user.jsp").forward(req, resp);
    }
}
