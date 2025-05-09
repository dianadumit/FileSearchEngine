package org.example.event;

import org.example.event.Event;

public class FundsDebited implements Event {
    public String userId;
    public double amount;

    public FundsDebited(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
