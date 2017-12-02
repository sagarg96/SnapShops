package com.example.sagar.popupshops_buyerside.Shop.SellerRecycleView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Registration.LaunchActivity;
import com.example.sagar.popupshops_buyerside.SelectActionActivity;
import com.example.sagar.popupshops_buyerside.Shop.Item;
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
    private boolean self;

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
    }

    private void initializeData() {
        items = new ArrayList<>();
        itemIds = new ArrayList<>();

        if (shopID.equals("self")) {
            self = true;
            FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
                @Override
                public void OnComplete(String value) {
                    shopID = value;
                    addItemsToView(shopID);
                }
            });
        } else {
            self = false;
            addItemsToView(shopID);
        }


    }

    public void addItemsToView(String shopID) {

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
                                            Float.parseFloat(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMPRICE).getValue().toString()),
                                            itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMDESCRIPTION).getValue().toString(),
                                            itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMIMAGE).getValue().toString(),
                                            Integer.parseInt(itemSnapshot.child(FirebaseEndpoint.ITEMS.ITEMSTOCK).getValue().toString()),
                                            itemSnapshot.getKey()
                                    )
                    );
                    itemIds.add(itemSnapshot.getKey());
                    Log.w(TAG, String.valueOf(added));
                    Log.w(TAG, "value of self" + self);
                    rvAdapter = new RVAdapter(items, self);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SelectActionActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings2) {
            FirebaseUtils.logoutUser();
            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}