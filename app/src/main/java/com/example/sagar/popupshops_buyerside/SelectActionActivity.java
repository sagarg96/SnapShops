package com.example.sagar.popupshops_buyerside;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.sagar.popupshops_buyerside.BuyerRecycleView.BuyerRecycle;
import com.example.sagar.popupshops_buyerside.Registration.LaunchActivity;
import com.example.sagar.popupshops_buyerside.Shop.vendor_dashboard;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectActionActivity extends AppCompatActivity {

    ImageButton buyButton;
    ImageButton sellButton;
    ImageButton wishListButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        buyButton = (ImageButton) findViewById(R.id.buyButton);
        sellButton = (ImageButton) findViewById(R.id.sellButton);
//        wishListButton = (ImageButton) findViewById(R.id.wishlistbutton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();


        buyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        sellButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkShopExists(FirebaseUtils.getCurrentUser());
            }
        });

//        wishListButton.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), BuyerRecycle.class);
//                startActivity(intent);
//            }
//        });
    }

    private void checkShopExists(FirebaseUser currentUser) {
        Query shopExistQuery = FirebaseUtils.getUsersRef().child(currentUser.getUid()).child(FirebaseEndpoint.USERS.SHOPS);
        shopExistQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //if shop exists go to shop dashboard
                    Log.w("here", "shop exists");
                    Intent intent = new Intent(SelectActionActivity.this, vendor_dashboard.class);
                    intent.putExtra("setup", false);
                    startActivity(intent);
                } else {
                    //if shop doesnt exist take ask for shop setup with an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectActionActivity.this);
                    builder.setMessage("It seems you don't have a shop setup. Setup Shop!")
                            .setPositiveButton("Lets Go!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(SelectActionActivity.this, vendor_dashboard.class);
                                    intent.putExtra("setup", true);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do Nothing
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
////                    Log.w("here", "query");
////                    Log.w("here", "query"+ snapshot.getValue().toString());
//                    if(snapshot.getKey().equals("shops")){
//                        Log.w("here", "shop exists");
//                    }
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("here", "loadPost:onCancelled", databaseError.toException());
                // ...

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
            Intent intent = new Intent(SelectActionActivity.this, SelectActionActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings2) {
            FirebaseUtils.logoutUser();
            Intent intent = new Intent(SelectActionActivity.this, LaunchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
