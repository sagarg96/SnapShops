package com.example.sagar.popupshops_buyerside.BuyerRecycleView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Shop.Item;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class BuyerRecycle extends AppCompatActivity {

    private static final String TAG = "BuyerRecycle";
    private List<Item> items;
    private List<String> itemIds;
    private RecyclerView rv;
    private BuyerRecycleAdapter buyerRecycleAdapter;
    private String shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_recycle);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();


    }

    private void initializeData() {
        items = new ArrayList<>();
        itemIds = new ArrayList<>();

        addItemsToView();


    }

    public void addItemsToView() {
        //TODO remove child event listener on app stop

        final Query itemQuery = FirebaseUtils.getWishListRef().orderByKey().equalTo(FirebaseUtils.getCurrentUser().getUid());
        itemQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot itemSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + itemSnapshot.getKey());
                Log.w("children count", "" + itemSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : itemSnapshot.getChildren()) {
                    Log.w("children count here", "" + snapshot.getChildrenCount());
                    if (snapshot.getChildrenCount() == Item.getAttributeCount()) {
                        Item item = new Item
                                (
                                        snapshot.child(FirebaseEndpoint.ITEMS.ITEMCATEGORY).getValue().toString(),
                                        Float.parseFloat(snapshot.child(FirebaseEndpoint.ITEMS.ITEMPRICE).getValue().toString()),
                                        snapshot.child(FirebaseEndpoint.ITEMS.ITEMDESCRIPTION).getValue().toString(),
                                        snapshot.child(FirebaseEndpoint.ITEMS.ITEMIMAGE).getValue().toString(),
                                        Integer.parseInt(snapshot.child(FirebaseEndpoint.ITEMS.ITEMSTOCK).getValue().toString()),
                                        snapshot.getKey()
                                );
                        item.setShopID(snapshot.child(FirebaseEndpoint.ITEMS.SHOPID).getValue().toString());
                        boolean added = items.add(item);
                        try {
                            itemIds.add(snapshot.getKey());
                            Log.w(TAG, String.valueOf(added));
                            buyerRecycleAdapter = new BuyerRecycleAdapter(items);
                            rv.setAdapter(buyerRecycleAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String itemKey = snapshot.getKey();

                    int itemIndex = itemIds.indexOf(itemKey);
                    if (itemIndex > -1) {
                        // Remove data from the list
                        itemIds.remove(itemIndex);
                        items.remove(itemIndex);

                        // Update the RecyclerView
                        buyerRecycleAdapter.notifyItemRemoved(itemIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + itemKey);
                    }
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
