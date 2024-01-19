package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    static final int SPLASH_SCREEN_TIMEOUT = 1000;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializing variables
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserAuthentication();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }

    void checkUserAuthentication() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            checkUserExistence(currentUser.getUid());
        } else {
            startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
            finish();
        }
    }

    void checkUserExistence(String userId) {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                    finish();
                } else {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}