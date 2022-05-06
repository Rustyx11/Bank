package com.example.demo1;

import java.util.Date;

public class Transfer {
    private static final long serialVersionUID = 62912321247155148L;
    private int id;
    private int id_user_form;
    private Date date;
    private Float cash;
    private String accout_number_from;
    private String accout_number_to;
    private String type;
    private String title;
    private String recpient;
    private Float cash_incoming;
    private String currency_short;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user_form() {
        return id_user_form;
    }

    public void setId_user_form(int id_user_form) {
        this.id_user_form = id_user_form;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getCash() {
        return cash;
    }

    public void setCash(Float cash) {
        this.cash = cash;
    }

    public String getAccout_number_from() {
        return accout_number_from;
    }

    public void setAccout_number_from(String accout_number_from) {
        this.accout_number_from = accout_number_from;
    }

    public String getAccout_number_to() {
        return accout_number_to;
    }

    public void setAccout_number_to(String accout_number_to) {
        this.accout_number_to = accout_number_to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecpient() {
        return recpient;
    }

    public void setRecpient(String recpient) {
        this.recpient = recpient;
    }

    public Float getCash_incoming() {
        return cash_incoming;
    }

    public void setCash_incoming(Float cash_incoming) {
        this.cash_incoming = cash_incoming;
    }

    public String getCurrency_short() {
        return currency_short;
    }

    public void setCurrency_short(String currency_short) {
        this.currency_short = currency_short;
    }


}
