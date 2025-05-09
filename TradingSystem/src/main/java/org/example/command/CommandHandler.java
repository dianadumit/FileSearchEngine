package org.example.command;

import org.example.event.*;
import org.example.store.EventStore;

public class CommandHandler {
    private final EventStore eventStore;

    public CommandHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void placeOrder(String orderId, String userId, String type, int quantity, double price){
        if(type.equalsIgnoreCase("buy")){
            double totalCost = quantity * price;
            //add checking if enough funds
            FundsDebited fundsDebited = new FundsDebited(userId, totalCost);
            eventStore.append(fundsDebited);
        }

        OrderPlaced orderPlaced = new OrderPlaced(orderId,userId,type,quantity,price);
        eventStore.append(orderPlaced);
    }

    public void cancelOrder(String orderId){
        OrderCancelled orderCancelled = new OrderCancelled(orderId);
        eventStore.append(orderCancelled);
        // add refund service
    }

    public void creditFunds(String userId, double amount) {
        eventStore.append(new FundsCredited(userId, amount));
    }

    public void executeTrade(String buyOrderId, String sellOrderId, int quantity, double price) {
        eventStore.append(new TradeExecuted(buyOrderId, sellOrderId, quantity, price));
    }

    public void withdrawFunds(String userId, double amount) {
        // check if enough funds
        FundsDebited fundsDebited = new FundsDebited(userId, amount);
        eventStore.append(fundsDebited);
    }

}
