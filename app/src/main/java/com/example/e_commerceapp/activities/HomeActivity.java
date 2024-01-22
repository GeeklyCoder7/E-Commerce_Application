package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.adapters.CategoryAdapter;
import com.example.e_commerceapp.adapters.ProductAdapter;
import com.example.e_commerceapp.databinding.ActivityHomeBinding;
import com.example.e_commerceapp.models.CategoryModel;
import com.example.e_commerceapp.models.ConstantValues;
import com.example.e_commerceapp.models.ProductModel;
import com.example.e_commerceapp.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ArrayList<CategoryModel> categoryModelArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ArrayList<CarouselItem> carouselItemArrayList;
    ArrayList<ProductModel> productModelArrayList;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializing variables
        categoryModelArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        carouselItemArrayList = new ArrayList<>();
        productModelArrayList = new ArrayList<>();

        //Calling necessary functions here
        setStatusBarColor();
        fetchCategories();
        populateCarousel();
        fetchRandomProducts();

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
            }
        });

        binding.offersAndNewsCarousel.registerLifecycle(getLifecycle());
    }

    //Function for setting the gradient drawable as the background of the status bar
    private void setStatusBarColor() {
        // Checking if the Android version is Lollipop or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            Drawable statusBarGradientBackgroundDrawable = ContextCompat.getDrawable(HomeActivity.this, R.drawable.search_section_background);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(statusBarGradientBackgroundDrawable);
        }
    }

    //Function for fetching the categories from the database
    void fetchCategories() {
        DatabaseReference categoriesNodeRef = databaseReference.child("categories");
        categoriesNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModelArrayList.clear();
                for (DataSnapshot categoriesSnapshot : snapshot.getChildren()) {
                    CategoryModel categoryModel = categoriesSnapshot.getValue(CategoryModel.class);
                    if (categoryModel != null) {
                        categoryModelArrayList.add(categoryModel);
                    }
                }
                setUpCategoriesRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Failed to fetch categories : ", error.toString());
            }
        });
    }

    //Function for setting up the categories recycler view
    void setUpCategoriesRecyclerView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(HomeActivity.this, categoryModelArrayList);
        binding.categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    //function to populate or initialize the Carousel
    void populateCarousel() {
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        binding.offersAndNewsCarousel.setData(carouselItemArrayList);
    }

    //Function for setting up the recycler view for the random products
    void setUpRandomProductsRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(HomeActivity.this, productModelArrayList, ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY);
        binding.randomProductsRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        binding.randomProductsRecyclerView.setAdapter(productAdapter);
    }

    //Function for fetching random products from the database
    void fetchRandomProducts() {
        DatabaseReference productsNodeRef = databaseReference.child("products");
        productsNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelArrayList.clear();
                for (DataSnapshot specificProductSnapshot : snapshot.getChildren()) {
                    ProductModel product = specificProductSnapshot.getValue(ProductModel.class);
                    if (product != null) {
                        productModelArrayList.add(product);
                    }
                }
                Collections.shuffle(productModelArrayList);
                setUpRandomProductsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}