package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.ActivitySignUpBinding;
import com.example.e_commerceapp.databinding.ActivitySigninBinding;
import com.example.e_commerceapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    String firstAndLastName = "", emailAddress = "", password = "", userId = "";
    UserModel userModel;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializing variables
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        binding.addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.firstAndLastNameSignUpEditText.getText().toString().equals("") || binding.emailSignUpEditText.getText().toString().equals("") || binding.passwordSignUpEditText.getText().toString().equals("") || binding.confirmPasswordSignUpEditText.getText().toString().equals("") || !binding.confirmPasswordSignUpEditText.getText().toString().equals(binding.passwordSignUpEditText.getText().toString())) {
                    if (binding.firstAndLastNameSignUpEditText.getText().toString().equals("")) {
                        binding.firstAndLastNameSignUpEditText.setError("Please enter First and Last name.");
                    }
                    if (binding.emailSignUpEditText.getText().toString().equals("")) {
                        binding.emailSignUpEditText.setError("Please enter the email address.");
                    }
                    if (binding.passwordSignUpEditText.getText().toString().equals("")) {
                        binding.passwordSignUpEditText.setError("Please set some password");
                    }
                    if (binding.confirmPasswordSignUpEditText.getText().toString().equals("")) {
                        binding.confirmPasswordSignUpEditText.setError("");
                    }
                    if (!binding.confirmPasswordSignUpEditText.getText().toString().equals(binding.passwordSignUpEditText.getText().toString())) {
                        binding.confirmPasswordSignUpEditText.setError("Passwords do not match.");
                    }
                } else {
                    firstAndLastName = binding.firstAndLastNameSignUpEditText.getText().toString();
                    emailAddress = binding.emailSignUpEditText.getText().toString();
                    password = binding.passwordSignUpEditText.getText().toString();

                    Intent goToAddressActivityIntent = new Intent(SignUpActivity.this, AddressActivity.class);
                    goToAddressActivityIntent.putExtra("firstAndLastName", firstAndLastName);
                    goToAddressActivityIntent.putExtra("emailAddress", emailAddress);
                    goToAddressActivityIntent.putExtra("password", password);

                    startActivity(goToAddressActivityIntent);
                    finish();

                }
            }
        });

        binding.goToSignInActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}