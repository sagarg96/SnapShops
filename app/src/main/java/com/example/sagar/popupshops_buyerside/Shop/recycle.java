package com.example.sagar.popupshops_buyerside.Shop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class recycle extends AppCompatActivity {

    private List<Item> items;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycle);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
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

        FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
            @Override
            public void OnComplete(String value) {
                Query itemQuery = FirebaseUtils.getItemRef().orderByChild(FirebaseEndpoint.ITEMS.ITEMPRICE);
                itemQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            if (itemSnapshot.getChildrenCount() == Item.getAttributeCount())
                                items.add(itemSnapshot.getValue(Item.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("item load recycle", "item loading failed");
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