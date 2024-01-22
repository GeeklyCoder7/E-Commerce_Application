package com.example.e_commerceapp.models;

public class UserModel {
    String firstAndLastName, emailAddress, password, userId;

    public UserModel(String firstAndLastName, String emailAddress, String password, String userId) {
        this.firstAndLastName = firstAndLastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.userId = userId;
    }

    public String getFirstAndLastName() {
        return firstAndLastName;
    }

    public void setFirstAndLastName(String firstAndLastName) {
        this.firstAndLastName = firstAndLastName;
    }

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

    public static class Constants {
        static final int FLAG_RECYCLER_CALLED_THROUGH_HOME_ACTIVITY = 1;
        static final int FLAG_RECYCLER_CALLED_THROUGH_PRODUCT_DETAILS_ACTIVITY = 2;
    }
}
