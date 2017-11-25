package com.example.sagar.popupshops_buyerside.Shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
    private List<String> itemIds;
    private RecyclerView rv;
    private RVAdapter rvAdapter;
    private String shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        Bundle extras = getIntent().getExtras();
        shopID = extras.getString("shopID");

        initializeData();


//        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
//        FAB.setOnClickListener(new FloatingActionButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //remove from dp here
//            }
//        });
    }

    private void initializeData() {
        items = new ArrayList<>();
        itemIds = new ArrayList<>();

        if (shopID.equals("self")) {
            FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
                @Override
                public void OnComplete(String value) {
                    shopID = value;
                    addItemsToView(shopID);
                }
            });
        } else {
            addItemsToView(shopID);
        }

//        FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
//            @Override
//            public void OnComplete(String value) {
//                final Query itemQuery = FirebaseUtils.getItemRef().orderByChild(FirebaseEndpoint.ITEMS.SHOPID).equalTo(value);
//                itemQuery.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot itemSnapshot, String s) {
//                        Log.d(TAG, "onChildAdded:" + itemSnapshot.getKey());
//                        Log.w("children count", "" + itemSnapshot.getChildrenCount());
//                        if (itemSnapshot.getChildrenCount() == Item.getAttributeCount()) {
//                            boolean added = items.add(
//                                    new Item
//                                            (
//                                                    itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMCATEGORY).getValue().toString(),
//                                                    Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMPRICE).getValue().toString()),
//                                                    itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMDESCRIPTION).getValue().toString(),
//                                                    itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMIMAGE).getValue().toString(),
//                                                    Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMSTOCK).getValue().toString())
//                                            )
//                            );
//                            itemIds.add(itemSnapshot.getKey());
//                            Log.w(TAG, String.valueOf(added));
//                            rvAdapter = new RVAdapter(items);
//                            rv.setAdapter(rvAdapter);
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//
//                        String itemKey = dataSnapshot.getKey();
//
//                        int itemIndex = itemIds.indexOf(itemKey);
//                        if (itemIndex > -1) {
//                            // Remove data from the list
//                            itemIds.remove(itemIndex);
//                            items.remove(itemIndex);
//
//                            // Update the RecyclerView
//                            rvAdapter.notifyItemRemoved(itemIndex);
//                        } else {
//                            Log.w(TAG, "onChildRemoved:unknown_child:" + itemKey);
//                        }
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "itemsDisplay:onCancelled", databaseError.toException());
//                        Toast.makeText(getParent().getBaseContext(), "Failed to load items.",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//
//            }
//        });

    }

    public void addItemsToView(String shopID) {
        //TODO remove child event listener on app stop

        final Query itemQuery = FirebaseUtils.getItemRef().orderByChild(FirebaseEndpoint.ITEMS.SHOPID).equalTo(shopID);
        itemQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot itemSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + itemSnapshot.getKey());
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
                    itemIds.add(itemSnapshot.getKey());
                    Log.w(TAG, String.valueOf(added));
                    rvAdapter = new RVAdapter(items);
                    rv.setAdapter(rvAdapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String itemKey = dataSnapshot.getKey();

                int itemIndex = itemIds.indexOf(itemKey);
                if (itemIndex > -1) {
                    // Remove data from the list
                    itemIds.remove(itemIndex);
                    items.remove(itemIndex);

                    // Update the RecyclerView
                    rvAdapter.notifyItemRemoved(itemIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + itemKey);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "itemsDisplay:onCancelled", databaseError.toException());
                Toast.makeText(getParent().getBaseContext(), "Failed to load items.",
                        Toast.LENGTH_SHORT).show();
            }

        });

    }


}