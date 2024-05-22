package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.databinding.UserOrderLayoutDesignBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.example.e_commerceapp.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersAdapter.UserOrdersAdapterViewHolder> {
    Context context;
    ArrayList<OrderModel> orderModelArrayList;

    public UserOrdersAdapter(Context context, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public UserOrdersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserOrdersAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.user_order_layout_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrdersAdapterViewHolder holder, int position) {
        OrderModel orderModel = orderModelArrayList.get(position);

        holder.binding.orderDesignOrderIdTextView.setText("" + orderModel.getOrderId());
        holder.binding.orderDesignOrderStatusTextView.setText("" + orderModel.getOrderStatus());
        holder.binding.orderDesignOrderDateTextView.setText("" + orderModel.getOrderDate());
        holder.binding.orderDesignOrderTotalTextView.setText(String.valueOf(orderModel.getOrderTotal()));
        holder.binding.orderDesignOrderEstimateTextView.setText("" + orderModel.getDeliveryEstimate());

        //Fetching the delivery address using callback due to asynchronous nature of firebase causing issues to fetch the address directly.
        getDeliveryAddress(orderModel.getDeliveryAddressId(), new DeliveryAddressCallback() {
            @Override
            public void onAddressFetched(String deliveryAddress) {
                holder.binding.orderDesignDeliveryAddressTextView.setText("" + deliveryAddress);
            }
        });

        holder.binding.showOrderedProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof HomeActivity) {
                    HomeActivity homeActivity = ((HomeActivity) context);
                    homeActivity.openOrderedProductsShowingFragment(orderModel.getOrderId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    public void getDeliveryAddress(String addressId, DeliveryAddressCallback deliveryAddressCallback) {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user_addresses").orderByChild("addressId").equalTo(addressId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot addressSnapshot : snapshot.getChildren()) {
                        AddressModel addressModel = addressSnapshot.getValue(AddressModel.class);
                        if (addressModel != null) {
                            String deliveryAddress = addressModel.getMobileNumber() + " - " + addressModel.getBuildingNameAndHouseNo() + ", (" + addressModel.getLandmarkName() + ") , " +  addressModel.getAreaName() + ", " + addressModel.getCityName() + ", " + addressModel.getStateName() + ", " + addressModel.getDistrictName() + ", " + " - " + addressModel.getPinCode();
                            deliveryAddressCallback.onAddressFetched(deliveryAddress);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                deliveryAddressCallback.onAddressFetched("No delivery addresses found!");
            }
        });
    }

    public interface DeliveryAddressCallback {
        void onAddressFetched(String deliveryAddress);
    }

    public static class UserOrdersAdapterViewHolder extends RecyclerView.ViewHolder {
        UserOrderLayoutDesignBinding binding;

        public UserOrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserOrderLayoutDesignBinding.bind(itemView);
        }
    }
}
