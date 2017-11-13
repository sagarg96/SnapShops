package com.example.sagar.popupshops_buyerside.Utility;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    public static void getCurrentShopID(@NonNull final Callback callback) {
        Query shopIDQuery = FirebaseUtils.getUsersRef().child(FirebaseUtils.getCurrentUser().getUid()).child(FirebaseEndpoint.USERS.SHOPS);
        shopIDQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean notDone = true;
                if (dataSnapshot.exists()) {
                    if (notDone) {
                        String val = dataSnapshot.getValue().toString();
                        callback.OnComplete(val.substring(1, val.length() - 1));
                        notDone = false;
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("here", "loadPost:onCancelled", databaseError.toException());
                // ...

            }
        });
    }

    public interface Callback {
        void OnComplete(String value);
    }
    public static void logoutUser() {
        FirebaseAuth.getInstance().signOut();
    }

}
