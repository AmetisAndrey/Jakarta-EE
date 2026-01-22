package model;

import java.math.BigDecimal;

public class CartItem {
    private final long productId;
    private final String productName;
    private final BigDecimal price;
    private int qty;

    public CartItem(long productId, String productName, BigDecimal price, int qty) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.qty = qty;
    }

    public long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public int getQty() { return qty; }
    public void inc() { qty++; }

    public BigDecimal getLineTotal() {
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
