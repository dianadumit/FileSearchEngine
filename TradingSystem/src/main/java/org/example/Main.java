package org.example;

import org.example.event.OrderPlaced;
import org.example.store.EventStore;

public class Main {
    public static void main(String[] args) {

        EventStore store = new EventStore();
        store.append(new OrderPlaced("order1", "user", "buy", 5, 4));
        System.out.println("Events stored: " + store.getAllEvents().size());

    }
}