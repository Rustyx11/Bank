package com.example.demo1;

public class Credit {
    private static final long serialVersionUID = 37832421247155148L;
    private int id;
    private int userId;
    private Float age;
    private String pesel;
    private Float cash;
    private int month;
    private int children;
    private int workContractMonth;
    private Float AvgPayout;
    private int anotherCreditsInt;
    private int listOfDebtorInt;
    private int workContractInt;
    private int accepted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Float getAge() {
        return age;
    }

    public void setAge(Float age) {
        this.age = age;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public Float getCash() {
        return cash;
    }

    public void setCash(Float cash) {
        this.cash = cash;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getWorkContractMonth() {
        return workContractMonth;
    }

    public void setWorkContractMonth(int workContractMonth) {
        this.workContractMonth = workContractMonth;
    }

    public Float getAvgPayout() {
        return AvgPayout;
    }

    public void setAvgPayout(Float avgPayout) {
        AvgPayout = avgPayout;
    }

    public int getAnotherCreditsInt() {
        return anotherCreditsInt;
    }

    public void setAnotherCreditsInt(int anotherCreditsInt) {
        this.anotherCreditsInt = anotherCreditsInt;
    }

    public int getListOfDebtorInt() {
        return listOfDebtorInt;
    }

    public void setListOfDebtorInt(int listOfDebtorInt) {
        this.listOfDebtorInt = listOfDebtorInt;
    }

    public int getWorkContractInt() {
        return workContractInt;
    }

    public void setWorkContractInt(int workContractInt) {
        this.workContractInt = workContractInt;
    }

    public int getWaccepted() {
        return accepted;
    }

    public void setaccepted(int accepted) {
        this.accepted = accepted;
    }
}
