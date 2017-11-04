package com.example.sagar.popupshops_buyerside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SelectActionActivity extends AppCompatActivity {

    Button logOutButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        logOutButton = (Button) findViewById(R.id.logOutButton);

        mAuth = FirebaseAuth.getInstance();

        logOutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(SelectActionActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });
    }
}
