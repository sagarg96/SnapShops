package com.example.sagar.popupshops_buyerside.Shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Registration.LaunchActivity;
import com.example.sagar.popupshops_buyerside.SelectActionActivity;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class vendor_dashboard extends AppCompatActivity {

    String id;
    EditText shopDescription;
    EditText shopName;
    LinearLayout setUpLayout;
    LinearLayout dashboardLayout;
    boolean isSetup;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_dashboard);

        Bundle extras = getIntent().getExtras();
        isSetup = extras.getBoolean("setup");

        id = FirebaseUtils.getCurrentUser().getUid();

        setUpLayout = (LinearLayout) findViewById(R.id.setupLayout);
        dashboardLayout = (LinearLayout) findViewById(R.id.dashboardLayout);

        setUpUI(isSetup);

        //dashboard layout buttons
        Button viewItemList = (Button) findViewById(R.id.viewItems);
        Button updateLocation = (Button) findViewById(R.id.updateLocation);
        Button closeShop = (Button) findViewById(R.id.closeShop);
        Button addItem = (Button) findViewById(R.id.addItem);

        //setup layout buttons
        Button setUpShop = (Button) findViewById(R.id.setUpSubmit);
        Button setLocation = (Button) findViewById(R.id.setUpLocation);


        shopDescription = (EditText) findViewById(R.id.shopDescr);
        shopDescription.setOnKeyListener(new descriptionTextHandler());
        shopName = (EditText) findViewById(R.id.shopName);
        shopName.setOnKeyListener(new nameTextHandler());

        shopDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        shopName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        viewItemList.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(vendor_dashboard.this, recycle.class);
                Intent intent = new Intent(vendor_dashboard.this, recycle.class);

                startActivity(intent);
            }
        });

        addItem.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(vendor_dashboard.this, add.class);
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
                FirebaseUtils.getCurrentShopID(new FirebaseUtils.Callback() {
                    @Override
                    public void OnComplete(String value) {
                        DatabaseReference databaseReference = FirebaseUtils.getShopsRef().child(value).child(FirebaseEndpoint.SHOPS.SHOPSTATUS);

                        databaseReference.setValue(ShopStatus.CLOSED);
                    }
                });
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
                Intent intent = new Intent(vendor_dashboard.this, SelectActionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpUI(boolean isSetup) {
        if (isSetup) {
            setUpLayout.setVisibility(View.VISIBLE);
            dashboardLayout.setVisibility(View.GONE);
        } else {
            setUpLayout.setVisibility(View.GONE);
            setUpLayout.setVisibility(View.VISIBLE);
            populateFields();
        }


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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            Intent intent = new Intent(vendor_dashboard.this, SelectActionActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings2) {
            FirebaseUtils.logoutUser();
            Intent intent = new Intent(vendor_dashboard.this, LaunchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public class descriptionTextHandler implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
            int viewID = view.getId();
            EditText input_text = (EditText) findViewById(viewID);
            final String the_text = input_text.getText().toString();

            if (KeyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(vendor_dashboard.this, "Description Updated " + the_text, Toast.LENGTH_LONG).show();
                //press enter to update shop description

                Query shopExistQuery = FirebaseUtils.getUsersRef().child(FirebaseUtils.getCurrentUser().getUid()).child(FirebaseEndpoint.USERS.SHOPS);
                shopExistQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean notDone = true;
                        if (dataSnapshot.exists()) {
                            Log.w("here", "shop exists " + dataSnapshot.getValue().toString());
//                            DatabaseReference databaseReference = FirebaseUtils.getCurrentUserShop().child(FirebaseEndpoint.SHOPS.DESCRIPTION).push();
//                            databaseReference.setValue(the_text);
                            if (notDone) {
                                String val = dataSnapshot.getValue().toString();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseEndpoint.ROOTS.SHOPS).child(val.substring(1, val.length() - 1)).child(FirebaseEndpoint.SHOPS.DESCRIPTION);
                                databaseReference.setValue(the_text);
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
            return false;
        }
    }

    public class nameTextHandler implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int KeyCode, KeyEvent keyEvent) {
            int viewID = view.getId();
            EditText input_text = (EditText) findViewById(viewID);
            final String the_text = input_text.getText().toString();

            if (KeyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(vendor_dashboard.this, "Shop Name Updated " + the_text, Toast.LENGTH_LONG).show();
                Query shopExistQuery = FirebaseUtils.getUsersRef().child(FirebaseUtils.getCurrentUser().getUid()).child(FirebaseEndpoint.USERS.SHOPS);
                shopExistQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean notDone = true;
                        if (dataSnapshot.exists()) {
                            Log.w("here", "shop exists " + dataSnapshot.getValue().toString());
                            if (notDone) {
                                String val = dataSnapshot.getValue().toString();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseEndpoint.ROOTS.SHOPS).child(val.substring(1, val.length() - 1)).child(FirebaseEndpoint.SHOPS.SHOPNAME);
                                databaseReference.setValue(the_text);
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
            return false;
        }
    }

}
