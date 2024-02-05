package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.ActivityAddressBinding;
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

public class AddressActivity extends AppCompatActivity {
    ActivityAddressBinding binding;
    String firstAndLastName = "", emailAddress = "", password = "", cityName = "", stateName = "", areaName = "", districtName = "", landmarkName = "", userId = "";
    String pinCode = "", mobileNumber = "";

    UserModel userModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Receiving values from SignUpActivity.java
        Intent valueReceivingIntent = getIntent();
        firstAndLastName = valueReceivingIntent.getStringExtra("firstAndLastName");
        emailAddress = valueReceivingIntent.getStringExtra("emailAddress");
        password = valueReceivingIntent.getStringExtra("password");

        //Initializing variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.cityNameAddAddressEditText.getText().toString().equals("") || binding.stateNameAddAddressEditText.getText().toString().equals("") || binding.areaNameAddAddressEditText.getText().toString().equals("") || binding.districtNameAddAddressEditText.getText().toString().equals("") || String.valueOf(binding.pincodeAddAddressEditText.getText().toString()).equals("") || binding.landmarkNameAddAddressEditText.getText().toString().equals("") || String.valueOf(binding.mobileNumberAddAddressEditText.getText().toString()).equals("")) {
                    if (binding.cityNameAddAddressEditText.getText().toString().equals("")) {
                        binding.cityNameAddAddressEditText.setError("Please enter the city name!");
                    }
                    if (binding.stateNameAddAddressEditText.getText().toString().equals("")) {
                        binding.stateNameAddAddressEditText.setError("Please enter the state name!");
                    }
                    if (binding.areaNameAddAddressEditText.getText().toString().equals("")) {
                        binding.areaNameAddAddressEditText.setError("Please enter the area/locality name!");
                    }
                    if (binding.districtNameAddAddressEditText.getText().toString().equals("")) {
                        binding.districtNameAddAddressEditText.setError("Please enter the district name!");
                    }
                    if (String.valueOf(binding.pincodeAddAddressEditText.getText().toString()).equals("")) {
                        binding.pincodeAddAddressEditText.setError("Please enter the pincode!");
                    }
                    if (binding.landmarkNameAddAddressEditText.getText().toString().equals("")) {
                        binding.landmarkNameAddAddressEditText.setError("Please enter the landmark!");
                    }
                    if (String.valueOf(binding.mobileNumberAddAddressEditText.getText().toString()).equals("")) {
                        binding.mobileNumberAddAddressEditText.setError("Please enter mobile number");
                    }
                } else {
                    cityName = binding.cityNameAddAddressEditText.getText().toString();
                    stateName = binding.stateNameAddAddressEditText.getText().toString();
                    areaName = binding.areaNameAddAddressEditText.getText().toString();
                    districtName = binding.districtNameAddAddressEditText.getText().toString();
                    pinCode = binding.pincodeAddAddressEditText.getText().toString();
                    landmarkName = binding.landmarkNameAddAddressEditText.getText().toString();
                    mobileNumber = binding.mobileNumberAddAddressEditText.getText().toString();

                    binding.addAddressNestedScrollView.setVisibility(View.GONE);
                    binding.addressActivityProgressBar.setVisibility(View.VISIBLE);

                    //Creating new user
                    auth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddressActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = auth.getCurrentUser();
                                userId = user.getUid();
                                addUserToDatabase();
                            } else {
                                Toast.makeText(AddressActivity.this, "Failed to create an account!", Toast.LENGTH_SHORT).show();
                                binding.addAddressNestedScrollView.setVisibility(View.VISIBLE);
                                binding.addressActivityProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    void addUserToDatabase() {
        userModel = new UserModel(firstAndLastName, emailAddress, password, cityName, stateName, areaName, districtName, landmarkName, pinCode, mobileNumber, userId);
        DatabaseReference currentUserReference = databaseReference.child("users").child(userModel.getUserId());

        //Adding user details to the Firebase Realtime Database.
        currentUserReference.setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                resetEverything();
                binding.addAddressNestedScrollView.setVisibility(View.VISIBLE);
                binding.addressActivityProgressBar.setVisibility(View.GONE);
                startActivity(new Intent(AddressActivity.this, SignInActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddressActivity.this, "Failed to add details to database!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void resetEverything() {
        binding.cityNameAddAddressEditText.setText("");
        binding.stateNameAddAddressEditText.setText("");
        binding.areaNameAddAddressEditText.setText("");
        binding.districtNameAddAddressEditText.setText("");
        binding.pincodeAddAddressEditText.setText("");
        binding.landmarkNameAddAddressEditText.setText("");
        binding.mobileNumberAddAddressEditText.setText("");
    }
}