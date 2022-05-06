package com.example.demo1;

public class Account {
    private static final long serialVersionUID = 458912321247155148L;
    private int id;
    private int id_currenct;
    private int id_user;
    private int id_card;
    private float current_cash;
    private String accout_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_currenct() {
        return id_currenct;
    }

    public void setId_currenct(int id_currenct) {
        this.id_currenct = id_currenct;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_card() {
        return id_card;
    }

    public void setId_card(int id_card) {
        this.id_card = id_card;
    }

    public float getCurrent_cash() {
        return current_cash;
    }

    public void setCurrent_cash(float current_cash) {
        this.current_cash = current_cash;
    }

    public String getAccout_number() {
        return accout_number;
    }

    public void setAccout_number(String accout_number) {
        this.accout_number = accout_number;
    }
}
