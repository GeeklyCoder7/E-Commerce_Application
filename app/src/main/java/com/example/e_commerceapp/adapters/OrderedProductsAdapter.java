package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.databinding.OrderedProductsLayoutDesignBinding;
import com.example.e_commerceapp.models.CartModel;

import java.util.ArrayList;

public class OrderedProductsAdapter extends RecyclerView.Adapter<OrderedProductsAdapter.OrderedProductsAdapterViewHolder> {
    Context context;
    ArrayList<CartModel> cartModelArrayList;

    public OrderedProductsAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    @NonNull
    @Override
    public OrderedProductsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderedProductsAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.ordered_products_layout_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedProductsAdapterViewHolder holder, int position) {
        CartModel orderedItemModel = cartModelArrayList.get(position);
        holder.binding.orderedProductNameTextView.setText(orderedItemModel.getProductName());
        Glide.with(context).load(orderedItemModel.getProductImage()).into(holder.binding.orderedProductImageView);
        holder.binding.orderedProductQuantityTextView.setText("X " + orderedItemModel.getCartProductQuantity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity homeActivity = ((HomeActivity) context);
                homeActivity.openProductDetailsFragment(orderedItemModel.getProductId());
            }
        });
    }
    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public static class OrderedProductsAdapterViewHolder extends RecyclerView.ViewHolder {
        OrderedProductsLayoutDesignBinding binding;

        public OrderedProductsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = OrderedProductsLayoutDesignBinding.bind(itemView);
        }
    }
}
