package listener;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.Product;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class AppInitListener implements ServletContextListener {

    public static final String CATALOG_ATTR = "catalog";
    public static final String EMF_ATTR = "emf";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        Map<Long, Product> catalog = new ConcurrentHashMap<>();
        catalog.put(1L, new Product(1L, "Кофе", new BigDecimal("4.50"), 5));
        catalog.put(2L, new Product(2L, "Чай", new BigDecimal("3.20"), 10));
        catalog.put(3L, new Product(3L, "Шоколад", new BigDecimal("2.10"), 8));
        catalog.put(4L, new Product(4L, "Печенье", new BigDecimal("1.80"), 12));
        catalog.put(5L, new Product(5L, "Сок", new BigDecimal("2.90"), 6));
        ctx.setAttribute(CATALOG_ATTR, catalog);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("miniShopPU");
        ctx.setAttribute(EMF_ATTR, emf);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Object obj = sce.getServletContext().getAttribute(EMF_ATTR);
        if (obj instanceof EntityManagerFactory emf) {
            emf.close();
        }
    }
}
