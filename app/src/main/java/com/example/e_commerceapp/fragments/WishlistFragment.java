package com.example.e_commerceapp.fragments;

import android.net.ipsec.ike.TransportModeChildSessionParams;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.adapters.WishlistAdapter;
import com.example.e_commerceapp.databinding.FragmentWishlistBinding;
import com.example.e_commerceapp.models.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class WishlistFragment extends Fragment {
    FragmentWishlistBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    ArrayList<ProductModel> wishlistArraylist;


    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWishlistBinding.inflate(inflater, container, false);

        //Initializing variables
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUser = auth.getCurrentUser();
        }
        wishlistArraylist = new ArrayList<>();

        //Calling necessary methods here
        fetchWishlistItems();

        return binding.getRoot();
    }

    void fetchWishlistItems() {
        DatabaseReference wishlistNodeRef = databaseReference.child("users").child(currentUser.getUid()).child("wishlist");
        wishlistNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded()) {
                    wishlistArraylist.clear();
                    for (DataSnapshot wishlistItemSnapshot : snapshot.getChildren()) {
                        ProductModel wishlistModel = wishlistItemSnapshot.getValue(ProductModel.class);
                        if (wishlistModel != null) {
                            wishlistArraylist.add(wishlistModel);
                        }
                    }
                    setUpWishlistRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setUpWishlistRecyclerView() {
        WishlistAdapter wishlistAdapter = new WishlistAdapter(requireContext(), wishlistArraylist);
        binding.wishlistReyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.wishlistReyclerView.setAdapter(wishlistAdapter);
    }
}