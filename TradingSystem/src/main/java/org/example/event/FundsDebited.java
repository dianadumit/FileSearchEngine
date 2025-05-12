package org.example.event;

import org.example.model.SystemState;

public class FundsDebited implements Event {
    public String userId;
    public double amount;

    public FundsDebited(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void replay(SystemState state) {
        state.getAccountManager().withdraw(userId, amount);
    }
}
