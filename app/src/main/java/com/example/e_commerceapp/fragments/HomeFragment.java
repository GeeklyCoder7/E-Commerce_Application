package com.example.e_commerceapp.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.AddressActivity;
import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.activities.SignUpActivity;
import com.example.e_commerceapp.adapters.CategoryAdapter;
import com.example.e_commerceapp.adapters.ProductAdapter;
import com.example.e_commerceapp.databinding.FragmentHomeBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.example.e_commerceapp.models.CategoryModel;
import com.example.e_commerceapp.models.ProductModel;
import com.example.e_commerceapp.models.UserModel;
import com.example.e_commerceapp.utils.ConstantValues;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<CategoryModel> categoryModelArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    ArrayList<CarouselItem> carouselItemArrayList;
    ArrayList<ProductModel> productModelArrayList;
    public HomeFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        //Initializing variables
        categoryModelArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUser = auth.getCurrentUser();
        }
        carouselItemArrayList = new ArrayList<>();
        productModelArrayList = new ArrayList<>();

        //Calling necessary functions here
        setStatusBarColor();
        setCurrentUserLocation();
        fetchCategories();
        populateCarousel();
        fetchRandomProducts();

        binding.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), SignUpActivity.class));
            }
        });

        binding.offersAndNewsCarousel.registerLifecycle(getLifecycle());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Function for setting the gradient drawable as the background of the status bar
    private void setStatusBarColor() {
        // Checking if the Android version is Lollipop or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            Drawable statusBarGradientBackgroundDrawable = ContextCompat.getDrawable(requireActivity(), R.drawable.search_section_background);
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
            window.setBackgroundDrawable(statusBarGradientBackgroundDrawable);
        }
    }

    //Function for fetching the categories from the database
    void fetchCategories() {
        DatabaseReference categoriesNodeRef = databaseReference.child("categories");
        categoriesNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryModelArrayList.clear();
                for (DataSnapshot categoriesSnapshot : snapshot.getChildren()) {
                    CategoryModel categoryModel = categoriesSnapshot.getValue(CategoryModel.class);
                    if (categoryModel != null) {
                        categoryModelArrayList.add(categoryModel);
                    }
                }
                setUpCategoriesRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Failed to fetch categories : ", error.toString());
            }
        });
    }

    //Function for setting up the categories recycler view
    void setUpCategoriesRecyclerView() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(requireContext(), categoryModelArrayList);
        binding.categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    //function to populate or initialize the Carousel
    void populateCarousel() {
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        carouselItemArrayList.add(new CarouselItem(R.drawable.placeholder, "Placeholder for testing"));
        binding.offersAndNewsCarousel.setData(carouselItemArrayList);
    }

    //Function for setting up the recycler view for the random products
    void setUpRandomProductsRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(requireContext(), productModelArrayList, ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY);
        binding.randomProductsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.randomProductsRecyclerView.setAdapter(productAdapter);
    }

    //Function for fetching random products from the database
    void fetchRandomProducts() {
        DatabaseReference productsNodeRef = databaseReference.child("products");
        productsNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelArrayList.clear();
                for (DataSnapshot particularProductSnapshot : snapshot.getChildren()) {
                    ProductModel product = particularProductSnapshot.getValue(ProductModel.class);
                    if (product != null) {
                        productModelArrayList.add(product);
                    }
                }
                Collections.shuffle(productModelArrayList);
                setUpRandomProductsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to fetch products!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method for setting the current users
    void setCurrentUserLocation() {
        DatabaseReference currentUserAddressesNodeRef = databaseReference.child("users").child(currentUser.getUid()).child("user_addresses");

        currentUserAddressesNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddressModel addressModel;
                for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                    if (addressSnapshot != null && Objects.requireNonNull(addressSnapshot.getValue(AddressModel.class)).getAddressStatus().equals(ConstantValues.ADDRESS_STATUS_SELECTED)) {
                        addressModel = addressSnapshot.getValue(AddressModel.class);
                        if (addressModel != null) {
                            binding.selectedAddressUserNameTextView.setText("" + addressModel.getFirstAndLastName());
                            binding.selectedAddressCityNameTextView.setText("" + addressModel.getCityName());
                            binding.selectedAddressPincodeTextView.setText("" + addressModel.getPinCode());
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("City and PinCode setting error : ", error.getMessage());
            }
        });
    }
}