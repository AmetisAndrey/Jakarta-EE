package servlets;

import listener.AppInitListener;
import model.CartItem;
import model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/shop")
public class MainServlet extends HttpServlet {

    private static final String CART_ATTR = "cart";
    private boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession(false) != null && req.getSession(false).getAttribute("authUser") != null;
    }


    @SuppressWarnings("unchecked")
    private Map<Long, Product> catalog() {
        return (Map<Long, Product>) getServletContext().getAttribute(AppInitListener.CATALOG_ATTR);
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> cart(HttpSession session) {
        Object existing = session.getAttribute(CART_ATTR);
        if (existing instanceof Map<?, ?>) {
            return (Map<Long, CartItem>) existing;
        }
        Map<Long, CartItem> created = new ConcurrentHashMap<>();
        session.setAttribute(CART_ATTR, created);
        return created;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<Long, Product> catalog = catalog();
        Map<Long, CartItem> cart = cart(req.getSession());

        req.setAttribute("catalogItems", catalog.values());
        req.setAttribute("cartItems", cart.values());
        req.setAttribute("cartTotal", calcTotal(cart.values()));

        req.getRequestDispatcher("/WEB-INF/jsp/shop.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }

        switch (action) {
            case "add" -> handleAdd(req, resp);
            case "checkout" -> handleCheckout(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/shop");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/shop?msg=bad_id");
            return;
        }

        Map<Long, Product> catalog = catalog();
        Product p = catalog.get(id);
        if (p == null) {
            resp.sendRedirect(req.getContextPath() + "/shop?msg=not_found");
            return;
        }


        boolean decremented = p.decrementStockIfAvailable();
        if (!decremented) {
            resp.sendRedirect(req.getContextPath() + "/shop?msg=out_of_stock&id=" + id);
            return;
        }

        Map<Long, CartItem> cart = cart(req.getSession());
        cart.compute(id, (k, v) -> {
            if (v == null) return new CartItem(p.getId(), p.getName(), p.getPrice(), 1);
            v.increment();
            return v;
        });

        resp.sendRedirect(req.getContextPath() + "/shop?msg=added");
    }

    private void handleCheckout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Map<Long, CartItem> cart = cart(req.getSession());
        if (cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/shop?msg=cart_empty");
            return;
        }

        BigDecimal total = calcTotal(cart.values());

        cart.clear();

        resp.sendRedirect(req.getContextPath() + "/shop?msg=paid&total=" + total);
    }

    private BigDecimal calcTotal(Collection<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total;
    }
}
