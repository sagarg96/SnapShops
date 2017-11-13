package com.example.sagar.popupshops_buyerside.Shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.example.sagar.popupshops_buyerside.recycle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class vendor_dashboard extends AppCompatActivity {

    String id;
    EditText shopDescription;
    EditText shopName;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_dashboard);

        Bundle extras = getIntent().getExtras();
        boolean isSetup = extras.getBoolean("setup");

        id = FirebaseUtils.getCurrentUser().getUid();

        LinearLayout setUpLayout = (LinearLayout) findViewById(R.id.setupLayout);
        LinearLayout dashboardLayout = (LinearLayout) findViewById(R.id.dashboardLayout);

        if (isSetup) {
            setUpLayout.setVisibility(View.VISIBLE);
            dashboardLayout.setVisibility(View.GONE);
        } else {
            setUpLayout.setVisibility(View.GONE);
            setUpLayout.setVisibility(View.VISIBLE);
            populateFields();
        }

        //dashboard layout buttons
        Button viewItemList = (Button) findViewById(R.id.viewItems);
        Button updateLocation = (Button) findViewById(R.id.updateLocation);
        Button closeShop = (Button) findViewById(R.id.closeShop);

        //setup layout buttons
        Button setUpShop = (Button) findViewById(R.id.setUpSubmit);
        Button setLocation = (Button) findViewById(R.id.setUpLocation);

        shopDescription = (EditText) findViewById(R.id.shopDescr);
        shopDescription.setOnKeyListener(new descriptionTextHandler());
        shopName = (EditText) findViewById(R.id.shopName);
        shopName.setOnKeyListener(new nameTextHandler());


        viewItemList.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(vendor_dashboard.this, recycle.class);
                startActivity(intent);
            }
        });

        updateLocation.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                //add location tracking
            }
        });

        closeShop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change shop status to closed
            }
        });

        setLocation.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                //add location tracking
            }
        });

        setUpShop.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                createShop(shopName.getText().toString(), shopDescription.getText().toString());
            }
        });
    }

    private void populateFields() {
        Query shopQuery = FirebaseUtils.getShopsRef();
        shopQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("userID")) {
                        if (snapshot.child("userID").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            shopDescription.setText(snapshot.child(FirebaseEndpoint.SHOPS.DESCRIPTION).getValue().toString(), TextView.BufferType.EDITABLE);
                            shopName.setText(snapshot.child(FirebaseEndpoint.SHOPS.SHOPNAME).getValue().toString(), TextView.BufferType.EDITABLE);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private class updateTextFields extends AsyncTask<Void, Void, HashMap<String, String>> {
//
//        @Override
//        protected HashMap<String, String> doInBackground(Void... voids) {
//            HashMap<String,String> map = new HashMap<>();
//            map.put()
//            return null;
//        }
//    }


    private void createShop(String shopName, String shopDescription) {
        //create shop in shop table
        // TODO test more - sometimes userID not register
        ShopProfile newShop = new ShopProfile(shopName, shopDescription, 0.0, 0.0, FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference databaseReference = FirebaseUtils.getShopsRef().push();
        databaseReference.setValue(newShop);

        //update shops in users table
        List shopList = new ArrayList<String>();
        shopList.add(databaseReference.getKey());
        FirebaseUtils.getUsersRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(FirebaseEndpoint.USERS.SHOPS).setValue(shopList);
    }

    public class descriptionTextHandler implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
            int viewID = view.getId();
            EditText input_text = (EditText) findViewById(viewID);
            String the_text = input_text.getText().toString();

            if (KeyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(vendor_dashboard.this, "Description Updated " + the_text, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    public class nameTextHandler implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
            int viewID = view.getId();
            EditText input_text = (EditText) findViewById(viewID);
            String the_text = input_text.getText().toString();

            if (KeyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(vendor_dashboard.this, "Shop Name Updated " + the_text, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }
}
