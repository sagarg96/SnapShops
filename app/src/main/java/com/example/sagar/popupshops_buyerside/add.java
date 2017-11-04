package com.example.sagar.popupshops_buyerside;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

public class add extends vendor_dashboard {

    static final int reqst_img = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);

        ImageButton.OnClickListener listener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePicetureIntent();
            }
        };
        imageButton1.setOnClickListener(listener);

    }


    private void dispatchTakePicetureIntent() {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, reqst_img);
        }

    }
}

