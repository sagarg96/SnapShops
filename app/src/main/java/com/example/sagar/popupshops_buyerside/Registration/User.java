package com.example.sagar.popupshops_buyerside.Registration;

import java.util.ArrayList;

public class User {

    private String email;
    private String userName;
    private ArrayList<Integer> shops;

    public User() {

    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.shops = new ArrayList<>();
    }

    public ArrayList<Integer> getShops() {
        return shops;
    }

    public void addShop(int newShopID) {
        shops.add(newShopID);
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", shops=" + shops +
                '}';
    }
}
