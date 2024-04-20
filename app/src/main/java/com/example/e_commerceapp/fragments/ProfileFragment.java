package com.example.e_commerceapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.activities.SignInActivity;
import com.example.e_commerceapp.databinding.FragmentProfileBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FirebaseAuth auth;
    HomeActivity homeActivity;
    String currentUserName = "";
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        //Initializing variables
        homeActivity = (HomeActivity) getActivity(); //Note : Always use this simple method to get the reference of the parent activity for calling some functions00000000000/ if you want to open another fragment through some other fragment within the same activity.

        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("user_addresses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot addressSnapShot : snapshot.getChildren()) {
                    AddressModel addressModel = addressSnapShot.getValue(AddressModel.class);
                    if (addressModel != null && addressModel.isDefault()) {
                        currentUserName = addressModel.getFirstAndLastName();
                        break;
                    }
                }
                if (isAdded()) {
                    binding.profileFragmentCurrentUserNameTextView.setText(currentUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.profileSectionSavedAddressesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.openUserAddressesFragment();
            }
        });

        binding.profileSectionUserWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.openWishlistFragment();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void logOutUser() {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        startActivity(new Intent(requireContext(), SignInActivity.class));
    }
}