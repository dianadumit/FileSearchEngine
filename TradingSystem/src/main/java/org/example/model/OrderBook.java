package org.example.model;

import org.example.event.OrderPlaced;

import java.util.*;

public class OrderBook {
    private final Map<String, OrderPlaced> activeOrders = new HashMap<>();
    private final Set<String> cancelledOrders = new HashSet<>();
    private final Set<String> executedOrders = new HashSet<>();

    public OrderPlaced getOrder(String orderId) {

        return activeOrders.get(orderId);
    }

    public boolean isCancelled(String orderId) {

        return cancelledOrders.contains(orderId);
    }

    public boolean isExecuted(String orderId) {

        return executedOrders.contains(orderId);
    }

    public void addOrder(OrderPlaced order) {

        activeOrders.put(order.orderId, order);
    }

    public void cancelOrder(String orderId) {
        cancelledOrders.add(orderId);
        activeOrders.remove(orderId);
    }

    public void markAsTraded(String orderId) {
        executedOrders.add(orderId);
        activeOrders.remove(orderId);
    }

    public void reset() {
        activeOrders.clear();
        cancelledOrders.clear();
        executedOrders.clear();
    }

}
