package com.knowhub.servlet;

import com.knowhub.entity.QaRecord;
import com.knowhub.entity.User;
import com.knowhub.service.QaService;
import com.knowhub.service.SearchService;
import com.knowhub.util.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/** 智能问答。 */
@WebServlet("/qa")
public class QaServlet extends HttpServlet {

    private final QaService qaService = new QaService(
            DaoFactory.getQaRecordDAO(),
            new SearchService(DaoFactory.getDocumentDAO()));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String view = req.getParameter("view");
        if ("history".equals(view)) {
            HttpSession s = req.getSession(false);
            User u = s == null ? null : (User) s.getAttribute("loginUser");
            List<QaRecord> list = qaService.history(u == null ? null : u.getId());
            req.setAttribute("recordList", list);
            req.getRequestDispatcher("/qa/history.jsp").forward(req, resp);
            return;
        }
        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loginUser");
        List<QaRecord> history = qaService.history(u == null ? null : u.getId());
        req.setAttribute("history", history);
        req.getRequestDispatcher("/qa.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String question = req.getParameter("question");

        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("loginUser");

        QaRecord record;
        try {
            record = qaService.ask(u == null ? null : u.getId(), question,
                    u == null ? null : u.getApiKey(),
                    u == null ? null : u.getAiBaseUrl(),
                    u == null ? null : u.getAiModel());
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("question", question);
            doGet(req, resp);
            return;
        }

        req.setAttribute("latestAnswer", record);
        List<QaRecord> history = qaService.history(u == null ? null : u.getId());
        req.setAttribute("history", history);
        req.getRequestDispatcher("/qa.jsp").forward(req, resp);
    }
}
