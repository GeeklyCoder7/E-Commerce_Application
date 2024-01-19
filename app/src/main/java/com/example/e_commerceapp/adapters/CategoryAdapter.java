package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.CategoryCircleSampleLayoutBinding;
import com.example.e_commerceapp.models.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapterViewHolder> {
    Context context;
    ArrayList<CategoryModel> categoryModelArrayList;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModelArrayList) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;
    }

    @NonNull
    @Override
    public CategoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.category_circle_sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapterViewHolder holder, int position) {
        CategoryModel categoryModel = categoryModelArrayList.get(position);
        holder.binding.categoryNameTextView.setText(categoryModel.getCategoryName());
        Glide.with(context).load(categoryModel.getCategoryIconImage()).into(holder.binding.categoryImageview);
    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public static class CategoryAdapterViewHolder extends RecyclerView.ViewHolder {
        CategoryCircleSampleLayoutBinding binding;
        public CategoryAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CategoryCircleSampleLayoutBinding.bind(itemView);
        }
    }
}
