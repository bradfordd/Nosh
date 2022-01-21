package com.android.nosh.src.restaurant;

import com.android.nosh.R;

import java.util.ArrayList;

public class MenuItemData {

    private ArrayList<MenuItem> menuItems;

    public MenuItemData(){
        menuItems = new ArrayList<>();

        menuItems.add(new MenuItem(
                "Buffalo Chicken Sub",
                "Buffalo Chicken footlong with hot sauce and ranch.",
                 R.mipmap.buffalochicken,
                "",
                0.0,
                "",
                "",
                false,
                false
                )
        );

        menuItems.add(new MenuItem(
                "Chicken Bacon Sub",
                "Chicken and Bacon sandwich with Ranch and Cheddar",
                 R.mipmap.chickenbacon,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Chicken Teriyaki",
                "White chicken strips with Sweet Onions",
                 R.mipmap.chickenteriyaki,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Ham Sandwich",
                "Ham Sandwich with Salami and Bologna",
                 R.mipmap.hamsandwich,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Italian BMT",
                "Salami, Spicy Pepperoni, and Black Forrest Ham",
                 R.mipmap.italianbmt,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Roasted Chicken Sub",
                "Roasted Chicken with vegetables",
                 R.mipmap.roastedchicken,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Spicy Italian Sub",
                "Sub with salami, cheese, and vegetables",
                 R.mipmap.spicyitalian,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Steak and Cheese",
                "Steak and melted cheddar",
                 R.mipmap.steakcheese,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Tuna Sub",
                "Tuna sandwich with mayonnaise and vegetables",
                 R.mipmap.tuna,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

        menuItems.add(new MenuItem(
                "Turkey Sub",
                "Sub containing roasted Turkey",
                 R.mipmap.turkeysandwich,
                "",
                0.0,
                "",
                "",
                false,
                false)
        );

    }

    public ArrayList<MenuItem> getMenuItems(){
        return menuItems;
    }
}
