package com.example.sagar.popupshops_buyerside;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.sagar.popupshops_buyerside.Registration.LaunchActivity;
import com.example.sagar.popupshops_buyerside.Registration.LoginActivity;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

public class MainActivity extends LoginActivity {

    private final String TAG = "MainActivity";
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button change to tab later
        Button near_me_tab = (Button) findViewById(R.id.near_me_tab);

        near_me_tab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Near_me.class);
                MainActivity.this.startActivity(myIntent);

            }
        });

        ///////////////////////////////////////////// Spinner drop down
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);





        //////////////////////////////////////////////////


        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));


//        for (Profile profile : Utils.loadProfiles(this.getApplicationContext())) {
//            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//        }

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG", "onChildAdded:" + dataSnapshot.getKey());
                Profile profile = dataSnapshot.getValue(Profile.class);
                mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("profile");

        myRef.addChildEventListener(childEventListener);

        /*public void TabNavigation(){

            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(actionBar.NAVIGATION_MODE_TABS);


    }*/

        /* Buttons for tinder
    findViewById(R.id.rejectBtn).

    setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
            mSwipeView.doSwipe(false);
        }
    });

    findViewById(R.id.acceptBtn).

    setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
            mSwipeView.doSwipe(true);
        }
    });
    */
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



}

