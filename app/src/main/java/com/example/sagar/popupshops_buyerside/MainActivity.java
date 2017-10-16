package com.example.sagar.popupshops_buyerside;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;


public class MainActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    //////////////////

    ////////////////////
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


        for (Profile profile : Utils.loadProfiles(this.getApplicationContext())) {
            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
        }

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


}

