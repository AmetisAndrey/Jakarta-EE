package listener;

import model.Product;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@WebListener
public class AppInitListener implements ServletContextListener {

    public static final String CATALOG_ATTR = "catalog";

    @Override
    public void contextInitialized(ServletContextEvent sce){
        Map<Long, Product> catalog = new ConcurrentHashMap<>();
        catalog.put(1L, new Product(1L, "Кофе арабика", new BigDecimal("7.90"), 5));
        catalog.put(2L, new Product(2L, "Чай зеленый", new BigDecimal("4.50"), 3));
        catalog.put(3L, new Product(3L, "Печенье овсяное", new BigDecimal("2.20"), 10));
        catalog.put(4L, new Product(4L, "Шоколад", new BigDecimal("1.80"), 0));
        catalog.put(5L, new Product(5L, "Молоко 1л", new BigDecimal("1.35"), 7));

        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute(CATALOG_ATTR, catalog);
    }
}
