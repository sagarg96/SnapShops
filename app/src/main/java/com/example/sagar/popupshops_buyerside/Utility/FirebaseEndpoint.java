package com.example.sagar.popupshops_buyerside.Utility;


public class FirebaseEndpoint {

    public interface ROOTS {
        String USERS = "users";
        String SHOPS = "shops";
    }

    public interface USERS {
        String USERNAME = "userName";
        String EMAIL = "email";
        String SHOPS = "shops";
    }

    public interface SHOPS {
        String SHOPNAME = "shopName";
        String DESCRIPTION = "description";
        String LOCATION = "location";
        String SHOPSTATUS = "shopStatus";
        String USERID = "userID";
    }
}
