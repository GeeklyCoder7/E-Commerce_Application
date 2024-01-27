package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.ProductDetailsShowingActivity;
import com.example.e_commerceapp.databinding.DetailsActivityRandomProductsSuggestionsSampleLayoutBinding;
import com.example.e_commerceapp.databinding.ProductCardSampleLayoutBinding;
import com.example.e_commerceapp.utils.ConstantValues;
import com.example.e_commerceapp.models.ProductModel;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> {
    Context context;
    ArrayList<ProductModel> productModelArrayList;
    private int flag;

    public ProductAdapter(Context context, ArrayList<ProductModel> productModelArrayList, int flag) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public ProductAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (flag == ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY) {
            return new ProductAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.product_card_sample_layout, parent, false));
        } else {
            return new ProductAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.details_activity_random_products_suggestions_sample_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);

        if (flag == ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY) {
            Glide.with(context).load(productModel.getProductImage()).into(holder.homeActivityProductCardBinding.productImageView);
            holder.homeActivityProductCardBinding.productTitleTextView.setText(productModel.getProductName());
            holder.homeActivityProductCardBinding.productDescriptionTextView.setText(productModel.getProductDescription());
            holder.homeActivityProductCardBinding.productPriceTextView.setText("" + productModel.getProductPrice());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productObjectSendingIntent = new Intent(context, ProductDetailsShowingActivity.class);
                    productObjectSendingIntent.putExtra("productModel", productModel);
                    context.startActivity(productObjectSendingIntent);
                }
            });
        } else {
            Glide.with(context).load(productModel.getProductImage()).into(holder.detailsActivityProductCardBinding.detailsActivityProductImageView);
            holder.detailsActivityProductCardBinding.detailsActivityProductNameTextView.setText("" + productModel.getProductName());
            holder.detailsActivityProductCardBinding.detailsActivityProductDescriptionTextView.setText("" + productModel.getProductDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productObjectSendingIntent = new Intent(context, ProductDetailsShowingActivity.class);
                    productObjectSendingIntent.putExtra("productModel", productModel);
                    context.startActivity(productObjectSendingIntent);
                    ((ProductDetailsShowingActivity) context).finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ProductAdapterViewHolder extends RecyclerView.ViewHolder {
        ProductCardSampleLayoutBinding homeActivityProductCardBinding;
        DetailsActivityRandomProductsSuggestionsSampleLayoutBinding detailsActivityProductCardBinding;
        public ProductAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            if (flag == ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY) {
                homeActivityProductCardBinding = ProductCardSampleLayoutBinding.bind(itemView);
            } else {
                detailsActivityProductCardBinding = DetailsActivityRandomProductsSuggestionsSampleLayoutBinding.bind(itemView);
            }
        }
    }
}
