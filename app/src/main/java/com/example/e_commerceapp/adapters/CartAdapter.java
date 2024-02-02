package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.CartItemSampleLayoutBinding;
import com.example.e_commerceapp.fragments.HomeFragment;
import com.example.e_commerceapp.models.CartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {
    Context context;
    ArrayList<CartModel> cartModelArrayList;

    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    void deleteCartItem(String productId, int position) {
        DatabaseReference cartItemReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("cart_items").child(productId);
        cartItemReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Product removed successfully.", Toast.LENGTH_SHORT).show();
                if (cartModelArrayList.size() > 0) {
                    cartModelArrayList.remove(position);
                }

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to removed the product!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        int itemPosition = position;
        CartModel cartModel = cartModelArrayList.get(position);
        Glide.with(context).load(cartModel.getProductImage()).into(holder.binding.cartItemProductImageView);
        holder.binding.cartItemProductTitleTextView.setText("" + cartModel.getProductName());
        holder.binding.cartItemProductPriceTextView.setText("" + cartModel.getProductPrice());
        holder.binding.cartItemQuantityEditText.setText(String.valueOf(cartModel.getCartProductQuantity()));
        holder.binding.cartItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCartItem(cartModel.getProductId(), itemPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public class CartAdapterViewHolder extends RecyclerView.ViewHolder {
        CartItemSampleLayoutBinding binding;

        public CartAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CartItemSampleLayoutBinding.bind(itemView);
        }
    }
}

