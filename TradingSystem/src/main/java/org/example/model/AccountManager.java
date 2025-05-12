package org.example.model;

import org.example.event.TradeExecuted;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private final Map<String, Double> balances = new HashMap<>();

    public double getBalance(String userId) {
        return balances.getOrDefault(userId, 0.0);
    }

    public void deposit(String userId, double amount) {
        balances.put(userId, balances.getOrDefault(userId, 0.0) + amount);
    }

    public void withdraw(String userId, double amount) {
        balances.put(userId, balances.getOrDefault(userId, 0.0) - amount);
    }

    public void processTrade(TradeExecuted trade) {
        double total = trade.quantity * trade.price;
        deposit(trade.sellerUserId, total);
    }

    public void reset() {
        balances.clear();
    }
}
