package com.example.demo1;

import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 378912321247155148L;
    private int id;
    private int cardProducentId;
    private String number;
    private int validYear;
    private int validMonth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardProducentId() {
        return cardProducentId;
    }

    public void setCardProducentId(int cardProducentId) {
        this.cardProducentId = cardProducentId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getValidYear() {
        return validYear;
    }

    public void setValidYear(int validYear) {
        this.validYear = validYear;
    }

    public int getValidMonth() {
        return validMonth;
    }

    public void setValidMonth(int validMonth) {
        this.validMonth = validMonth;
    }
}
