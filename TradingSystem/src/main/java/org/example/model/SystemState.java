package org.example.model;

public class SystemState {
    public final AccountManager accountManager = new AccountManager();
    public final OrderBook orderBook = new OrderBook();

    public AccountManager getAccountManager() {

        return accountManager;
    }

    public OrderBook getOrderBook() {

        return orderBook;
    }

}
