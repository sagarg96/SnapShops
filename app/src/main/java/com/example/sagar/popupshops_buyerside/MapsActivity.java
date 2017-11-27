package com.example.sagar.popupshops_buyerside;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    double latitude;
    double longitude;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            shopName = extras.getString("shopName");
            //The key argument here must match that used in the other activity
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        // and move the map's camera to the same location.
        LatLng shop = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(shop)
                .title(shopName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(shop));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}