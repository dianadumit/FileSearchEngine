package org.example.model;

import org.example.event.Event;
import org.example.event.FundsCredited;
import org.example.event.FundsDebited;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManager {
    private final Map<String, Double> balances = new HashMap<>();

    public void replay(List<Event> events) {
        for (Event e : events) {
            if (e instanceof FundsCredited){
                FundsCredited fundsCredited = (FundsCredited) e;
                balances.put(fundsCredited.userId, balances.getOrDefault(fundsCredited.userId, 0.0) + fundsCredited.amount);
            } else if (e instanceof FundsDebited){
                FundsDebited fundsDebited = (FundsDebited) e;
                balances.put(fundsDebited.userId, balances.getOrDefault(fundsDebited.userId, 0.0) - fundsDebited.amount);
            }
        }
    }
    public double getBalance(String userId) {
        return balances.getOrDefault(userId, 0.0);
    }
}
