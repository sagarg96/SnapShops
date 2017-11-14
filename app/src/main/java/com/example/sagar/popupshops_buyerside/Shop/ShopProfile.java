package com.example.sagar.popupshops_buyerside.Shop;

import com.example.sagar.popupshops_buyerside.Registration.GetLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShopProfile {

    private String shopName;
    private Map<String, Double> location; //need to check datatype
    private String description;
    private ArrayList<Item> items;
    private ShopStatus shopStatus;
    private String userID;

    public ShopProfile() {

    }

    public ShopProfile(String shopName, String description, double locationLatitude, double locationLongitude, String userID) {
        this.shopName = shopName;
        this.description = description;
        this.items = new ArrayList<>();
        this.location = GetLocation.getLocation();
//        location = new HashMap<>();
//        location.put("latitude", locationLatitude);
//        location.put("longitude", locationLongitude);
        shopStatus = ShopStatus.OPEN;
        this.userID = userID;
    }

    public String getShopName() {
        return shopName;
    }

    public Map<String, Double> getLocation() {
        return location;
    }

    public ShopStatus getShopStatus() {
        return shopStatus;
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

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "ShopProfile{" +
                "shopName='" + shopName + '\'' +
                ", location=" + location +
                ", description='" + description + '\'' +
                ", items=" + items +
                ", shopStatus=" + shopStatus +
                '}';
    }
}
