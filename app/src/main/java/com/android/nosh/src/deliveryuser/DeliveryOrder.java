package com.android.nosh.src.deliveryuser;

import com.android.nosh.src.customer.Order;
import com.android.nosh.src.restaurant.MenuItem;

import java.util.ArrayList;

public class DeliveryOrder extends Order {

    private double mLat, mLon;
    private boolean mIsEnRoute, mOrderDelivered;
    private String mDeliveryUserID;

    public DeliveryOrder() {super();}

    public DeliveryOrder(ArrayList<MenuItem> itemsOrdered,
                         String orderID,
                         long timestamp,
                         String userID,
                         String name,
                         double price,
                         double lat,
                         double lon,
                         boolean isEnRoute,
                         String deliveryUserID,
                         boolean orderDelivered){
        super(itemsOrdered, orderID, timestamp, userID, name, price);
        this.mIsEnRoute = isEnRoute;
        this.mLat = lat;
        this.mLon = lon;
        this.mDeliveryUserID = deliveryUserID;
        this.mOrderDelivered = orderDelivered;
    }

    public DeliveryOrder(Order order,
                         double lat,
                         double lon,
                         boolean isEnRoute,
                         String deliveryUserID,
                         boolean delivered){
        super(order.getItemsOrdered(), order.getOrderID(), order.getTimeStamp(), order.getUserID(), order.getName(), order.getPrice());
        this.mIsEnRoute = isEnRoute;
        this.mLat = lat;
        this.mLon = lon;
        this.mDeliveryUserID = deliveryUserID;
        this.mOrderDelivered = delivered;

    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double mLon) {
        this.mLon = mLon;
    }

    public boolean isIsEnRoute() {
        return mIsEnRoute;
    }

    public void setIsEnRoute(boolean mIsEnRoute) {
        this.mIsEnRoute = mIsEnRoute;
    }

    public String getDeliveryUserID() { return mDeliveryUserID; }

    public void setDeliveryUserID(String mDeliveryUserID) { this.mDeliveryUserID = mDeliveryUserID; }

    public boolean isOrderDelivered() {
        return mOrderDelivered;
    }

    public void setOrderDelivered(boolean orderDelivered) {
        this.mOrderDelivered = orderDelivered;
    }
}
