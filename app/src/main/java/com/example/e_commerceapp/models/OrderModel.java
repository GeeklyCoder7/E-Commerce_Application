package com.example.e_commerceapp.models;

public class OrderModel {
    String orderDate = "";
    String orderStatus = "";
    String orderId = "";
    float orderTotal = 0;
    String deliveryAddressId = "";
    String deliveryEstimate = "";

    public OrderModel(String orderDate, String orderStatus, String orderId, int orderTotal, String deliveryAddressId, String deliveryEstimate) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.orderTotal = orderTotal;
        this.deliveryAddressId = deliveryAddressId;
        this.deliveryEstimate = deliveryEstimate;
    }

    public OrderModel() {

    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(String deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public String getDeliveryEstimate() {
        return deliveryEstimate;
    }

    public void setDeliveryEstimate(String deliveryEstimate) {
        this.deliveryEstimate = deliveryEstimate;
    }
}
