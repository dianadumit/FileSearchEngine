package org.example;

import org.example.command.CommandHandler;
import org.example.event.OrderPlaced;
import org.example.store.EventStore;

public class Main {
    public static void main(String[] args) {

        EventStore store = new EventStore();
        CommandHandler handler = new CommandHandler(store);
        handler.placeOrder("order1", "user", "buy", 5, 4);
        System.out.println("Events stored: " + store.getAllEvents().size());
        //handler.cancelOrder("order1");
        handler.creditFunds("user", 100.0);
        handler.executeTrade("buy", "sell", 5, 20.0);
        System.out.println("Events stored: " + store.getAllEvents().size());


    }
}