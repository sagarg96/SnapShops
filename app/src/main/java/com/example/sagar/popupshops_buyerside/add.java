package com.example.sagar.popupshops_buyerside;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class add extends recycle {

    static final int reqst_img = 1;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static Uri imageData = null;
    private StorageReference mStorageRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        final Button attachButton = (Button) findViewById(R.id.attachButton);
        final Button uploadButton = (Button) findViewById(R.id.uploadButton);

        ImageButton.OnClickListener listener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        };
        imageButton1.setOnClickListener(listener);

        attachButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        uploadButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view){
                if (imageData != null){
                    mStorageRef.child("images/test.png").putFile(imageData)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(add.this, "Upload unsuccessful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            Toast.makeText(add.this,"Image successfully uploaded", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                    );
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicIntent, reqst_img);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imageData = selectedImage;
        }
    }
}

