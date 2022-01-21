package com.android.nosh.src.deliveryuser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.nosh.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryActivity extends Activity {

    private Button acceptOrderButton, currentOrderButton;
    private ArrayList<DeliveryOrder> orders;
    private RecyclerView rvOrderQueue;
    private static Map<DeliveryOrder, CheckBox> listOfCheckBoxes;
    private FirebaseRecyclerOptions<DeliveryOrder> options;
    private FirebaseRecyclerAdapter<DeliveryOrder, OrderHolder> adapter;
    private String currentUserID;
    private DatabaseReference queueRef;
    private boolean orderInProgress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery);

        orders = new ArrayList<>();

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        queueRef = FirebaseDatabase.getInstance().getReference().child("queue");

        acceptOrderButton = findViewById(R.id.acceptOrderButton);
        acceptOrderButton.setOnClickListener(v -> acceptOrder());
        currentOrderButton = findViewById(R.id.currentOrderButton);
        currentOrderButton.setOnClickListener(v -> goToOrder());

        listOfCheckBoxes = new HashMap<>();

        Query query = queueRef.orderByChild("timeStamp").limitToLast(10);

        rvOrderQueue = findViewById(R.id.rvActiveOrders);
        rvOrderQueue.setLayoutManager(new LinearLayoutManager(this));
//        rvOrderQueue.setHasFixedSize(true);

        DecimalFormat df = new DecimalFormat("0.00");

        options = new FirebaseRecyclerOptions.Builder<DeliveryOrder>()
                .setQuery(query, DeliveryOrder.class).build();

        adapter = new FirebaseRecyclerAdapter<DeliveryOrder, OrderHolder>(options) {

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.model_activeorders, parent, false);
                return new OrderHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull DeliveryOrder model) {
                listOfCheckBoxes.put(model, holder.getOrderBox());
                orders.add(model);

                holder.getOrderBox().setText(model.getName());
                holder.getOrderName().setText(model.getOrderID());
                holder.getOrderCost().setText(df.format(model.getPrice()));
            }
        };

        orderInProgress = isAnOrderInProgress();

        if(orderInProgress)
            currentOrderButton.setVisibility(View.VISIBLE);

        rvOrderQueue.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void acceptOrder(){
        DeliveryOrder order = isOnlyOneOrderChecked();
        if(order != null && !orderInProgress) {
            order.setDeliveryUserID(currentUserID);
            DatabaseReference standbyRef = FirebaseDatabase.getInstance().getReference()
                    .child("standby")
                    .child(currentUserID);

            DatabaseReference queueOrderRef = FirebaseDatabase.getInstance().getReference()
                    .child("queue")
                    .child(order.getOrderID());

            standbyRef.setValue(order).addOnCompleteListener(task -> {
                queueOrderRef.removeValue((error, ref) -> {
                    DatabaseReference deliveryStatusRef = FirebaseDatabase.getInstance().getReference().child("deliveryStatus").child(order.getUserID());
                    deliveryStatusRef.setValue(order);
                    goToOrder();
                });
            });
        }
        else if(orderInProgress){
            Toast.makeText(this, "An order is already in progress. Click the See Current Order button to finish the current order", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAnOrderInProgress() {
        DatabaseReference standbyRef = FirebaseDatabase.getInstance().getReference().child("standby");
        DatabaseReference deliveringRef = FirebaseDatabase.getInstance().getReference().child("delivering");
        orderInProgress = false;

        standbyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(currentUserID)) {
                    orderInProgress = true;
                    currentOrderButton.setVisibility(View.VISIBLE);
                }
                else{
                    deliveringRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(currentUserID)) {
                                orderInProgress = true;
                                currentOrderButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return orderInProgress;
    }

    private void goToOrder() {
        Intent intent = new Intent(DeliveryActivity.this, DeliveryPickupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private DeliveryOrder isOnlyOneOrderChecked() {
        DeliveryOrder isOneOrderChecked = null;
        for(DeliveryOrder order: listOfCheckBoxes.keySet()){
            CheckBox box = listOfCheckBoxes.get(order);
            if(box.isChecked() && isOneOrderChecked == null){
                isOneOrderChecked = order;
            }
            else if(box.isChecked() && isOneOrderChecked != null){
                Toast.makeText(DeliveryActivity.this, "Please choose only one order.", Toast.LENGTH_LONG).show();
                return null;
            }
        }

        return isOneOrderChecked;
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        private final TextView orderName, orderCost;
        private final CheckBox orderBox;
        private final View view;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            orderBox = (CheckBox) view.findViewById(R.id.orderCheckbox);
            orderName = (TextView) view.findViewById(R.id.orderName);
            orderCost = (TextView) view.findViewById(R.id.orderCost);

        }

        public TextView getOrderName(){return orderName;}
        public CheckBox getOrderBox(){return orderBox;}
        public TextView getOrderCost(){return orderCost;}
        public View getView(){return view;}
    }
}
