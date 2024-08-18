package com.example.rac.notelist.arb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountBalancesResponse {
    private String id;
    private String currency;
    private String type;
    private double balance;
    private double available;
    private double holds;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getHolds() {
        return holds;
    }

    public void setHolds(double holds) {
        this.holds = holds;
    }
}