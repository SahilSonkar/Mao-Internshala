package com.example.p_1internshala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SelectYourLocation extends AppCompatActivity {


    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    RadioButton targetbutton;
    RadioGroup group;
    Button save;

    ProgressDialog pd;


    TextView addressM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_your_location);

        pd = new ProgressDialog(SelectYourLocation.this);
        final String firstName = ""+getIntent().getStringExtra("first");
        final String lastName = ""+getIntent().getStringExtra("last");

        group = findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                targetbutton = findViewById(checkedId);
            }
        });

        addressM=findViewById(R.id.address);
//        stateM=findViewById(R.id.city);
//        countyM = findViewById(R.id.country);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapAPI);

        client = LocationServices.getFusedLocationProviderClient(this);

        //check premission for my current location
        if (ActivityCompat.checkSelfPermission(SelectYourLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            // call the methods
            getCurrentLocation();

        } else {
            // when the request permission is denie
            //reques permission again
            ActivityCompat.requestPermissions(SelectYourLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pd.setTitle("Saving..");
                pd.show();
                String addressTobeStored = addressM.getText().toString();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("User/Customers").child(user.getUid().toLowerCase());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("First Name",firstName);
                hashMap.put("Last Name",lastName);
                hashMap.put("Address" + targetbutton.getText().toString(),addressM.getText().toString()+"");

                myRef.setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(SelectYourLocation.this, "saved..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SelectYourLocation.this, Dashboard.class));
                        }
                        else
                            pd.dismiss();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(SelectYourLocation.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                Toast.makeText(SelectYourLocation.this, ""+firstName +lastName + addressTobeStored, Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(SelectYourLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {

                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap googleMap) {

                            double lat=location.getLatitude();
                            double log=location.getLongitude();
                            // initialise LatLan
                            LatLng latLng = new LatLng(lat, log);
                            // create marker at my location//
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You Location");
                            //for zooming in map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            //add marker on map
                            googleMap.addMarker(markerOptions);
                            double lattitude = latLng.latitude;
                            double longgitude = latLng.longitude;
                            Geocoder geocoder = new Geocoder(SelectYourLocation.this, Locale.getDefault());

                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(lattitude, longgitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String addressp =""+ addresses.get(0).getAddressLine(0);
                            String cityp = ""+addresses.get(0).getLocality();
                            String statep = ""+addresses.get(0).getAdminArea();
                            String zipp = ""+addresses.get(0).getPostalCode();
                            String countryp = ""+addresses.get(0).getCountryName();
                            addressM.setText(addressp);

                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {


                                    double lattitude = latLng.latitude;
                                    double longgitude = latLng.longitude;
                                    Geocoder geocoder = new Geocoder(SelectYourLocation.this, Locale.getDefault());

                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocation(lattitude, longgitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String address =""+ addresses.get(0).getAddressLine(0);
                                    String city = ""+addresses.get(0).getLocality();
                                    String state = ""+addresses.get(0).getAdminArea();
                                    String zip = ""+addresses.get(0).getPostalCode();
                                    String country = ""+addresses.get(0).getCountryName();
                                    googleMap.clear();
                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You Location ?");
//                                  //for zooming in map
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                                //add marker on map
                                    googleMap.addMarker(markerOptions);
                                    Toast.makeText(SelectYourLocation.this, ""+
                                            address+
                                            city +
                                            state+
                                            zip, Toast.LENGTH_SHORT).show();
                                    addressM.setText(address);
                                }

                            });
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                // permission granted
                // call that method again
                getCurrentLocation();
            }
        }
    }
}