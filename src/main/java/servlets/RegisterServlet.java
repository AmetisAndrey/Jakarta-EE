package servlets;

import dao.UserDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");

        if (username.isEmpty() || email.isEmpty() || password == null || password.length() < 6) {
            req.setAttribute("error", "Заполните поля. Пароль минимум 6 символов.");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (userDao.existsByUsername(username)) {
                req.setAttribute("error", "Имя пользователя уже занято.");
                req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
                return;
            }
            if (userDao.existsByEmail(email)) {
                req.setAttribute("error", "Email уже зарегистрирован.");
                req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
                return;
            }

            User user = userDao.create(username, email, password);
            req.getSession(true).setAttribute("authUser", user);

            resp.sendRedirect(req.getContextPath() + "/shop?msg=welcome");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
