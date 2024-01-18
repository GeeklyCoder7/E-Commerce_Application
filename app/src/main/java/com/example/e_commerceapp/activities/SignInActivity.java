package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerceapp.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    ActivitySigninBinding binding;
    private FirebaseAuth auth;

    String emailAddress = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializing variables
        auth = FirebaseAuth.getInstance();

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.mobileOrEmailSignInEditText.getText().toString().equals("") || binding.passwordSignInEditText.getText().toString().equals("")) {
                    if (binding.mobileOrEmailSignInEditText.getText().toString().equals("")) {
                        binding.mobileOrEmailSignInEditText.setError("Please enter the email address.");
                    }
                    if (binding.passwordSignInEditText.getText().toString().equals("")) {
                        binding.passwordSignInEditText.setError("Please enter the password.");
                    }
                } else {
                    emailAddress = binding.mobileOrEmailSignInEditText.getText().toString();
                    password = binding.passwordSignInEditText.getText().toString();

                    binding.signInLinearLayout.setVisibility(View.GONE);
                    binding.signInProgressBar.setVisibility(View.VISIBLE);

                    //authenticating and signing in the user through firebase auth
                    auth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                binding.signInProgressBar.setVisibility(View.GONE);
                                binding.signInLinearLayout.setVisibility(View.VISIBLE);
                                resetEverything();
                                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                binding.signInProgressBar.setVisibility(View.GONE);
                                binding.signInLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    void resetEverything() {
        binding.mobileOrEmailSignInEditText.setText("");
        binding.passwordSignInEditText.setText("");
    }
}