package com.example.e_commerceapp.models;

public class AddressModel {
    String firstAndLastName, cityName, stateName, areaName, districtName, landmarkName, pinCode, mobileNumber, addressId, addressStatus;

    public AddressModel(String firstAndLastName, String cityName, String stateName, String areaName, String districtName, String landmarkName, String pinCode, String mobileNumber, String addressId, String addressStatus) {
        this.firstAndLastName = firstAndLastName;
        this.cityName = cityName;
        this.stateName = stateName;
        this.areaName = areaName;
        this.districtName = districtName;
        this.landmarkName = landmarkName;
        this.pinCode = pinCode;
        this.mobileNumber = mobileNumber;
        this.addressId = addressId;
        this.addressStatus = addressStatus;
    }

    public AddressModel() {}

    public String getFirstAndLastName() {
        return firstAndLastName;
    }

    public void setFirstAndLastName(String firstAndLastName) {
        this.firstAndLastName = firstAndLastName;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }
}