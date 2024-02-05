package com.example.e_commerceapp.models;

public class UserModel {
    String firstAndLastName, emailAddress, password, userId, cityName, stateName, areaName, districtName, landmarkName;
    String pinCode, mobileNumber;

    public UserModel(String firstAndLastName, String emailAddress, String password, String cityName, String stateName, String areaName, String districtName, String landmarkName, String pincode, String mobileNumber, String userId) {
        this.firstAndLastName = firstAndLastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.cityName = cityName;
        this.stateName = stateName;
        this.areaName = areaName;
        this.districtName = districtName;
        this.landmarkName = landmarkName;
        this.pinCode = pincode;
        this.mobileNumber = mobileNumber;
        this.userId = userId;
    }

    //Defining no-argument constructor in order to fetch data from data base in the form of UserModel.java
    public UserModel() {}

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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public static class Constants {
        static final int FLAG_RECYCLER_CALLED_THROUGH_HOME_ACTIVITY = 1;
        static final int FLAG_RECYCLER_CALLED_THROUGH_PRODUCT_DETAILS_ACTIVITY = 2;
    }
}