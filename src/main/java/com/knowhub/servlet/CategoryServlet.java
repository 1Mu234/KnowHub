package com.knowhub.servlet;

import com.knowhub.entity.Category;
import com.knowhub.service.CategoryService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 分类增删改查。 */
@WebServlet("/category")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService(DaoFactory.getCategoryDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            String id = req.getParameter("id");
            Category c = id == null ? null : categoryService.getById(Integer.parseInt(id));
            req.setAttribute("cat", c);
        }
        req.setAttribute("categoryList", categoryService.list());
        req.getRequestDispatcher("/category/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String name = req.getParameter("name");
        String desc = req.getParameter("description");
        String msg = null;

        switch (action == null ? "list" : action) {
            case "add":
                if (!categoryService.add(name, desc)) msg = "新增失败（可能分类名已存在）";
                break;
            case "update":
                String idStr = req.getParameter("id");
                if (idStr == null || !categoryService.update(Integer.parseInt(idStr), name, desc)) {
                    msg = "修改失败";
                }
                break;
            case "delete":
                String delId = req.getParameter("id");
                if (delId == null || !categoryService.delete(Integer.parseInt(delId))) {
                    msg = "删除失败";
                }
                break;
            default:
                break;
        }
        if (msg != null) req.setAttribute("msg", msg);
        req.setAttribute("categoryList", categoryService.list());
        req.getRequestDispatcher("/category/list.jsp").forward(req, resp);
    }
}
