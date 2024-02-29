package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.e_commerceapp.databinding.ActivityAddressBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddressActivity extends AppCompatActivity {
    ActivityAddressBinding binding;
    String firstAndLastName = "", cityName = "", stateName = "", areaName = "", districtName = "", landmarkName = "", pinCode = "", mobileNumber = "", addressId = "", addressStatus = "", buildingNameAndHouseNo = "";
    AddressModel addressModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializing variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUser = auth.getCurrentUser();
        }

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.firstAndLastNameAddAddressEditText.getText().toString().equals("") || binding.cityNameAddAddressEditText.getText().toString().equals("") || binding.stateNameAddAddressEditText.getText().toString().equals("") || binding.areaNameAddAddressEditText.getText().toString().equals("") || binding.districtNameAddAddressEditText.getText().toString().equals("") || String.valueOf(binding.pincodeAddAddressEditText.getText().toString()).equals("") || binding.landmarkNameAddAddressEditText.getText().toString().equals("") || String.valueOf(binding.mobileNumberAddAddressEditText.getText().toString()).equals("") || binding.buildingNameAndHouseNoEditText.getText().toString().equals("")) {
                    if (binding.firstAndLastNameAddAddressEditText.getText().toString().equals("")) {
                        binding.firstAndLastNameAddAddressEditText.setError("Please enter the recipient's name!");
                    }
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
                    if (binding.buildingNameAndHouseNoEditText.getText().toString().equals("")) {
                        binding.buildingNameAndHouseNoEditText.setError("Please enter the building name or house number!");
                    }
                } else {
                    firstAndLastName = binding.firstAndLastNameAddAddressEditText.getText().toString();
                    cityName = binding.cityNameAddAddressEditText.getText().toString();
                    stateName = binding.stateNameAddAddressEditText.getText().toString();
                    areaName = binding.areaNameAddAddressEditText.getText().toString();
                    districtName = binding.districtNameAddAddressEditText.getText().toString();
                    pinCode = binding.pincodeAddAddressEditText.getText().toString();
                    landmarkName = binding.landmarkNameAddAddressEditText.getText().toString();
                    mobileNumber = binding.mobileNumberAddAddressEditText.getText().toString();
                    buildingNameAndHouseNo = binding.buildingNameAndHouseNoEditText.getText().toString();

                    addAddress(); //Calling the addAddress() method to add the address details to the database.

                    binding.addAddressNestedScrollView.setVisibility(View.GONE);
                    binding.addressActivityProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //Method for adding new address to the currentUser's node in the database.
    void addAddress() {
        addressModel = new AddressModel(firstAndLastName, cityName, stateName, areaName, districtName, landmarkName, pinCode, mobileNumber, "", buildingNameAndHouseNo, true);
        DatabaseReference newAddressReference = databaseReference.child("users").child(currentUser.getUid()).child("user_addresses").push();
        addressId = newAddressReference.getKey();
        addressModel.setAddressId(addressId);

        newAddressReference.setValue(addressModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddressActivity.this, "Address added successfully.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddressActivity.this, SignInActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddressActivity.this, "Failed to add address!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void resetEverything() {
        binding.firstAndLastNameAddAddressEditText.setText("");
        binding.cityNameAddAddressEditText.setText("");
        binding.stateNameAddAddressEditText.setText("");
        binding.areaNameAddAddressEditText.setText("");
        binding.districtNameAddAddressEditText.setText("");
        binding.pincodeAddAddressEditText.setText("");
        binding.landmarkNameAddAddressEditText.setText("");
        binding.mobileNumberAddAddressEditText.setText("");
    }
}