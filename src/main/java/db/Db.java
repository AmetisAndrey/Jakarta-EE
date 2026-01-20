package db;

import javax.sql.DataSource;
import javax.naming.InitialContext;

public final class Db {
    private Db() {}

    public static DataSource ds() {
        try {
            return (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MiniShopDS");
        } catch (Exception e) {
            throw new IllegalStateException("JNDI DataSource jdbc/MiniShopDS not found", e);
        }
    }
}
