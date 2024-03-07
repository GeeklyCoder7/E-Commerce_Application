package com.example.e_commerceapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.adapters.ProductAdapter;
import com.example.e_commerceapp.databinding.FragmentProductDetailBinding;
import com.example.e_commerceapp.models.CartModel;
import com.example.e_commerceapp.models.ProductModel;
import com.example.e_commerceapp.utils.ConstantValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ProductDetailFragment extends Fragment {
    FragmentProductDetailBinding binding;
    ProductModel receivedProductModelObj;
    String productName, productDescription, productCategory, productImage, productId;
    Float productPrice;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference cartReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    ArrayList<ProductModel> productModelArrayList;
    int inWishlistFlag = -1; // -1 indicates that the product is not in wishlist.

    public ProductDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);

        // Receiving the product object from the HomeActivity
        Bundle arguments = getArguments();
        if (arguments != null) {
            receivedProductModelObj = arguments.getParcelable("productModel");

            if (receivedProductModelObj != null) {
                productName = receivedProductModelObj.getProductName();
                productDescription = receivedProductModelObj.getProductDescription();
                productCategory = receivedProductModelObj.getProductCategory();
                productImage = receivedProductModelObj.getProductImage();
                productId = receivedProductModelObj.getProductId();
                productPrice = receivedProductModelObj.getProductPrice();

                // Initializing variables
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                productModelArrayList = new ArrayList<>();

                // Calling necessary functions here
                setUpProductDetails();
                fetchRandomProducts();

                binding.productDetailsAddToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProductToCart();
                    }
                });

                binding.bookmarkImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.productDetailsFragmentProgressBar.setVisibility(View.VISIBLE);
                        binding.nestedScrollView.setVisibility(View.GONE);
                        dimScreen();
                        checkIsInWishlist(new WishlistCheckCallback() {
                            @Override
                            public void onWishlistCheck(int result) {
                                binding.productDetailsFragmentProgressBar.setVisibility(View.GONE);
                                binding.nestedScrollView.setVisibility(View.VISIBLE);
                                restoreScreenBrightness();
                                if (result == -1) { // Product is not in wishlist
                                    ProductModel productModel = new ProductModel(productName, productDescription, productCategory, productImage, productPrice, productId);
                                    DatabaseReference newWishlistProductRef = databaseReference.child("users").child(currentUser.getUid()).child("wishlist").child(productModel.getProductId());
                                    newWishlistProductRef.setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(requireContext(), "Added to Wishlist.", Toast.LENGTH_SHORT).show();
                                            setBookmarkIcon(); // Update bookmark icon after adding
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(requireContext(), "Failed to add to Wishlist!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else if (result == 1) { // Product is in wishlist
                                    databaseReference.child("users").child(currentUser.getUid()).child("wishlist").child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(requireContext(), "Removed from Wishlist.", Toast.LENGTH_SHORT).show();
                                            setBookmarkIcon(); // Update bookmark icon after removing
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(requireContext(), "Failed to remove!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

            } else {
                // Handle the case where receivedProductModelObj is null
                Toast.makeText(requireContext(), "Failed to receive product details", Toast.LENGTH_SHORT).show();
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setUpProductDetails() {
        Glide.with(requireContext()).load(productImage).into(binding.productDetailsImageView);
        binding.productDetailsPriceTextView.setText("Price : INR " + productPrice);
        binding.productDetailsDescriptionTextView.setText("" + productDescription);
        binding.productDetailsProductNameTextView.setText("" + productName);
        setBookmarkIcon();
    }

    void fetchRandomProducts() {
        DatabaseReference productsNodeRef = databaseReference.child("products");
        productsNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelArrayList.clear();
                for (DataSnapshot specificProductSnapShot : snapshot.getChildren()) {
                    ProductModel product = specificProductSnapShot.getValue(ProductModel.class);
                    if (product != null && !product.getProductId().equals(productId)) {
                        productModelArrayList.add(product);
                    }
                }
                Collections.shuffle(productModelArrayList);
                setUpProductsSuggestionsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to fetch products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setUpProductsSuggestionsRecyclerView() {
        ProductAdapter productAdapter = new ProductAdapter(requireContext(), productModelArrayList, ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_PRODUCT_DETAILS_ACTIVITY);
        binding.detailsScreenProductsSuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.detailsScreenProductsSuggestionsRecyclerView.setAdapter(productAdapter);
    }

    void addProductToCart() {
        CartModel cartModel = new CartModel(receivedProductModelObj.getProductName(), receivedProductModelObj.getProductDescription(), receivedProductModelObj.getProductCategory(), receivedProductModelObj.getProductImage(), receivedProductModelObj.getProductPrice(), receivedProductModelObj.getProductId(), 1);
        cartReference = databaseReference.child("users").child(currentUser.getUid()).child("cart_items");
        DatabaseReference particularCartItemReference = cartReference.child(cartModel.getProductId());

        particularCartItemReference.setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(requireContext(), "Added to cart.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to add to cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void setBookmarkIcon() {
        checkIsInWishlist(new WishlistCheckCallback() {

            @Override
            public void onWishlistCheck(int result) {
                if (result == 1) {
                    binding.bookmarkImageView.setImageResource(R.drawable.bookmark_added_icon);
                } else {
                    binding.bookmarkImageView.setImageResource(R.drawable.bookmark_not_added_icon);
                }
            }
        });
    }

    void checkIsInWishlist(WishlistCheckCallback callback) {
        databaseReference.child("users").child(currentUser.getUid()).child("wishlist").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onWishlistCheck(1); // Product is in wishlist
                } else {
                    callback.onWishlistCheck(-1); // Product is not in wishlist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Some Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Interface for handling Asynchronous nature of firebase realtime database.
    public interface WishlistCheckCallback {
        void onWishlistCheck(int result);
    }

    //Method for dimming the screen
    void dimScreen() {
        WindowManager.LayoutParams layoutParams = requireActivity().getWindow().getAttributes();
        layoutParams.alpha = 0.3f;
        requireActivity().getWindow().setAttributes(layoutParams);
    }

    //Method for restoring screen brightness
    void restoreScreenBrightness() {
        WindowManager.LayoutParams layoutParams = requireActivity().getWindow().getAttributes();
        layoutParams.alpha = 1f;
        requireActivity().getWindow().setAttributes(layoutParams);
    }
}