package servlets;


import dao.UserDao;
import model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String login = trim(req.getParameter("login"));
        String password = req.getParameter("password");

        if (login.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Введите логин (или email) и пароль.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userDao.authenticate(login, password);
            if (user == null) {
                req.setAttribute("error", "Неверный логин/email или пароль.");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                return;
            }

            req.getSession(true).setAttribute("authUser", user);
            resp.sendRedirect(req.getContextPath() + "/shop");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
