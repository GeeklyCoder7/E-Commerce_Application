package com.example.e_commerceapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.adapters.CartAdapter;
import com.example.e_commerceapp.databinding.FragmentCartBinding;
import com.example.e_commerceapp.databinding.ProceedToBuyDialogBoxLayoutBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.example.e_commerceapp.models.CartModel;
import com.example.e_commerceapp.models.OrderModel;
import com.example.e_commerceapp.models.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    ArrayList<CartModel> cartModelArrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    float totalCartAmount = 0;
    float amountToAddForFreeDelivery = 0;
    int numberOfCartItems = 0;
    ArrayList<ProductModel> searchResultArraylist;
    String deliveryAddressId = "";

    public CartFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        //Initializing variables
        cartModelArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        searchResultArraylist = new ArrayList<>();

        binding.mainNestedScrollView.setVisibility(View.GONE);
        binding.cartFragmentProgressBar.setVisibility(View.VISIBLE);

        //Calling necessary functions here
        fetchCartItems();

        binding.cartFragmentSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedText = binding.cartFragmentSearchView.getText().toString();
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

    public interface DeliveryAddressCallback {
        void onAddressFetched(String deliveryAddress);
    }

    void setUpCartItemsRecyclerView() {
        binding.cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        CartAdapter cartAdapter = new CartAdapter(requireContext(), cartModelArrayList);
        binding.cartItemsRecyclerView.setAdapter(cartAdapter);
        binding.cartFragmentProgressBar.setVisibility(View.GONE);
        binding.mainNestedScrollView.setVisibility(View.VISIBLE);
    }

    void fetchCartItems() {
        DatabaseReference cartItemsNodeRef = databaseReference.child("users").child(currentUser.getUid()).child("cart_items");
        cartItemsNodeRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded()) {  // Check if fragment is still attached
                    cartModelArrayList.clear();
                    totalCartAmount = 0;
                    for (DataSnapshot particularCartItemSnapshot : snapshot.getChildren()) {
                        try {
                            CartModel cartItem = particularCartItemSnapshot.getValue(CartModel.class);
                            if (cartItem != null) {
                                cartModelArrayList.add(cartItem);
                            }
                        } catch (Exception e) {
                            Log.e("These are cart details : ", "Error parsing cart item", e);
                        }
                    }

                    numberOfCartItems = cartModelArrayList.size();

                    if (isAdded()) {  // Check if fragment is still attached
                        setTotalCartAmount();
                        setUpCartItemsRecyclerView();
                        if (binding.cartItemsRecyclerView.getAdapter() != null) {
                            binding.cartItemsRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded()) {  // Check if fragment is still attached
                    Toast.makeText(requireContext(), "Failed to load cart!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    void setTotalCartAmount() {
        float sum = 0;

        for (int i = 0; i < cartModelArrayList.size(); i++) {
            CartModel cartModel = cartModelArrayList.get(i);

            if (cartModel.getProductPrice() != null) {
                int currentProductQuantity = cartModel.getCartProductQuantity();
                float currentProductPrice = cartModel.getProductPrice();
                sum = currentProductPrice * currentProductQuantity;
                totalCartAmount += sum;
            }
        }

        binding.cartTotalTextView.setText("" + totalCartAmount);

        if (numberOfCartItems <= 0) {
            binding.proceedToBuyButton.setText("Add items to cart");
            binding.proceedToBuyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(requireContext(), HomeActivity.class));
                }
            });
        } else {
            if (numberOfCartItems == 1) {
                binding.proceedToBuyButton.setText("Proceed to buy (" + numberOfCartItems + ") item");
            } else {
                binding.proceedToBuyButton.setText("Proceed to buy (" + numberOfCartItems + ") items");
            }

            binding.proceedToBuyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeOrderConfirmation();

                }
            });
        }

        if (eligibleForFreeDelivery()) {
            binding.freeDeliveryEligibilityTextView.setText(R.string.order_eligible);
            binding.freeDeliveryEligibilityTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_eligible_left_drawable_icon, 0, 0, 0);
            binding.freeDeliveryEligibilityTextView.setCompoundDrawablePadding(20);
            binding.freeDeliveryEligibilityTextView.setTextColor(getResources().getColor(R.color.order_eligible_for_free_delivery_text_color));
        } else {
            amountToAddForFreeDelivery = 499 - totalCartAmount;
            String notEligibleSentence = "Add Rs. " + amountToAddForFreeDelivery + " " + getString(R.string.order_not_eligible);
            SpannableString spannableString = new SpannableString(notEligibleSentence);
            int startIndex = notEligibleSentence.indexOf("Rs. ");
            int endIndex = startIndex + String.valueOf("Rs. " + String.valueOf(amountToAddForFreeDelivery)).length();
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.freeDeliveryEligibilityTextView.setText(spannableString);
            binding.freeDeliveryEligibilityTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.order_not_eligible_left_drawable_icon, 0, 0, 0);
            binding.freeDeliveryEligibilityTextView.setCompoundDrawablePadding(20);
            binding.freeDeliveryEligibilityTextView.setTextColor(getResources().getColor(R.color.black));
        }
    }

    boolean eligibleForFreeDelivery() {
        return totalCartAmount >= 499;
    }

    public void searchProducts(String s) {
        DatabaseReference productNodeRef = databaseReference.child("products");
        productNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchResultArraylist.clear();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    ProductModel productModel = productSnapshot.getValue(ProductModel.class);
                    if (productModel != null && productModel.getProductName().toLowerCase().contains(s.toLowerCase()) || Objects.requireNonNull(productModel).getProductDescription().toLowerCase().contains(s.toLowerCase())) {
                        searchResultArraylist.add(productModel);
                    }
                }

                //Sending the search results to the SearchResultsFragment
                HomeActivity homeActivity = (HomeActivity) requireActivity();
                homeActivity.openSearchResultsFragment(searchResultArraylist, s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to fetch products!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Method for placing the order
    void placeOrder() {
        DatabaseReference userOrdersNodeRef = databaseReference.child("users").child(currentUser.getUid()).child("user_orders");
        DatabaseReference newOrderNodeRef = userOrdersNodeRef.push();
        String orderId = newOrderNodeRef.getKey();
        String orderDate = getCurrentDateAndTime();
        String orderStatus = "Order Placed!";

        //Calculating the delivery estimate for the order to be placed
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
        final String deliveryEstimate = simpleDateFormat.format(calendar.getTime());

        //Getting the ID of the default delivery address
        databaseReference.child("users").child(currentUser.getUid()).child("user_addresses").orderByChild("default").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                        deliveryAddressId = addressSnapshot.getKey();
                        //Creating a new order model
                        OrderModel orderModel = new OrderModel();
                        orderModel.setOrderId(orderId);
                        orderModel.setOrderDate(orderDate);
                        orderModel.setOrderStatus(orderStatus);
                        orderModel.setOrderTotal(totalCartAmount);
                        orderModel.setDeliveryAddressId(deliveryAddressId);
                        orderModel.setDeliveryEstimate(deliveryEstimate);

                        newOrderNodeRef.setValue(orderModel);

                        for (CartModel cartModel : cartModelArrayList) {
                            newOrderNodeRef.child(cartModel.getProductId()).setValue(cartModel);
                        }

                        databaseReference.child("users").child(currentUser.getUid()).child("cart_items").removeValue();

                        // Notify the user that the order has been placed
                        Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(requireContext(), "No default address found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Some error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Method for fetching user's current date and time in proper format
    String getCurrentDateAndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    //Method for taking confirmation from the user for placing the order
    @SuppressLint("SetTextI18n")
    void takeOrderConfirmation() {
        Dialog dialog = new Dialog(requireContext());
        ProceedToBuyDialogBoxLayoutBinding dialogBoxLayoutBinding = ProceedToBuyDialogBoxLayoutBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBoxLayoutBinding.getRoot());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //Setting the delivery address for user to confirm the address
        getDeliveryAddressString(new DeliveryAddressCallback() {
            @Override
            public void onAddressFetched(String deliveryAddress) {
                dialogBoxLayoutBinding.deliveryAddressTextView.setText(deliveryAddress);
            }
        });

        dialogBoxLayoutBinding.proceedOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
                dialog.dismiss();
            }
        });

        dialogBoxLayoutBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Negative Button!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialogBoxLayoutBinding.changeDeliveryAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = (HomeActivity) requireActivity();
                homeActivity.openUserAddressesFragment();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Method for creating the default delivery address string
    void getDeliveryAddressString(DeliveryAddressCallback deliveryAddressCallback) {
        databaseReference.child("users").child(currentUser.getUid()).child("user_addresses").orderByChild("default").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot defaultAddressSnapshot : snapshot.getChildren()) {
                        AddressModel defaultDeliveryAddressModel = defaultAddressSnapshot.getValue(AddressModel.class);
                        if (defaultDeliveryAddressModel != null) {
                            String deliveryAddressString = defaultDeliveryAddressModel.getMobileNumber() + " - " + defaultDeliveryAddressModel.getBuildingNameAndHouseNo() + ", (" + defaultDeliveryAddressModel.getLandmarkName() + ") , " +  defaultDeliveryAddressModel.getAreaName() + ", " + defaultDeliveryAddressModel.getCityName() + ", " + defaultDeliveryAddressModel.getStateName() + ", " + defaultDeliveryAddressModel.getDistrictName() + ", " + " - " + defaultDeliveryAddressModel.getPinCode();
                            deliveryAddressCallback.onAddressFetched(deliveryAddressString);
                        }
                    }
                } else {
                    deliveryAddressCallback.onAddressFetched("No default delivery addresses found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to fetch the defaul delivery address!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}