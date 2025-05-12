package org.example.event;

import org.example.model.SystemState;

public class OrderCancelled implements Event {
    public String orderId;

    public OrderCancelled(String orderId) {

        this.orderId = orderId;
    }

    @Override
    public void replay(SystemState state) {

        state.getOrderBook().cancelOrder(orderId);
    }
}