package org.example.event;

import org.example.event.Event;

public class OrderPlaced implements Event {
    public String orderId;
    public String userId;
    public String type;
    public int quantity;
    public double price;

    public OrderPlaced(String orderId, String userId, String type, int quantity, double price) {
        this.orderId = orderId;
        this.userId = userId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

}
