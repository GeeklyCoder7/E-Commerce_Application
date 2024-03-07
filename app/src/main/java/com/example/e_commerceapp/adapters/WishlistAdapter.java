package com.example.e_commerceapp.adapters;

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
import com.example.e_commerceapp.databinding.WishlistItemSampleLayoutBinding;
import com.example.e_commerceapp.models.CartModel;
import com.example.e_commerceapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistAdapterViewHolder> {
    Context context;
    ArrayList<ProductModel> wishlistArraylist;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    public WishlistAdapter(Context context, ArrayList<ProductModel> wishlistArraylist) {
        this.context = context;
        this.wishlistArraylist = wishlistArraylist;
    }

    @NonNull
    @Override
    public WishlistAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WishlistAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.wishlist_item_sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapterViewHolder holder, int position) {
        ProductModel wishlistModel = wishlistArraylist.get(position);
        Glide.with(context).load(wishlistModel.getProductImage()).into(holder.binding.wishlistFragmentProductImageView);
        holder.binding.wishlistFragmentProductTitleTextView.setText("" + wishlistModel.getProductName());
        holder.binding.wishlistFragmentProductDescriptionTextView.setText("" + wishlistModel.getProductDescription());
        holder.binding.wishlistFragmentProductPriceTextView.setText("" + wishlistModel.getProductPrice());
        holder.binding.wishlistFragmentProductDeliveryEstimateTextView.setText("Deliver estimate.");

        holder.binding.wishlistFragmentRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("users").child(currentUser.getUid()).child("wishlist").child(wishlistModel.getProductId()).removeValue();
            }
        });

        holder.binding.wishlistFragmentAddtoCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("users").child(currentUser.getUid()).child("cart_items").child(wishlistModel.getProductId()).setValue(new CartModel(wishlistModel.getProductName(), wishlistModel.getProductDescription(), wishlistModel.getProductCategory(), wishlistModel.getProductImage(), wishlistModel.getProductPrice(), wishlistModel.getProductId(), 1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Successfully added to cart.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to add to cart!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlistArraylist.size();
    }

    public class WishlistAdapterViewHolder extends RecyclerView.ViewHolder {
        WishlistItemSampleLayoutBinding binding;
        public WishlistAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = WishlistItemSampleLayoutBinding.bind(itemView);
        }
    }
}
