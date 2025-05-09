package org.example.event;

import org.example.event.Event;

public class OrderCancelled implements Event {
    public String orderId;

    public OrderCancelled(String orderId) {
        this.orderId = orderId;
    }

}