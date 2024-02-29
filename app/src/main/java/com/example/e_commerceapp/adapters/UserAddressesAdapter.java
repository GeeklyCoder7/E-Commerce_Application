package com.example.e_commerceapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.UserAddressesSampleLayoutBinding;
import com.example.e_commerceapp.models.AddressModel;
import com.example.e_commerceapp.utils.ConstantValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class UserAddressesAdapter extends RecyclerView.Adapter<UserAddressesAdapter.UserAddressesAdapterViewHolder> {
    ArrayList<AddressModel> userAddressesArrayList;
    Context context;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    public UserAddressesAdapter(ArrayList<AddressModel> userAddressesArrayList, Context context) {
        this.userAddressesArrayList = userAddressesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAddressesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAddressesAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.user_addresses_sample_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAddressesAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        AddressModel addressModel = userAddressesArrayList.get(position);

        holder.binding.userAddressesLayoutNameTextView.setText("" + addressModel.getFirstAndLastName());

        holder.binding.userAddressesMobileNoTextView.setText("" + addressModel.getMobileNumber());

        String completeUserAddress = addressModel.getBuildingNameAndHouseNo() + ", (" + addressModel.getLandmarkName() + ") , " +  addressModel.getAreaName() + ", " + addressModel.getCityName() + ", " + addressModel.getStateName() + ", " + addressModel.getDistrictName() + ", " + " - " + addressModel.getPinCode();

        holder.binding.userAddressesCompleteAddressTextView.setText(completeUserAddress);
        holder.binding.setAsDefaultRadioButton.setChecked(addressModel.isDefault());
        holder.binding.setAsDefaultRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addressModel.isDefault()) {
                    updateAddressAndButtonStatus(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userAddressesArrayList.size();
    }

    void updateAddressAndButtonStatus(int currentPosition) {
        for (int i = 0; i < userAddressesArrayList.size(); i++) {
            AddressModel addressModel = userAddressesArrayList.get(i);
            if (i == currentPosition) {
                addressModel.setDefault(true);
            } else {
                addressModel.setDefault(false);
            }
            databaseReference.child("users").child(firebaseUser.getUid())
                    .child("user_addresses").child(addressModel.getAddressId())
                    .child("default").setValue(addressModel.isDefault()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Default address changed successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to change default address!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static class UserAddressesAdapterViewHolder extends RecyclerView.ViewHolder {
        UserAddressesSampleLayoutBinding binding;
        public UserAddressesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserAddressesSampleLayoutBinding.bind(itemView);
        }
    }
}
