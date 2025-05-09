package org.example.event;

import org.example.event.Event;

public class TradeExecuted implements Event {

    public String buyOrderId;
    public String sellOrderId;
    public int quantity;
    public double price;

    public TradeExecuted(String buyOrderId, String sellOrderId, int quantity, double price) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.quantity = quantity;
        this.price = price;
    }
}
