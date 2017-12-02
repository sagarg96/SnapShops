package com.example.sagar.popupshops_buyerside;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.Registration.GPSTracker;
import com.example.sagar.popupshops_buyerside.Registration.LaunchActivity;
import com.example.sagar.popupshops_buyerside.Shop.Item;
import com.example.sagar.popupshops_buyerside.Shop.ShopProfile;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    private final String TAG = "MainActivity";
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    // GPSTracker class
    GPSTracker gps;
    //location coordinates
    double latitude;
    double longitude;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private GeoFire geoFire;
    private double radius = 2;
    private Button setRadius;
    private ArrayList<Item> items = new ArrayList<Item>();

    final DatabaseReference categoryRef = FirebaseUtils.getCategoryRef();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geoFire = new GeoFire(FirebaseUtils.getItemLocationRef());


        //Location Permissions
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //button change to tab later
        Button near_me_tab = (Button) findViewById(R.id.near_me_tab);
        setRadius = (Button) findViewById(R.id.set_radius);
        final EditText radiusInput = (EditText) findViewById(R.id.radius);

        setRadius.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                radius = Double.parseDouble(radiusInput.getText().toString());
                runGeoQuery();

            }
        });

        near_me_tab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showAlertbox("Yolo");
//                Intent myIntent = new Intent(MainActivity.this, Near_me.class);
//                MainActivity.this.startActivity(myIntent);

            }
        });

        final Spinner dropdown = (Spinner) findViewById(R.id.spinner1);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSwipeView.removeAllViews();
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    String selectedItem = dropdown.getSelectedItem().toString();
                    if (selectedItem == "All" || (item.getItemCategory() != null && item.getItemCategory().equals(selectedItem))) {
                        mSwipeView.addView(new ItemCard(mContext, item, mSwipeView));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{});
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> categoryHashMap = (HashMap<String, String>) dataSnapshot.getValue();
                ArrayList<String> categories = new ArrayList<String>();
                categories.add("All");
                if (categoryHashMap != null) {
                    String[] dbCategories = categoryHashMap.values().toArray(new String[categoryHashMap.size()]);
                    for (int i = 0; i < dbCategories.length; i++) {
                        categories.add(dbCategories[i]);
                    }
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, categories);
                dropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


    }

    @Override
    protected void onStart() {

        super.onStart();

        setLocationAttributes();
        runGeoQuery();

    }

    public void runGeoQuery() {

        items.clear();
        mSwipeView.removeAllViews();

        GeoQuery itemLocationQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), radius);
        itemLocationQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.w(TAG, "key entered" + key);
                Query itemQuery = FirebaseUtils.getItemRef().orderByKey().equalTo(key);

                itemQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                        final Item item = dataSnapshot.getValue(Item.class);
                        final Spinner dropdown = (Spinner) findViewById(R.id.spinner1);
                        Log.w(TAG, item.getShopID());
                        Query shopQuery = FirebaseUtils.getShopsRef().orderByKey().equalTo(item.getShopID());
                        shopQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    ShopProfile shop = snapshot.getValue(ShopProfile.class);
                                    Log.w(TAG, snapshot.getChildrenCount() + "");
                                    Log.w(TAG, "here");
                                    if (shop.getShopStatus().name().equals("OPEN")) {
                                        items.add(item);
                                        String selectedItem = dropdown.getSelectedItem().toString();
                                        if (selectedItem == "All" || (item.getItemCategory() != null && item.getItemCategory().equals(selectedItem))) {
                                            mSwipeView.addView(new ItemCard(mContext, item, mSwipeView));
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "error:" + databaseError.getDetails());

                    }
                });

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void setLocationAttributes() {
        // create class object
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
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
            Intent intent = new Intent(MainActivity.this, SelectActionActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings2) {
            FirebaseUtils.logoutUser();
            Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void showAlertbox(String title) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_popup_scroll);
        dialog.setCanceledOnTouchOutside(false);
        TextView alertbox_title = (TextView) dialog
                .findViewById(R.id.alertbox_title);
        alertbox_title.setText(title);

        Button yes = (Button) dialog.findViewById(R.id.alertbox_yes);
        Button no = (Button) dialog.findViewById(R.id.alertbox_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when YES button is clicked
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when NO button is clicked
            }
        });

        dialog.show();
    }
}

