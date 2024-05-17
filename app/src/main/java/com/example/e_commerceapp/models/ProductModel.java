package com.example.e_commerceapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class ProductModel implements Parcelable {
    String productName, productDescription, productCategory, productImage, productId;
    Float productPrice;

    public ProductModel(String productName, String productDescription, String productCategory, String productImage, Float productPrice, String productId) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productId = productId;
    }

    //Making the class Parcelable so that we can send an entire product object to the next screen on item click instead of sending each product variable independently.

    protected ProductModel(Parcel in) {
        productName = in.readString();
        productDescription = in.readString();
        productCategory = in.readString();
        productImage = in.readString();
        if (in.readByte() == 0) {
            productPrice = null;
        } else {
            productPrice = in.readFloat();
        }
        productId = in.readString();
    }

    public static final Parcelable.Creator<ProductModel> CREATOR = new Parcelable.Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel source) {
            return new ProductModel(source);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public ProductModel() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(productDescription);
        dest.writeString(productCategory);
        dest.writeString(productImage);
        if (productPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(productPrice);
        }
        dest.writeString(productId);
    }
}
