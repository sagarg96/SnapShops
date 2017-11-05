package com.example.sagar.popupshops_buyerside;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    Button logOutButton;
    Button buyButton;
    Button sellButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        logOutButton = (Button) findViewById(R.id.logOutButton);
        buyButton = (Button) findViewById(R.id.buyButton);
        sellButton = (Button) findViewById(R.id.sellButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        logOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(SelectActionActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });

        buyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkShopExists(FirebaseAuth.getInstance().getCurrentUser());
            }
        });

        sellButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActionActivity.this, vendor_dashboard.class);
                startActivity(intent);
            }
        });


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
                    intent.putExtra("setup", "false");
                    startActivity(intent);
                } else {
                    //if shop doesnt exist take ask for shop setup with an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectActionActivity.this);
                    builder.setMessage("It seems you don't have a shop setup. Setup Shop!")
                            .setPositiveButton("Lets Go!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(SelectActionActivity.this, vendor_dashboard.class);
                                    intent.putExtra("setup", "true");
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
}
