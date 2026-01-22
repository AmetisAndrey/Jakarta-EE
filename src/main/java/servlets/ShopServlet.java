package servlets;

import dao.OrderRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static listener.AppInitListener.CATALOG_ATTR;
import static listener.AppInitListener.EMF_ATTR;

@WebServlet("/shop")
public class ShopServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    private Map<Long, Product> catalog() {
        return (Map<Long, Product>) getServletContext().getAttribute(CATALOG_ATTR);
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> cart(HttpSession session) {
        Object obj = session.getAttribute("cart");
        if (obj instanceof Map) return (Map<Long, CartItem>) obj;

        Map<Long, CartItem> cart = new LinkedHashMap<>();
        session.setAttribute("cart", cart);
        return cart;
    }

    private OrderRepository orderRepo() {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute(EMF_ATTR);
        return new OrderRepository(emf);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fillCartTotals(req);
        req.getRequestDispatcher("/WEB-INF/jsp/shop.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = nvl(req.getParameter("action"));
        HttpSession session = req.getSession(true);

        switch (action) {
            case "add" -> handleAdd(req, session);
            case "clear" -> handleClear(session);
            case "checkout" -> handleCheckout(req, session);
            default -> req.setAttribute("error", "Неизвестное действие.");
        }

        resp.sendRedirect(req.getContextPath() + "/shop");
    }

    private void handleAdd(HttpServletRequest req, HttpSession session) {
        String idStr = req.getParameter("id");
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (Exception e) {
            session.setAttribute("flashError", "Некорректный id товара.");
            return;
        }

        Product p = catalog().get(id);
        if (p == null) {
            session.setAttribute("flashError", "Товар не найден.");
            return;
        }

        // По ТЗ: если склад 0 — НЕ добавлять
        if (!p.decrementStockIfAvailable()) {
            session.setAttribute("flashError", "Товара нет на складе.");
            return;
        }

        Map<Long, CartItem> cart = cart(session);
        CartItem item = cart.get(id);
        if (item == null) {
            cart.put(id, new CartItem(p.getId(), p.getName(), p.getPrice(), 1));
        } else {
            item.inc();
        }

        session.setAttribute("flashMsg", "Товар добавлен в корзину.");
    }

    private void handleClear(HttpSession session) {
        session.removeAttribute("cart");
        session.setAttribute("flashMsg", "Корзина очищена.");
    }

    private void handleCheckout(HttpServletRequest req, HttpSession session) {
        Object auth = session.getAttribute("authUser");
        if (!(auth instanceof User user)) {
            session.setAttribute("flashError", "Сначала войдите в аккаунт.");
            return;
        }

        Map<Long, CartItem> cart = cart(session);
        if (cart.isEmpty()) {
            session.setAttribute("flashError", "Корзина пуста.");
            return;
        }

        OrderEntity order = new OrderEntity(user.getUsername());
        for (CartItem ci : cart.values()) {
            order.addItem(new OrderItemEntity(
                    ci.getProductId(),
                    ci.getProductName(),
                    ci.getPrice(),
                    ci.getQty()
            ));
        }
        orderRepo().create(order);

        session.removeAttribute("cart");
        session.setAttribute("flashMsg", "Покупка оформлена. Заказ № " + order.getId());
    }

    private void fillCartTotals(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return;

        Object e = session.getAttribute("flashError");
        Object m = session.getAttribute("flashMsg");
        if (e != null) { req.setAttribute("error", e); session.removeAttribute("flashError"); }
        if (m != null) { req.setAttribute("msg", m); session.removeAttribute("flashMsg"); }

        @SuppressWarnings("unchecked")
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return;

        int count = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.values()) {
            count += ci.getQty();
            total = total.add(ci.getLineTotal());
        }
        req.setAttribute("cartCount", count);
        req.setAttribute("cartTotal", total);
    }

    private static String nvl(String s) { return s == null ? "" : s; }
}
