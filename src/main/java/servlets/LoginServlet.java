package servlets;

import dao.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

import static listener.AppInitListener.EMF_ATTR;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserRepository repo() {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute(EMF_ATTR);
        return new UserRepository(emf);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");

        User user = username.isEmpty() ? null : repo().findByUsername(username);

        if (user == null || password == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
            req.setAttribute("error", "Неверный логин или пароль.");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        req.getSession(true).setAttribute("authUser", user);
        resp.sendRedirect(req.getContextPath() + "/shop");
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
