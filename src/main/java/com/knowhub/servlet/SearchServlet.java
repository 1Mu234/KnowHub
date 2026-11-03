package com.knowhub.servlet;

import com.knowhub.entity.Document;
import com.knowhub.service.SearchService;
import com.knowhub.util.DaoFactory;
import com.knowhub.util.SnippetUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 搜索。 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private final SearchService searchService = new SearchService(DaoFactory.getDocumentDAO());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Document> results = searchService.search(keyword, 20);

        // 为每条结果提取「命中关键词的那一句话」
        Map<Integer, String> snippets = new LinkedHashMap<>();
        for (Document d : results) {
            snippets.put(d.getId(), SnippetUtil.extract(d.getContent(), keyword));
        }

        req.setAttribute("keyword", keyword);
        req.setAttribute("results", results);
        req.setAttribute("snippets", snippets);
        req.getRequestDispatcher("/search.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
