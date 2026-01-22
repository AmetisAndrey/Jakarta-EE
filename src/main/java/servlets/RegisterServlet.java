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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserRepository repo() {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute(EMF_ATTR);
        return new UserRepository(emf);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");

        if (username.isEmpty() || password == null || password.isBlank()) {
            req.setAttribute("error", "Введите логин и пароль.");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        UserRepository r = repo();
        if (r.findByUsername(username) != null) {
            req.setAttribute("error", "Пользователь уже существует.");
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
            return;
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        r.create(new User(username, hash));

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
