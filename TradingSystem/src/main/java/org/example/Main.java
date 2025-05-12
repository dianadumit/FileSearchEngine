package org.example;

import org.example.command.CommandHandler;
import org.example.model.SystemState;
import org.example.store.EventStore;

public class Main {
    public static void main(String[] args) {
        EventStore store = new EventStore();
        SystemState state = new SystemState();
        CommandHandler handler = new CommandHandler(store, state);

        handler.creditFunds("user1", 200);
        handler.creditFunds("user2", 100);
        handler.withdrawFunds("user1", 50);

        handler.placeOrder("buy1", "user1", "buy", 5, 10);
        handler.placeOrder("sell1", "user2", "sell", 5, 10);

        handler.executeTrade("buy1", "sell1", "user2", 5, 10);

        handler.placeOrder("buy2", "user1", "buy", 5, 10);
        handler.cancelOrder("buy2");

        System.out.println("User1 balance: " + state.getAccountManager().getBalance("user1"));
        System.out.println("User2 balance: " + state.getAccountManager().getBalance("user2"));

        System.out.println("Is buy1 active? " + (state.getOrderBook().getOrder("buy1") != null));
        System.out.println("Is buy1 executed? " + state.getOrderBook().isExecuted("buy1"));
        System.out.println("Is sell1 executed? " + state.getOrderBook().isExecuted("sell1"));
        System.out.println("Is buy2 cancelled? " + state.getOrderBook().isCancelled("buy2"));
    }
}
