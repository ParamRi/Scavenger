package com.teamgamma.scavenger;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.teamgamma.scavenger.API.API;
import com.teamgamma.scavenger.plant.Plant;

public class AddPlantActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Location location;
    protected Context context;
    private boolean gps_enabled;
    private boolean network_enabled;
    private static final long MIN_TIME_BW_UPDATES = 5000; //5 seconds
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 meters
    private double latitude;
    private double longitude;
    private EditText plantNameText;
    private EditText plantSciNameText;
    private EditText plantDescText;
    private ImageView plantImage;
    private CheckBox edibilityCheckBox;
    private Button addPlantButton;
    private Button addImageButton;
    private Plant createPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add Plant");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gps_enabled) {
            if (location == null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
                Log.d("activity", "RLOC: GPS Enabled");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.d("activity", "RLOC: loc by GPS");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }
        plantNameText = (EditText) findViewById(R.id.plantNameText);
        plantSciNameText = (EditText) findViewById(R.id.sciNameText);
        plantDescText = (EditText) findViewById(R.id.descriptionTextEditor);
        edibilityCheckBox = (CheckBox) findViewById(R.id.checkBox);
        addPlantButton = (Button) findViewById(R.id.addPlantButton);
        addPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlant(v);
            }
        });
        addImageButton = (Button) findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    /**
     * addPlant() builds and uploads a plant object onto the database
     */
    public void addPlant(View view) {
        createPlant = new Plant(plantNameText.getText().toString(), plantSciNameText.getText().toString(),
                plantDescText.getText().toString(),
                edibilityCheckBox.isChecked(), false, new LatLng(latitude, longitude));
        API.getReference().child("plants").push().setValue(createPlant);
        Log.d("AddPlantActivity", "added plant");
    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
