package com.example.e_commerceapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.databinding.FragmentAddAddressBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAddressFragment extends Fragment {
    FragmentAddAddressBinding binding;
    String firstAndLastName = "", cityName = "", stateName = "", areaName = "", districtName = "", landmarkName = "", pinCode = "", mobileNumber = "", addressId = "", addressStatus = "", buildingNameAndHouseNo = "";
    AddressModel addressModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    HomeActivity homeActivity;

    public AddAddressFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false);

        //Initializing variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUser = auth.getCurrentUser();
        }
        homeActivity = (HomeActivity) getActivity();

        binding.addAddressFragmentAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addAddressFragmentFirstAndLastNameEditText.getText().toString().equals("") || binding.addAddressFragmentCityNameEditText.getText().toString().equals("") || binding.addAddressFragmentStateNameEditText.getText().toString().equals("") || binding.addAddressFragmentAreaNameEditText.getText().toString().equals("") || binding.addAddressFragmentDistrictNameEditText.getText().toString().equals("") || String.valueOf(binding.addAddressFragmentPinCodeEditText.getText().toString()).equals("") || binding.addAddressFragmentLandmarkEditText.getText().toString().equals("") || String.valueOf(binding.mobileNumberAddAddressEditText.getText().toString()).equals("") || binding.addAddressFragmentBuildingNameAndHouseNoEditText.getText().toString().equals("")) {
                    if (binding.addAddressFragmentFirstAndLastNameEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentFirstAndLastNameEditText.setError("Please enter the recipient's name!");
                    }
                    if (binding.addAddressFragmentCityNameEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentCityNameEditText.setError("Please enter the city name!");
                    }
                    if (binding.addAddressFragmentStateNameEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentStateNameEditText.setError("Please enter the state name!");
                    }
                    if (binding.addAddressFragmentAreaNameEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentAreaNameEditText.setError("Please enter the area/locality name!");
                    }
                    if (binding.addAddressFragmentDistrictNameEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentDistrictNameEditText.setError("Please enter the district name!");
                    }
                    if (String.valueOf(binding.addAddressFragmentPinCodeEditText.getText().toString()).equals("")) {
                        binding.addAddressFragmentPinCodeEditText.setError("Please enter the pincode!");
                    }
                    if (binding.addAddressFragmentLandmarkEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentLandmarkEditText.setError("Please enter the landmark!");
                    }
                    if (String.valueOf(binding.mobileNumberAddAddressEditText.getText().toString()).equals("")) {
                        binding.mobileNumberAddAddressEditText.setError("Please enter mobile number");
                    }
                    if (binding.addAddressFragmentBuildingNameAndHouseNoEditText.getText().toString().equals("")) {
                        binding.addAddressFragmentBuildingNameAndHouseNoEditText.setError("Please enter the building name or house number!");
                    }
                } else {
                    firstAndLastName = binding.addAddressFragmentFirstAndLastNameEditText.getText().toString();
                    cityName = binding.addAddressFragmentCityNameEditText.getText().toString();
                    stateName = binding.addAddressFragmentStateNameEditText.getText().toString();
                    areaName = binding.addAddressFragmentAreaNameEditText.getText().toString();
                    districtName = binding.addAddressFragmentDistrictNameEditText.getText().toString();
                    pinCode = binding.addAddressFragmentPinCodeEditText.getText().toString();
                    landmarkName = binding.addAddressFragmentLandmarkEditText.getText().toString();
                    mobileNumber = binding.mobileNumberAddAddressEditText.getText().toString();
                    buildingNameAndHouseNo = binding.addAddressFragmentBuildingNameAndHouseNoEditText.getText().toString();

                    addAddress(); //Calling the addAddress() method to add the address details to the database.
                }
            }
        });

        return binding.getRoot();
    }

    void addAddress() {
        addressModel = new AddressModel(firstAndLastName, cityName, stateName, areaName, districtName, landmarkName, pinCode, mobileNumber, "", buildingNameAndHouseNo, false);
        DatabaseReference newAddressReference = databaseReference.child("users").child(currentUser.getUid()).child("user_addresses").push();
        addressId = newAddressReference.getKey();
        addressModel.setAddressId(addressId);

        newAddressReference.setValue(addressModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(), "Address added successfully.", Toast.LENGTH_SHORT).show();
                homeActivity.openUserAddressesFragment();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to add address!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}