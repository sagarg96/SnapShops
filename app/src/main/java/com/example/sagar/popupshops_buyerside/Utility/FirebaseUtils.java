package com.example.sagar.popupshops_buyerside.Utility;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child(FirebaseEndpoint.ROOTS.USERS);
    }

    public static DatabaseReference getShopsRef() {
        return getBaseRef().child(FirebaseEndpoint.ROOTS.SHOPS);
    }

    public static DatabaseReference getItemRef() {
        return getBaseRef().child(FirebaseEndpoint.ROOTS.ITEM);
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface Callback {
        void OnComplete(String value);
    }

}
