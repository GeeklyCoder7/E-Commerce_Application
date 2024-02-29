package com.example.e_commerceapp.fragments;

import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.adapters.UserAddressesAdapter;
import com.example.e_commerceapp.databinding.FragmentUserAddressesBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserAddressesFragment extends Fragment {
    FragmentUserAddressesBinding binding;
    ArrayList<AddressModel> addressModelArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    HomeActivity homeActivity;

    public UserAddressesFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserAddressesBinding.inflate(inflater, container, false);

        //Initializing variables
        addressModelArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            firebaseUser = auth.getCurrentUser();
        }
        homeActivity = (HomeActivity) getActivity();

        //Calling necessary functions here
        fetchSavedAddresses();

        //Setting on-click methods here
        binding.addNewAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.openAddAddressesFragment();
            }
        });

        return binding.getRoot();
    }

    void setUpRecyclerView() {
        if (isAdded()) {
            UserAddressesAdapter userAddressesAdapter = new UserAddressesAdapter(addressModelArrayList, requireContext());
            binding.userAddressesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.userAddressesRecyclerView.setAdapter(userAddressesAdapter);
        }
    }

    void fetchSavedAddresses() {
        DatabaseReference userAddressesNodeRef = databaseReference.child("users").child(firebaseUser.getUid()).child("user_addresses");
        userAddressesNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    addressModelArrayList.clear();
                    for (DataSnapshot savedAddressSnapshot : snapshot.getChildren()) {
                        AddressModel addressModel = savedAddressSnapshot.getValue(AddressModel.class);
                        if (addressModel != null) {
                            addressModelArrayList.add(addressModel);
                        }
                    }
                setUpRecyclerView(); //Setting up the recyclerview here so that it does not cause errors due to Asynchronous nature of the firebase realtime database.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}