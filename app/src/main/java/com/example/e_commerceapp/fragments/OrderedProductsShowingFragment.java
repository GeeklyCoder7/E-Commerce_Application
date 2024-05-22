package com.example.e_commerceapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.adapters.OrderedProductsAdapter;
import com.example.e_commerceapp.databinding.FragmentOrderedProductsShowingBinding;
import com.example.e_commerceapp.models.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OrderedProductsShowingFragment extends Fragment {
    FragmentOrderedProductsShowingBinding binding;
    String orderId;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ArrayList<CartModel> cartModelArrayList;


    public OrderedProductsShowingFragment(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderedProductsShowingBinding.inflate(inflater, container, false);

        //Initializing variables below
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        cartModelArrayList = new ArrayList<>();

        //Calling necessary functions here
        fetchOrderedItems(orderId);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Method for setting up the recycler view.
    void setUpRecyclerView() {
        OrderedProductsAdapter orderedProductsAdapter = new OrderedProductsAdapter(requireContext(), cartModelArrayList);
        binding.orderedProductsRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.orderedProductsRecyclerview.setAdapter(orderedProductsAdapter);
    }

    //Method for for fetching the ordered products inside each individual order
    void fetchOrderedItems(String orderId) {
        // Reference to the user's orders node
        DatabaseReference userOrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser.getUid())
                .child("user_orders")
                .child(orderId); // Directly referencing the orderId node

        // Adding a listener to fetch data
        userOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded()) {
                    if (snapshot.exists()) {
                        cartModelArrayList.clear();
                        // Loop through each product in the order
                        for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                            // Check if this child node represents a product
                            if (productSnapshot.hasChild("productId") && productSnapshot.hasChild("productName")) {
                                CartModel cartModel = new CartModel();
                                cartModel.setProductId(productSnapshot.child("productId").getValue(String.class));
                                cartModel.setProductImage(productSnapshot.child("productImage").getValue(String.class));
                                cartModel.setProductName(productSnapshot.child("productName").getValue(String.class));

                                // Handle null case for cartProductQuantity
                                Integer quantity = productSnapshot.child("cartProductQuantity").getValue(Integer.class);
                                if (quantity != null) {
                                    cartModel.setCartProductQuantity(quantity);
                                } else {
                                    cartModel.setCartProductQuantity(0); // Set to default value if null
                                }
                                cartModelArrayList.add(cartModel);
                            }
                        }
                    } else {
                        Log.d("OrderedProduct", "No products found for orderId: " + orderId);
                    }

                    setUpRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OrderedProduct", "Database error: " + error.getMessage());
            }
        });
    }

}