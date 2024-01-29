package com.example.e_commerceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.activities.HomeActivity;
import com.example.e_commerceapp.databinding.DetailsActivityRandomProductsSuggestionsSampleLayoutBinding;
import com.example.e_commerceapp.databinding.ProductCardSampleLayoutBinding;
import com.example.e_commerceapp.models.CartModel;
import com.example.e_commerceapp.utils.ConstantValues;
import com.example.e_commerceapp.models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> {
    Context context;
    ArrayList<ProductModel> productModelArrayList;
    private int flag;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference cartReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

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
            holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartModel cartModel = new CartModel(productModel.getProductName(), productModel.getProductDescription(), productModel.getProductCategory(), productModel.getProductImage(), productModel.getProductPrice(), productModel.getProductId(), 1);
                    cartReference = databaseReference.child("users").child(currentUser.getUid()).child("cart_items");
                    DatabaseReference particularCartItemReference = cartReference.child(cartModel.getProductId());
                    particularCartItemReference.setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to add to cart!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).openProductDetailsFragment(productModel);
                }
            });
        } else {
            Glide.with(context).load(productModel.getProductImage()).into(holder.detailsActivityProductCardBinding.detailsActivityProductImageView);
            holder.detailsActivityProductCardBinding.detailsActivityProductNameTextView.setText("" + productModel.getProductName());
            holder.detailsActivityProductCardBinding.detailsActivityProductDescriptionTextView.setText("" + productModel.getProductDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).openProductDetailsFragment(productModel);
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
