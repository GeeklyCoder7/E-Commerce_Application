package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.adapters.ProductAdapter;
import com.example.e_commerceapp.databinding.ActivityProductDetailsShowingBinding;
import com.example.e_commerceapp.models.CartModel;
import com.example.e_commerceapp.models.CategoryModel;
import com.example.e_commerceapp.utils.ConstantValues;
import com.example.e_commerceapp.models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ProductDetailsShowingActivity extends AppCompatActivity {
    ActivityProductDetailsShowingBinding binding;
    Intent productObjectReceivingIntent;
    ProductModel receivedProductModelObj;
    String productName, productDescription, productCategory, productImage, productId;
    Float productPrice;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference cartReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    ArrayList<ProductModel> productModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsShowingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Receiving the product object from the HomeActivity
        productObjectReceivingIntent = getIntent();

        //Storing values received from intent
        receivedProductModelObj = productObjectReceivingIntent.getParcelableExtra("productModel");
        assert receivedProductModelObj != null;
        productName = receivedProductModelObj.getProductName();
        productDescription = receivedProductModelObj.getProductDescription();
        productCategory = receivedProductModelObj.getProductCategory();
        productImage = receivedProductModelObj.getProductImage();
        productId = receivedProductModelObj.getProductId();
        productPrice = receivedProductModelObj.getProductPrice();

        //Initializing variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        productModelArrayList = new ArrayList<>();

        //Calling necessary functions here
        setUpProductDetails();
        fetchRandomProducts();

        binding.productDetailsAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });
    }

    void setUpProductDetails() {
        Glide.with(ProductDetailsShowingActivity.this).load(productImage).into(binding.productDetailsImageView);
        binding.productDetailsPriceTextView.setText("Price : INR " + productPrice);
        binding.productDetailsDescriptionTextView.setText("" + productDescription);
        binding.productDetailsProductNameTextView.setText("" + productName);
    }

    void fetchRandomProducts() {
        DatabaseReference productsNodeRef = databaseReference.child("products");
        productsNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelArrayList.clear();
                for (DataSnapshot specificProductSnapShot : snapshot.getChildren()) {
                    ProductModel product = specificProductSnapShot.getValue(ProductModel.class);
                    if (product != null && !product.getProductId().equals(productId)) {
                        productModelArrayList.add(product);
                    }
                }
                Collections.shuffle(productModelArrayList);
                setUpProductsSuggestionsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailsShowingActivity.this, "Failed to fetch products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setUpProductsSuggestionsRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(ProductDetailsShowingActivity.this, productModelArrayList, ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_PRODUCT_DETAILS_ACTIVITY);
        binding.detailsScreenProductsSuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailsShowingActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.detailsScreenProductsSuggestionsRecyclerView.setAdapter(productAdapter);
    }

    void addProductToCart() {
        CartModel cartModel = new CartModel(receivedProductModelObj.getProductName(), receivedProductModelObj.getProductDescription(), receivedProductModelObj.getProductCategory(), receivedProductModelObj.getProductImage(), receivedProductModelObj.getProductPrice(), receivedProductModelObj.getProductId(), 1);
        cartReference = databaseReference.child("users").child(currentUser.getUid()).child("cart_items");
        DatabaseReference particularCartItemReference = cartReference.child(cartModel.getProductId());

        particularCartItemReference.setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProductDetailsShowingActivity.this, "Added to cart.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailsShowingActivity.this, "Failed to add to cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}