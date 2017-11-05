package com.example.sagar.popupshops_buyerside.Utility;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child(FirebaseEndpoint.ROOTS.USERS);
    }

}
