package com.example.demo1;


import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 3789909326487155148L;
    private int id;
    private String username;
    private String lastName;
    private String firstName;
    private String password;


    private Boolean admin_permission;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin_permission() {
        return admin_permission;
    }

    public void setAdmin_permission(Boolean admin_permission) {
        this.admin_permission = admin_permission;
    }


}
