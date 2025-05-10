package org.example.model;

import org.example.event.Event;
import org.example.event.OrderCancelled;
import org.example.event.OrderPlaced;
import org.example.event.TradeExecuted;

import java.util.*;

public class OrderBook {
    private final Map<String, OrderPlaced> activeOrders = new HashMap<>();
    private final Set<String> cancelledOrders = new HashSet<>();
    private final Set<String> executedOrders = new HashSet<>();

    public void replay(List<Event> events) {
        for (Event e : events) {
            if (e instanceof OrderPlaced) {
                OrderPlaced orderPlaced = (OrderPlaced) e;
                activeOrders.put(orderPlaced.orderId, orderPlaced);
            } else if (e instanceof OrderCancelled) {
                OrderCancelled orderCancelled = (OrderCancelled) e;
                cancelledOrders.add(orderCancelled.orderId);
                activeOrders.remove(orderCancelled.orderId);
            } else if (e instanceof TradeExecuted) {
                TradeExecuted tradeExecuted = (TradeExecuted) e;
                executedOrders.add(tradeExecuted.buyOrderId);
                executedOrders.add(tradeExecuted.sellOrderId);
                activeOrders.remove(tradeExecuted.buyOrderId);
                activeOrders.remove(tradeExecuted.sellOrderId);
            }
        }
    }

    public OrderPlaced getOrder(String orderId) {
        return activeOrders.get(orderId);
    }

    public boolean isCancelled(String orderId) {
        return cancelledOrders.contains(orderId);
    }

    public boolean isExecuted(String orderId) {
        return executedOrders.contains(orderId);
    }
}
