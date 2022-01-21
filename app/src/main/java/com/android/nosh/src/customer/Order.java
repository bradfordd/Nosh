package com.android.nosh.src.customer;

import com.android.nosh.src.restaurant.MenuItem;

import java.util.ArrayList;

public class Order {

    private ArrayList<MenuItem> mItemsOrdered;
    private String mOrderID, mUserID, mName;
    private long mTimestamp;
    private double mPrice;


    public Order(){}

    public Order(ArrayList<MenuItem> itemsOrdered,
                 String orderID,
                 long timestamp,
                 String userID,
                 String name,
                 double price){
        setItemsOrdered(itemsOrdered);
        setOrderID(orderID);
        setTimeStamp(timestamp);
        setUserID(userID);
        setName(name);
        setPrice(price);
    }

    private void setPrice(double price) { mPrice = price; }

    private void setName(String name) { mName = name; }

    private void setUserID(String userID) {
        this.mUserID = userID;
    }

    public void setTimeStamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    public void setOrderID(String orderID) {
        this.mOrderID = orderID;
    }

    public void setItemsOrdered(ArrayList<MenuItem> itemsOrdered) { this.mItemsOrdered = itemsOrdered; }

    public long getTimeStamp() {
        return mTimestamp;
    }

    public String getOrderID() {
        return mOrderID;
    }

    public ArrayList<MenuItem> getItemsOrdered() {
        return mItemsOrdered;
    }

    public String getUserID() {
        return mUserID;
    }

    public String getName() {return mName;}

    public double getPrice() {return mPrice;};

}
