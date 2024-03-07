package com.example.e_commerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.e_commerceapp.R;
import com.example.e_commerceapp.databinding.ActivityHomeBinding;
import com.example.e_commerceapp.fragments.AddAddressFragment;
import com.example.e_commerceapp.fragments.CartFragment;
import com.example.e_commerceapp.fragments.HomeFragment;
import com.example.e_commerceapp.fragments.OrdersFragment;
import com.example.e_commerceapp.fragments.ProductDetailFragment;
import com.example.e_commerceapp.fragments.ProfileFragment;
import com.example.e_commerceapp.fragments.SearchResultsFragment;
import com.example.e_commerceapp.fragments.UserAddressesFragment;
import com.example.e_commerceapp.fragments.WishlistFragment;
import com.example.e_commerceapp.models.CategoryModel;
import com.example.e_commerceapp.models.ProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    ArrayList<CategoryModel> categoryModelArrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ArrayList<CarouselItem> carouselItemArrayList;
    ArrayList<ProductModel> productModelArrayList;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (item.getItemId() == R.id.homeMenuItem) {
                    loadFragment(new HomeFragment(), true);
                } else if (item.getItemId() == R.id.userMenuItem) {
                    loadFragment(new ProfileFragment(), false);
                } else if (item.getItemId() == R.id.cartMenuItem) {
                    loadFragment(new CartFragment(), false);
                } else if (item.getItemId() == R.id.ordersMenuItem) {
                    loadFragment(new OrdersFragment(), false);
                }
                return false;
            }
        });

        binding.bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);
    }

    void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (flag) {
            fragmentTransaction.add(R.id.mainFrameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    public void openProductDetailsFragment(ProductModel productModel) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("productModel", productModel);
        productDetailFragment.setArguments(bundle);
        loadFragment(productDetailFragment, false);
    }

    public void openSearchResultsFragment(ArrayList<ProductModel> searchResultsArraylist, String searchedText) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchResultsArraylist", searchResultsArraylist);
        bundle.putString("searchedText", searchedText);
        searchResultsFragment.setArguments(bundle);
        loadFragment(searchResultsFragment, false);
    }

    public void openUserAddressesFragment() {
        UserAddressesFragment userAddressesFragment = new UserAddressesFragment();
        loadFragment(userAddressesFragment, false);
    }

    public void openAddAddressesFragment() {
        AddAddressFragment addAddressFragment = new AddAddressFragment();
        loadFragment(addAddressFragment, false);
    }

    public void openWishlistFragment() {
        WishlistFragment wishlistFragment = new WishlistFragment();
        loadFragment(wishlistFragment, false);
    }
}