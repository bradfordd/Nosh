package com.android.nosh.src.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.nosh.R;
import com.android.nosh.src.restaurant.RestaurantListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CustomerActivity extends Activity {

    private TextView nameView;
    private Button startOrder, trackOrder;
    private ImageButton cartButton;
    private String userName;
    private FirebaseAuth mAuth;
    private DatabaseReference userNameRef;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer);

        // Set vars
        mAuth = FirebaseAuth.getInstance();
        nameView = findViewById(R.id.homeTextView);
        startOrder = findViewById(R.id.startOrderButton);
        trackOrder = findViewById(R.id.trackOrderbutton);
        cartButton = findViewById(R.id.cartButton);

        displayName();
        startOrder.setOnClickListener(v -> StartOrder());
        trackOrder.setOnClickListener(v -> TrackOrder());
        cartButton.setOnClickListener(v -> toCart());
    }

    private void displayName(){

        // Get name of user from db
        String userUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        userNameRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("name");

        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = Objects.requireNonNull(snapshot.getValue()).toString();
                nameView.setText("Hello, " + userName + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void StartOrder() {

        Intent intent = new Intent(getBaseContext(), RestaurantListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void TrackOrder() {

        Intent intent = new Intent(getBaseContext(), CustomerOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void toCart() {

        Intent intent = new Intent(getBaseContext(), CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
