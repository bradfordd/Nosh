package com.android.nosh.src.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.android.nosh.R;
import com.android.nosh.src.customer.CartActivity;
import com.android.nosh.src.customer.CustomerActivity;

import java.util.ArrayList;

public class RestaurantListActivity extends Activity {

    private ImageButton subwayButton, cartButton, homeButton;
    private ArrayList<MenuItem> cart;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_restaurantlist);

        subwayButton = findViewById(R.id.subwayButton);
        cartButton = findViewById(R.id.cartButton);
        homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(v -> goHome());
        cartButton.setOnClickListener(v -> goCart());
        subwayButton.setOnClickListener(v -> goMenu());
    }

    private void goHome() {
        Intent intent = new Intent(RestaurantListActivity.this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goCart() {
        Intent intent = new Intent(RestaurantListActivity.this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void goMenu() {
        Intent intent = new Intent(RestaurantListActivity.this, RestaurantMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
