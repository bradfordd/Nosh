package com.android.nosh.src.deliveryuser;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.nosh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryPickupActivity extends Activity {

    private ImageButton homeButton;
    private Button markPickedUp, markDelivered;
    private TextView nameView, orderView;
    private DeliveryOrder currentOrder;
    private DatabaseReference currentOrderRef;
    private DatabaseReference orderRef;
    private ValueEventListener ve;
    private DatabaseReference deliveringRef;
    private String currentUserID;
    private DatabaseReference deliveryStatusRef;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_deliverypickup);

        nameView = findViewById(R.id.nameView);
        orderView = findViewById(R.id.orderView);

        currentOrder = null;

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        orderRef = FirebaseDatabase.getInstance().getReference().child("standby").child(currentUserID);
        ve = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentOrder = snapshot.getValue(DeliveryOrder.class);
                currentOrderRef = snapshot.getRef();
                nameView.setText("Name: " + currentOrder.getName());
                if(currentOrder.isIsEnRoute())
                    orderView.setText("Status: Delivering");
                else
                    orderView.setText("Status: Ready for delivery");
                deliveryStatusRef = FirebaseDatabase.getInstance().getReference().child("deliveryStatus").child(currentOrder.getUserID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        orderRef.addValueEventListener(ve);


        homeButton = findViewById(R.id.deliveryHomeButton);
        markPickedUp = findViewById(R.id.pickedUpButton);
        markDelivered = findViewById(R.id.deliveredButton);

        homeButton.setOnClickListener(v -> goHome());
        markPickedUp.setOnClickListener(v -> pickedUp());
        markDelivered.setOnClickListener(v -> delivered());
    }

    private void goHome(){
        Intent intent = new Intent(DeliveryPickupActivity.this, DeliveryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void pickedUp(){
        currentOrder.setIsEnRoute(true);
        currentOrderRef.child("isEnRoute").setValue(true);
        deliveringRef = FirebaseDatabase.getInstance().getReference().child("delivering").child(currentUserID);
        deliveringRef.setValue(currentOrder);
        deliveryStatusRef.setValue(currentOrder);
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryPickupActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        else
            startLocationService();
    }

    private void delivered(){
        currentOrder.setOrderDelivered(true);
        orderRef.removeEventListener(ve);
        currentOrderRef.removeValue();
        deliveringRef.removeValue();
        deliveryStatusRef.setValue(currentOrder);
        stopLocationService();
        goHome();
    }

    private boolean isLocationServiceRunning(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        if(am != null){
            for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE))
                if(LocationService.class.getName().equals(service.service.getClassName()))
                    if(service.foreground)
                        return true;
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
        }
    }

    private void stopLocationService() {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }
            else{}
        }
    }
}