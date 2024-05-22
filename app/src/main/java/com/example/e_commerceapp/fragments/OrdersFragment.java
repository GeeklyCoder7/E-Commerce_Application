package com.example.e_commerceapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.adapters.UserOrdersAdapter;
import com.example.e_commerceapp.databinding.FragmentOrdersBinding;
import com.example.e_commerceapp.models.OrderModel;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    FragmentOrdersBinding binding;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ArrayList<OrderModel> orderModelArrayList;
    public OrdersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);

        //Initializing the variables
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        orderModelArrayList = new ArrayList<>();

        //Calling necessary functions here
        fetchOrders();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Method for setting up the user's orders recyclerview
    void setUpUserOrdersRecyclerView() {
        if (isAdded()) {
            UserOrdersAdapter userOrdersAdapter = new UserOrdersAdapter(requireContext(), orderModelArrayList);
            binding.ordersFragmentUserOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.ordersFragmentUserOrdersRecyclerView.setAdapter(userOrdersAdapter);
        }
    }

    //Method for fetching the user's orders
    void fetchOrders() {
        databaseReference.child("users").child(currentUser.getUid()).child("user_orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded()) {
                    orderModelArrayList.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            OrderModel orderModel = orderSnapshot.getValue(OrderModel.class);
                            if (orderModel != null) {
                                orderModelArrayList.add(orderModel);
                            }
                        }
                    }
                    if (isAdded()) {
                        setUpUserOrdersRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to fetch orders!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}