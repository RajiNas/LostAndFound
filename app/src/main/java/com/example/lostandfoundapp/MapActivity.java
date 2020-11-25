package com.example.lostandfoundapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    LatLng latLng;
    String locationName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

         locationName = getIntent().getStringExtra("address");
//        Toast.makeText(this, "ddddddddddddddddddddddddd" + getLocation, Toast.LENGTH_SHORT).show();

        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);


        try {
            geoLocate();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
    }

    public void geoLocate( ) throws IOException {

//         locationName = "montreal";
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocationName(locationName,1);

         Address add = addressList.get(0);
        String locality = add.getLocality();
        Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();

        double lat = add.getLatitude();
        double longitude = add.getLongitude();

        gotoLocation(lat,longitude);



    }
    private void gotoLocation(double lat , double longitude){
         latLng = new LatLng(lat, longitude);
        Log.e("MapActivity", "The location is" +latLng.toString());
      //  map.addMarker(new MarkerOptions().position(latLng));
        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
    }

}