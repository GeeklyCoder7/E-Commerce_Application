package com.example.e_commerceapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterViewHolder> {
    Context context;
    ArrayList<ProductModel> productModelArrayList;
    private int flag;
    String testing = "";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference cartReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    boolean productIsInCart = false;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapterViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);

        if (flag == ConstantValues.FLAG_ADAPTER_CALLED_THROUGH_HOME_ACTIVITY) {
            Glide.with(context).load(productModel.getProductImage()).into(holder.homeActivityProductCardBinding.productImageView);
            holder.homeActivityProductCardBinding.productTitleTextView.setText(productModel.getProductName());
            holder.homeActivityProductCardBinding.productDescriptionTextView.setText(productModel.getProductDescription());
            holder.homeActivityProductCardBinding.productPriceTextView.setText("" + productModel.getProductPrice());

            //Setting the delivery estimate date for the product
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
            String deliveryEstimate = simpleDateFormat.format(calendar.getTime());
            holder.homeActivityProductCardBinding.productDeliveryEstimateTextView.setText("Delivery estimate : \n" + deliveryEstimate);

            databaseReference.child("users").child(currentUser.getUid()).child("cart_items").orderByChild("productId").equalTo(productModel.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        productIsInCart = true;
                        holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setBackgroundResource(R.drawable.product_already_in_cart_button_background_drawable);
                        holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setText("Added");
                        holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.added_to_cart_left_drawable_icon, 0, 0, 0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show();
                }
            });

            holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("users").child(currentUser.getUid()).child("cart_items").orderByChild("productId").equalTo(productModel.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Toast.makeText(context, "Product is Already in the cart!", Toast.LENGTH_SHORT).show();
                            } else {
                                CartModel cartModel = new CartModel(productModel.getProductName(), productModel.getProductDescription(), productModel.getProductCategory(), productModel.getProductImage(), productModel.getProductPrice(), productModel.getProductId(), 1);
                                cartReference = databaseReference.child("users").child(currentUser.getUid()).child("cart_items");
                                DatabaseReference particularCartItemReference = cartReference.child(cartModel.getProductId());
                                particularCartItemReference.setValue(cartModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setBackgroundResource(R.drawable.product_already_in_cart_button_background_drawable);
                                        holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setText("Added");
                                        holder.homeActivityProductCardBinding.homeScreenAddToCartButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.added_to_cart_left_drawable_icon, 0, 0, 0);
                                        Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to add to cart!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).openProductDetailsFragment(productModel.getProductId());
                }
            });
        } else {
            Glide.with(context).load(productModel.getProductImage()).into(holder.detailsActivityProductCardBinding.detailsActivityProductImageView);
            holder.detailsActivityProductCardBinding.detailsActivityProductNameTextView.setText("" + productModel.getProductName());
            holder.detailsActivityProductCardBinding.detailsActivityProductDescriptionTextView.setText("" + productModel.getProductDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) context).openProductDetailsFragment(productModel.getProductId());
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
