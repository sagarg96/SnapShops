package com.example.sagar.popupshops_buyerside.Shop;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add extends vendor_dashboard {

    static final int reqst_img = 1;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        final EditText nameInput = (EditText)findViewById(R.id.nameInput);
        final EditText priceInput = (EditText)findViewById(R.id.priceInput);
        Button attachButton = (Button)findViewById(R.id.attachButton);
        Button upload = (Button)findViewById(R.id.uploadButton);

        ImageButton.OnClickListener listener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePicetureIntent();
            }
        };
        imageButton1.setOnClickListener(listener);

        upload.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                String name = nameInput.getText().toString();
                String priceString = priceInput.getText().toString();

                if (priceString.length() != 0) {
                    DatabaseReference ref = database.getReference("profile").push();
                   // ref.setValue(new Profile(name, Integer.parseInt(priceString)));
                    Toast.makeText(add.this,"Profile added", Toast.LENGTH_LONG).show();
                }
            }
        });

        AutoCompleteTextView categoryinput = (AutoCompleteTextView) findViewById(R.id.categoryinput) ;
        categoryinput.setAdapter(suggest(this));

    }


    private void dispatchTakePicetureIntent()
    {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, reqst_img);
        }

    }

    private ArrayAdapter<String> suggest(Context context)
    {
        String[]suggestions = {"General","Bags","Books"};
        String[]addresses = new String[suggestions.length];
        for(int i =0;i<suggestions.length;i++)
        {
            addresses[i]=suggestions[i];
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }
}

