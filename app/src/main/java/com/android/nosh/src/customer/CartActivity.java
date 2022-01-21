package com.android.nosh.src.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.nosh.R;
import com.android.nosh.src.deliveryuser.DeliveryOrder;
import com.android.nosh.src.restaurant.MenuItem;
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
import java.util.List;
import java.util.Map;

public class CartActivity extends Activity {

    private TextView subTotTxtView, taxTxtView, deliveryFeeTxtView, totalTxtView;
    private ImageButton homeButton;
    private Button purchaseButton, removeCart;
    private ArrayList<MenuItem> cart;
    private RecyclerView rvCartItems;
    private static Map<String, CheckBox> listOfCheckBoxes;
    private List<Double> prices;
    private double deliveryFee = 2.99, taxRate = 0.08,  subtotal = 0.0, total = 0.0, tax = 0.0;
    private String selectedCombo = "Combo", notCombo = "Not a Combo";
    private FirebaseRecyclerOptions<MenuItem> options;
    private FirebaseRecyclerAdapter<MenuItem, CartItemHolder> adapter;
    private String currentUserID;
    private DatabaseReference cartRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        cart = new ArrayList<>();

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference().child("carts").child(currentUserID);

        // sub total, tax, deliv fee, total

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> goHome());

        listOfCheckBoxes = new HashMap<>();

        removeCart = findViewById(R.id.removeButton);
        removeCart.setOnClickListener(v -> removeFromCart());

        purchaseButton = findViewById(R.id.purchaseButton);
        purchaseButton.setOnClickListener(v -> goOrder());

        Query query = cartRef.orderByKey();

        // Recycler View
        rvCartItems = findViewById(R.id.rvShoppingCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
//        rvCartItems.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<MenuItem>()
                .setQuery(query, MenuItem.class).build();
        
        adapter = new FirebaseRecyclerAdapter<MenuItem, CartItemHolder>(options) {

            @NonNull
            @Override
            public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.model_shoppingcart, parent, false);
                return new CartItemHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CartItemHolder holder, int position, @NonNull MenuItem item) {

                holder.getItemName().setText(item.getItemName());
                listOfCheckBoxes.put(item.getItemID(), holder.getItemName());
//                cart.add(item);

                // combo or not
                if(item.getCombo()) {
                    holder.getItemCombo().setText(selectedCombo);
                }
                else {
                    holder.getItemCombo().setText(notCombo);
                }

                holder.getItemCost().setText("$" + Double.toString(item.getPrice()));
            }
        };

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot menuItemSnap : snapshot.getChildren()) {
                    cart.add(menuItemSnap.getValue(MenuItem.class));
                }

                DecimalFormat df = new DecimalFormat("0.00");

                prices = getTotal();
                subTotTxtView = findViewById(R.id.subTotalTxtView);
                subTotTxtView.setText("$" + df.format(prices.get(0)));
                taxTxtView = findViewById(R.id.taxTextView);
                taxTxtView.setText("$" + df.format(prices.get(1)));
                deliveryFeeTxtView = findViewById(R.id.delFeeTxtView);
                deliveryFeeTxtView.setText("$" + df.format(deliveryFee));
                totalTxtView = findViewById(R.id.totalCostTextView);
                totalTxtView.setText("$" + df.format(prices.get(2)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.notifyDataSetChanged();
        rvCartItems.setAdapter(adapter);

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

    private void goOrder() {
        DatabaseReference orderQueueRef = FirebaseDatabase.getInstance().getReference().child("queue");
        String orderID = orderQueueRef.push().getKey();
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUserID)
                .child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DeliveryOrder order = new DeliveryOrder(
                                cart,
                                orderID,
                                System.currentTimeMillis(),
                                currentUserID,
                                snapshot.getValue().toString(),
                                prices.get(2),
                                0.0,
                                0.0,
                                false,
                                null,
                                false);
                        orderQueueRef.child(orderID).setValue(order);
                        DatabaseReference deliveryStatusRef = FirebaseDatabase.getInstance().getReference().child("deliveryStatus").child(order.getUserID());
                        deliveryStatusRef.setValue(order);
                        cart.clear();
                        cartRef.removeValue();
                        Intent intent = new Intent(CartActivity.this,
                                CustomerOrderActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void goHome(){
        Intent intent = new Intent(CartActivity.this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private List<Double> getTotal(){
        double total = 0.0, subTot =  0.0, tax = 0.0, temp;

        DecimalFormat df = new DecimalFormat("#.##");

        for(int i = 0; i < cart.size(); i++){
            temp = cart.get(i).getPrice();
            subTot += temp;
            if(cart.get(i).getCombo()){
                subTot += 1.49;
            }
        }

        tax = Double.parseDouble(df.format(subTot * taxRate));
        total = Double.parseDouble(df.format(subTot + tax + deliveryFee));

        List<Double> retPrices = new ArrayList<>();
        retPrices.add(subTot);
        retPrices.add(tax);
        if(cart.isEmpty()) {
            total = 0.0;
        }
        retPrices.add(total);
        return retPrices;
    }

    private void removeFromCart(){
        cart.clear();
        for(String itemID : listOfCheckBoxes.keySet()){
            CheckBox checkBox = listOfCheckBoxes.get(itemID);
            if(checkBox.isChecked()){
                cartRef.child(itemID).removeValue();
            }
        }
        prices = getTotal();
        adapter.notifyDataSetChanged();
    }


    public class CartItemHolder extends RecyclerView.ViewHolder {

        private final TextView itemCombo, itemCost;
        private final CheckBox itemName;
        private final View view;

        public CartItemHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            itemName = (CheckBox) itemView.findViewById(R.id.itemCheckbox);
            itemCombo = (TextView) itemView.findViewById(R.id.itemCombo);
            itemCost = (TextView) itemView.findViewById(R.id.itemCost);
        }

        public CheckBox getItemName(){return itemName;}
        public TextView getItemCombo(){return itemCombo;}
        public TextView getItemCost(){return itemCost;}
        public View getView(){return view;}

    }
}
