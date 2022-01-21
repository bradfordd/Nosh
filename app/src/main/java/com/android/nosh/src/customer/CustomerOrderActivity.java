package com.android.nosh.src.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.nosh.R;
import com.android.nosh.src.deliveryuser.DeliveryOrder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class CustomerOrderActivity extends FragmentActivity implements OnMapReadyCallback{

    private ImageButton homeButton, cartButton;
    private GoogleMap mMap;
    private TextView orderID, orderStatus;
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private String currentUserID;
    private DeliveryOrder currentOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customerorder);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference deliveryStatusRef = FirebaseDatabase.getInstance().getReference().child("deliveryStatus").child(currentUserID);

        deliveryStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentOrder = snapshot.getValue(DeliveryOrder.class);
                subscribeToUpdates();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderID = findViewById(R.id.orderIdView);
        orderStatus = findViewById(R.id.orderStatusView);
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> goHome());
        cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> toCart());

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
    }

    private void toCart() {
        Intent intent = new Intent(CustomerOrderActivity.this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void goHome(){
        Intent intent = new Intent(CustomerOrderActivity.this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void subscribeToUpdates(){
        if(currentOrder != null) {
            orderID.setText(String.format("%s", currentOrder.getOrderID()));
            if(currentOrder.isOrderDelivered())
                orderStatus.setText("Delivered");
            else if(currentOrder.isIsEnRoute())
                orderStatus.setText(R.string.on_the_way_status_msg);
            else
                orderStatus.setText(R.string.picked_up_status_msg);
            if(currentOrder.getDeliveryUserID() != null && !currentOrder.isOrderDelivered()) {
                DatabaseReference deliveryRef = FirebaseDatabase.getInstance().getReference().child("delivering").child(currentOrder.getDeliveryUserID());
                deliveryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentOrder = snapshot.getValue(DeliveryOrder.class);
                        if(currentOrder != null)
                            if (currentOrder.getLat() != 0.0 && currentOrder.getLon() != 0.0)
                                setMarker(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void setMarker(DataSnapshot snap){
        String key = snap.getKey();
        DeliveryOrder order = snap.getValue(DeliveryOrder.class);
        assert order != null;
        double lat = order.getLat();
        double lon = order.getLon();
        LatLng location = new LatLng(lat, lon);
        if(!mMarkers.containsKey(key)){
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
        }
        else{
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Marker marker : mMarkers.values())
            builder.include(marker.getPosition());
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }
}
