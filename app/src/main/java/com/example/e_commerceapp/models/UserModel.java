package com.example.e_commerceapp.models;

public class UserModel {
    String emailAddress, password, userId;

    public UserModel(String emailAddress, String password, String userId) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.userId = userId;
    }

    public UserModel() {}

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
