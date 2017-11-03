package com.example.sagar.popupshops_buyerside;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VendorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        Button insert = (Button)findViewById(R.id.insert_button);
        final EditText nameInput = (EditText)findViewById(R.id.nameInput);
        final EditText priceInput = (EditText)findViewById(R.id.priceInput);
        final EditText imageUrlInput = (EditText)findViewById(R.id.imageUrlInput);
        final EditText locationInput = (EditText)findViewById(R.id.locationInput);

        insert.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                String name = nameInput.getText().toString();
                String priceString = priceInput.getText().toString();
                String imageUrl = imageUrlInput.getText().toString();
                String location = locationInput.getText().toString();

                if (name.length() != 0 && imageUrl.length() != 0  && priceString.length() != 0 && location.length() != 0) {
                    DatabaseReference ref = database.getReference("profile").push();
                    ref.setValue(new Profile(name, Integer.parseInt(priceString), location, imageUrl));
                    Toast.makeText(VendorActivity.this,"Profile added", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}