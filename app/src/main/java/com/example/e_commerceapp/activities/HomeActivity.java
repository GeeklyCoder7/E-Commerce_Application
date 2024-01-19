package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.adapters.CategoryAdapter;
import com.example.e_commerceapp.databinding.ActivityHomeBinding;
import com.example.e_commerceapp.models.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ArrayList<CategoryModel> categoryModelArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
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

        //Calling necessary functions here
        setStatusBarColor();
        fetchCategories();

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
            }
        });
    }
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

    void fetchCategories() {
        DatabaseReference categoriesNodeRef = databaseReference.child("categories");
        categoriesNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categoriesSnapshot : snapshot.getChildren()) {
                    CategoryModel categoryModel = categoriesSnapshot.getValue(CategoryModel.class);
                    if (categoryModel != null) {
                        categoryModelArrayList.add(categoryModel);
                    }
                }
                setUpRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Failed to fetch categories : ", error.toString());
            }
        });
    }

    void setUpRecyclerView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(HomeActivity.this, categoryModelArrayList);
        binding.categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);
    }
}