package model;

import java.math.BigDecimal;

public class CartItem {
    private final long productId;
    private final String name;
    private final BigDecimal price;
    private int quantity;

    public CartItem(long productId, String name, BigDecimal price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public long getProductId() { return productId; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void increment() { this.quantity++; }

    public BigDecimal getLineTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
