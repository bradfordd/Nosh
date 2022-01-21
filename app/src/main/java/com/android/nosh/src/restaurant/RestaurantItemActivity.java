package com.android.nosh.src.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.android.nosh.R;
import com.android.nosh.src.customer.CartActivity;
import com.android.nosh.src.customer.CustomerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RestaurantItemActivity extends Activity {

    private ImageButton cartButton, homeButton;
    private Button addButton;
    private MenuItem item;
    private int id;
    private CheckBox sixIn, ftlong, wheat, italian, herbNCheese, flat, combo;
    private EditText customizations;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_restaurantitem);

        addButton = findViewById(R.id.addButton);
        cartButton = findViewById(R.id.cartButton);
        homeButton = findViewById(R.id.homeButton);
        sixIn = findViewById(R.id.sixInCheckBox);
        ftlong = findViewById(R.id.ftLongCheckBox);
        wheat = findViewById(R.id.wheatCheckBox);
        italian = findViewById(R.id.italianCheckBox);
        herbNCheese = findViewById(R.id.herbCheckBox);
        flat = findViewById(R.id.flatBreadCheckBox);
        combo = findViewById(R.id.comboCheckBox);
        customizations = findViewById(R.id.customizeEditTxt);

        id = getIntent().getIntExtra("ITEM_ID", -1);

        MenuItemData data = new MenuItemData();
        item = data.getMenuItems().get(id);


        homeButton.setOnClickListener(v -> goHome());
        cartButton.setOnClickListener(v -> goCart());
        addButton.setOnClickListener(v -> addToCart(item));
    }

    private void goHome() {
        Intent intent = new Intent(RestaurantItemActivity.this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goCart() {
        Intent intent = new Intent(RestaurantItemActivity.this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void addToCart(MenuItem item){
        // Sandwich size
        if(sixIn.isChecked()){
            item.setFtlong(false);
        }
        else if (ftlong.isChecked()){
            item.setFtlong(true);
        }

        // Bread type
        if(wheat.isChecked()){
            item.setBread("Wheat");
        }
        else if (italian.isChecked()){
            item.setBread("Italian");
        }
        else if (herbNCheese.isChecked()){
            item.setBread("Italian Herbs and Cheese");
        }
        else if (flat.isChecked()){
            item.setBread("Flatbread");
        }

        // Is combo
        if(combo.isChecked()){
            item.setCombo(true);
        }

        item.setPrice();

        // Add customer notes
        item.setCustomizations(customizations.getText().toString().trim());

        // Add to cart
        AddToCartOnFirebase(item);

        goCart();
    }

    private void AddToCartOnFirebase(MenuItem item) {

        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String itemID = FirebaseDatabase.getInstance().getReference().child("carts").child(currentUserID).push().getKey();
        DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference().child("carts").child(currentUserID).child(itemID);
        item.setItemID(itemID);
        cartItemRef.setValue(item);

    }
}
