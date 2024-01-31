package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.CartItemSampleLayoutBinding;
import com.example.e_commerceapp.models.CartModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {
    Context context;
    ArrayList<CartModel> cartModelArrayList;

    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        CartModel cartModel = cartModelArrayList.get(position);
        Glide.with(context).load(cartModel.getProductImage()).into(holder.binding.cartItemProductImageView);
        holder.binding.cartItemProductTitleTextView.setText("" + cartModel.getProductName());
        holder.binding.cartItemProductPriceTextView.setText("" + cartModel.getProductPrice());
//        holder.binding.cartItemQuantityEditText.setText(cartModel.getCartProductQuantity());
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
