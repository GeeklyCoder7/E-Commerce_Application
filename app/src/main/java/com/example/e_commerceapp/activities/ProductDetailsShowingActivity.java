package com.example.e_commerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.ActivityProductDetailsShowingBinding;
import com.example.e_commerceapp.models.ProductModel;

public class ProductDetailsShowingActivity extends AppCompatActivity {
    ActivityProductDetailsShowingBinding binding;
    Intent productObjectReceivingIntent;
    ProductModel receivedProductModelObj;
    String productName, productDescription, productCategory, productImage, productId;
    Float productPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsShowingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Receiving the product object from the HomeActivity
        productObjectReceivingIntent = getIntent();

        //Initializing variables
        receivedProductModelObj = productObjectReceivingIntent.getParcelableExtra("productModel");
        assert receivedProductModelObj != null;
        productName = receivedProductModelObj.getProductName();
        productDescription = receivedProductModelObj.getProductDescription();
        productCategory = receivedProductModelObj.getProductCategory();
        productImage = receivedProductModelObj.getProductImage();
        productId = receivedProductModelObj.getProductId();
        productPrice = receivedProductModelObj.getProductPrice();

        //Calling necessary functions here
        setUpProductDetails();
    }

    void setUpProductDetails() {
        Glide.with(ProductDetailsShowingActivity.this).load(productImage).into(binding.productDetailsImageView);
        binding.productDetailsPriceTextView.setText("Price : INR " + productPrice);
        binding.productDetailsDescriptionTextView.setText("" + productDescription);
    }
}