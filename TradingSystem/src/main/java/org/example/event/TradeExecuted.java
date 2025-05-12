package org.example.event;

import org.example.model.SystemState;

public class TradeExecuted implements Event {

    public String buyOrderId;
    public String sellOrderId;
    public String sellerUserId;
    public int quantity;
    public double price;

    public TradeExecuted(String buyOrderId, String sellOrderId, String sellerUserId, int quantity, double price) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.sellerUserId = sellerUserId;
        this.quantity = quantity;
        this.price = price;
    }

    public void replay(SystemState state) {
        state.getOrderBook().markAsTraded(buyOrderId);
        state.getOrderBook().markAsTraded(sellOrderId);
        state.getAccountManager().processTrade(this);
    }
}
