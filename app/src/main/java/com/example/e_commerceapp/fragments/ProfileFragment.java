package com.example.e_commerceapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.activities.SignInActivity;
import com.example.e_commerceapp.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FirebaseAuth auth;
    HomeActivity homeActivity;
    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        //Initializing variables
        homeActivity = (HomeActivity) getActivity(); //Note : Always use this simple method to get the reference of the parent activity for calling some functions00000000000/ if you want to open another fragment through some other fragment within the same activity.

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