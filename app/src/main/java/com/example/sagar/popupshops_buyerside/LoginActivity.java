package com.example.sagar.popupshops_buyerside;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buyerButton = (Button)findViewById(R.id.buyer_button);
        Button vendorButton = (Button)findViewById(R.id.vendor_button);

        buyerButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        vendorButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, vendor_dashboard.class);
                startActivity(intent);
            }
        });
    }
}