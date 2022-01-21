package com.android.nosh.src.restaurant;

public class MenuItem {

    private String mItemName, mItemDescription, mBread, mCustomizations, mItemID;
    private int mItemImage;
    private boolean mFtlong, mCombo;
    private double mPrice;

    public MenuItem(){}

    public MenuItem(String itemName,
                    String itemDescription,
                    int itemImage,
                    String itemID,
                    double price,
                    String bread,
                    String customizations,
                    boolean ftlong,
                    boolean combo) {
        this.mItemName = itemName;
        this.mItemDescription = itemDescription;
        this.mItemImage = itemImage;
        this.mItemID = itemID;
        this.mPrice = price;
        this.mBread = bread;
        this.mCustomizations = customizations;
        this.mFtlong = ftlong;
        this.mCombo = combo;
    }

    public void setItemID(String itemID) { this.mItemID = itemID;}

    public String getItemID() { return mItemID;}

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        this.mItemName = itemName;
    }

    public String getItemDescription() {
        return mItemDescription;
    }

    public void setItemDescription(String itemDescription){this.mItemDescription = itemDescription;}

    public int getItemImage() {
        return mItemImage;
    }
    public void setItemImage(int itemImage){ this.mItemImage = itemImage;}

    public boolean getFtlong() {
        return mFtlong;
    }

    public void setFtlong(boolean ftlong) {
        this.mFtlong = ftlong;
    }

    public String getBread() {
        return mBread;
    }

    public void setBread(String bread) {
        this.mBread = bread;
    }

    public String getCustomizations() {
        return mCustomizations;
    }

    public void setCustomizations(String customizations) {
        this.mCustomizations = customizations;
    }

    public boolean getCombo() {
        return mCombo;
    }

    public void setCombo(boolean combo) {
        this.mCombo = combo;
    }

    public double getPrice() {
        return this.mPrice;
    }

    public void setPrice(double price){
        this.mPrice = price;
    }

    public void setPrice() {
        if(getFtlong()){
            mPrice = 9.99;
        }
        else{
            mPrice = 4.99;
        }
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "itemName='" + mItemName + '\'' +
                ", itemDescription='" + mItemDescription + '\'' +
                ", bread='" + mBread + '\'' +
                ", customizations='" + mCustomizations + '\'' +
                ", itemImage=" + mItemImage +
                ", ftlong=" + mFtlong +
                ", combo=" + mCombo +
                '}';
    }
}
