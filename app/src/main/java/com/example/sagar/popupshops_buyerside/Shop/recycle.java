package com.example.sagar.popupshops_buyerside.Shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class recycle extends AppCompatActivity {

    private static final String TAG = "recycle";
    private List<Item> items;
    private RecyclerView rv;
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        //TODO possible issue here initialise adapter after data added
//        initializeAdapter();

//        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
//        FAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //remove from dp here
//            }
//        })
    }

    private void initializeData() {
        items = new ArrayList<>();
        //items.add(new Item("hello",0,"hello","hello",0));

        FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
            @Override
            public void OnComplete(String value) {
                final Query itemQuery = FirebaseUtils.getItemRef().orderByChild(FirebaseEndpoint.ITEMS.SHOPID).equalTo(value);
                itemQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot itemSnapshot, String s) {
                        Log.w("children count", "" + itemSnapshot.getChildrenCount());
                        if (itemSnapshot.getChildrenCount() == Item.getAttributeCount()) {
                            boolean added = items.add(
                                    new Item
                                            (
                                                    itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMCATEGORY).getValue().toString(),
                                                    Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMPRICE).getValue().toString()),
                                                    itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMDESCRIPTION).getValue().toString(),
                                                    itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMIMAGE).getValue().toString(),
                                                    Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMSTOCK).getValue().toString())
                                            )
                            );

                            Log.w(TAG, String.valueOf(added));
                            rvAdapter = new RVAdapter(items);
                            rv.setAdapter(rvAdapter);
                        }


//                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                            Log.w("here", itemSnapshot.getKey());
//                            Log.w("here", itemSnapshot.getValue().toString());
//                            if (itemSnapshot.getChildrenCount() == Item.getAttributeCount()) {
//                                items.add(
//                                        new Item
//                                                (
//                                                        itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMCATEGORY).getValue().toString(),
//                                                        Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMPRICE).getValue().toString()),
//                                                        itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMDESCRIPTION).getValue().toString(),
//                                                        itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMIMAGE).getValue().toString(),
//                                                        Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMSTOCK).getValue().toString())
//                                                )
//                                );
//                            }
////                                items.add(itemSnapshot.getValue(Item.class));
//                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        });

        //connect to DB
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(items);
        rv.setAdapter(adapter);
    }
}