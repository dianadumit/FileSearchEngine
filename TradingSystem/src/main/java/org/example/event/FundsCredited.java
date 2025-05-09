package org.example.event;

public class FundsCredited implements Event{
    public String userId;
    public double amount;

    public FundsCredited(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
