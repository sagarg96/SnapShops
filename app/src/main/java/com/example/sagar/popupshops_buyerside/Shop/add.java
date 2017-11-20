package com.example.sagar.popupshops_buyerside.Shop;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sagar.popupshops_buyerside.R;
import com.example.sagar.popupshops_buyerside.Registration.LaunchActivity;
import com.example.sagar.popupshops_buyerside.SelectActionActivity;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseEndpoint;
import com.example.sagar.popupshops_buyerside.Utility.FirebaseUtils;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class add extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private static final String TAG = "add";
    private static Uri imageUrl = null;
    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    String mCurrentPhotoPath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);

        final ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        final EditText descriptionInput = (EditText) findViewById(R.id.descriptionInput);
        final EditText priceInput = (EditText) findViewById(R.id.priceInput);
        final EditText stockInput = (EditText) findViewById(R.id.stockInput);
        final Button attachButton = (Button) findViewById(R.id.attachButton);
        final Button upload = (Button) findViewById(R.id.uploadButton);


        attachButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        ImageButton.OnClickListener listener = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        };
        imageButton1.setOnClickListener(listener);

        attachButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        final AutoCompleteTextView categoryInput = (AutoCompleteTextView) findViewById(R.id.categoryInput);
        categoryInput.setAdapter(suggest(this));

        upload.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final String priceString = priceInput.getText().toString();
                final String categoryString = categoryInput.getText().toString();
                final String stockString = stockInput.getText().toString();
                final DatabaseReference dbRef = database.getReference("item").push();

                if (priceString.length() != 0 && description.length() != 0 && categoryString.length() != 0 && stockString.length() !=0 && imageUrl != null) {
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
                                                                            categoryString, Float.parseFloat(priceString), description, taskSnapshot.getMetadata().getDownloadUrl().toString(), Integer.parseInt(stockString)
                                                                    )
                                                            );
                                                            //add shop id to item record
                                                            Query shopIDQuery = FirebaseUtils.getUsersRef().child(FirebaseUtils.getCurrentUser().getUid()).child(FirebaseEndpoint.USERS.SHOPS);
                                                            shopIDQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    boolean notDone = true;
                                                                    if (dataSnapshot.exists()) {
                                                                        if (notDone) {
                                                                            String val = dataSnapshot.getValue().toString();
                                                                            dbRef.child(FirebaseEndpoint.ITEMS.SHOPID).setValue(val.substring(1, val.length() - 1));
                                                                            notDone = false;

                                                                            Query shopLocationQuery = FirebaseUtils.getShopsRef().child(val.substring(1, val.length() - 1));
                                                                            shopLocationQuery.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if (dataSnapshot.hasChild(FirebaseEndpoint.SHOPS.LOCATION)) {
                                                                                        Log.w(TAG, dataSnapshot.child(FirebaseEndpoint.SHOPS.LOCATION).getValue().toString());
                                                                                        double latitude = (double) dataSnapshot.child(FirebaseEndpoint.SHOPS.LOCATION).child("latitude").getValue();
                                                                                        double longitude = (double) dataSnapshot.child(FirebaseEndpoint.SHOPS.LOCATION).child("longitude").getValue();
                                                                                        DatabaseReference geoRef = FirebaseUtils.getBaseRef().child("item_location");
                                                                                        GeoFire geofire = new GeoFire(geoRef);
                                                                                        geofire.setLocation(dbRef.getKey(), new GeoLocation(latitude, longitude));

                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    // Getting Post failed, log a message
                                                                    Log.w("here", "loadPost:onCancelled", databaseError.toException());
                                                                    // ...

                                                                }
                                                            });


                                                            Toast.makeText(add.this, "Item successfully uploaded", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                    );
                }
                else {
                    Toast.makeText(add.this, "Please input all fields and attach an image", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(add.this, vendor_dashboard.class);
                startActivity(intent);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton1);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageButton.setImageURI(imageUrl);
        }
    }

    private ArrayAdapter<String> suggest(Context context) {
        String[] suggestions = {"General", "Bags", "Books", "back"};
        String[] addresses = new String[suggestions.length];
        for (int i = 0; i < suggestions.length; i++) {
            addresses[i] = suggestions[i];
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
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
            Intent intent = new Intent(add.this, SelectActionActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings2) {
            FirebaseUtils.logoutUser();
            Intent intent = new Intent(add.this, LaunchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}

