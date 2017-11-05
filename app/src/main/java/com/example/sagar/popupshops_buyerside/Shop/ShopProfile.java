package com.example.sagar.popupshops_buyerside.Shop;

import java.util.ArrayList;


public class ShopProfile {

    private String shopName;
    private String location; //need to check datatype
    private String description;
    private ArrayList<Item> items;
    private ShopStatus shopStatus;

    public ShopProfile() {

    }

    public ShopProfile(String shopName, String location, String description) {
        this.shopName = shopName;
        this.location = location;
        this.description = description;
        this.items = new ArrayList<>();

    }

    public String getShopName() {
        return shopName;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item newItem) {
        items.add(newItem);
    }

}
