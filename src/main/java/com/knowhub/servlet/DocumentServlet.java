package com.knowhub.servlet;

import com.knowhub.service.DocumentService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 文档列表 / 删除。
 *  修复：原来 new DocumentService(new MockDocumentDAO(), null) 传了 null 给 CategoryDAO，
 *  JDBC 模式下会 NPE，现在两个 DAO 都从 DaoFactory 取。 */
@WebServlet("/document")
public class DocumentServlet extends HttpServlet {

    private final DocumentService documentService = new DocumentService(
            DaoFactory.getDocumentDAO(), DaoFactory.getCategoryDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("documentList", documentService.list());
        req.getRequestDispatcher("/document/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String msg = null;
        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr == null || !documentService.delete(Integer.parseInt(idStr))) {
                msg = "删除失败";
            }
        }
        if (msg != null) req.setAttribute("msg", msg);
        req.setAttribute("documentList", documentService.list());
        req.getRequestDispatcher("/document/list.jsp").forward(req, resp);
    }
}
