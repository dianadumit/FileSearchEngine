package org.example.command;

import org.example.event.*;
import org.example.model.SystemState;
import org.example.store.EventStore;

public class CommandHandler {
    private final EventStore eventStore;
    public final SystemState systemState;

    public CommandHandler(EventStore eventStore, SystemState systemState) {
        this.eventStore = eventStore;
        this.systemState = systemState;
    }

    public void placeOrder(String orderId, String userId, String type, int quantity, double price) {
        if (type.equalsIgnoreCase("buy")) {
            double totalCost = quantity * price;
            double balance = systemState.getAccountManager().getBalance(userId);
            if (balance < totalCost) {
                throw new IllegalArgumentException("Insufficient funds for placing this order");
            }
            FundsDebited fundsDebited = new FundsDebited(userId, totalCost);
            eventStore.append(fundsDebited);
            fundsDebited.replay(systemState);
        }
        OrderPlaced orderPlaced = new OrderPlaced(orderId, userId, type, quantity, price);
        eventStore.append(orderPlaced);
        orderPlaced.replay(systemState);
    }

    public void cancelOrder(String orderId) {
        OrderPlaced originalOrder = systemState.getOrderBook().getOrder(orderId);
        if (originalOrder == null || systemState.getOrderBook().isCancelled(orderId)) {
            throw new IllegalArgumentException("Order not found or already cancelled");
        }
        OrderCancelled orderCancelled = new OrderCancelled(orderId);
        eventStore.append(orderCancelled);
        orderCancelled.replay(systemState);

        if (originalOrder.type.equalsIgnoreCase("buy")) {
            double refundAmount = originalOrder.quantity * originalOrder.price;
            FundsCredited fundsCredited = new FundsCredited(originalOrder.userId, refundAmount);
            eventStore.append(fundsCredited);
            fundsCredited.replay(systemState);
        }
    }

    public void creditFunds(String userId, double amount) {
        FundsCredited fundsCredited = new FundsCredited(userId, amount);
        eventStore.append(fundsCredited);
        fundsCredited.replay(systemState);
    }

    public void executeTrade(String buyOrderId, String sellOrderId, String sellUserId, int quantity, double price) {
        TradeExecuted tradeExecuted = new TradeExecuted(buyOrderId, sellOrderId, sellUserId, quantity, price);
        eventStore.append(tradeExecuted);
        tradeExecuted.replay(systemState);
    }

    public void withdrawFunds(String userId, double amount) {
        if (amount > systemState.getAccountManager().getBalance(userId)) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
        FundsDebited fundsDebited = new FundsDebited(userId, amount);
        eventStore.append(fundsDebited);
        fundsDebited.replay(systemState);
    }

}
