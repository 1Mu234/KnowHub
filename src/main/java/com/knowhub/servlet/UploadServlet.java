package com.knowhub.servlet;

import com.knowhub.entity.Document;
import com.knowhub.service.CategoryService;
import com.knowhub.service.DocumentService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/** 文件上传 Servlet。 */
@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 2 * 1024 * 1024,
        maxFileSize = 20 * 1024 * 1024,
        maxRequestSize = 50 * 1024 * 1024
)
public class UploadServlet extends HttpServlet {

    private final DocumentService documentService = new DocumentService(
            DaoFactory.getDocumentDAO(), DaoFactory.getCategoryDAO());
    private final CategoryService categoryService = new CategoryService(DaoFactory.getCategoryDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<com.knowhub.entity.Category> cats = categoryService.list();
        req.setAttribute("categoryList", cats);
        req.getRequestDispatcher("/document/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String title = req.getParameter("title");
        String categoryIdStr = req.getParameter("categoryId");
        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.isBlank()) {
            try { categoryId = Integer.parseInt(categoryIdStr); } catch (NumberFormatException ignored) {}
        }

        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            req.setAttribute("msg", "请选择文件");
            req.setAttribute("categoryList", categoryService.list());
            req.getRequestDispatcher("/document/upload.jsp").forward(req, resp);
            return;
        }

        String fileName = filePart.getSubmittedFileName();
        if (fileName == null) {
            String cd = filePart.getHeader("Content-Disposition");
            if (cd != null) {
                for (String seg : cd.split(";")) {
                    seg = seg.trim();
                    if (seg.startsWith("filename=\"")) {
                        fileName = seg.substring(10, seg.length() - 1);
                        break;
                    }
                }
            }
        }

        try (InputStream is = filePart.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            Document doc = documentService.upload(fileName, bytes, title, categoryId);
            System.out.println("[UploadServlet] 上传成功: id=" + doc.getId() + ", path=" + doc.getFilePath()
                    + ", impl=" + DaoFactory.isJdbcMode());
        } catch (IllegalArgumentException e) {
            req.setAttribute("msg", e.getMessage());
            req.setAttribute("categoryList", categoryService.list());
            req.getRequestDispatcher("/document/upload.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/document");
    }
}
