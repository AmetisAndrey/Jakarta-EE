package model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shop_order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrderEntity order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 120)
    private String productName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int qty;

    protected OrderItemEntity() { }

    public OrderItemEntity(Long productId, String productName, BigDecimal price, int qty) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.qty = qty;
    }

    void setOrder(OrderEntity order) { this.order = order; }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getPrice() { return price; }
    public int getQty() { return qty; }
}
