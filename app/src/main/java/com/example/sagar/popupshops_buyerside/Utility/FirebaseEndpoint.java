package com.example.sagar.popupshops_buyerside.Utility;


public class FirebaseEndpoint {

    public interface ROOTS {
        String USERS = "users";
        String SHOPS = "shops";
        String ITEM = "item";
        String SHOP_LOCATION = "shops_location";
        String ITEM_LOCATION = "item_location";
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

    public interface ITEMS {
        String ITEMCATEGORY = "itemCategory";
        String ITEMIMAGE = "itemImage";
        String ITEMPRICE = "itemPrice";
        String ITEMSTOCK = "itemStock";
        String SHOPID = "shopID";
        String ITEMDESCRIPTION = "itemDescription";
    }
}
