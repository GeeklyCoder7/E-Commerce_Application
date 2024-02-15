package com.example.e_commerceapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.e_commerceapp.adapters.ProductAdapter;
import com.example.e_commerceapp.databinding.FragmentSearchResultsBinding;
import com.example.e_commerceapp.models.ProductModel;
import com.example.e_commerceapp.utils.ConstantValues;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchResultsFragment extends Fragment {
    FragmentSearchResultsBinding binding;
    ArrayList<ProductModel> searchResultsArraylist;
    String searchedText = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public SearchResultsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);

        //Initializing variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle arguments = getArguments();

        if (arguments != null) {
            searchResultsArraylist = arguments.getParcelableArrayList("searchResultsArraylist");
            searchedText = arguments.getString("searchedText");
            binding.searchResultsFragmentSearchView.setText("" + searchedText);
        }

        //Calling necessary functions here
        setUpSearchResultsRecyclerView();

        binding.searchFragmentSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchedText = binding.searchResultsFragmentSearchView.getText().toString();
                if (searchedText.length() > 0) {
                    searchProducts(searchedText);
                } else {
                    Toast.makeText(requireContext(), "Please enter something to search!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    void setUpSearchResultsRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(requireContext(), searchResultsArraylist, ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY);
        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.searchResultsRecyclerView.setAdapter(productAdapter);
        if (searchResultsArraylist.size() > 0) {
            binding.showingResultsForTextView.setText("Showing results for \"" + searchedText + "\"");
            binding.nothingToShowImageView.setVisibility(View.GONE);
        } else {
            binding.showingResultsForTextView.setText("No results found for \"" + searchedText + "\"");
            binding.nothingToShowImageView.setVisibility(View.VISIBLE);
        }
    }

    public void searchProducts(String s) {
        DatabaseReference productNodeRef = databaseReference.child("products");
        productNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchResultsArraylist.clear();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    ProductModel productModel = productSnapshot.getValue(ProductModel.class);
                    if (productModel != null && productModel.getProductName().toLowerCase().contains(s.toLowerCase()) || Objects.requireNonNull(productModel).getProductDescription().toLowerCase().contains(s.toLowerCase())) {
                        searchResultsArraylist.add(productModel);
                    }
                }

                //Sending the search results to the SearchResultsFragment
                HomeActivity homeActivity = (HomeActivity) requireActivity();
                homeActivity.openSearchResultsFragment(searchResultsArraylist, s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to fetch products!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}