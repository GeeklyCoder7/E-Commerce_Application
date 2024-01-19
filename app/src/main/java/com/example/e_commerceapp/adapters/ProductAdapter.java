package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.ProductCardSampleLayoutBinding;
import com.example.e_commerceapp.models.ProductModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> {
    Context context;
    ArrayList<ProductModel> productModelArrayList;

    public ProductAdapter(Context context, ArrayList<ProductModel> productModelArrayList) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.product_card_sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);
        Glide.with(context).load(productModel.getProductImage()).into(holder.binding.productImageView);
        holder.binding.productTitleTextView.setText(productModel.getProductName());
        holder.binding.productDescriptionTextView.setText(productModel.getProductDescription());
        holder.binding.productPriceTextView.setText("" + productModel.getProductPrice());
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ProductAdapterViewHolder extends RecyclerView.ViewHolder {
        ProductCardSampleLayoutBinding binding;
        public ProductAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductCardSampleLayoutBinding.bind(itemView);
        }
    }
}
