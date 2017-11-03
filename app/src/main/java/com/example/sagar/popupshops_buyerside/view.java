package com.example.sagar.popupshops_buyerside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Vikram on 03-Nov-17.
 */

    public class view extends LoginActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.view);

            Button buyerButton = (Button)findViewById(R.id.buyer_button);
            Button vendorButton = (Button)findViewById(R.id.vendor_button);

            buyerButton.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(com.example.sagar.popupshops_buyerside.view.this, recycle.class);
                    startActivity(intent);
                }
            });

            vendorButton.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view){
                    Intent intent = new Intent(com.example.sagar.popupshops_buyerside.view.this, add.class);
                    startActivity(intent);
                }
            });
        }
    }
