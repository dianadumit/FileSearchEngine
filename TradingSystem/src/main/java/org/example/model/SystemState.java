package org.example.model;

import org.example.event.Event;

import java.util.List;

public class SystemState {
    public final AccountManager accountManager = new AccountManager();
    public final OrderBook orderBook = new OrderBook();

    public void replayAll(List<Event> events) {
        for (Event e : events) {
            e.replay(this);
        }
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
    public void reset() {
        accountManager.reset();
        orderBook.reset();
    }

}
