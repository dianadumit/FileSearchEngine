package org.example;

import org.example.command.CommandHandler;
import org.example.event.Event;
import org.example.model.AccountManager;
import org.example.model.OrderBook;
import org.example.store.EventStore;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EventStore store = new EventStore();
        CommandHandler handler = new CommandHandler(store);

        handler.creditFunds("user1", 100);
        handler.withdrawFunds("user1", 40);
        handler.placeOrder("order1", "user1", "buy", 10, 5);
        //handler.cancelOrder("order1");

        List<Event> events = store.getAllEvents();
        AccountManager accountManager = new AccountManager();
        accountManager.replay(events);

        OrderBook orderBook = new OrderBook();
        orderBook.replay(events);

        System.out.println("Balance of user1: " + accountManager.getBalance("user1"));
        System.out.println("Order1 is active? " + (orderBook.getOrder("order1") != null));
        System.out.println("Order1 is cancelled? " + orderBook.isCancelled("order1"));
    }
}