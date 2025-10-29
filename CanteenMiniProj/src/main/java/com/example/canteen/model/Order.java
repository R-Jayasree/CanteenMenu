package com.example.canteen.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;

    private String userId;
    private String menuItemId;
    private int quantity;
    private String status; // PENDING, PREPARING, READY, DELIVERED
    private LocalDateTime orderedAt;

    public Order() {}

    public Order(String userId, String menuItemId, int quantity, String status, LocalDateTime orderedAt) {
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
}
