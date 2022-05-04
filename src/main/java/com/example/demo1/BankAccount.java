package com.example.demo1;

import java.io.Serializable;

public class BankAccount implements Serializable {
    private static final long serialVersionUID = 3789909321247155148L;
    private int id;
    private int currencyId;
    private String currencyShort;
    private String currencyLong;
    private float currentCash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyShort() {
        return currencyShort;
    }

    public void setCurrencyShort(String currencyShort) {
        this.currencyShort = currencyShort;
    }

    public String getCurrencyLong() {
        return currencyLong;
    }

    public void setCurrencyLong(String currencyLong) {
        this.currencyLong = currencyLong;
    }

    public float getCurrentCash() {
        return currentCash;
    }

    public void setCurrentCash(float currentCash) {
        this.currentCash = currentCash;
    }
}
