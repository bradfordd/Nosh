package com.android.nosh.src.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.nosh.R;
import com.android.nosh.src.customer.CartActivity;
import com.android.nosh.src.customer.CustomerActivity;

import java.util.ArrayList;

public class RestaurantMenuActivity extends Activity {

    private ImageButton homeButton, cartButton;
    private RecyclerView rvMenuItems;
    private static ArrayList<MenuItem> cart;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_restaurantmenu);

        homeButton = findViewById(R.id.homeButton);
        cartButton = findViewById(R.id.cartButton);
        rvMenuItems = (RecyclerView) findViewById(R.id.rvMenuItems);

        homeButton.setOnClickListener(v -> goHome());
        cartButton.setOnClickListener(v -> goCart());

        MenuItemAdapter adapter = new MenuItemAdapter(new MenuItemData());

        rvMenuItems.setLayoutManager(new LinearLayoutManager(this));
        rvMenuItems.setHasFixedSize(true);
        rvMenuItems.setAdapter(adapter);
    }


    private void goCart() {
        Intent intent = new Intent(RestaurantMenuActivity.this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goHome() {
        Intent intent = new Intent(RestaurantMenuActivity.this, CustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder> {

        private ArrayList<MenuItem> itemData;

        public MenuItemAdapter() {}

        public MenuItemAdapter(MenuItemData items){
            itemData = items.getMenuItems();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MenuItemAdapter.MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.model_menuitem, parent, false);
            return new MenuItemAdapter.MenuItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
            MenuItem item = itemData.get(position);
            holder.getItemName().setText(item.getItemName());
            holder.getItemName().setClickable(true);
            holder.getItemName().setOnClickListener(v -> holder.goItem(position, cart));

            holder.getItemDescription().setText(item.getItemDescription());
            holder.getItemDescription().setClickable(true);
            holder.getItemDescription().setOnClickListener(v -> holder.goItem(position, cart));

            holder.getItemImage().setImageResource(item.getItemImage());
            holder.getItemImage().setClickable(true);
            holder.getItemImage().setOnClickListener(view -> holder.goItem(position,cart));
        }

        @Override
        public int getItemCount() {
            return itemData.size();
        }

        public class MenuItemHolder extends RecyclerView.ViewHolder {

            private final TextView itemName, itemDescription;
            private final ImageView itemImage;
            private final View view;

            public MenuItemHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                itemName = (TextView) itemView.findViewById(R.id.itemName);
                itemDescription = (TextView) itemView.findViewById(R.id.itemDescription);
                itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            }

            public TextView getItemName(){return itemName;}
            public TextView getItemDescription(){return itemDescription;}
            public ImageView getItemImage(){return itemImage;}
            public View getView(){return view;}

            private void goItem(int id, ArrayList<MenuItem> cart) {
                Intent intent = new Intent(view.getContext(), RestaurantItemActivity.class);
                intent.putExtra("ITEM_ID", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                view.getContext().startActivity(intent);
            }
        }
    }
}
