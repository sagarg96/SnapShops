package com.example.sagar.popupshops_buyerside.Shop;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class add extends vendor_dashboard {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 2;

    private static Uri imageUrl = null;
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        final EditText nameInput = (EditText)findViewById(R.id.nameInput);
        final EditText priceInput = (EditText)findViewById(R.id.priceInput);
        final Button attachButton = (Button)findViewById(R.id.attachButton);
        final Button upload = (Button)findViewById(R.id.uploadButton);

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

        final AutoCompleteTextView categoryInput = (AutoCompleteTextView) findViewById(R.id.categoryinput);
        categoryInput.setAdapter(suggest(this));

        upload.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                final String name = nameInput.getText().toString();
                final String priceString = priceInput.getText().toString();
                final String categoryString = categoryInput.getText().toString();
                final DatabaseReference dbRef = database.getReference("item").push();

                if (priceString.length() != 0 && name.length() != 0 && categoryString.length() != 0 && imageUrl != null) {
                    String itemID = dbRef.getKey();
                    storageRef.child("images/" + itemID + ".png").putFile(imageUrl)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(add.this, "Upload unsuccessful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    dbRef.setValue(
                                            new Item(
                                                    categoryString,Integer.parseInt(priceString),null, taskSnapshot.getMetadata().getDownloadUrl().toString(), 1
                                            )
                                    );
                                    Toast.makeText(add.this,"Item successfully uploaded", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUrl = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG" + timeStamp + "";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton1);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageButton.setImageURI(imageUrl);
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

