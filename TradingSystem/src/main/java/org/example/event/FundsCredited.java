package org.example.event;

import org.example.model.SystemState;

public class FundsCredited implements Event {
    public String userId;
    public double amount;

    public FundsCredited(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void replay(SystemState state) {

        state.getAccountManager().deposit(userId, amount);
    }
}
