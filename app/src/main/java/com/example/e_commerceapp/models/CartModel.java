package com.example.e_commerceapp.models;

public class CartModel extends ProductModel {
    int cartProductQuantity;

    public CartModel(String productName, String productDescription, String productCategory, String productImage, Float productPrice, String productId, int cartProductQuantity) {
        super(productName, productDescription, productCategory, productImage, productPrice, productId);
        this.cartProductQuantity = cartProductQuantity;
    }

    //No argument constructor in order for firebase to work properly
    public CartModel() {

    }

    public int getCartProductQuantity() {
        return cartProductQuantity;
    }

    public void setCartProductQuantity(int cartProductQuantity) {
        this.cartProductQuantity = cartProductQuantity;
    }
}
